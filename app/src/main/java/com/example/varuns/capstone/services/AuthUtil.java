package com.example.varuns.capstone.services;

import com.example.varuns.capstone.model.User;

public class AuthUtil {
    private static String token;
    private static User user;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AuthUtil.token = token;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        AuthUtil.user = user;
    }
}
