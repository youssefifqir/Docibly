package com.docibly.dms.config.security.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissions {

    /**
     * Operations allowed on ANY resource (no ownership check).
     * Example: ["CREATE", "READ"]
     */
    @Builder.Default
    private Set<String> allOperations = Set.of();

    /**
     * Operations allowed ONLY on resources where createdBy == currentUser.
     * Example: ["UPDATE", "DELETE"]
     */
    @Builder.Default
    private Set<String> ownOperations = Set.of();

    /**
     * Operations filtered by a scoping field (e.g. department, tenant).
     * The scopeField identifies which entity attribute to match against the current user.
     * Example: scopedOperations=["READ"], scopeField="departmentId" → entity.departmentId == user.departmentId
     */
    @Builder.Default
    private Set<String> scopedOperations = Set.of();

    /**
     * Field name used for scoped filtering. Defaults to "createdBy".
     * Must exist on both the entity and the User bean.
     */
    @Builder.Default
    private String scopeField = "createdBy";
}
