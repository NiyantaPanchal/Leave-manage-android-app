package com.example.niyanta.askforleave.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.niyanta.askforleave.Adapters.NotificationAdapter;
import com.example.niyanta.askforleave.Common.API;
import com.example.niyanta.askforleave.Common.PrefsUtils;
import com.example.niyanta.askforleave.Common.SharedPrefManager;
import com.example.niyanta.askforleave.Interface.getNotificationFromFirebase;
import com.example.niyanta.askforleave.Models.LeaveModel;
import com.example.niyanta.askforleave.Models.NotificationData;
import com.example.niyanta.askforleave.Common.DatabaseManager;
import com.example.niyanta.askforleave.R;
import com.example.niyanta.askforleave.VolleyAPI.CustomRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.niyanta.askforleave.Common.SharedPrefManager.message;

public class NotificationView extends AppCompatActivity {
    Context context;
    String message;
    DatabaseManager databaseManager;
    ArrayList<NotificationData> notificationDataArrayList;
    NotificationAdapter notificationAdapter;

    private Toolbar toolbar;
    private ImageView ivBack;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView notificationList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

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
        setContentView(R.layout.activity_notification_view);

        // fcm successfully registered
        // now subscribe to `global` topic to receive app wide notifications
        FirebaseMessaging.getInstance().subscribeToTopic(SharedPrefManager.TOPIC_GLOBAL);

        initComponent();
        initData();
        initClickListeners();

        if (!isConnectd(getApplicationContext())) {
            Toast.makeText(this, "Check your network connection", Toast.LENGTH_SHORT).show();
        } else {

            //this method will fetch and parse json
            Log.e("FROM LOGIN", " ");

            //to display it in recyclerview
            notificationListAPI();
        }


    }

    private void notificationListAPI() {

        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("TAG", "sign_in");
        parameter.put("email", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.email, ""));
        parameter.put("password",PrefsUtils.getPreferenceValue(getApplicationContext(),PrefsUtils.password,""));
        /*
         * Creating a custom Request
         * The request type is POST defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, API.NOTIF_URL,parameter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("FROM LOGIN", response.toString());


                notificationDataArrayList = new ArrayList<>();

                try {

                    JSONObject userObject = response.getJSONObject("user");

                    //converting the string to json array object
                    JSONArray NotificationList = userObject.getJSONArray("notification");

                    //traversing through all the object
                    for (int i = 0; i < NotificationList.length(); i++){
                        JSONObject NotificationListOb = NotificationList.getJSONObject(i);


                        NotificationData model=new NotificationData();
                        model.setId(NotificationListOb.getString("id"));
                        model.setTitle(NotificationListOb.getString("title"));
                        model.setMessage(NotificationListOb.getString("message"));
                        model.setTime(NotificationListOb.getString("time"));

                        notificationDataArrayList.add(model);
                    }
                    //creating adapter object and setting it to recyclerview
                    NotificationAdapter adapter = new NotificationAdapter (NotificationView.this, notificationDataArrayList);
                    notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    notificationList.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //adding our customrequest to queue
        Volley.newRequestQueue(NotificationView.this).add(customRequest);
    }

    public void initComponent() {

        databaseManager = new DatabaseManager(NotificationView.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) toolbar.findViewById(R.id.ivBack);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        notificationList = (RecyclerView) findViewById(R.id.notification_list);

        notificationList.setLayoutManager(new LinearLayoutManager(NotificationView.this));
    }

    public void initData() {

        swipeRefresh.setRefreshing(true);

        databaseManager.getAllNotifications(new getNotificationFromFirebase() {
            @Override
            public void OnSuccess(ArrayList<NotificationData> notificationData) {
                notificationDataArrayList = notificationData;
                notificationAdapter = new NotificationAdapter(NotificationView.this, notificationDataArrayList);
                notificationList.setAdapter(notificationAdapter);
                notificationList.setVisibility(View.VISIBLE);

                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void OnFailure(String Message) {
                swipeRefresh.setRefreshing(false);
                notificationList.setVisibility(View.GONE);
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    public void initClickListeners() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}