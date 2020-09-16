package com.example.android.socializer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.io.File;
import java.util.List;

public class WhatsAppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WhatsAppObject> list;
    private Context context;
    private  Boolean video ;
    private VideoWhatsAppHolder.PlayButtonListerner playButtonListerner;
    Bitmap thumb;
    public  VideoWhatsAppHolder videoWhatsAppHolderThumbnail;

    public WhatsAppAdapter(List<WhatsAppObject> list, Context context, Boolean video, VideoWhatsAppHolder.PlayButtonListerner playButtonListerner) {
        this.list = list;
        this.context = context;
        this.video = video;
        this.playButtonListerner = playButtonListerner;
    }
    public WhatsAppAdapter(List<WhatsAppObject> list, Context context, Boolean video) {
        this.list = list;
        this.context = context;
        this.video = video;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {


        if(!video) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagewhatsapp, parent, false);
            return new ImageWhatsaAppHolder(view) {
            };
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_whatsapp, parent, false);
            return  new VideoWhatsAppHolder(view,playButtonListerner);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(!video) {
        ImageWhatsaAppHolder imageWhatsaAppHolder = (ImageWhatsaAppHolder) holder;
        imageWhatsaAppHolder.storyWhatsapp.setImageBitmap(list.get(position).getBitmap());
        imageWhatsaAppHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context,ImageViewer.class);
                ImageViewer.RequestedSource = "WhatsAppImage";
                ImageViewer.FolderPath="";
                GlobalAccessibleClass.imageWhatsAppObject = list.get(position);
                 context.startActivity(intent);

            }
        });
        } else {
            final VideoWhatsAppHolder videoWhatsAppHolder = (VideoWhatsAppHolder) holder;
                videoWhatsAppHolder.Thumbnail_videoStory.setImageBitmap(list.get(position).getBitmap());
            videoWhatsAppHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayer.RequestedSrc = "VideoWhatsApp";
                    Intent intent  = new Intent(context,VideoPlayer.class);
                    GlobalAccessibleClass.videowhatsappobject = list.get(position);
                    context.startActivity(intent);
                }
            });
        }
    }

//    private void setVideo(VideoWhatsAppHolder videoWhatsAppHolder, String videoUri) {
//
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//       TrackSelector trackSelector  =new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context,trackSelector);
//
//        Uri uri = Uri.fromFile(new File(videoUri));
//        MediaSource mediaSource  = new ExtractorMediaSource(uri,new DefaultDataSourceFactory(context,"Exo-Player"),new DefaultExtractorsFactory(),null,null);
//        whatsapp_story_exo = videoWhatsAppHolder.whatsapp_story_exo;
//
//        whatsapp_story_exo.setPlayer(simpleExoPlayer);
//        simpleExoPlayer.prepare(mediaSource);
//        simpleExoPlayer.setPlayWhenReady(false);
//
//
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    }



