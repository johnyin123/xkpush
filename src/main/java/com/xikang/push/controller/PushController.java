package com.xikang.push.controller;

import com.xikang.push.apns.IOSPush;
import com.xikang.push.dto.DeviceInfo;
import com.xikang.push.rabbit.hello.HelloSender;
import com.xikang.push.rabbit.topic.TopicSender;
import com.xikang.push.service.PushService;
import com.xikang.push.service.impl.pushServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * @program: push
 * @description: 推送接口
 * @author: Wangshihai
 * @create: 2019-06-17 15:38
 **/
@Controller
public class PushController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PushController.class);

    @Autowired
    private PushService pushService;

//    @Autowired
//    private HelloSender helloSender;

    @Autowired
    private TopicSender topicSender;

    @ApiOperation(value = "发送推送医生")
    @RequestMapping(value = "/push/xfconsult",method = RequestMethod.GET)
    @ResponseBody
    public String pushXFconsult(){

        topicSender.sendConsultPush();
        return "success";
    }

    @ApiOperation(value = "发送推送患者")
    @RequestMapping(value = "/push/xkhygea",method = RequestMethod.GET)
    @ResponseBody
    public String pushXKhygea(){

        topicSender.sendHygeaPush();
        return "sucess";
    }

    @ApiOperation(value = "发送测试")
    @RequestMapping(value = "/push/test",method = RequestMethod.GET)
    @ResponseBody
    public String pushTest(){

        topicSender.sendTest();
        return "sucess";
    }


    /**
     * @return
     */
    @ApiOperation(value = "将token注册到数据库")
    @RequestMapping(value = "/regist/token",method = RequestMethod.POST)
    @ResponseBody
    public String registToken(@RequestBody DeviceInfo info){

        if ( !(StringUtils.isEmpty(info.getToken()) &&
                StringUtils.isEmpty(info.getAppType()) &&
                StringUtils.isEmpty(info.getPhrCode()) &&
                StringUtils.isEmpty(info.getUserName()))
                ){
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setAppType(info.getAppType());
            deviceInfo.setPhrCode(info.getPhrCode());
            deviceInfo.setToken(info.getToken());
            deviceInfo.setUserName(info.getUserName());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            deviceInfo.setUpdateTime(timestamp);
            info.setUpdateTime(timestamp);
            pushService.createPushTokenInfo(deviceInfo);
        }
        else{
            logger.error("数据为空");
        }


        return "success";
    }



}
