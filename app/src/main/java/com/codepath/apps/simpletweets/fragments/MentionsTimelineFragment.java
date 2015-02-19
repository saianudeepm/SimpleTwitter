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

public class MentionsTimelineFragment extends TweetsListFragment {
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

    //Send an api request
    //Fill the list view by creating the listview objects from the json
    @Override
    protected void populateTimeline() {
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
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
        client.getMoreMentionsTimeline(tweets.get(tweets.size()-1).getUid(),new JsonHttpResponseHandler() {
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
