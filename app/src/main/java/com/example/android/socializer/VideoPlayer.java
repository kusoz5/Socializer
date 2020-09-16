package com.example.android.socializer;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class VideoPlayer extends AppCompatActivity {


    public static  String RequestedSrc ;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
   FloatingActionButton main_fab_whatsappv,delete_fab_whatsappv,download_fab_whatsappv,share_fab_whatsappv;
   Animation fab_anticlockrotate,fab_clockrotate,fab_close,fab_open;
   private  Boolean isOpen = false;
   File file ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        this.main_fab_whatsappv = (FloatingActionButton) findViewById(R.id.main_fab_whatsappv);
        this.download_fab_whatsappv = (FloatingActionButton) findViewById(R.id.download_fab_whatsappv);
        this.delete_fab_whatsappv = (FloatingActionButton) findViewById(R.id.delete_fab_whatsappv);
        this.share_fab_whatsappv =  (FloatingActionButton) findViewById(R.id.share_fab_whatsappv);

        fab_anticlockrotate = AnimationUtils.loadAnimation(this,R.anim.fab_anticlockrotate);
        fab_clockrotate  =  AnimationUtils.loadAnimation(this,R.anim.fab_clockrotate);
        fab_close  = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(this,R.anim.fab_open);

        if(RequestedSrc.equals("VideoWhatsApp")) {

            startExo(GlobalAccessibleClass.videowhatsappobject.getVideoUri());
        }
        if  (RequestedSrc.equals("InstaStoryVideo")) {
            ViewGroup viewGroup = (ViewGroup) delete_fab_whatsappv.getParent();
            viewGroup.removeView(delete_fab_whatsappv);
            viewGroup.removeView(share_fab_whatsappv);
            startExo(GlobalAccessibleClass.selectedUserInsta.getVideoLink(),"true");

        }

         main_fab_whatsappv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!isOpen){
                     main_fab_whatsappv.setAnimation(fab_clockrotate);
                     main_fab_whatsappv.setBackground(getDrawable(R.drawable.deletefav));

                     delete_fab_whatsappv.show();
                     delete_fab_whatsappv.setAnimation(fab_open);

                     download_fab_whatsappv.show();
                     download_fab_whatsappv.setAnimation(fab_open);

                     share_fab_whatsappv.show();
                     share_fab_whatsappv.setAnimation(fab_open);

                     isOpen = true;

                     download_fab_whatsappv.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {

                         if(RequestedSrc.equals("VideoWhatsApp")) {
                             downloadVideo();
                         }
                         if(RequestedSrc.equals("InstaStoryVideo")) {
                             downloadinstaStoryVideo(GlobalAccessibleClass.selectedUserInsta.VideoLink);
                         }
                         }
                     });

                     delete_fab_whatsappv.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                           deleteVideo();
                         }
                     });

                     share_fab_whatsappv.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {




                         }
                     });

                 }
                 else {
                     main_fab_whatsappv.setAnimation(fab_anticlockrotate);
                     main_fab_whatsappv.setBackground(getDrawable(R.drawable.plus));

                     delete_fab_whatsappv.setAnimation(fab_close);
                     delete_fab_whatsappv.hide();

                     download_fab_whatsappv.setAnimation(fab_close);
                     download_fab_whatsappv.hide();

                     share_fab_whatsappv.setAnimation(fab_close);
                     share_fab_whatsappv.hide();

                     isOpen = false;
                 }


             }
         });


//        String nameVide = GlobalAccessibleClass.videowhatsappobject.getVideoUri();
//
//        nameVide = nameVide.substring(nameVide.lastIndexOf('/') + 1);
//
//        File cFile = new File(GlobalAccessibleClass.wvfile + "/" + nameVide);

 //       if (cFile.exists()) {

//            downloadVideo.setEnabled(false);
//            downloadVideo.setText("Downloaded");
//            deleteVideo.setEnabled(true);
//        } else {
//
//
//            downloadVideo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        Log.e("Copying", "onClick: ");
//
//                        String nameVide = GlobalAccessibleClass.videowhatsappobject.getVideoUri();
//
//                        nameVide = nameVide.substring(nameVide.lastIndexOf('/') + 1);
//
//                        String fpath = GlobalAccessibleClass.wvfile + "/" + nameVide;
//                        FileUtils.copyFile(new File(GlobalAccessibleClass.videowhatsappobject.getVideoUri()), new File(fpath));
//                        Toast.makeText(VideoPlayer.this, "Downloaded,", Toast.LENGTH_SHORT).show();
//                        downloadVideo.setText("Downloaded");
//                        downloadVideo.setEnabled(false);
//                        deleteVideo.setEnabled(true);
//
//
//                    } catch (Exception e) {
//                        Log.e("Exceptipn", "onClick: " + e.toString());
//                    }
//                }
//            });
//
//
//            deleteVideo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String nameVide = GlobalAccessibleClass.videowhatsappobject.getVideoUri();
//
//                    nameVide = nameVide.substring(nameVide.lastIndexOf('/') + 1);
//                    try {
//                        FileUtils.forceDelete(new File(GlobalAccessibleClass.wvfile+"/"+nameVide));
//                    } catch (IOException e) {
//
//                        Log.e("Delted", "onClick: "+e.toString() );                                }
//
//                    deleteVideo.setEnabled(false);
//                    Toast.makeText(VideoPlayer.this, "Deleted,", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//
//        }


        }

    private void downloadinstaStoryVideo(String VideoUri) {
        if (GlobalAccessibleClass.selectedUserInsta.NmaeOfTheFile.equals("")) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(VideoUri));
            request.setTitle("Socializer:" + "InstaStoryImage");
            request.setDescription("Downloading Story" + " " + "InstaStoryImage");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            GlobalAccessibleClass.selectedUserInsta.NmaeOfTheFile = String.valueOf(System.currentTimeMillis());
            request.setDestinationInExternalPublicDir("/Socializer/Instagram/Story/Videos/", GlobalAccessibleClass.selectedUserInsta.NmaeOfTheFile + ".mp4");
            DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            Toast.makeText(this, "Downloading Starting ", Toast.LENGTH_SHORT).show();
            file  = new File("/Socializer/Instagram/Story/Images/"+GlobalAccessibleClass.selectedUserInsta.NmaeOfTheFile);
        }
        else {
            Toast.makeText(this, "File Already Downloaded", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        simpleExoPlayer.setPlayWhenReady(false);
        super.onBackPressed();
    }

    public void startExo(String VideoUri) {
        Log.e("StartEx",VideoUri);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.SimpleExoPlayerView);

        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        Uri uri = Uri.fromFile(new File(VideoUri));
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(this, "Video-Player"), new DefaultExtractorsFactory(), null, null);

        simpleExoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }

    private void startExo(String VideoUri,String Stream){
        try{
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.SimpleExoPlayerView);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
            Uri video = Uri.parse(VideoUri);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exolab");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource =  new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(false);
            Log.e("stsrt", "onCreate: " );

        } catch (Exception e)
        {

            e.printStackTrace();
        }
    }
    public void deleteVideo() {
        String nameVide = GlobalAccessibleClass.videowhatsappobject.getVideoUri();

        nameVide = nameVide.substring(nameVide.lastIndexOf('/') + 1);

        File cFile = new File(GlobalAccessibleClass.wvfile + "/" + nameVide);

        if(!cFile.exists()) {
            Toast.makeText(VideoPlayer.this, "File Not Exist ! ", Toast.LENGTH_SHORT).show();

        } else {

            try {
                FileUtils.forceDelete(cFile);
                Toast.makeText(VideoPlayer.this, "Deleted  ", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Log.e("Delted Video Whatsapp", "onClick: "+e.toString() );
            }
        }
    }

    public void downloadVideo() {
        String nameVide = GlobalAccessibleClass.videowhatsappobject.getVideoUri();

        nameVide = nameVide.substring(nameVide.lastIndexOf('/') + 1);

        File cFile = new File(GlobalAccessibleClass.wvfile + "/" + nameVide);


        if(!cFile.exists()) {
            String fpath = GlobalAccessibleClass.wvfile + "/" + nameVide;
            try {
                FileUtils.copyFile(new File(GlobalAccessibleClass.videowhatsappobject.getVideoUri()), new File(fpath));
                Toast.makeText(VideoPlayer.this, "Downloaded", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

            }
        } else {
            Toast.makeText(VideoPlayer.this, "Already Exists ,", Toast.LENGTH_SHORT).show();

        }
    }
}
