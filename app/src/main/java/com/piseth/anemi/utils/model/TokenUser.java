package com.piseth.anemi.utils.model;

public class TokenUser extends User {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public TokenUser(User user)
    {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setAvatar(user.getAvatar());
        this.setUsername(user.getUsername());
        this.setRoleId(user.getRoleId());
        this.setPhone(user.getPhone());
        this.setAddress(user.getAddress());
        this.setDob(user.getDob());
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
