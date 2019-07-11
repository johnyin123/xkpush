package com.xikang.push.apns;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import com.turo.pushy.apns.util.concurrent.PushNotificationResponseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: push
 * @description: ios 推送静态定义
 * @author: Wangshihai
 * @create: 2019-06-11 09:33
 **/
public class IOSPush {

    private static final Logger logger = LoggerFactory.getLogger(IOSPush.class);

    private static ApnsClient apnsClient = null;

    private static final Semaphore semaphore = new Semaphore(10000);//Semaphore又称信号量，是操作系统中的一个概念，在Java并发编程中，信号量控制的是线程并发的数量。

    /**
     *
     * ios的推送
     * @param deviceTokens
     * @param alertTitle
     * @param alertBody
     * @param contentAvailable true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
     * @param customProperty 附加参数
     * @param badge 如果badge小于0，则不推送这个右上角的角标，主要用于消息盒子新增或者已读时，更新此状态
     */
    @SuppressWarnings("rawtypes")
    public static void pushToHygea(final List<String> deviceTokens, String alertTitle, String alertBody, boolean contentAvailable, Map<String, Object> customProperty, int badge) {

        long startTime = System.currentTimeMillis();

        ApnsClient apnsClient = APNSConnect.getAPNSHygaConnect();

        long total = deviceTokens.size();

        final CountDownLatch latch = new CountDownLatch(deviceTokens.size());//每次完成一个任务（不一定需要线程走完），latch减1，直到所有的任务都完成，就可以执行下一阶段任务，可提高性能

        final AtomicLong successCnt = new AtomicLong(0);//线程安全的计数器

        long startPushTime =  System.currentTimeMillis();

        for (String deviceToken : deviceTokens) {

            ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();

            if (alertBody != null && alertTitle != null) {
                payloadBuilder.setAlertBody(alertBody);
                payloadBuilder.setAlertTitle(alertTitle);
            }


            //如果badge小于0，则不推送这个右上角的角标，主要用于消息盒子新增或者已读时，更新此状态
            if (badge > 0) {
                payloadBuilder.setBadgeNumber(badge);
            }

            //将所有的附加参数全部放进去
            if (customProperty != null) {
                for (Map.Entry<String, Object> map : customProperty.entrySet()) {
                    payloadBuilder.addCustomProperty(map.getKey(), map.getValue());
                }
            }

            // true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
            payloadBuilder.setContentAvailable(contentAvailable);

            String payload = payloadBuilder.buildWithDefaultMaximumLength();
            final String token = TokenUtil.sanitizeTokenString(deviceToken);
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "com.xikang.XKHygea", payload);

            try {
                semaphore.acquire();//从信号量中获取一个允许机会
            } catch (Exception e) {
                logger.error("ios push get semaphore failed, deviceToken:{}", deviceToken);//线程太多了，没有多余的信号量可以获取了
                e.printStackTrace();
            }

            final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                    sendNotificationFuture = apnsClient.sendNotification(pushNotification);

            //---------------------------------------------------------------------------------------------------
            try {
                final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();

                sendNotificationFuture.addListener(new PushNotificationResponseListener<SimpleApnsPushNotification>() {

                    @Override
                    public void operationComplete(final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future) throws Exception {
                        // When using a listener, callers should check for a failure to send a
                        // notification by checking whether the future itself was successful
                        // since an exception will not be thrown.
                        if (future.isSuccess()) {
                            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                                    sendNotificationFuture.getNow();
                            if (pushNotificationResponse.isAccepted()) {
                                successCnt.incrementAndGet();
                            } else {
                                Date invalidTime = pushNotificationResponse.getTokenInvalidationTimestamp();
                                logger.error("Notification rejected by the APNs gateway: " + pushNotificationResponse.getRejectionReason());
                                if (invalidTime != null) {
                                    logger.error("\t…and the token is invalid as of " + pushNotificationResponse.getTokenInvalidationTimestamp());
                                }
                            }
                            // Handle the push notification response as before from here.
                        } else {
                            // Something went wrong when trying to send the notification to the
                            // APNs gateway. We can find the exception that caused the failure
                            // by getting future.cause().
                            future.cause().printStackTrace();
                        }
                        latch.countDown();
                        semaphore.release();//释放允许，将占有的信号量归还
                    }
                });

            } catch (final ExecutionException e) {
                System.err.println("Failed to send push notification.");
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //---------------------------------------------------------------------------------------------------

        }

        long endPushTime = System.currentTimeMillis();

        logger.info("test pushMessage success. [共推送" + total + "个][成功" + (successCnt.get()) + "个],totalcost= " + (endPushTime - startTime) + ", pushCost=" + (endPushTime - startPushTime));
    }


    /**
     *
     * ios的推送
     * @param deviceTokens
     * @param alertTitle
     * @param alertBody
     * @param contentAvailable true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
     * @param customProperty 附加参数
     * @param badge 如果badge小于0，则不推送这个右上角的角标，主要用于消息盒子新增或者已读时，更新此状态
     */
    @SuppressWarnings("rawtypes")
    public static void pushToXFConsult(final List<String> deviceTokens, String alertTitle, String alertBody, boolean contentAvailable, Map<String, Object> customProperty, int badge) {

        long startTime = System.currentTimeMillis();

        ApnsClient apnsClient = APNSConnect.getAPNSXFConsultConnect();

        long total = deviceTokens.size();

        final CountDownLatch latch = new CountDownLatch(deviceTokens.size());//每次完成一个任务（不一定需要线程走完），latch减1，直到所有的任务都完成，就可以执行下一阶段任务，可提高性能

        final AtomicLong successCnt = new AtomicLong(0);//线程安全的计数器

        long startPushTime =  System.currentTimeMillis();

        for (String deviceToken : deviceTokens) {

            ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();

            if (alertBody != null && alertTitle != null) {
                payloadBuilder.setAlertBody(alertBody);
                payloadBuilder.setAlertTitle(alertTitle);
            }

            //如果badge小于0，则不推送这个右上角的角标，主要用于消息盒子新增或者已读时，更新此状态
            if (badge > 0) {
                payloadBuilder.setBadgeNumber(badge);
            }


            //将所有的附加参数全部放进去
            if (customProperty != null) {
                for (Map.Entry<String, Object> map : customProperty.entrySet()) {
                    payloadBuilder.addCustomProperty(map.getKey(), map.getValue());
                }
            }

            // true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
            payloadBuilder.setContentAvailable(contentAvailable);

            payloadBuilder.setSound("default");

            String payload = payloadBuilder.buildWithDefaultMaximumLength();
            final String token = TokenUtil.sanitizeTokenString(deviceToken);
            SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "com.xikang.XFConsultant", payload);

            try {
                semaphore.acquire();//从信号量中获取一个允许机会
            } catch (Exception e) {
                logger.error("ios push get semaphore failed, deviceToken:{}", deviceToken);//线程太多了，没有多余的信号量可以获取了
                e.printStackTrace();
            }

            final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                    sendNotificationFuture = apnsClient.sendNotification(pushNotification);

            //---------------------------------------------------------------------------------------------------
            try {
                final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();

                sendNotificationFuture.addListener(new PushNotificationResponseListener<SimpleApnsPushNotification>() {

                    @Override
                    public void operationComplete(final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future) throws Exception {
                        // When using a listener, callers should check for a failure to send a
                        // notification by checking whether the future itself was successful
                        // since an exception will not be thrown.
                        if (future.isSuccess()) {
                            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                                    sendNotificationFuture.getNow();
                            if (pushNotificationResponse.isAccepted()) {
                                successCnt.incrementAndGet();
                            } else {
                                Date invalidTime = pushNotificationResponse.getTokenInvalidationTimestamp();
                                logger.error("Notification rejected by the APNs gateway: " + pushNotificationResponse.getRejectionReason());
                                if (invalidTime != null) {
                                    logger.error("\t…and the token is invalid as of " + pushNotificationResponse.getTokenInvalidationTimestamp());
                                }
                            }
                            // Handle the push notification response as before from here.
                        } else {
                            // Something went wrong when trying to send the notification to the
                            // APNs gateway. We can find the exception that caused the failure
                            // by getting future.cause().
                            future.cause().printStackTrace();
                        }
                        latch.countDown();
                        semaphore.release();//释放允许，将占有的信号量归还
                    }
                });

            } catch (final ExecutionException e) {
                System.err.println("Failed to send push notification.");
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //---------------------------------------------------------------------------------------------------

        }

        long endPushTime = System.currentTimeMillis();

        logger.info("test pushMessage success. [共推送" + total + "个][成功" + (successCnt.get()) + "个],totalcost= " + (endPushTime - startTime) + ", pushCost=" + (endPushTime - startPushTime));
    }

}
