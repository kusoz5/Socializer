package com.example.android.socializer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.C;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectedStoryAdapterInsta extends RecyclerView.Adapter<instaSelectedViewHolder> {

    List<SelectedUserInsta> list;
   Context context;
    public SelectedStoryAdapterInsta(List<SelectedUserInsta> list, Context context){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public instaSelectedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_story_insta,viewGroup,false);
        return new instaSelectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull instaSelectedViewHolder viewHolder, final int i) {
      final   SelectedUserInsta SelectedUserInsta = list.get(i);
        if(SelectedUserInsta.isVideo){
            Picasso.get().load(SelectedUserInsta.ImageLink).into(viewHolder.instaDisplay);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =  new Intent(context,VideoPlayer.class);
                    GlobalAccessibleClass.selectedUserInsta = SelectedUserInsta;
                    VideoPlayer.RequestedSrc = "InstaStoryVideo";
                    context.startActivity(intent);
                }
            });

        }
        else {
            viewHolder.HidePlayButton();
            Picasso.get().load(SelectedUserInsta.ImageLink).into(viewHolder.instaDisplay);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  =  new Intent(context,ImageViewer.class);
                    GlobalAccessibleClass.selectedUserInsta = SelectedUserInsta;
                    ImageViewer.FolderPath = GlobalAccessibleClass.isifile.getAbsolutePath();
                    ImageViewer.RequestedSource = "InstaStoryImage";
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
