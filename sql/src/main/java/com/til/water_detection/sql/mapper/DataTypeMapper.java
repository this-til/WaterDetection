package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.DataType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DataTypeMapper {

    @Insert("""
            insert into water_detection_data.data_type (user_id)
            values (#{userId})
            """)
    int addDataType(@Param("userId") int userId);

    @Delete("""
            delete
            from water_detection_data.data_type
            where id = #{id}
            """)
    int removeDataTypeById(int id);

    @Delete("""
            delete
            from water_detection_data.data_type
            where id = #{id} && user_id = #{userId}
            """)
    int removeDataTypeById( @Param("id")  int id,@Param("userId")  int userId);

    @Update("""
            update water_detection_data.data_type
            set another_name = #{anotherName}
            where id = #{id}
            """)
    int updateDataTypeAnotherNameById(@Param("id") int id, @Param("anotherName") String anotherName);

    @Update("""
            update water_detection_data.data_type
            set another_name = #{anotherName}
            where id = #{id} && user_id = #{userId}
            """)
    int updateDataTypeAnotherNameById(@Param("id") int id, @Param("userId") int userId, @Param("anotherName") String anotherName);

    @Select("""
            select *
            from water_detection_data.data_type
            where id = #{id}
            """)
    DataType getDataTypeById(int id);

    @Select("""
            select *
            from water_detection_data.data_type
            where id = #{id} && user_id = #{userId}
            """)
    DataType getDataTypeById(int id, int userId);

    @Select("""
            select *
            from water_detection_data.data_type
            where user_id = #{userId}
            """)
    List<DataType> getDataTypeListByUserId(int userId);

}
