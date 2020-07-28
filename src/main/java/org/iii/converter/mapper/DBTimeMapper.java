package org.iii.converter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import org.iii.converter.datamodel.DBTime;

@Mapper
public interface DBTimeMapper {
    @Select("select CURRENT_TIMESTAMP as dbTime;")
    public DBTime getDBTime();
}
