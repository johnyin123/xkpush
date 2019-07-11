package com.xikang.push.apns;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;


/**
 * @program: push
 * @description: IOS 推送客户端
 * @author: Wangshihai
 * @create: 2019-06-11 09:02
 **/
public class APNSConnect {

    private static  final Logger logger = LoggerFactory.getLogger(APNSConnect.class);

    private static ApnsClient apnsClientCFConsult = null;

    private static ApnsClient apnsClientHygea = null;

    public static ApnsClient getAPNSXFConsultConnect(){

        if (apnsClientCFConsult == null){

            try {
                InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/xk-hsp-m-xfconsult_Development.p12");
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup(30);
                apnsClientCFConsult = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                            .setClientCredentials(stream,"xikang")
                            .setConcurrentConnections(30).setEventLoopGroup(eventLoopGroup).build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return apnsClientCFConsult;
    }

    public static ApnsClient getAPNSHygaConnect(){

        if (apnsClientHygea == null){

            try {
                InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/xk-hsp-m-hygea_Development.p12");
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup(30);
                apnsClientHygea = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                        .setClientCredentials(stream,"xikang")
                        .setConcurrentConnections(30).setEventLoopGroup(eventLoopGroup).build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return apnsClientHygea;
    }

}
