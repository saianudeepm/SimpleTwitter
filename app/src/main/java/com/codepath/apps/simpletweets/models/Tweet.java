package com.codepath.apps.simpletweets.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 *
 */
@Table(name = "items")
public class Tweet extends Model {
    // Define table fields
    @Column(name = "name")
    private String body;
    private long uid;
    private String createdAt;
    private User user;
    private String retweetCount;
    private String favoriteCount;

    public Tweet() {
        super();
    }

    // Parse model from JSON
    //Single Object Deserializer
    public static  Tweet fromJson (JSONObject jsonObject){
        Tweet tweet =  new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.retweetCount = jsonObject.getString("retweet_count");
            tweet.favoriteCount = jsonObject.getString("favorite_count");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            
            //Not working ???
            String newTime = jsonObject.getString("created_at");
             tweet.createdAt = getRelativeTimeAgo(newTime);
            //tweet.createdAt= "1d";

        } catch (JSONException e) {
            e.printStackTrace();
        }
     return  tweet;
    }

    
    //Array Deserializer
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJson(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
    
    /*
    // Record Finders
    public static Tweet byId(long id) {
        return new Select().from(Tweet.class).where("id = ?", id).executeSingle();
    }

    public static List<Tweet> recentItems() {
        return new Select().from(SampleModel.class).orderBy("id DESC").limit("300").execute();
    }*/

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            System.out.println("So the date is " + relativeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
    
}
