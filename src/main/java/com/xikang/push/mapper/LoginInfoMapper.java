package com.xikang.push.mapper;

import com.xikang.push.dto.DeviceInfo;
import com.xikang.push.dto.LoginInfoDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface LoginInfoMapper {

    @Insert("INSERT INTO login_user_info(PHRCODE,DEVSTATUS,APPTYPE,USERNAME,PASSWORD,NICKNAME,OPT_TIMESTAMP) " +
            "VALUES(#{phrCode},#{status},#{appType},#{userName},#{password},#{nickName},#{updateTime})" +
            "ON DUPLICATE KEY UPDATE PHRCODE = #{phrCode},DEVSTATUS = #{status},APPTYPE = #{appType},USERNAME = #{userName},PASSWORD = #{password},NICKNAME = #{nickName},OPT_TIMESTAMP = #{updateTime}")
    @Results({
            @Result(property = "phrCode", column = "PHRCODE"),
            @Result(property = "status", column = "DEVSTATUS"),
            @Result(property = "appType", column = "APPTYPE"),
            @Result(property = "userName", column = "USERNAME"),
            @Result(property = "password", column = "PASSWORD"),
            @Result(property = "nickName", column = "NICKNAME"),
            @Result(property = "updateTime", column = "OPT_TIMESTAMP")
    })
    int insertLoginInfo(LoginInfoDto infoDto);



    @Select("SELECT * FROM login_user_info where DEVSTATUS like #{status} and APPTYPE like  #{appType} order by OPT_TIMESTAMP DESC")
    @Results({
            @Result(property = "phrCode", column = "PHRCODE"),
            @Result(property = "status", column = "DEVSTATUS"),
            @Result(property = "appType", column = "APPTYPE"),
            @Result(property = "userName", column = "USERNAME"),
            @Result(property = "password", column = "PASSWORD"),
            @Result(property = "nickName", column = "NICKNAME"),
            @Result(property = "updateTime", column = "OPT_TIMESTAMP")

    })
      List<LoginInfoDto> findByStatus(@Param("status") String status,@Param("appType") String appType);
}
