package com.example.android.socializer;


import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstagramStory extends Fragment {

    HashMap<String, Bitmap> dps = new HashMap<>();
      List<instaStoryListObject> list = new ArrayList<>();
    boolean islogged = false;
    RelativeLayout loadingPanelInstagramStory;
     TextView TextUpdateInstaStory;
     ImageView InstaUser;
    RecyclerView recyclerView;
    TextView textView ;
    public InstagramStory() {


    }



    @Override
    public void onResume() {
        super.onResume();
        HomeActivity homeActivity = new HomeActivity();
        homeActivity.setBottom(1);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.instastoryList);
        loadingPanelInstagramStory = view.findViewById(R.id.loadingPanelInstagramStory);
        TextUpdateInstaStory = view.findViewById(R.id.Logout);
        InstaUser = (ImageView) view.findViewById(R.id.InstaUser);
        textView = (TextView) view.findViewById(R.id.Logout);
          setRecycler();

          textView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  new saveCookie(getActivity()).savecookie("Authenticated","false");
                  Toast.makeText(getActivity(),"Logged Out",Toast.LENGTH_SHORT).show();

              }
          });


    }


    private  void setRecycler() {


        gettingstory gettingstory = new gettingstory();
        gettingstory.execute(HTTP.reelids);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instagram_story, container, false);
    }

    public class gettingstory extends AsyncTask<String,Void,String> {


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                if(HTTP.storyResponse.equals("SessionExpired")) {
                    Toast.makeText(getActivity(),"Session Log Out Please Log in again ",Toast.LENGTH_SHORT).show();


                } else {
                    processStoryResponse(HTTP.storyResponse);

                }

            } catch (Exception e) {
                Toast.makeText(getContext(),"Internet connectivity issue or some other error occured",Toast.LENGTH_LONG).show();
            }

        }

        protected String doInBackground(String... strings) {

            String response = "";

            HttpRequest httpRequest = new HttpRequest(getContext());
       return      httpRequest.makeHttpRequest(strings[0],"reelids");


        }
    }

    private void processStoryResponse(String storyResponse) {
        try {
            JSONObject root = new JSONObject(storyResponse);
            JSONObject data = root.getJSONObject("data");
            JSONArray reels_media = data.getJSONArray("reels_media");
            for (int l = 0; l < reels_media.length(); l++) {
                instaStoryListObject storyOfAccounts= new instaStoryListObject();
                JSONObject unique_AC = reels_media.getJSONObject(l);
                JSONObject owner = unique_AC.getJSONObject("owner");
                storyOfAccounts.profile_pic_url = owner.getString("profile_pic_url");
                storyOfAccounts.instausername = owner.getString("username");
                storyOfAccounts.Profile_Pic = HTTP.usernamewithdp.get(storyOfAccounts.instausername);
                JSONArray items = unique_AC.getJSONArray("items");
                storyOfAccounts.items = items.length();
                storyOfAccounts.linkarray = new String[items.length()];
                storyOfAccounts.VideoDisplay =  new String[items.length()];
                storyOfAccounts.isVideo = new boolean[items.length()];
                for (int k = 0; k < items.length(); k++) {
                    if (!items.getJSONObject(k).getBoolean("is_video")) {
                        storyOfAccounts.linkarray[k] = items.getJSONObject(k).getString("display_url");
                        storyOfAccounts.isVideo[k] =false;
                    } else {
                        JSONArray video_resources = items.getJSONObject(k).getJSONArray("video_resources");
                      storyOfAccounts.VideoDisplay[k] = items.getJSONObject(k).getString("display_url");
                        storyOfAccounts.isVideo[k] =true;
                        if (video_resources.length() != 1) {
                            storyOfAccounts.linkarray[k] = video_resources.getJSONObject(1).getString("src");
                        } else {
                            storyOfAccounts.linkarray[k] = video_resources.getJSONObject(0).getString("src");
                        }


                    }


                }
                list.add(storyOfAccounts);

            }
            loadingPanelInstagramStory.setVisibility(View.GONE);
            try {
                Picasso.get().load(new saveCookie(getActivity()).getcookie("ProfileInstaUser",null)).into(InstaUser);
            }
            catch (Exception e){
                Log.e("Loading Profile Pic",e.toString());
            }
            textView.setText(R.string.LogoutOption);
            instaStoryListAdapter listAdapter = new instaStoryListAdapter(getContext(), list);
            recyclerView.setAdapter(listAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        } catch (Exception e) {
            Log.e("Displa selected user",e.toString());
        }
    }


}
