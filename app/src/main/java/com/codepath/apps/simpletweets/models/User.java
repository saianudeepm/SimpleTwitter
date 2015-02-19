package com.codepath.apps.simpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saianudeepm on 2/9/15.
 */
public class User {

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private String backgroundImageUrl;
    private int followersCount;
    private int tweetsCount;
    private int followingCount;
    private int retweetsCount;
    private int favoritesCount;

    
    private static User currentUser;


    public int getRetweetsCount() {
        return retweetsCount;
    }

    public void setRetweetsCount(int retweetsCount) {
        this.retweetsCount = retweetsCount;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }


    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }



    public static User fromJSON(JSONObject json) {
        User user = new User();
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = "@" +json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.tagline = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
            user.tweetsCount = json.getInt("statuses_count");
            user.backgroundImageUrl = json.getString("profile_background_image_url");
            user.favoritesCount = json.getInt("favourites_count");
            //user.retweetsCount = json.getInt("retweet_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    
    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    // Return current logged in user
    public static User getCurrentUser() {
        return User.currentUser;
    }
}


