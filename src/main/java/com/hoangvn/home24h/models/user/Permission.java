package com.hoangvn.home24h.models.user;

import javax.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {
    private String permissionName;
    private String permissionKey;

    public Permission() {
    }

    public Permission(String permissionName, String permissionKey) {
        this.permissionName = permissionName;
        this.permissionKey = permissionKey;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }
}
