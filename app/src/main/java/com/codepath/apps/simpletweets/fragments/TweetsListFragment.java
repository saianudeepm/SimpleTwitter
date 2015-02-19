package com.codepath.apps.simpletweets.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.simpletweets.activities.TimelineActivity;
import com.codepath.apps.simpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.listeners.EndlessScrollListener;
import com.codepath.apps.simpletweets.models.Tweet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TweetsListFragment.TweetsListFragmentDelegate} interface
 * to handle interaction events.
 */
public abstract class TweetsListFragment extends Fragment {

    protected ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    
    private ImageView ivProfile;

    private SwipeRefreshLayout swipeContainer;
    private TweetsListFragmentDelegate mListener;

      public TweetsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create the arraylist (datasource)
        tweets = new ArrayList<>();
        //construct the adapter from the datasource
        aTweets = new TweetsArrayAdapter(getActivity(),tweets);
        
        //Set the title on the Actionbar
        // getSupportActionBar().setTitle("Home");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        //set up the swipe refresh
        setupSwipeRefresh(view);

        //Find the Listview
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadMoreTimeline();
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        //attach the adapter to the listview
        lvTweets.setAdapter(aTweets);
        aTweets.setDelegate((TweetsArrayAdapter.TweetDelegate)mListener);
        return view;
    }

    private void setupSwipeRefresh(View view) {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }
    
    public void addTweets(ArrayList<Tweet> newTweets, boolean isRefresh){
        if(isRefresh){
            aTweets.clear();
        }
        aTweets.addAll(newTweets);
        if(isRefresh){
            swipeContainer.setRefreshing(false);
            aTweets.notifyDataSetChanged();
        }
        
    }

    //Send an api request
    //Fill the list view by creating the listview objects from the json
    protected abstract void populateTimeline();
    
    protected abstract void loadMoreTimeline();

    // When some one clicks on the profile view
    // set the current user and then send him to the profile view of the user


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface TweetsListFragmentDelegate {
        // TODO: Update argument type and name
       public void onTweetsListFragment(String message);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if(activity instanceof TimelineActivity)
                mListener = (TimelineActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
