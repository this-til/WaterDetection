package com.til.water_detection.sql.mapper;

import com.til.water_detection.data.DataType;
import org.apache.ibatis.annotations.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mapper
public interface IDataTypeMapper {

    @Insert("""
            insert into water_detection_data.data_type (name)
            values (#{name})
            """)
    int registerDataType(String name);

    @Delete("""
            delete
            from water_detection_data.data_type
            where id = #{id}
            """)
    int removeDataTypeById(@Param("id") int id);

    @Update("""
            update water_detection_data.data_type
            set name = #{anotherName}
            where id = #{id}
            """)
    int updateDataTypeAnotherNameById(@Param("id") int id, @Param("anotherName") String anotherName);

    @Update("""
            update water_detection_data.data_type
            set suffix = #{suffix}
            where id = #{id}
            """)
    int updateDataTypeSuffixById(@Param("id") int id, @Param("suffix") String suffix);

    @Update("""
            update water_detection_data.data_type
            set percent = #{percent}
            where id = #{id}
            """)
    int updateDataTypePercentById(@Param("id") int id, @Param("percent") boolean percent);

    @Select("""
            select *
            from water_detection_data.data_type
            where id = #{id}
            """)
    @Nullable
    DataType getDataTypeById(@Param("id") int id);

    @Select("""
            select *
            from water_detection_data.data_type
            where name = #{name}
            """)
    @Nullable
    DataType getDataTypeByName(@Param("name")  String name);

    @Select("""
            select *
            from water_detection_data.data_type
            """)
    List<DataType> getAllDataType();


    List<DataType> getDataTypeByIdArray(@Param("id") int[] id);

    List<DataType> getDataTypeByNameArray(@Param("name") String[] name);
}
