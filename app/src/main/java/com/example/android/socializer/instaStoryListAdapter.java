package com.example.android.socializer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class instaStoryListAdapter extends RecyclerView.Adapter<instaStoryListViewHolder> {


    Context context;

    public instaStoryListAdapter(Context context, List<instaStoryListObject> list) {
        this.context = context;
        this.list = list;
    }

    List<instaStoryListObject> list = new ArrayList<>();

    @NonNull
    @Override
    public instaStoryListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.instastorylist,viewGroup,false);
         return  new instaStoryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull instaStoryListViewHolder instaStoryListViewHolder, final int position) {

        instaStoryListViewHolder.username.setText(list.get(position).instausername);
        Picasso.get().load(list.get(position).profile_pic_url).into(instaStoryListViewHolder.dp);
            instaStoryListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalAccessibleClass.instaStoryListObject = list.get(position);
                    Intent intent = new Intent(context,InstaStorySelectedViewer.class);
                    context.startActivity(intent);

                }
            });
        Log.e("url", "onBindViewHolder: "+list.get(position).profile_pic_url+list.get(position).Profile_Pic );


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
