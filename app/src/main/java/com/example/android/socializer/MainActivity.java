package com.example.android.socializer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.security.Permission;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != (PackageManager.PERMISSION_GRANTED)) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this).setTitle("Socializer").setMessage("We need permission to save the Stories, Post's on your device so please grant the permission ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);

        } else  {
            Timer timer = new Timer();
            TimerTask task = new OpenHomeActivity();
            timer.schedule(task,2000);
        }

    }


    public class OpenHomeActivity extends TimerTask{

        @Override
        public void run() {
            Intent intent =  new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
        }
    }

    public MainActivity() {
        super();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 2 ) {

            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Timer timer = new Timer();
                TimerTask task = new OpenHomeActivity();
                timer.schedule(task,2000);



                GlobalAccessibleClass.wifile.mkdirs();

                GlobalAccessibleClass.wvfile.mkdirs();

                GlobalAccessibleClass.ipifile.mkdirs();

                GlobalAccessibleClass.ipvfile.mkdirs();

                GlobalAccessibleClass.isifile.mkdirs();

                GlobalAccessibleClass.isvfile.mkdirs();






            } else {

                Toast.makeText(this,"Please Grant The Permission Thanks ",Toast.LENGTH_LONG).show();
            }
        }
    }
}
