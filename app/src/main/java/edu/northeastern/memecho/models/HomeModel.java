package edu.northeastern.memecho.models;

public class HomeModel {
    private String userName, timestamp, profileImage, postImage, id;
    private int likeCount;

    public HomeModel() {
    }

    public HomeModel(String userName, String timestamp, String profileImage, String postImage, String id, int likeCount) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.id = id;
        this.likeCount = likeCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
