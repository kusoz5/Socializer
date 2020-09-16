package com.example.android.socializer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageWhatsaAppHolder extends RecyclerView.ViewHolder {

    ImageView storyWhatsapp;

    public ImageWhatsaAppHolder(@NonNull View itemView) {
        super(itemView);
        storyWhatsapp = itemView.findViewById(R.id.storyWhatsapp);
    }
}
