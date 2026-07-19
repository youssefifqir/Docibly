package com.docibly.dms.config.security.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Configuration
public class PermissionConfig {

    @Bean
    public Map<String, EntityPermissions> permissionRegistry() {
        Map<String, EntityPermissions> registry = new LinkedHashMap<>();

        // Organization permissions
        registry.put("Organization", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("READ"))
                                .build()
                ))
                .build());

        // OrganizationRequest permissions
        registry.put("OrganizationRequest", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("CREATE"))
                                .ownOperations(Set.of("READ"))
                                .build()
                ))
                .build());

        // OrganizationMember permissions
        registry.put("OrganizationMember", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .ownOperations(Set.of("READ"))
                                .build()
                ))
                .build());

        // OrganizationInvitation permissions
        registry.put("OrganizationInvitation", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .ownOperations(Set.of("READ"))
                                .build()
                ))
                .build());

        // Folder permissions
        registry.put("Folder", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build()
                ))
                .build());

        // Document permissions
        registry.put("Document", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build()
                ))
                .build());

        // DocumentVersion permissions
        registry.put("DocumentVersion", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build()
                ))
                .build());

        // DocumentShare permissions
        registry.put("DocumentShare", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build()
                ))
                .build());

        // DocumentComment permissions
        registry.put("DocumentComment", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build()
                ))
                .build());

        // AuditLog permissions
        registry.put("AuditLog", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ"))
                                .build()
                ))
                .build());

        // Notification permissions
        registry.put("Notification", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .ownOperations(Set.of("READ", "UPDATE", "DELETE"))
                                .build()
                ))
                .build());

        // Tag permissions
        registry.put("Tag", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build()
                ))
                .build());

        // SearchIndex permissions
        registry.put("SearchIndex", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build()
                ))
                .build());

        // Department permissions
        registry.put("Department", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("READ"))
                                .build()
                ))
                .build());

        // DepartmentMember permissions
        registry.put("DepartmentMember", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .ownOperations(Set.of("READ"))
                                .build()
                ))
                .build());

        // DepartmentDocumentShare permissions
        registry.put("DepartmentDocumentShare", EntityPermissions.builder()
                .rolePermissions(Map.of(
                        "ADMIN", RolePermissions.builder()
                                .allOperations(Set.of("CREATE", "READ", "UPDATE", "DELETE"))
                                .build(),
                        "USER", RolePermissions.builder()
                                .allOperations(Set.of("READ"))
                                .build()
                ))
                .build());

        return registry;
    }
}
