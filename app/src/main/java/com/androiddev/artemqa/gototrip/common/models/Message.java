package com.androiddev.artemqa.gototrip.common.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by artemqa on 04.04.2018.
 */

public class Message {


    private String mMessageId;
    private String mAuthorId;
    private String mChatId;
    private String mText;
    private HashMap<String, Object> mDateCreated;
    private String mAuthorUrlAvatar;

    public Message() {
    }

    public Message(String messageId, String authorId, String chatId, String text,String authorUrlAvatar) {
        mMessageId = messageId;
        mAuthorId = authorId;
        mChatId = chatId;
        mText = text;
        HashMap<String, Object> dateLastChangedObj = new HashMap<>();
        dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
        this.mDateCreated = dateLastChangedObj;
        mAuthorUrlAvatar = authorUrlAvatar;
    }


    @Exclude
    public long getDateCreatedLong() {
        return (long) mDateCreated.get("date");
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getChatId() {
        return mChatId;
    }

    public void setChatId(String chatId) {
        mChatId = chatId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public HashMap<String, Object> getDateCreated() {

        if (mDateCreated != null) {
            return mDateCreated;
        }

        HashMap<String, Object> dateCreatedObj = new HashMap<>();
        dateCreatedObj.put("date", ServerValue.TIMESTAMP);
        return dateCreatedObj;
    }

    public void setDateCreated(HashMap<String, Object> dateCreated) {
        this.mDateCreated = dateCreated;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String messageId) {
        mMessageId = messageId;
    }
    public String getAuthorUrlAvatar() {
        return mAuthorUrlAvatar;
    }

    public void setAuthorUrlAvatar(String authorUrlAvatar) {
        mAuthorUrlAvatar = authorUrlAvatar;
    }

}
