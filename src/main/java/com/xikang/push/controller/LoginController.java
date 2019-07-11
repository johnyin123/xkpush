package com.xikang.push.controller;

import com.xikang.push.dto.DeviceInfo;
import com.xikang.push.dto.LoginInfoDto;
import com.xikang.push.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @program: push
 * @description: 登录注册信息 获取信息
 * @author: Wangshihai
 * @create: 2019-07-04 13:30
 **/
@Controller
public class LoginController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginService loginService;

    /**
     * @return
     */
    @ApiOperation(value = "将token注册到数据库")
    @RequestMapping(value = "/login/regist",method = RequestMethod.POST)
    @ResponseBody
    public String registToken(@RequestBody LoginInfoDto info){

        if ( !(StringUtils.isEmpty(info.getPhrCode()) &&
                StringUtils.isEmpty(info.getUserName()) &&
                StringUtils.isEmpty(info.getPassword()) &&
                StringUtils.isEmpty(info.getNickName()) &&
                StringUtils.isEmpty(info.getStatus()))
                ){
            LoginInfoDto loginInfoDto = new LoginInfoDto();
            loginInfoDto.setPhrCode(info.getPhrCode());
            loginInfoDto.setNickName(info.getNickName());
            loginInfoDto.setUserName(info.getUserName());
            loginInfoDto.setPassword(info.getPassword());
            loginInfoDto.setStatus(info.getStatus());
            loginInfoDto.setAppType(info.getAppType());

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            loginInfoDto.setUpdateTime(timestamp);
            loginService.createLoginInfo(loginInfoDto);
        }
        else{
            logger.error("数据为空");
        }

        return "success";
    }


    /**
     * @return
     */
    @ApiOperation(value = "获取登录人信息")
    @RequestMapping(value = "/login/userinfo/{appType}/{status}",method = RequestMethod.GET)
    @ResponseBody
    public Object loginUserInfo(@PathVariable("appType") String appType,@PathVariable("status") String status){

        logger.info(status);

        List<LoginInfoDto> loginInfoDtos = loginService.loadLoginInfo(appType,status);

        return loginInfoDtos;
    }

}
