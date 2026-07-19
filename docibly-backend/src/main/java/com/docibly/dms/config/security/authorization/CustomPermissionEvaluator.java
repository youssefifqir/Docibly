package com.docibly.dms.config.security.authorization;

import com.docibly.dms.bean.core.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final Map<String, EntityPermissions> permissionRegistry;
    private final EntityManager entityManager;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated access attempt to {} with permission {}",
                    targetType, permission);
            return false;
        }

        String entityType = targetType;
        String operation = permission.toString();

        log.debug("Checking permission: user={}, entity={}, operation={}, resourceId={}",
                authentication.getName(), entityType, operation, targetId);

        // 1. Get permission configuration for this entity
        EntityPermissions entityPermissions = permissionRegistry.get(entityType);
        if (entityPermissions == null) {
            log.warn("No permission configuration found for entity: {}", entityType);
            return false;
        }

        // 2. Get current user and their roles
        User user = (User) authentication.getPrincipal();
        Set<String> userRoles = user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.debug("User roles: {}", userRoles);

        // 3. Evaluate permissions — most-permissive-wins across all user roles.
        //    Role iteration order follows YAML declaration order (LinkedHashMap).
        //    If any role grants "all" access, evaluation stops immediately.
        //    "own" and "scoped" are checked per-resource; for list endpoints
        //    (targetId==null) the service layer applies row-level filtering.
        for (String role : userRoles) {
            RolePermissions rolePerms = entityPermissions.getRolePermissions().get(role);
            if (rolePerms == null) {
                continue;
            }

            // STEP A: Check "all" list — allow immediately, no ownership check
            if (rolePerms.getAllOperations().contains(operation)) {
                log.debug("Role {} allows operation {} on ALL {} resources",
                        role, operation, entityType);
                return true;
            }

            // STEP B: Check "own" list — verify ownership
            if (rolePerms.getOwnOperations().contains(operation)) {
                log.debug("Role {} allows operation {} on OWN {} resources — checking ownership",
                        role, operation, entityType);

                if (targetId == null) {
                    // List endpoint (findAll, findPaginatedByCriteria):
                    // service layer applies row-level ownership filtering.
                    if ("READ".equals(operation)) {
                        log.debug("Own-scoped READ on list endpoint — delegating to service-layer filtering");
                        return true;
                    }
                    log.warn("Cannot check ownership: targetId is null for non-READ operation {}", operation);
                    return false;
                }

                boolean isOwner = checkOwnership(targetId, entityType, user.getId(), "createdBy");
                log.debug("Ownership check result: {}", isOwner);
                return isOwner;
            }

            // STEP C: Check "scoped" list — verify scope field match
            if (rolePerms.getScopedOperations().contains(operation)) {
                String scopeField = rolePerms.getScopeField();
                log.debug("Role {} allows operation {} on SCOPED {} resources (field={}) — checking scope",
                        role, operation, entityType, scopeField);

                if (targetId == null) {
                    // List endpoint — service layer applies scoped filtering
                    log.debug("Scoped operation on list endpoint — delegating to service-layer filtering");
                    return true;
                }

                Object scopeValue = getUserFieldValue(user, scopeField);
                if (scopeValue == null) {
                    log.warn("Cannot resolve scoped field '{}' on user — denying access", scopeField);
                    return false;
                }
                boolean isScoped = checkScopedAccess(targetId, entityType, scopeField, scopeValue);
                log.debug("Scope check result for field '{}': {}", scopeField, isScoped);
                return isScoped;
            }
        }

        // STEP D: No role grants this operation — deny
        log.debug("Permission denied: no role allows operation {} on {}", operation, entityType);
        return false;
    }

    /**
     * Check if the current user owns the specified resource
     * by verifying entity.createdBy == currentUserId.
     */
    private boolean checkOwnership(Object resourceId, String entityType, String currentUserId, String fieldName) {
        try {
            String jpql = String.format(
                    "SELECT COUNT(e) > 0 FROM %s e WHERE e.id = :id AND e.%s = :userId",
                    entityType, fieldName
            );

            Boolean isOwner = entityManager.createQuery(jpql, Boolean.class)
                    .setParameter("id", resourceId)
                    .setParameter("userId", currentUserId)
                    .getSingleResult();

            return Boolean.TRUE.equals(isOwner);

        } catch (Exception e) {
            log.error("Error checking ownership for {} with id {}: {}",
                    entityType, resourceId, e.getMessage());
            return false;
        }
    }

    /**
     * Reads a field value from the User object reflectively.
     * Used for scoped permission checks where the field name is dynamic
     * (e.g. gets 'departmentId' from User.getDepartmentId()).
     */
    private Object getUserFieldValue(User user, String fieldName) {
        try {
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            return user.getClass().getMethod(getterName).invoke(user);
        } catch (Exception e) {
            log.error("Cannot read field '{}' from User: {}", fieldName, e.getMessage());
            return null;
        }
    }

    /**
     * Checks scoped access by verifying entity.{fieldName} == fieldValue.
     * Unlike ownership checks (which compare against userId),
     * this compares against the actual User field value (e.g. departmentId).
     */
    private boolean checkScopedAccess(Object resourceId, String entityType, String fieldName, Object fieldValue) {
        try {
            String jpql = String.format(
                    "SELECT COUNT(e) > 0 FROM %s e WHERE e.id = :id AND e.%s = :fieldVal",
                    entityType, fieldName
            );
            return Boolean.TRUE.equals(entityManager.createQuery(jpql, Boolean.class)
                    .setParameter("id", resourceId)
                    .setParameter("fieldVal", fieldValue)
                    .getSingleResult());
        } catch (Exception e) {
            log.error("Error checking scoped access for {} with id {}: {}",
                    entityType, resourceId, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {
        if (targetDomainObject == null) {
            log.warn("Cannot check permission on null target object");
            return false;
        }

        String entityType = targetDomainObject.getClass().getSimpleName();

        Serializable targetId = null;
        try {
            var idMethod = targetDomainObject.getClass().getMethod("getId");
            targetId = (Serializable) idMethod.invoke(targetDomainObject);
        } catch (Exception e) {
            log.warn("Could not extract ID from domain object: {}", e.getMessage());
        }

        return hasPermission(authentication, targetId, entityType, permission);
    }
}
