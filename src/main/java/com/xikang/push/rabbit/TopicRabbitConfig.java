package com.xikang.push.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: push
 * @description: 配置
 * @author: Wangshihai
 * @create: 2019-07-02 09:57
 **/
@Configuration
public class TopicRabbitConfig {

    final static String message = "topic.xkhygea";
    final static String messages = "topic.cfconsult";
    final static String messageTest = "topic.test";

    @Bean
    public Queue queueMessage(){ return new Queue(TopicRabbitConfig.message);}

    @Bean
    public Queue queueMessages(){return new Queue(TopicRabbitConfig.messages);}

    @Bean
    public Queue queueMessageTest(){return  new Queue(TopicRabbitConfig.messageTest);};

    @Bean
    TopicExchange exchange(){return  new TopicExchange("topicExchange");}

    @Bean
    Binding bindingExchange(Queue queueMessage, TopicExchange exchange){
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.xkhygea");
    }

    @Bean
    Binding bindingExchangeMessages(Queue queueMessages,TopicExchange exchange){
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.cfconsult");
    }

    @Bean
    Binding bindingExchangeMessageTest(Queue queueMessageTest, TopicExchange exchange){
        return BindingBuilder.bind(queueMessageTest).to(exchange).with("topic.test");
    }


}
