package com.codepath.apps.simpletweets.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the singleton client
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return super.onCreateView(inflater, parent, savedInstanceState);
    }


    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }
    
    //Send an api request
    //Fill the list view by creating the listview objects from the json
    @Override
    protected void populateTimeline() {
        String screenName= getArguments().getString("screen_name");
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Deserialize the json object
                //create the models and add them to the adapter
                //Load the model data in to the listview
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                addTweets(newTweets, true);
                Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    protected void loadMoreTimeline() {
        String screenName= getArguments().getString("screen_name");
        client.getMoreUserTimeline(tweets.get(tweets.size()-1).getUid(),screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Deserialize the json object
                //create the models and add them to the adapter
                //Load the model data in to the listview
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                addTweets(newTweets, false);
                Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
