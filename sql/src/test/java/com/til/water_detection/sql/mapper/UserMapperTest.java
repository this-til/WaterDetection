package com.til.water_detection.sql.mapper;


import com.til.water_detection.data.User;
import com.til.water_detection.sql.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class UserMapperTest {

    @Test
    public void addUserTest() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = User.newUser("sasdds", "454");
        mapper.addUser(user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void setUserPhotoByIdTest() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.setUserPhotoById(18, "114514");
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void setUserEmailByIdTest() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.setUserEmailById(18, "114514@163.com");
        sqlSession.commit();
        sqlSession.close();
    }


}
