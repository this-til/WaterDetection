package com.til.water_detection.sql.mapper;


import com.til.water_detection.data.User;
import com.til.water_detection.sql.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

    @Test
    public void addUserTest() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = User.newUser("sasdds", "454");
        mapper.addUser(user.getUsername(), user.getPassword(), user.getSalt());
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void setUserPhotoByIdTest() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUserPhone(18, "114514");
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUserEmailTest() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUserEmail(18, "114514@163.com");
        sqlSession.commit();
        sqlSession.close();
    }


}
