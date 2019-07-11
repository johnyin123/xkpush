package com.xikang.push.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @program: push
 * @description: 登录信息dto
 * @author: Wangshihai
 * @create: 2019-07-04 11:55
 **/
@Data
public class LoginInfoDto {
    private String phrCode;
    private String appType;
    private String status;
    private String userName;
    private String password;
    private String nickName;
    private Timestamp updateTime;
}
