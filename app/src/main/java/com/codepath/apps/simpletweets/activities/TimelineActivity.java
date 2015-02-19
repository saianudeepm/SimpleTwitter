package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.simpletweets.fragments.ComposeFragment;
import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.simpletweets.fragments.TweetsListFragment;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity implements
        ComposeFragment.OnFragmentInteractionListener, TweetsListFragment.TweetsListFragmentDelegate, TweetsArrayAdapter.TweetDelegate {

    private TwitterClient client;
    private ComposeFragment mComposeFragment;
    private TweetsListFragment mTweetsListFragment;
    private FragmentManager fragmentManager;


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        //fragment inits
        fragmentManager= getSupportFragmentManager();
        mComposeFragment = new ComposeFragment();
        //get the client
        client = TwitterApplication.getRestClient();
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

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

    
    //Private Methods
    @Override
    public void onComposeTweet(String message) {
        // post the message to the time line
        client.composeTweet(message,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(TimelineActivity.this,"posted the tweet",Toast.LENGTH_SHORT).show();
                //TODO send this using a callback to update the adapter
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(TimelineActivity.this,"unable to post the tweet",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTweetsListFragment(String message) {
        //TODO if needed to get any message from TweetsListFragments
    }

    //handle on profile menu item click
    public void onProfileView(MenuItem item) {
         showProfile();

        
    }

    private void launchProfileActivity() {
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("screen_name",User.getCurrentUser().getScreenName());
        startActivity(intent);
    }

    @Override
    public void showProfile() {

        // we need the user before we can show his details in the profile view
        client = TwitterApplication.getRestClient();
        if(User.getCurrentUser()!= null){
            //launch profile activity
            launchProfileActivity();

        }
        else {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                    //Deserialize the json object
                    //create the models and add them to the adapter
                    //Load the model data in to the listview

                    User user = User.fromJSON(jsonObject);//gets the current user
                    Log.d("DEBUG", jsonObject.toString());
                    launchProfileActivity();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                    Toast.makeText(TimelineActivity.this, "ERROR fetching user details", Toast.LENGTH_SHORT).show();
                }
            });
        }
        
    }

    //Returns the order of fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles [] = {"Home", "Mentions"};
        
        //Adapter gets the fragment manager to insert or remove fragments from the activity
        TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        //The order and creation of the fragments within the pager
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new HomeTimelineFragment();
                
                case 1: return new MentionsTimelineFragment();
                
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
       
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
