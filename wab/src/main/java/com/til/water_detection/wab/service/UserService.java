package com.til.water_detection.wab.service;

import com.til.water_detection.data.User;

public interface UserService {

    User getUser(String username);

    User getUser(int id);

    void register(String username, String password);

    void updateUserPhoto(int id, String path);

    void updateUserEmail(int id, String email);

    void updateUserPassword(int id, String password);
}
