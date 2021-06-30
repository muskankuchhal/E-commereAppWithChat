package com.example.e_commerce.Model;

public class Message {

    private String messageId,message, senderId;
    private int feeling;

    public Message() {
        feeling=-1;
    }

    public Message(String messageId,String message, String senderId) {
        this.messageId=messageId;
        this.message = message;
        this.senderId = senderId;


    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }
}

