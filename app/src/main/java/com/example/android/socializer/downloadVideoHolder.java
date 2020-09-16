package com.example.android.socializer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class downloadVideoHolder extends RecyclerView.ViewHolder {

    ImageView Thumbnail_videoStory ;

    public downloadVideoHolder(@NonNull View itemView) {
        super(itemView);
        Thumbnail_videoStory = itemView.findViewById(R.id.Thumbnail_videoStory);


    }
}
