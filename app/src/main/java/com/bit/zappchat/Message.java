package com.bit.zappchat;

public class Message {
    String msgId;
    String senderId;
    String message;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message() {
    }

    public Message(String msgId, String senderId, String message) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
    }
}
