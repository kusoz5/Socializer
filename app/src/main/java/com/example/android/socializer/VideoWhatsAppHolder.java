package com.example.android.socializer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class VideoWhatsAppHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Override
    public void onClick(View v) {
     playButtonListerner.ClickonPlay(getAdapterPosition());
    }

    public ImageView Thumbnail_videoStory;
   public ImageView playbutton;
   public PlayButtonListerner playButtonListerner;

    public VideoWhatsAppHolder(@NonNull View itemView, PlayButtonListerner playButtonListerner)  {
        super(itemView);
        Thumbnail_videoStory =  itemView.findViewById(R.id.Thumbnail_videoStory);
        playbutton = itemView.findViewById(R.id.playbutton);
        this.playButtonListerner = playButtonListerner;


    }
    public  interface PlayButtonListerner{
      void ClickonPlay(int position);
    }

}
