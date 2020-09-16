package com.example.android.socializer;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class ImageViewer extends AppCompatActivity {

    private FloatingActionButton main_fab_whatsappi, delete_fab_whatsappi, download_fab_whatsappi, share_fab_whatsappi;
    private Animation fab_open, fab_close, fab_clockrotate, fab_anticlockrotate;
    private Boolean isOpen = false;
    String srcfile ;
    String name ;
   static String wiimagepath;
    static String FolderPath,RequestedSource;
    File imagefile ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ImageView DownloadWhatsAppImage = (ImageView) findViewById(R.id.DownloadWhatsAppImage);
        this.delete_fab_whatsappi = (FloatingActionButton) findViewById(R.id.delete_fab_whatsappi);
        if(RequestedSource == "WhatsAppImage"){
            ViewGroup viewGroup = (ViewGroup) delete_fab_whatsappi.getParent();
            viewGroup.removeView(delete_fab_whatsappi);
        DownloadWhatsAppImage.setImageBitmap(GlobalAccessibleClass.imageWhatsAppObject.getBitmap());}
        else if(RequestedSource == "InstaStoryImage" ){
            Picasso.get().load(GlobalAccessibleClass.selectedUserInsta.ImageLink).into(DownloadWhatsAppImage);
        }
        this.main_fab_whatsappi = (FloatingActionButton) findViewById(R.id.main_fab_whatsappi);
        this.download_fab_whatsappi = (FloatingActionButton) findViewById(R.id.download_fab_whatsappi);
        this.share_fab_whatsappi = (FloatingActionButton) findViewById(R.id.share_fab_whatsappi);

        ViewGroup view = (ViewGroup) this.share_fab_whatsappi.getParent();
        view.removeView(this.share_fab_whatsappi);
        this.fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        this.fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        this.fab_clockrotate = AnimationUtils.loadAnimation(this, R.anim.fab_clockrotate);
        this.fab_anticlockrotate = AnimationUtils.loadAnimation(this, R.anim.fab_anticlockrotate);

        main_fab_whatsappi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mangageFab();
            }
        });


    }


    private void downloadImage() {
             srcfile = GlobalAccessibleClass.imageWhatsAppObject.getWHATSAPP_IMAGE_PATH();
        name  = srcfile.substring(srcfile.lastIndexOf("/")+1, srcfile.length());
        imagefile     = new File(GlobalAccessibleClass.wifile + "/"+ name);
        if (!imagefile.exists()) {

            try {
                FileUtils.copyFile(new File(srcfile), imagefile);
                Toast.makeText(this, "File  Downloaded", Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                Log.e("Download WhatsApp Image", e.toString());
            }

        } else {
            Toast.makeText(this, "File Already Downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteImage() {
    try {


        if (!imagefile.exists()) {

            Toast.makeText(this, "File Doesn't Exist", Toast.LENGTH_SHORT).show();

        } else {

            FileUtils.forceDelete(imagefile);
            Toast.makeText(this, "File Deleted", Toast.LENGTH_SHORT).show();


        }
    }
         catch (Exception e){
             Toast.makeText(this, "File Doesn't Exist", Toast.LENGTH_SHORT).show();
        }
    }


    private void mangageFab() {
        if (!isOpen) {
            main_fab_whatsappi.setAnimation(fab_clockrotate);
            if(!RequestedSource.equals("InstaStoryImage")) {
                delete_fab_whatsappi.show();
                delete_fab_whatsappi.setAnimation(fab_open);
                delete_fab_whatsappi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteImage();

                    }

                });
            }

            share_fab_whatsappi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(wiimagepath));
                    startActivity(Intent.createChooser(intent, "Share Image Using"));

                    Log.e("share image path", "onClick: "+wiimagepath );


                }
            });


            download_fab_whatsappi.show();
            download_fab_whatsappi.setAnimation(fab_open);
            download_fab_whatsappi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!RequestedSource.equals("InstaStoryImage"))
                    downloadImage();
                    else {
                        downloadInstagramStoryImage(GlobalAccessibleClass.selectedUserInsta.ImageLink);
                    }
                }
            });

            share_fab_whatsappi.show();
            share_fab_whatsappi.setAnimation(fab_open);

            isOpen = true;
          //  Log.e("Is open value1", "onClick: " + isOpen);
        } else {
            main_fab_whatsappi.setAnimation(fab_anticlockrotate);
            delete_fab_whatsappi.setAnimation(fab_close);
            delete_fab_whatsappi.hide();
            download_fab_whatsappi.setAnimation(fab_close);
            download_fab_whatsappi.hide();
            share_fab_whatsappi.setAnimation(fab_close);
            share_fab_whatsappi.hide();
            isOpen = false;
            Log.e("Is open value", "onClick: " + isOpen);

        }
    }


    private void downloadInstagramStoryImage(String imageLink) {
        if (GlobalAccessibleClass.selectedUserInsta.NmaeOfTheFile.equals("")) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageLink));
            request.setTitle("Socializer:" + "InstaStoryImage");
            request.setDescription("Downloading Story" + " " + "InstaStoryImage");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            GlobalAccessibleClass.selectedUserInsta.NmaeOfTheFile = String.valueOf(System.currentTimeMillis());
            request.setDestinationInExternalPublicDir("/Socializer/Instagram/Story/Images/", GlobalAccessibleClass.selectedUserInsta.NmaeOfTheFile + ".jpeg");
            DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            Toast.makeText(this, "Downloading Starting ", Toast.LENGTH_SHORT).show();
            imagefile  = new File("/Socializer/Instagram/Story/Images/"+GlobalAccessibleClass.selectedUserInsta.NmaeOfTheFile);
        }
        else {
            Toast.makeText(this, "File Already Downloaded", Toast.LENGTH_SHORT).show();

        }
    }
}
