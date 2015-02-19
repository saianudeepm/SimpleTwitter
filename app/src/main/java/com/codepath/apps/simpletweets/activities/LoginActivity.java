package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
        return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
        getClient().getUserInfo(new JsonHttpResponseHandler() {
               @Override
               public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                   //Deserialize the json object
                   //create the models and add them to the adapter
                   //Load the model data in to the listview

                   User user= User.fromJSON(jsonObject);//gets the current user
                   //set the current user
                   User.setCurrentUser(user);
                   Log.d("DEBUG", jsonObject.toString());
                   getSupportActionBar().setTitle("@"+user.getName());

               }
               @Override
               public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                   Log.d("DEBUG", errorResponse.toString());
               }
           });

        Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
        Toast.makeText(this,"hey you logged in successfully",Toast.LENGTH_SHORT).show();
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
