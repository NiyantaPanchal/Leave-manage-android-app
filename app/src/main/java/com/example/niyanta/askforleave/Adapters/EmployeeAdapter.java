package com.example.niyanta.askforleave.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.niyanta.askforleave.Activity.ApproveActivity;
import com.example.niyanta.askforleave.Models.EmployeeModel;
import com.example.niyanta.askforleave.R;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private Context context;
    private ArrayList<EmployeeModel> Employeeleavelist;

    public EmployeeAdapter(Context context, ArrayList<EmployeeModel> Employeeleavelist) {
        this.context = context;
        this.Employeeleavelist = Employeeleavelist;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_leave_list, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        EmployeeViewHolder viewHolder = new EmployeeViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.EmployeeViewHolder holder, int position) {
        EmployeeModel employeeModel = Employeeleavelist.get(position);
        holder.txtname.setText(employeeModel.getName());
        holder.txtFrom_date.setText(employeeModel.getFrom_date());
        holder.txtTo_date.setText(employeeModel.getTo_date());
        holder.txtLeaveReason.setText(employeeModel.getReason());

    }

    @Override
    public int getItemCount() {
        return Employeeleavelist.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView txtname, txtFrom_date, txtTo_date, txtLeaveReason;
        protected ImageView IVemployee;
        private Button btnApprove,btnReject;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            IVemployee = (ImageView) itemView.findViewById(R.id.IVemployee);
            txtname = (TextView) itemView.findViewById(R.id.txtname);
            txtFrom_date = (TextView) itemView.findViewById(R.id.txtFrom_date);
            txtTo_date = (TextView) itemView.findViewById(R.id.txtTo_date);
            txtLeaveReason = (TextView) itemView.findViewById(R.id.txtLeavereason);
            btnApprove =(Button)itemView.findViewById(R.id.btnApprove) ;
            btnReject =(Button)itemView.findViewById(R.id.btnReject) ;
            itemView.setOnClickListener(this);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent intent = new Intent(context, ApproveActivity.class);

            System.out.println("request_id " + Employeeleavelist.get(position).getRequest_id());


            intent.putExtra("name", Employeeleavelist.get(position).getName());
            intent.putExtra("from_date", Employeeleavelist.get(position).getFrom_date());
            intent.putExtra("to_date", Employeeleavelist.get(position).getTo_date());
            intent.putExtra("leavereason", Employeeleavelist.get(position).getReason());
            intent.putExtra("action", "update");

            context.startActivity(intent);

        }
    }
}
