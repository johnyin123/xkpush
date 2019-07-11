package com.xikang.push.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @program: push
 * @description: bundleId配置
 * @author: Wangshihai
 * @create: 2019-06-24 09:39
 **/
@Data
@ConfigurationProperties(prefix="xikang.push")
@PropertySource("classpath:application.properties")
@Component
public class ProjectBundleConfig {

    /**
     * 推送hygea bundle id
     */
    public String hygea;
    /**
     * 推送consult bundle id
     */
    public String consult;

}
