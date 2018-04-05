package com.androiddev.artemqa.gototrip.common.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by artemqa on 04.04.2018.
 */

public class Chat {

    private String mChatId;
    private Map<String, Boolean> mMembers = new HashMap<>();
    private Map<String, Boolean> mMessages = new HashMap<>();
    private String mLastMessageId;

    public Chat() {

    }

    public Chat(String chatId, Map<String, Boolean> members, Map<String, Boolean> messages, String lastMessageId) {
        mChatId = chatId;
        mMembers = members;
        mMessages = messages;
        mLastMessageId = lastMessageId;
    }

    public String getChatId() {
        return mChatId;
    }

    public void setChatId(String chatId) {
        mChatId = chatId;
    }

    public Map<String, Boolean> getMembers() {
        return mMembers;
    }

    public void setMembers(Map<String, Boolean> members) {
        mMembers = members;
    }

    public Map<String, Boolean> getMessages() {
        return mMessages;
    }

    public void setMessages(Map<String, Boolean> messages) {
        mMessages = messages;
    }

    public String getLastMessageId() {
        return mLastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        mLastMessageId = lastMessageId;
    }


}
