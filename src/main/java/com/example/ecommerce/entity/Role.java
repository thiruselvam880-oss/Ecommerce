package com.example.ecommerce.entity;



import java.util.Set;

public enum Role {
    ADMIN(Set.of(Permissions.UPDATE,Permissions.DELETE,Permissions.READ,Permissions.WRITE)),
    USER(Set.of(Permissions.READ));
    private final Set<Permissions> permissions ;
    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }
    public Set<Permissions> getPermissions() {
        return permissions;
    }

    }
