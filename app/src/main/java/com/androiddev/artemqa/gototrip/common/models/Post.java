package com.androiddev.artemqa.gototrip.common.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by artjk on 09.04.2018.
 */

public class Post {
    private String mPostId;

    public Post(String postId, String titlePost, String authorId, String authorName, String authorUriAvatar, String textPost, String photoUrlPost, HashMap<String, Object> dateCreated) {
        mPostId = postId;
        mTitlePost = titlePost;
        mAuthorId = authorId;
        mAuthorName = authorName;
        mAuthorUriAvatar = authorUriAvatar;
        mTextPost = textPost;
        mPhotoUrlPost = photoUrlPost;
        HashMap<String, Object> dateLastChangedObj = new HashMap<>();
        dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
        mDateCreated = dateLastChangedObj;
    }

    private String mTitlePost;
    private String mAuthorId;
    private String mAuthorName;
    private String mAuthorUriAvatar;
    private String mTextPost;
    private String mPhotoUrlPost;
    private HashMap<String, Object> mDateCreated;
    private HashMap<String, Boolean> mLikeUsers;
    private HashMap<String, Boolean> mComments;

    public Post() {

    }


    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
    }

    public String getTitlePost() {
        return mTitlePost;
    }

    public void setTitlePost(String titlePost) {
        mTitlePost = titlePost;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getAuthorUriAvatar() {
        return mAuthorUriAvatar;
    }

    public void setAuthorUriAvatar(String authorUriAvatar) {
        mAuthorUriAvatar = authorUriAvatar;
    }

    public String getTextPost() {
        return mTextPost;
    }

    public void setTextPost(String textPost) {
        mTextPost = textPost;
    }

    public String getPhotoUrlPost() {
        return mPhotoUrlPost;
    }

    public void setPhotoUrlPost(String photoUrlPost) {
        mPhotoUrlPost = photoUrlPost;
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
        mDateCreated = dateCreated;
    }

    public HashMap<String, Boolean> getLikeUsers() {
        return mLikeUsers;
    }

    public void setLikeUsers(HashMap<String, Boolean> likeUsers) {
        mLikeUsers = likeUsers;
    }

    public HashMap<String, Boolean> getComments() {
        return mComments;
    }

    public void setComments(HashMap<String, Boolean> comments) {
        mComments = comments;
    }

    @Exclude
    public long getDateCreatedLong() {
        return (long) mDateCreated.get("date");
    }
}
