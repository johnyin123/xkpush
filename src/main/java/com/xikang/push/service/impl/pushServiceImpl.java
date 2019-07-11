package com.xikang.push.service.impl;

import com.xikang.push.dto.DeviceInfo;
import com.xikang.push.mapper.PushMapper;
import com.xikang.push.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @program: push
 * @description: 推送数据库查询类
 * @author: Wangshihai
 * @create: 2019-06-24 15:00
 **/
@Service
public class pushServiceImpl implements PushService {

    @Autowired
    private PushMapper pushMapper;

    @Override
    public int createPushTokenInfo(DeviceInfo deviceInfo) {
        pushMapper.insert(deviceInfo);
        return 0;
    }

    @Override
    public List<DeviceInfo> findall() {

        List<DeviceInfo> deviceInfos = pushMapper.findAll();
        return deviceInfos;
    }

    @Override
    public List <DeviceInfo> findByName(String appType) {
        List<DeviceInfo> deviceInfos = pushMapper.findByName(appType);
        return deviceInfos;
    }
}
