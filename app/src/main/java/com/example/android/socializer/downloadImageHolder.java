package com.example.android.socializer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class downloadImageHolder extends RecyclerView.ViewHolder {


    ImageView imageView;

    public downloadImageHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.dimage);
    }
}
