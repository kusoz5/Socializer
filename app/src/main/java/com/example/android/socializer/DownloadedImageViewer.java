package com.example.android.socializer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class DownloadedImageViewer extends AppCompatActivity {

    ImageView Downloaded_Image_Viewer;
    FloatingActionButton FabOfDownloadViewer,DeleteFabDownloadViewer,ShareFabDownloadViewer;
    public boolean isOpen = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_image_viewer);
        Downloaded_Image_Viewer = (ImageView) findViewById(R.id.Downloaded_Image_Viewer);
        Downloaded_Image_Viewer.setImageBitmap(SelectedDownloadImageObject.bitmap);

        FabOfDownloadViewer = (FloatingActionButton) findViewById(R.id.FabOfDownloadViewer);
        DeleteFabDownloadViewer = (FloatingActionButton) findViewById(R.id.DeleteFabDownloadViewer);
                ShareFabDownloadViewer = (FloatingActionButton) findViewById(R.id.ShareFabDownloadViewer);

                FabOfDownloadViewer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ManageFabClick();
                    }
                });
                DeleteFabDownloadViewer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteImageOnClick();
                    }
                });
                ShareFabDownloadViewer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareImageOnClick();
                    }
                });
    }

    private void ShareImageOnClick() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(SelectedDownloadImageObject.filePath));
            startActivity(Intent.createChooser(intent,"Share Image Using"));
        } catch (Exception e) {

        }

    }

    private void DeleteImageOnClick() {

        try{
            FileUtils.forceDelete(new File(SelectedDownloadImageObject.filePath));
            Toast.makeText(this,"File Deleted Success",Toast.LENGTH_SHORT).show();
            this.finish();
        }catch (Exception e){
            Log.e("Del Download Image",e.toString());
        }
    }

    private void ManageFabClick() {
        if(!isOpen ){

            FabOfDownloadViewer.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_clockrotate));
            DeleteFabDownloadViewer.show();
            ShareFabDownloadViewer.show();

            DeleteFabDownloadViewer.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_open));
            ShareFabDownloadViewer.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fab_open));

            isOpen = true;
        }
        else
        {
            FabOfDownloadViewer.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_anticlockrotate));
            DeleteFabDownloadViewer.hide();
            ShareFabDownloadViewer.hide();

            isOpen =false;
        }
    }

}
