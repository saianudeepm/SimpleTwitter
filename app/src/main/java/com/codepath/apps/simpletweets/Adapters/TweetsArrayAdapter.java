package com.codepath.apps.simpletweets.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by saianudeepm on 2/9/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the Tweet
        Tweet tweet = getItem(position);

        //find the subviews to fill with data in the template
        //populate data into the subviews
        //return the view to be inserted into the list

        //Find or inflate the template
        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.item_tweet, parent, false);
        }
        // Find the views within template
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvTweet);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvTime =  (TextView) convertView.findViewById(R.id.tvTime);
        //set the values
        
        // to clear out the old image for the recycled view
        ivProfile.setImageResource(android.R.color.transparent);
        // Populate views with tweet data
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfile);
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvTime.setText(tweet.getCreatedAt());
        return convertView;
    }
    
    
}
