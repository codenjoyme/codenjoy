package com.codenjoy.dojo.services.entity.server;

public class User {

    private String email;
    private int approved;
    private String password;
    private String code;
    private String data;

    public User() {
        // do nothing
    }

    public User(String email, int approved, String password, String code, String data) {
        this.email = email;
        this.approved = approved;
        this.password = password;
        this.code = code;
        this.data = data;
    }

    public String getEmail() {
        return email;
    }

    public int getApproved() {
        return approved;
    }

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", email_approved=" + approved +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}