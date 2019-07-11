package com.xikang.push.service;

import com.xikang.push.dto.DeviceInfo;

import java.util.List;


/**
 * @author wangshihai
 */
public interface PushService {

    public int createPushTokenInfo(DeviceInfo deviceInfo);

    public List<DeviceInfo> findall();

    public List<DeviceInfo> findByName(String appType);
}
