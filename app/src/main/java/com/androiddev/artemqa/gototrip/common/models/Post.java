package com.androiddev.artemqa.gototrip.common.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by artjk on 09.04.2018.
 */

public class Post {

    private String mPostId;
    private String mTitlePost;
    private String mAuthorId;
    private String mAuthorName;
    private String mAuthorUriAvatar;
    private String mTextPost;
    private String mPhotoUrlPost;
    private Long mPostDate;
    private Double mLatitudeMap;
    private Double mLongitudeMap;
    @Exclude
    private ArrayList<Photo> mPhotos;
    private HashMap<String, Object> mDateCreated = new HashMap<>();
    private Map<String, Boolean> mLikeUsers = new HashMap<>();
    private Map<String, Boolean> mComments = new HashMap<>();
    private Map<String,Boolean> mListPhoto = new HashMap<>();
    private Map<String, Boolean> mViewUsers = new HashMap<>();

    public Post(String postId,String titlePost, String authorId, String authorName,
                String authorUriAvatar, String textPost,
                Long postDate,Map<String,Boolean> listPhoto,Double latitudeMap,Double longitudeMap) {
        mPostId = postId;
        mTitlePost = titlePost;
        mAuthorId = authorId;
        mAuthorName = authorName;
        mAuthorUriAvatar = authorUriAvatar;
        mTextPost = textPost;
        HashMap<String, Object> dateLastChangedObj = new HashMap<>();
        dateLastChangedObj.put("date", ServerValue.TIMESTAMP);
        mDateCreated = dateLastChangedObj;
        mPostDate = postDate;
        mListPhoto = listPhoto;
        mLatitudeMap = latitudeMap;
        mLongitudeMap = longitudeMap;
    }

    public Post() {

    }

    public Long getPostDate() {
        return mPostDate;
    }

    public void setPostDate(Long postDate) {
        mPostDate = postDate;
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

    public Map<String, Object> getDateCreated() {

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

    public Map<String, Boolean> getLikeUsers() {
        return mLikeUsers;
    }

    public void setLikeUsers(Map<String, Boolean> likeUsers) {
        mLikeUsers = likeUsers;
    }

    public Map<String, Boolean> getComments() {
        return mComments;
    }

    public void setComments(Map<String, Boolean> comments) {
        mComments = comments;
    }

    @Exclude
    public long getDateCreatedLong() {
        return (long) mDateCreated.get("date");
    }
    public Double getLatitudeMap() {
        return mLatitudeMap;
    }

    public void setLatitudeMap(Double latitudeMap) {
        mLatitudeMap = latitudeMap;
    }

    public Double getLongitudeMap() {
        return mLongitudeMap;
    }

    public void setLongitudeMap(Double longitudeMap) {
        mLongitudeMap = longitudeMap;
    }

    public Map<String, Boolean> getListPhoto() {
        return mListPhoto;
    }

    public void setListPhoto(Map<String, Boolean> listPhoto) {
        mListPhoto = listPhoto;
    }

    public ArrayList<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        mPhotos = photos;
    }
    public Map<String, Boolean> getViewUsers() {
        return mViewUsers;
    }

    public void setViewUsers(Map<String, Boolean> viewUsers) {
        mViewUsers = viewUsers;
    }
}
