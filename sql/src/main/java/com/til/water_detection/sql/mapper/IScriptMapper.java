package com.til.water_detection.sql.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface IScriptMapper {

    @Insert("""
            insert into water_detection_data.script(id, script)
            values(LAST_INSERT_ID(),#{script})
            """)
    int registerScript(String script);

    @Delete("""
            delete from water_detection_data.script
            where script=#{script}
            """)
    int removeScript(int id);

    @Update("""
            update water_detection_data.script
            set script=#{script}
            where id =#{id}
            """)
    int updateScriptById(@Param("id") int id, @Param("script") String script);

    @Select("""
            select script from water_detection_data.script
            where id=#{id}
            """)
    String getScriptById(int id);

    @Select("""
            SELECT LAST_INSERT_ID()
            """)
    int getLastInsertId();
}
