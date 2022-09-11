package com.raptor.entity;

import java.io.Serializable;

/**
 * @author 陈文豪(chenwenhao.0401 @ bytedance.com)
 * @version 1.0
 * @created 2022/9/10  22:53
 */
public class Message implements Serializable {
    public static final long serialVersionMessage = 1L;

    private String sender;
    private String getter;
    private String sendTime;
    private String content;
    private String msgType;

    public Message() {
    }

    public Message(String sender, String getter, String sendTime, String content, String msgType) {
        this.sender = sender;
        this.getter = getter;
        this.sendTime = sendTime;
        this.content = content;
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
