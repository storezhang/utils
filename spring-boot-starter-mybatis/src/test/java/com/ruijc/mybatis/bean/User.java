package com.ruijc.mybatis.bean;

import com.ruijc.IdObject;

public class User extends IdObject {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
