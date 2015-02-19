package com.codepath.apps.simpletweets.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHeaderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tvName;
    ImageView ivProfile;
    TextView tvTweetCount;
    TextView tvFollowingCount;
    TextView tvFollowersCount;
    TextView tvScreenName;
    
    User currentUser;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHeaderFragment newInstance(User user) {
        UserHeaderFragment fragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        return fragment;
    }

    public UserHeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user_header, container, false);
        setupViews(view);
        populateProfileHeader(currentUser);
        return view;

    }

    public void populateProfileHeader(User user) {
        try{
            tvName.setText(user.getName());
            tvFollowingCount.setText(String.valueOf(user.getFollowingCount()));
            tvFollowersCount.setText(String.valueOf(user.getFollowersCount()));
            tvTweetCount.setText(String.valueOf(user.getTweetsCount()));
            Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfile);
            tvScreenName.setText(user.getScreenName());
            
        }catch (Exception e){
            Log.e(this.toString(),e.getMessage());
            
        }
        
    }

    public void setupViews(View view) {
         tvName= (TextView) view.findViewById(R.id.tvProfileName);
         ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
         tvTweetCount = (TextView) view.findViewById(R.id.tvTweetCount);
         tvFollowingCount = (TextView) view.findViewById(R.id.tvFollowingCount);
         tvFollowersCount = (TextView) view.findViewById(R.id.tvFollowersCount);
         tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
    }
    
    public void setCurrentUser(User user){
       this.currentUser= user;
        
    }

}
