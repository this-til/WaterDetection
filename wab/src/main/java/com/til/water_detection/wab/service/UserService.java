package com.til.water_detection.wab.service;

import com.til.water_detection.data.User;

public interface UserService {

    User getUserByUsername(String username);

    User getUserById(int id);

    void register(String username, String password);

    void setUserPhotoById(int id, String path);

    void setUserEmailById(int id, String email);

    void setUserPasswordById(int id, String password);
}
