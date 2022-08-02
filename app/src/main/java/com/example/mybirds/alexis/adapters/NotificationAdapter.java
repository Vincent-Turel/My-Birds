package com.example.mybirds.alexis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybirds.R;
import com.example.mybirds.alexis.models.NotificationItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationItem> notifications;
    private final Context context;

    public NotificationAdapter(List<NotificationItem> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.notification_layout, parent, false);

        return new NotificationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_profile.setImageResource(notifications.get(position).getImg_user());
        holder.tv_title.setText(notifications.get(position).getTxt_description());
        holder.tv_uname.setText(notifications.get(position).getTxt_username());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView iv_profile;
        TextView tv_title;
        TextView tv_uname;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            iv_profile = (CircleImageView) itemView.findViewById(R.id.notification_profile);
            tv_title = (TextView) itemView.findViewById(R.id.notification_title);
            tv_uname = (TextView) itemView.findViewById(R.id.notification_uname);

        }
    }
}
