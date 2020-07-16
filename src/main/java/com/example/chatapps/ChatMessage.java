package com.example.chatapps;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messegeUser;
    private long messageTime;

    public ChatMessage(String messageText, String messegeUser){
        this.messageText = messageText;
        this.messegeUser = messegeUser;

        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {

        return messageText;
    }

    public void setMessageText(String messageText) {

        this.messageText = messageText;
    }

    public String getMessegeUser() {

        return messegeUser;
    }

    public void setMessegeUser(String messegeUser) {

        this.messegeUser = messegeUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
