package com.til.water_detection.wab.service.impl;

import com.til.water_detection.data.User;
import com.til.water_detection.sql.mapper.UserMapper;
import com.til.water_detection.wab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User getUser(String username) {
        return userMapper.getUser(username);
    }

    @Override
    public void register(String username, String password) {
        User user = User.newUser(username, password);
        userMapper.addUser(username, user.getPassword(), user.getUsername());
    }

    @Override
    public User getUser(int id) {
        return userMapper.getUser(id);
    }

    @Override
    public void updateUserPhoto(int id, String path) {
        userMapper.updateUserPhone(id, path);
    }

    @Override
    public void updateUserEmail(int id, String email) {
        userMapper.updateUserEmail(id, email);
    }

    @Override
    public void updateUserPassword(int id, String password) {
        userMapper.updateUserPassword(id, password);
    }
}

