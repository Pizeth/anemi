package com.piseth.anemi.utils.model;

import java.util.List;

public class Role {
    private int id;
    private String roleName;
    private List<User> users;

    public Role() {}
    public Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
