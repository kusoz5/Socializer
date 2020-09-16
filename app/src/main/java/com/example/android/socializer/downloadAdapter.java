package com.example.android.socializer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class downloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull

    Context context ;
    List<downloadImage> downloadImages;
    List<downloadVideo> downloadVideoList;
    downloadVideo downloadVideo;
    downloadImage downloadImage;
    boolean isvideo;

    public  downloadAdapter(Context context, List<downloadImage> downloadImages,List<downloadVideo> downloadVideo,boolean isvideo) {
        this.context = context;
        this.downloadImages = downloadImages;
        this.downloadVideoList = downloadVideo;
        this.isvideo = isvideo;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        if(!isvideo) {

            View view = inflater.inflate(R.layout.downloadimage, viewGroup, false);

            return new downloadImageHolder(view);
        } else {

            View view = inflater.inflate(R.layout.video_whatsapp,viewGroup,false);

            return new downloadVideoHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
          Log.e("ON Bind","DownloadAdaptetr" +  " "+ isvideo);
        if(!isvideo) {
            final downloadImageHolder downloadImageHolder = (com.example.android.socializer.downloadImageHolder) viewHolder;

            downloadImageHolder.imageView.setImageBitmap(downloadImages.get(position).getBitmap());

            downloadImageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   // GlobalAccessibleClass.imageWhatsAppObject = new WhatsAppObject(downloadImages.get(position).getBitmap(),null);
                   //  ImageViewer.RequestedSource = "WhatsAppImage";
                    downloadImage SelectedImage = downloadImages.get(position);
                      SelectedDownloadImageObject.filePath = SelectedImage.getPath();
                      SelectedDownloadImageObject.bitmap = SelectedImage.getBitmap();
                    Intent intent = new Intent(context,DownloadedImageViewer.class);
                    //ImageViewer.wiimagepath = downloadImages.get(position).path;
                    context.startActivity(intent);
                }
            });

        }  else {

            final downloadVideoHolder downloadVideoHolder = (com.example.android.socializer.downloadVideoHolder) viewHolder;

            downloadVideo = downloadVideoList.get(position);
            Log.e("CheckVideo",downloadVideo.getUri());

            Bitmap thumbnailVideo = ThumbnailUtils.createVideoThumbnail(downloadVideo.getUri(),1);

            downloadVideoHolder.Thumbnail_videoStory.setImageBitmap(thumbnailVideo);

            downloadVideoHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context,DownloadVideoPlayer.class);
                    //VideoPlayer.RequestedSrc = "VideoWhatsApp";
                   // WhatsAppObject whatsAppObject =  new WhatsAppObject(null,downloadVideoList.get(position).getUri());
                   // GlobalAccessibleClass.videowhatsappobject = whatsAppObject;
            GlobalAccessibleClass.downloadVideoDetails.VideoUri = downloadVideoList.get(position).getUri();
                    Log.e("Passing VideoUri",GlobalAccessibleClass.downloadVideoDetails.getVideoUri());

                    context.startActivity(intent);



                }
            });


        }


    }

    @Override
    public int getItemCount() {

        if(!isvideo) {
            return downloadImages.size();
        }  else {
            return downloadVideoList.size();
        }
    }
}
