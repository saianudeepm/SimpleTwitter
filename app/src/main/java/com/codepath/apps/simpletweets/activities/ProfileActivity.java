package com.codepath.apps.simpletweets.activities;
import android.support.v4.app.FragmentTransaction; 
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.simpletweets.fragments.UserHeaderFragment;
import com.codepath.apps.simpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {
    
    private TwitterClient client;
    UserHeaderFragment userHeaderFragment;
    UserTimelineFragment userTimelineFragment;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle(User.getCurrentUser().getScreenName());
        
        if(savedInstanceState==null){
            //Get the screen name from the activity that launches it
            String screenName = getIntent().getStringExtra("screen_name");
            
            userTimelineFragment= UserTimelineFragment.newInstance(screenName);
            //Display userTimelineFragment fragment dynamically by replacing the flcontainer
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer,userTimelineFragment);
            ft.commit(); //changes the fragment

            userHeaderFragment = UserHeaderFragment.newInstance(User.getCurrentUser());
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.rlUserHeader, userHeaderFragment);
            ft2.commit();
            
            // call the method on the fragment and make it populate its view using the user object.
            userHeaderFragment.setCurrentUser(User.getCurrentUser());
            
        }
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
