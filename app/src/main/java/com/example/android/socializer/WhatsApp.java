package com.example.android.socializer;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.source.MediaSource;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WhatsApp extends Fragment implements VideoWhatsAppHolder.PlayButtonListerner {



    @Override
    public void ClickonPlay(int position) {
        WhatsAppObject whatsAppObject = list_video.get(position);
        Log.e("Fragment Click  On Play",whatsAppObject.getVideoUri());
        Intent intent =  new Intent(getActivity(),VideoPlayer.class);
        GlobalAccessibleClass.videowhatsappobject = new WhatsAppObject(whatsAppObject.getBitmap(),whatsAppObject.getVideoUri());
        startActivity(intent);
    }

    //TextView textView;
RelativeLayout Loading ;
    List<WhatsAppObject> list_images =  new ArrayList<>();

    List<WhatsAppObject> list_video =  new ArrayList<>();

    WhatsAppAdapter whatsAppAdapter;

    RecyclerView recyclerView;

    Switch isVideo ;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e("bottom nav id", "onViewCreated: " +view.findViewById(R.id.bottomNav));

      isVideo =   view.findViewById(R.id.optionVideo);
        recyclerView  = (RecyclerView) view.findViewById(R.id.recyclerViewWhatsApp);
        Loading = (RelativeLayout) view.findViewById(R.id.loadingPanel);
        initializeList();
      //  setRecycler(view);


       isVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(!isVideo.isChecked()) {

                   whatsAppAdapter = new WhatsAppAdapter(list_images, getActivity(),false);

                   recyclerView.setAdapter(whatsAppAdapter);

                   recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
               } else {

                   whatsAppAdapter = new WhatsAppAdapter(list_video,getActivity(),true);

                   recyclerView.setAdapter(whatsAppAdapter);

                   recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
               }
           }
       });
    }

    private void initializeList() {

        checkFiles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_whats_app, container, false);
    }

    private void setRecycler(View view) {

        recyclerView  = (RecyclerView) view.findViewById(R.id.recyclerViewWhatsApp);



        if(!isVideo.isChecked()) {

            whatsAppAdapter = new WhatsAppAdapter(list_images, getActivity(),false);

            recyclerView.setAdapter(whatsAppAdapter);

            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        } else {

            whatsAppAdapter = new WhatsAppAdapter(list_video,getActivity(),true);

            recyclerView.setAdapter(whatsAppAdapter);

            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        }



    }

    private void checkFiles() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        path = path+"/WhatsApp/Media/.Statuses";

        File file = new File(path);

        File[]  num = file.listFiles();

        loadList loadList = new loadList();
        loadList.execute(num);





    }

    @Override
    public void onResume() {
        super.onResume();
        initializeList();
        HomeActivity homeActivity = new HomeActivity();
        homeActivity.setBottom(0);
    }

    private void setList(File[] file) {

        list_video.clear();
        list_images.clear();
        for (int l = 0; l < file.length ; l++ ) {

            String path  = file[l].getAbsolutePath();

           int index  = path.indexOf('.');

           if(!path.substring(index,path.length()).contains("mp4")) {

               Bitmap image = BitmapFactory.decodeFile(path);

               list_images.add(new WhatsAppObject(image, path, null));
           } else {

               Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path,MediaStore.Images.Thumbnails.MINI_KIND);
               list_video.add(new WhatsAppObject(thumbnail,path));
           }

        }

        Log.e("List Size", "setList: "+list_images.size()+" video list size "+list_video.size() );

    }


    public class loadList extends AsyncTask<File[],Void,String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Loading.setVisibility(View.GONE);
            if(!isVideo.isChecked()) {

                whatsAppAdapter = new WhatsAppAdapter(list_images, getActivity(),false);

                recyclerView.setAdapter(whatsAppAdapter);

                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            } else {

                whatsAppAdapter = new WhatsAppAdapter(list_video,getActivity(),true);

                recyclerView.setAdapter(whatsAppAdapter);

                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            }

        }

        @Override
        protected String doInBackground(File[]... files) {
            list_video.clear();
            list_images.clear();
            File[] file = files[0];
            for (int l = 0; l < file.length ; l++ ) {

                String path  = file[l].getAbsolutePath();

                int index  = path.indexOf('.');

                if(!path.substring(index,path.length()).contains("mp4")) {

                    Bitmap image = BitmapFactory.decodeFile(path);

                    list_images.add(new WhatsAppObject(image, path, null));
                } else {

                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path,MediaStore.Images.Thumbnails.MINI_KIND);
                    list_video.add(new WhatsAppObject(thumbnail,path));
                }

            }

            Log.e("List Size", "setList: "+list_images.size()+" video list size "+list_video.size() );

            return null;
        }
    }

}
