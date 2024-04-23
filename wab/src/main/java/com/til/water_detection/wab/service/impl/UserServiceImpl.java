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
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public void register(String username, String password) {
        User user = User.newUser(username, password);
        userMapper.addUser(username, user.getPassword(), user.getUsername());
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserByID(id);
    }

    @Override
    public void setUserPhotoById(int id, String path) {
        userMapper.setUserPhoneById(id, path);
    }

    @Override
    public void setUserEmailById(int id, String email) {
        userMapper.setUserEmailById(id, email);
    }

    @Override
    public void setUserPasswordById(int id, String password) {
        userMapper.setUserPasswordById(id, password);
    }
}

