package com.example.android.socializer;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.cnrylmz.zionfiledownloader.DownloadVideo;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class DownloadVideoPlayer extends AppCompatActivity {
    SimpleExoPlayerView DownloadVideoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    boolean isOpen= false;
    FloatingActionButton FabOfDownloadVideoViewer,DeleteFabDownloadVideoViewer,ShareFabDownloadVideoViewer;
    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.stop();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_video_player);
        FabOfDownloadVideoViewer = (FloatingActionButton) findViewById(R.id.FabOfDownloadVideoViewer);
        DeleteFabDownloadVideoViewer = (FloatingActionButton) findViewById(R.id.DeleteFabDownloadVideoViewer);
        ShareFabDownloadVideoViewer = (FloatingActionButton) findViewById(R.id.ShareFabDownloadVideoViewer);

        Log.e("VideoUri",GlobalAccessibleClass.downloadVideoDetails.getVideoUri());
        startExo(GlobalAccessibleClass.downloadVideoDetails.getVideoUri());

        FabOfDownloadVideoViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageFabVideo();
            }
        });
        DeleteFabDownloadVideoViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteVideoPlayer();
            }
        });
        ShareFabDownloadVideoViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareVideo();
            }
        });
    }

    private void ShareVideo() {

        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(DownloadVideoDetails.getVideoUri()));
            startActivity(Intent.createChooser(intent,"Share Video Using"));
        } catch (Exception e) {

        }

    }

    private void DeleteVideoPlayer() {
        try{
            FileUtils.forceDelete(new File(DownloadVideoDetails.getVideoUri()));
            Toast.makeText(this,"File Deleted Success",Toast.LENGTH_SHORT).show();
            this.finish();
        }catch (Exception e){
            Log.e("Del Download Video",e.toString());
        }
    }

    private void ManageFabVideo() {

        if(!isOpen ){

            FabOfDownloadVideoViewer.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_clockrotate));
            DeleteFabDownloadVideoViewer.show();
            ShareFabDownloadVideoViewer.show();

            DeleteFabDownloadVideoViewer.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_open));
            ShareFabDownloadVideoViewer.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_open));

            isOpen = true;
        }
        else
        {
            FabOfDownloadVideoViewer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_anticlockrotate));
            DeleteFabDownloadVideoViewer.hide();
            ShareFabDownloadVideoViewer.hide();

            isOpen =false;
        }
    }

    private void startExo(String videoUri) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DownloadVideoPlayerView = (SimpleExoPlayerView) findViewById(R.id.DownloadVideoPlayerView);

        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        Uri uri = Uri.fromFile(new File(videoUri));
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(this, "Video-Player"), new DefaultExtractorsFactory(), null, null);

        DownloadVideoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);

    }
}
