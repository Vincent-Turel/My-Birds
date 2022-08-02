package com.example.mybirds.alexis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybirds.R;
import com.example.mybirds.alexis.factories.postcontent.ContentPost;
import com.example.mybirds.alexis.factories.postcreator.UserPost;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>  {

    List<UserPost> postCreator;
    List<ContentPost> postContent;
    private final Context context;

    public PostAdapter(List<UserPost> postCreator, List<ContentPost> postContent, Context context) {
        this.postCreator = postCreator;
        this.postContent = postContent;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.home_post_layout, parent, false);

        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_post.setImageResource(postContent.get(position).getImg_post());
        holder.iv_profile.setImageResource(postCreator.get(position).getImg_user());
        holder.tv_uname.setText(postCreator.get(position).getTxt_username());
        holder.tv_time.setText(postContent.get(position).getTxt_time());
    }

    @Override
    public int getItemCount() {
        return postContent.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_post;
        CircleImageView iv_profile;
        TextView tv_uname;
        TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_post = (ImageView) itemView.findViewById(R.id.iv_card_post);
            iv_profile= (CircleImageView) itemView.findViewById(R.id.iv_card_profile);
            tv_uname = (TextView) itemView.findViewById(R.id.textView_card_username);
            tv_time = (TextView) itemView.findViewById(R.id.textView_card_time);

        }
    }
}