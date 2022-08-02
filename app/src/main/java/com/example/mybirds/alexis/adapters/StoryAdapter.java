package com.example.mybirds.alexis.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybirds.R;


import de.hdodenhof.circleimageview.CircleImageView;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private final int[] storyItems;
    private final Context context;

    public StoryAdapter(int[] storyItems, Context context) {
        this.storyItems = storyItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.home_story_layout, parent, false);

        return  new StoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_story.setImageResource(storyItems[position]);
        final int color = Color.parseColor("#6f1fde");
        holder.iv_story.setBorderColor(color);
        holder.iv_story.setBorderWidth(6);
    }

    @Override
    public int getItemCount() {
        return storyItems.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView iv_story;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            iv_story = (CircleImageView) itemView.findViewById(R.id.iv_card_profile);

        }
    }
}