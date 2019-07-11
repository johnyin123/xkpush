package com.xikang.push.mapper;

import com.xikang.push.dto.DeviceInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PushMapper {

    @Insert("INSERT INTO push_device_info(TOKEN,PHRCODE,APPTYPE,USERNAME,OPT_TIMESTAMP) " +
            "VALUES(#{token},#{phrCode},#{appType},#{userName},#{updateTime})" +
            "ON DUPLICATE KEY UPDATE PHRCODE = #{phrCode},APPTYPE = #{appType},USERNAME = #{userName},OPT_TIMESTAMP = #{updateTime}")
    @Results({
            @Result(property = "token", column = "TOKEN"),
            @Result(property = "phrCode", column = "PHRCODE"),
            @Result(property = "appType", column = "APPTYPE"),
            @Result(property = "userName", column = "USERNAME"),
            @Result(property = "updateTime", column = "OPT_TIMESTAMP")
    })
    int insert(DeviceInfo deviceInfo);


    @Select("SELECT * FROM push_device_info")
    @Results({
            @Result(property = "token", column = "TOKEN"),
            @Result(property = "phrCode", column = "PHRCODE"),
            @Result(property = "APPTYPE", column = "APPTYPE"),
            @Result(property = "userName", column = "USERNAME"),
            @Result(property = "updateTime", column = "OPT_TIMESTAMP")

    })
    List<DeviceInfo> findAll();

    @Select("SELECT * FROM push_device_info where APPTYPE = #{appType} order by OPT_TIMESTAMP DESC")
    @Results({
            @Result(property = "token", column = "TOKEN"),
            @Result(property = "phrCode", column = "PHRCODE"),
            @Result(property = "appType", column = "APPTYPE"),
            @Result(property = "userName", column = "USERNAME"),
            @Result(property = "updateTime", column = "OPT_TIMESTAMP")

    })
    List<DeviceInfo> findByName(String  appType);
}
