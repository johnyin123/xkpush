package com.xikang.push.dto;
import lombok.Data;
import java.sql.Timestamp;

/**
 * @program: push
 * @description: 推送信息结构体
 * @author: Wangshihai
 * @create: 2019-06-21 16:14
 **/
@Data
public class DeviceInfo {

    private String token;
    private String phrCode;
    private String appType;
    private String userName;
    private Timestamp updateTime;

}
