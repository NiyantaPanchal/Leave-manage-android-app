package com.example.niyanta.askforleave.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.niyanta.askforleave.Adapters.EmployeeAdapter;
import com.example.niyanta.askforleave.Common.API;
import com.example.niyanta.askforleave.Common.PrefsUtils;
import com.example.niyanta.askforleave.Models.EmployeeModel;
import com.example.niyanta.askforleave.R;
import com.example.niyanta.askforleave.VolleyAPI.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class EmployeeLeave extends AppCompatActivity {

    ArrayList<EmployeeModel> Employeeleavelist;
    RecyclerView recyclerView;
    final Context context = this;


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
        setContentView(R.layout.activity_employee_leave);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ImageView ivBack = (ImageView) toolbar.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.employee_leave_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Employeeleavelist = new ArrayList<>();

        if (!isConnectd(getApplicationContext())) {
            Toast.makeText(this, "Check your network connection", Toast.LENGTH_SHORT).show();
        } else {

            Log.e("LOGIN", "CALL API");
            getemployeelist();
        }

    }



    private void getemployeelist() {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("TAG", "approvals");
        //parameter.put("email", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.email, ""));
        // parameter.put("password",PrefsUtils.getPreferenceValue(getApplicationContext(),PrefsUtils.password,""));
        parameter.put("user_id", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.user_id, ""));


        CustomRequest customRequest = new CustomRequest(Request.Method.POST, API.COMMON_URL, parameter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("LOGIN 123", response.toString());

                Employeeleavelist = new ArrayList<>();

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat format_new = new SimpleDateFormat("dd-MMMM-yyyy");

                Date from_date = new Date();
                Date to_date = new Date();

                try {
                    JSONArray EmployeeleaveList = response.getJSONArray("data");

                    for (int i = 0; i < EmployeeleaveList.length(); i++) {

                        JSONObject LeaveListObj = EmployeeleaveList.getJSONObject(i);

                        Iterator iterator = LeaveListObj.keys();

                        //while(iterator.hasNext()){
                        String key = (String) iterator.next();
                        JSONObject issue = LeaveListObj.getJSONObject(key);

                        Log.e("JSON", issue.toString());
                        //JSONObject newObj = issue.optJSONObject("u");

                        from_date = format.parse(issue.optString("from_date"));
                        to_date = format.parse(issue.optString("to_date"));

                        EmployeeModel model = new EmployeeModel();
                        model.setReason(issue.optString("reason"));

                        model.setFrom_date(format_new.format(from_date));
                        model.setTo_date(format_new.format(to_date));

                        String key1 = (String) iterator.next();
                        JSONObject issue1 = LeaveListObj.getJSONObject(key1);
                        Log.e("JSON1", issue1.toString());
                        model.setName(issue1.optString("userfullname"));

                        Employeeleavelist.add(model);

                    }
                    EmployeeAdapter adapter = new EmployeeAdapter(EmployeeLeave.this, Employeeleavelist);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(EmployeeLeave.this).add(customRequest);
    }
}
