package com.xikang.push.rabbit.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: push
 * @description: 接受测试
 * @author: Wangshihai
 * @create: 2019-07-09 10:39
 **/
@Component
@RabbitListener(queues = "topic.test")
public class TopicRecevierTest {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver  : " + hello);
    }

}

