package com.example.android.socializer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InstaStorySelectedViewer extends AppCompatActivity {

    List<SelectedUserInsta> list =  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_story_viewer);
        LoadSelectedUserDetails();

        RecyclerView recyclerView =(RecyclerView) findViewById(R.id.selectedStoryRecycler);
        SelectedStoryAdapterInsta selectedStoryAdapterInsta =  new SelectedStoryAdapterInsta(list,this);
        recyclerView.setAdapter(selectedStoryAdapterInsta);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
    }
    private void LoadSelectedUserDetails(){
        for(int i = 0 ; i <GlobalAccessibleClass.instaStoryListObject.linkarray.length ; i++){
           if(GlobalAccessibleClass.instaStoryListObject.isVideo[i]){
               list.add( new SelectedUserInsta(GlobalAccessibleClass.instaStoryListObject.linkarray[i],GlobalAccessibleClass.instaStoryListObject.VideoDisplay[i],true));
           }
           else {
               list.add(new SelectedUserInsta(null,GlobalAccessibleClass.instaStoryListObject.linkarray[i],false));
           }
        }
    }
}
