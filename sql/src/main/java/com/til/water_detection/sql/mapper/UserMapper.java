package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("""
            insert into water_detection_data.user ( username, password, salt)
            values (#{username}, #{password}, #{salt})
            """)
    int addUser(@Param("username") String username, @Param("password") String password, @Param("salt") String salt);

    @Update("""
            update water_detection_data.user
            set phone = #{phone}
            where id = #{id}
            """)
    int updateUserPhone(@Param("id") int id, @Param("phone") String phone);

    @Update("""
            update  water_detection_data.user
            set email = #{email}
            where id = #{id}
            """)
    int updateUserEmail(@Param("id") int id, @Param("email") String email);

    @Update("""
            update  water_detection_data.user
            set password = #{password}
            where id = #{id}
            """)
    int updateUserPassword(@Param("id") int id, @Param("password") String password);

    @Select("""
            select *
            from  water_detection_data.user
            where id = #{id}
            """)
    User getUser(int id);

    @Select("""
            select  *
            from water_detection_data.user
            where username = #{username}
            """)
    User getUser(String username);
}
