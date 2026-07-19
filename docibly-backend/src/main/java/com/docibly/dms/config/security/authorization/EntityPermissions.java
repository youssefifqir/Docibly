package com.docibly.dms.config.security.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityPermissions {

    /**
     * Map of role name to permissions for that role on this entity.
     * Example: {"ADMIN": RolePermissions{all=["CREATE","READ","UPDATE","DELETE"], own=[]}}
     */
    private Map<String, RolePermissions> rolePermissions;
}
