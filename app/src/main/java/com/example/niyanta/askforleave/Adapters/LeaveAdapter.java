package com.example.niyanta.askforleave.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.niyanta.askforleave.Models.LeaveModel;
import com.example.niyanta.askforleave.R;

import java.util.List;
import java.util.Locale;

public class LeaveAdapter  extends RecyclerView.Adapter<LeaveAdapter.LeaveViewHolder> {
    private Context context;
    private List<LeaveModel> leaveList;

    public LeaveAdapter(Context context, List<LeaveModel> leaveList) {
        this.leaveList = leaveList;
        this.context = context;
    }

    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_list, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        LeaveViewHolder viewHolder = new LeaveViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveViewHolder holder, int position) {

        LeaveModel leave = leaveList.get(position);
        holder.tVFrom_date.setText("From:   "+ leave.getFrom_date());
        holder.tVTo_date.setText  ("To:     "+ leave.getTo_date());
        holder.tVReason.setText   ("Reason: "+ leave.getreason());
        String leaveStatus = leaveList.get(position).getLeavestatus();
        if (leaveStatus.equals("Panding for approval")) {
            holder.img_LeaveAdapter_highlight.setBackgroundColor(ContextCompat.getColor(context, R.color.Gold));

        } else if (leaveStatus.equals("Approved")) {
            holder.img_LeaveAdapter_highlight.setBackgroundColor(ContextCompat.getColor(context, R.color.Green));

        }else if (leaveStatus.equals("Rejected")) {
            holder.img_LeaveAdapter_highlight.setBackgroundColor(ContextCompat.getColor(context, R.color.Red));

        }
        }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    public class LeaveViewHolder extends RecyclerView.ViewHolder  {
        protected TextView tVFrom_date, tVTo_date,tVReason;
        protected ImageView img_LeaveAdapter_highlight;
        public View container;

        public LeaveViewHolder(View itemView) {
            super(itemView);
            img_LeaveAdapter_highlight=itemView.findViewById(R.id.img_LeaveAdapter_highlight);
            tVFrom_date = itemView.findViewById(R.id.tVFrom_date);
            tVTo_date = itemView.findViewById(R.id.tVTo_date);
            tVReason = itemView.findViewById(R.id.tVReason);

        }
        }

}


