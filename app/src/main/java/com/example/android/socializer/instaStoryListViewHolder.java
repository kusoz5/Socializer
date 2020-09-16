package com.example.android.socializer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mikhaellopez.circularimageview.CircularImageView;

public class instaStoryListViewHolder extends RecyclerView.ViewHolder  {
    public TextView username;
    public ImageView dp;


    public instaStoryListViewHolder(@NonNull View itemView) {
        super(itemView);

        dp = itemView.findViewById(R.id.instastoryuserdp);
        username = itemView.findViewById(R.id.instastoryusername);

    }
}
