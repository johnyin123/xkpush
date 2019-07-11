package com.xikang.push.dto;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Map;

/**
 * @program: push
 * @description: 推送信息
 * @author: Wangshihai
 * @create: 2019-07-02 13:04
 **/

public class PushInfo implements Serializable {

    private String title;
    private String body;
    private String badage;
    private Map<String,String>pushValues;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBadage() {
        return badage;
    }

    public void setBadage(String badage) {
        this.badage = badage;
    }

    public Map <String, String> getPushValues() {
        return pushValues;
    }

    public void setPushValues(Map <String, String> pushValues) {
        this.pushValues = pushValues;
    }

    @Override
    public String toString() {
        return "PushInfo{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", badage='" + badage + '\'' +
                ", pushValues=" + pushValues +
                '}';
    }
}
