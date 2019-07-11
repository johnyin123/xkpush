package com.xikang.push.rabbit.topic;

import com.xikang.push.dto.PushInfo;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @program: push
 * @description: 发送方
 * @author: Wangshihai
 * @create: 2019-07-02 09:58
 **/
@Component
public class TopicSender {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TopicSender.class);


    @Autowired
    private AmqpTemplate rabbitTemplate;


    public void  send(){
        String context = "hi, i am message all";
        System.out.println("sender: " + context);
        this.rabbitTemplate.convertAndSend("topicExchange","topic.1",context);
    }

    public void  sendTest(){
        String context = "hi, i am message hell test";
        System.out.println("sender: " + context);

        try {
            this.rabbitTemplate.convertAndSend("topicExchange","topic.test",context);

        }catch (AmqpException exception){

            logger.error("错误信息提示" + exception.getMessage());

        }

    }

    public void sendHygeaPush(){
        String context = "hi, i am message1";
        System.out.println("sender:" + context);
        PushInfo pushInfo = new PushInfo();
        pushInfo.setTitle("医生端新安装包");
        pushInfo.setBody("您有新的安装包");
        byte[] bytes= new byte[0];
        try {
            bytes = getBytesFromObject(pushInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(bytes);

        try {

            this.rabbitTemplate.convertAndSend("topicExchange","topic.xkhygea",bytes);

        }catch (AmqpException exception){

            logger.error("错误信息提示" + exception.getMessage());

        }
    }

    public void sendConsultPush(){
        String context = "hi, i am message2";
        System.out.println("sender:" + context);
        PushInfo pushInfo = new PushInfo();
        pushInfo.setTitle("医生端新安装包");
        pushInfo.setBody("您有新的安装包");
        byte[] bytes= new byte[0];
        try {
            bytes = getBytesFromObject(pushInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.rabbitTemplate.convertAndSend("topicExchange","topic.cfconsult",bytes);

        }catch (AmqpException exception){

            logger.error("错误信息提示" + exception.getMessage());

        }

        System.out.println(bytes);

    }

    //对象转化为字节码
    public  byte[] getBytesFromObject(Serializable obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }

}
