package com.til.water_detection.data;

import org.jetbrains.annotations.Nullable;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public final class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    @Nullable
    private String email;
    @Nullable
    private String phone;

    public User(int id, String username, String password, String salt, @Nullable String email, @Nullable String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.phone = phone;
    }

    public static User newUser(String username, String password) {
        String salt = Util.generateSalt(16);
        return new User(-1, username, Util.hashPassword(password, salt), salt, null, null);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }


}
