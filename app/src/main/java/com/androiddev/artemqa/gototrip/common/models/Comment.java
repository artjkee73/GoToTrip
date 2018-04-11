package com.androiddev.artemqa.gototrip.common.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by artemqa on 11.04.2018.
 */

public class Comment {

    private String mAuthorId;
    private String mNameAuthor;
    private String mPostId;
    private String mUrlAvatarAuthor;
    private String mTextComment;
    private HashMap<String, Object> mDateCreated;
    private String mCommentId;

    public Comment(String commentId, String authorId, String nameAuthor, String urlAvatarAuthor, String textComment,String postId) {
        mCommentId = commentId;
        mAuthorId = authorId;
        mNameAuthor = nameAuthor;
        mUrlAvatarAuthor = urlAvatarAuthor;
        mTextComment = textComment;
        HashMap<String, Object> dateLastChangedObj = new HashMap<>();
        dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
        this.mDateCreated = dateLastChangedObj;
        mPostId = postId;
    }

    public Comment() {

    }

    public String getCommentId() {
        return mCommentId;
    }

    public void setCommentId(String commentId) {
        mCommentId = commentId;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getNameAuthor() {
        return mNameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        mNameAuthor = nameAuthor;
    }

    public String getUrlAvatarAuthor() {
        return mUrlAvatarAuthor;
    }

    public void setUrlAvatarAuthor(String urlAvatarAuthor) {
        mUrlAvatarAuthor = urlAvatarAuthor;
    }

    public String getTextComment() {
        return mTextComment;
    }

    public void setTextComment(String textComment) {
        mTextComment = textComment;
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

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
    }

    @Exclude
    public long getDateCreatedLong() {
        return (long) mDateCreated.get("date");
    }
}
