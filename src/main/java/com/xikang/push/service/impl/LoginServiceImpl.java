package com.xikang.push.service.impl;

import com.xikang.push.dto.LoginInfoDto;
import com.xikang.push.mapper.LoginInfoMapper;
import com.xikang.push.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: push
 * @description: 登录信息继承
 * @author: Wangshihai
 * @create: 2019-07-04 11:54
 **/
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginInfoMapper loginInfoMapper;

    @Override
    public int createLoginInfo(LoginInfoDto infoDto) {
        loginInfoMapper.insertLoginInfo(infoDto);
        return 0;
    }

    @Override
    public List<LoginInfoDto> loadLoginInfo(String appType,String status) {
//        List<LoginInfoDto> loginInfoDtos = loginInfoMapper.findByStatus(status);
        List<LoginInfoDto> loginInfoDtos = loginInfoMapper.findByStatus(status,appType);
        return loginInfoDtos;
    }
}
