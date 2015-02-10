package com.codepath.apps.simpletweets.activities;

import android.app.DialogFragment;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.simpletweets.Adapters.TweetsArrayAdapter;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.fragments.ComposeFragment;
import com.codepath.apps.simpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity implements ComposeFragment.OnFragmentInteractionListener{

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    
    private ComposeFragment mComposeFragment;
    private FragmentManager fragmentManager;

    private SwipeRefreshLayout swipeContainer;
    
    
    
    //Send an api request
    //Fill the list view by creating the listview objects from the json
    private void populateTimeline(int since, final boolean isRefresh) {
        client.getHomeTimeline(since,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Deserialize the json object
                //create the models and add them to the adapter
                //Load the model data in to the listview
                if(isRefresh){
                    aTweets.clear();
                }
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                aTweets.addAll(newTweets);
                if(isRefresh){
                    swipeContainer.setRefreshing(false);
                    aTweets.notifyDataSetChanged();    
                }
                
                Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
        
        
        
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //set up the swipe refresh
        setupSwipeRefresh();
        //Find the Listview
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        //create the arraylist (datasource)
        tweets = new ArrayList<>();
        //construct the adapter from the datasource
        aTweets = new TweetsArrayAdapter(this,tweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                populateTimeline(totalItemsCount,false);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
        
        //attach the adapter to the listview
        lvTweets.setAdapter(aTweets);
        //Set the title on the Actionbar
        getSupportActionBar().setTitle("Home");
        //get the client
        client = TwitterApplication.getRestClient();
        //populate the timeline with the tweets
        populateTimeline(1,false);

        //fragment inits
        fragmentManager= getSupportFragmentManager();
        mComposeFragment = new ComposeFragment();
    }

    private void setupSwipeRefresh() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(1,true);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miCompose) {
            //show the compose view dialog and let the user compose a tweet
            mComposeFragment.show(fragmentManager,"fragment_compose");
            mComposeFragment.setCancelable(false);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComposeTweet(String message) {
        // post the message to the time line
        client.composeTweet(message,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(TimelineActivity.this,"posted the tweet",Toast.LENGTH_SHORT).show();
                populateTimeline(1,true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(TimelineActivity.this,"unable to post the tweet",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
