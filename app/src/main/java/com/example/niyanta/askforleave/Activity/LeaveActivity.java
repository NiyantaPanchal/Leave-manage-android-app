package com.example.niyanta.askforleave.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.example.niyanta.askforleave.Common.API;
import com.example.niyanta.askforleave.Adapters.LeaveAdapter;
import com.example.niyanta.askforleave.Models.LeaveModel;
import com.example.niyanta.askforleave.R;
import com.example.niyanta.askforleave.Common.PrefsUtils;
import com.example.niyanta.askforleave.VolleyAPI.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.niyanta.askforleave.Common.PrefsUtils.email;

public class LeaveActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //a list to store all the products
    List<LeaveModel> leaveModelList;

    //the recyclerview
    RecyclerView recyclerView;
    EditText inputSearch;
    SearchView editsearch;

    private SwipeRefreshLayout swipeRefresh;



    public static boolean isConnectd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaves);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);



        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.leave_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the leavelist
        leaveModelList = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        TextView txtID = (TextView) hView.findViewById(R.id.txtID);
        txtID.setText("Your ID : " + PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.user_id, ""));

        TextView txtrep_mang_Id = (TextView) hView.findViewById(R.id.txtrep_mang_ID);
        txtrep_mang_Id.setText("Reporting Manager ID : " + PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.rep_mang_id, ""));


        if (!isConnectd(getApplicationContext())) {
            Toast.makeText(this, "Check your network connection", Toast.LENGTH_SHORT).show();
        } else {

            //this method will fetch and parse json
            Log.e("FROM LOGIN", " ");

            //to display it in recyclerview
            getleavelistAPI();
        }


        navigationView.setNavigationItemSelectedListener(this);
        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        //editsearch.setOnQueryTextListener(this);
    }



    private void getleavelistAPI() {

        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("TAG", "sign_in");
        parameter.put("email",PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.email, ""));
        parameter.put("password",PrefsUtils.getPreferenceValue(getApplicationContext(),PrefsUtils.password,""));
       //parameter.put("user_id", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.user_id, ""));
        /*
         * Creating a custom Request
         * The request type is POST defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, API.COMMON_URL,parameter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("FROM LOGIN", response.toString());


                leaveModelList = new ArrayList<>();

                try {

                    JSONObject userObject = response.getJSONObject("user");

                    //converting the string to json array object
                    JSONArray LeaveList = userObject.getJSONArray("leaves");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat format_new = new SimpleDateFormat("dd-MMMM-yyyy");

                    //traversing through all the object
                    for (int i = 0; i < LeaveList.length(); i++){
                        JSONObject LeaveListOb = LeaveList.getJSONObject(i);
                        JSONObject LeaveListObj = LeaveListOb.getJSONObject("MainLeaverequest");


                        LeaveModel model=new LeaveModel();
                        model.setUser_id(LeaveListObj.getString("user_id"));

                        Date fromDate = format.parse(LeaveListObj.getString("from_date"));
                        Date toDate = format.parse(LeaveListObj.getString("to_date"));

                        model.setFrom_date(format_new.format(fromDate));
                        model.setTo_date(format_new.format(toDate));
                        model.setLeavestatus(LeaveListObj.getString("leavestatus"));
                        model.setreason(LeaveListObj.getString("reason"));

                        leaveModelList.add(model);
                        }
                     //creating adapter object and setting it to recyclerview
                    LeaveAdapter adapter = new LeaveAdapter (LeaveActivity.this, leaveModelList);
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

        //adding our customrequest to queue
        Volley.newRequestQueue(LeaveActivity.this).add(customRequest);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_add_Leave: {

                Intent intent = new Intent(LeaveActivity.this, AddLeave1Activity.class);
                startActivity(intent);

                break;
            }
            case R.id.nav_dashboard: {

                Intent intent = new Intent(LeaveActivity.this, LeaveActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.nav_approved:{

                Intent intent = new Intent(LeaveActivity.this,EmployeeLeave.class);
                startActivity(intent);

                break;
            }
            case R.id.nav_notification:{
                Intent intent = new Intent(LeaveActivity.this,NotificationView.class);
                startActivity(intent);

                break;
            }

            case R.id.nav_logout: {

                SharedPreferences sharedPreferences = PrefsUtils.getPreferences(getApplicationContext());
                final SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(PrefsUtils.isLogin, false).apply();
                editor.putString(PrefsUtils.id, "").apply();
                editor.putString(PrefsUtils.user_id, "").apply();
                editor.putString(PrefsUtils.name, "").apply();
                editor.putString(email, "").apply();
                editor.commit();

                Intent intent = new Intent(LeaveActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;
            }

        }
        return false;
    }
}

