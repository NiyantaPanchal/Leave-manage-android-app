package com.example.niyanta.askforleave.Activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niyanta.askforleave.Common.API;
import com.example.niyanta.askforleave.Common.PrefsUtils;
import com.example.niyanta.askforleave.Models.EmployeeModel;
import com.example.niyanta.askforleave.R;
import com.example.niyanta.askforleave.VolleyAPI.CustomRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApproveActivity extends AppCompatActivity {
    private ImageView IVemployee;
    private TextView txtname;
    private EditText edtFromdate,edtTodate,edtLeaveReason;
    private Button btnApprove,btnReject;
    private Intent intent;
    private String action_string;
    private String request_id;
    private String comments;
    String strFrom_Date, strTo_Date, strReason, strName;
    final Context context = this;

    private ArrayList<EmployeeModel> Employeeleavelist;

    public static boolean isConnectd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView ivBack = toolbar.findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Employeeleavelist = new ArrayList<>();
        IVemployee = (ImageView) findViewById(R.id.IVemployee);
        txtname  =(TextView) findViewById(R.id.txtname);
        edtFromdate =(EditText) findViewById(R.id.edtFromdate);
        edtTodate =(EditText) findViewById(R.id.edtTodate);
        edtLeaveReason =(EditText) findViewById(R.id.edtLeaveReason);

        intent = getIntent();


        if (intent != null && intent.hasExtra("action")) {

            action_string = "1";

            strName = intent.getStringExtra("name");
            strFrom_Date = intent.getStringExtra("from_date");
            strTo_Date = intent.getStringExtra("to_date");
            strReason= intent.getStringExtra("leavereason");
            request_id = intent.getStringExtra("request_id");
            txtname.setText(strName);
            edtFromdate.setText(strFrom_Date);
            edtTodate.setText(strTo_Date);
            edtLeaveReason.setText(strReason);
            }

        btnApprove =(Button) findViewById(R.id.btnApprove);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve(); }
        });

        btnReject =(Button) findViewById(R.id.btnReject);

    }


    public void ShowDialog(View view){

            LayoutInflater li = LayoutInflater.from(context);
            View customView = li.inflate(R.layout.custom, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);
        final EditText userInput = (EditText) customView.findViewById(R.id.editTextDialogUserInput);
        builder.setCancelable(false);
        builder.setTitle("Reject");
       // builder.setMessage("Please enter the reason:");
        builder.setPositiveButton("Ok!!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, API.APPROVE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ApproveActivity.this, "Response : " + response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("VolleyError " + error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> parameter = new HashMap<>();
                        parameter.put("user_id", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.user_id, ""));
                        parameter.put("request_id", request_id);
                        parameter.put("Status", "rejected");
                        parameter.put("comments", comments);
                        return parameter;
                    }
                    };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Toast.makeText(getApplicationContext(),"Ok",Toast.LENGTH_SHORT).show();
                finish();
                Volley.newRequestQueue(ApproveActivity.this).add(stringRequest);

            }



        }) .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Cancle",Toast.LENGTH_SHORT).show();
                finish();
                }
        });
        builder.create().show();

        }

    private void approve() {
        if (isConnectd(ApproveActivity.this)) {

            StringRequest mRequest = new StringRequest(Request.Method.POST, API.APPROVE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // response
                    Log.d("Response", response);
                    Toast.makeText(ApproveActivity.this, "Response : " + response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("VolleyError " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> mParam = new HashMap<>();
                   // mParam.put("user_id", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.user_id, ""));
                    mParam.put("request_id",request_id);
                    mParam.put("status","approved");
                    return mParam;

                }
                };
            mRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(ApproveActivity.this).add(mRequest);
        }
        {
            Intent intent = new Intent(ApproveActivity.this, EmployeeLeave.class);
            startActivity(intent);
        }
    }

    }

