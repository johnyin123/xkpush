package com.xikang.push.service;

import com.xikang.push.dto.LoginInfoDto;

import java.util.List;

public interface LoginService {

    public int createLoginInfo(LoginInfoDto infoDto);

    public List<LoginInfoDto> loadLoginInfo(String appType,String status);
}
