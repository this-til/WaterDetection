package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    int addUser(User user);

    int setUserPhotoById(@Param("id") int id, @Param("phone") String phone);

    int setUserEmailById(@Param("id") int id, @Param("email") String email);

    User getUserByID(int id);

    User getUserByUsername(String username);
}
