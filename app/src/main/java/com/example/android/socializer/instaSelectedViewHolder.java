package com.example.android.socializer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class instaSelectedViewHolder extends RecyclerView.ViewHolder {

    public ImageView instaDisplay;
    public ImageView playbuttonInstaStory;
    public instaSelectedViewHolder(@NonNull View itemView) {
        super(itemView);

        playbuttonInstaStory  = (ImageView) itemView.findViewById(R.id.playbuttonInstaStory);
        instaDisplay  = (ImageView) itemView.findViewById(R.id.instaDisplay);

    }
    public void HidePlayButton(){
        ViewGroup viewGroup = (ViewGroup) playbuttonInstaStory.getParent();
        if(viewGroup != null){
            viewGroup.removeView(playbuttonInstaStory);
        }


    }
}
