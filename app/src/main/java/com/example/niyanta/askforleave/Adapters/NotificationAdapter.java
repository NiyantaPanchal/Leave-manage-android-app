package com.example.niyanta.askforleave.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niyanta.askforleave.Models.NotificationData;
import com.example.niyanta.askforleave.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    ArrayList<NotificationData> notificationDataArrayList;

    public NotificationAdapter(Context context, ArrayList<NotificationData> notificationDataArrayList) {
        this.context = context;
        this.notificationDataArrayList = notificationDataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtMessage.setText(notificationDataArrayList.get(position).getMessage());
        holder.txtTime.setText(notificationDataArrayList.get(position).getTime());
        holder.txtTitle.setText(notificationDataArrayList.get(position).getTitle());
        holder.ivLogo.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_notifications));
    }

    @Override
    public int getItemCount() {
        return notificationDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView txtTime;
        TextView txtTitle;
        ImageView ivLogo;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
        }
    }
}