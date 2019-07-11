package com.xikang.push.rabbit.topic;

import com.xikang.push.apns.IOSPush;
import com.xikang.push.dto.DeviceInfo;
import com.xikang.push.dto.PushInfo;
import com.xikang.push.service.PushService;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: push
 * @description: topic 接收方
 * @author: Wangshihai
 * @create: 2019-07-02 09:56
 **/
@Component
@RabbitListener(queues = "topic.xkhygea")
public class TopicReceiver {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TopicReceiver.class);

    @Autowired
    PushService pushService;

    @RabbitHandler
    public void process(byte[] bytes){

        PushInfo pushInfo= null;
        try {
            pushInfo = (PushInfo) getObjectFromBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> devicesToken = new ArrayList<>();
        List<DeviceInfo> infos =  pushService.findByName("XKHygea");
        for (DeviceInfo info:infos){
            String token = info.getToken();
            devicesToken.add(token);
            logger.info("token is:"+token);
        }

        if (pushInfo!=null && (pushInfo.getTitle().length()>0) && (pushInfo.getBody().length() >0)){

            IOSPush.pushToHygea(devicesToken,pushInfo.getTitle(),pushInfo.getBody(),false,null,1);
        }
        else{
            logger.error("医生端端发送消息体为空");
        }




        System.out.println("Topic Recevier1 :" + pushInfo.getTitle());
    }

    //字节码转化为对象
    public  Object getObjectFromBytes(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

}
