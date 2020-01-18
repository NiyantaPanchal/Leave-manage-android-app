package com.example.niyanta.askforleave.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.niyanta.askforleave.Adapters.LeaveAdapter;
import com.example.niyanta.askforleave.Adapters.NotificationAdapter;
import com.example.niyanta.askforleave.Common.API;
import com.example.niyanta.askforleave.Common.Common;
import com.example.niyanta.askforleave.Models.LeaveModel;
import com.example.niyanta.askforleave.R;
import com.example.niyanta.askforleave.Common.PrefsUtils;
import com.example.niyanta.askforleave.Common.SharedPrefManager;
import com.example.niyanta.askforleave.VolleyAPI.CustomRequest;
import com.example.niyanta.askforleave.VolleyAPI.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText etemail, etpassword;
    Button buttonlogin;
    ImageView logo;
    ProgressBar progressBar;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = PrefsUtils.getPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        logo = (ImageView) findViewById(R.id.leaveLogo);
        progressBar = findViewById(R.id.progressBar);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        buttonlogin = (Button) findViewById(R.id.buttonlogin);

        // Login button click event
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username, password from EditText
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();

                if (!isConnected(getApplicationContext())) {
                    Toast.makeText(LoginActivity.this, "Check your network connection", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    etemail.setError("Enter Email");
                    etemail.requestFocus();
                } else if (password.isEmpty()) {
                    etpassword.setError("Enter Password");
                    etpassword.requestFocus();
                } else {
                    final android.app.AlertDialog alertDialog = Common.showDialog(LoginActivity.this);
                    final HashMap<String, String> paramater = new HashMap<String, String>();
                    paramater.put("TAG", "sign_in");
                    paramater.put("email", email);
                    paramater.put("password", password);

                    final CustomRequest customRequest = new CustomRequest(Request.Method.POST, API.COMMON_URL, paramater, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("FROM LOGIN", response.toString());
                            try {
                                String errorCode = response.optString("success");
                                if (errorCode.equalsIgnoreCase("1")) {

                                    JSONObject userObject = response.getJSONObject("user");
                                    //Log.d("sign_in", LeaveList.toString());

                                    String email = userObject.optString("email");
                                    String password = userObject.optString("password");
                                    String id = userObject.optString("id");
                                    String user_id = userObject.optString("user_id");
                                    String name = userObject.optString("name");
                                    String rep_mang_id = userObject.optString("rep_mang_id");
                                    String leave_count = userObject.optString("leave_count");
                                    String leavestatus = userObject.optString("leavestatus");
                                    String reason = userObject.optString("reason");
                                    String from_date = userObject.optString("from_date");
                                    String to_date = userObject.optString("to_date");
                                    String role = userObject.optString("role");

                                    editor.putBoolean(PrefsUtils.isLogin, true).apply();
                                    editor.putString(PrefsUtils.id, id).apply();
                                    editor.putString(PrefsUtils.user_id, user_id).apply();
                                    editor.putString(PrefsUtils.name, name).apply();
                                    editor.putString(PrefsUtils.email, etemail.getText().toString()).apply();
                                    editor.putString(PrefsUtils.password, etpassword.getText().toString()).apply();
                                    editor.putString(PrefsUtils.rep_mang_id, rep_mang_id).apply();
                                    editor.putString(PrefsUtils.leave_count, leave_count).apply();
                                    editor.putString(PrefsUtils.leavestatus, leavestatus).apply();
                                    editor.putString(PrefsUtils.reason,reason).apply();
                                    editor.putString(PrefsUtils.from_date, from_date).apply();
                                    editor.putString(PrefsUtils.to_date, to_date).apply();
                                    editor.putString(PrefsUtils.role,role).apply();


                                    final HashMap<String, String> paramater1 = new HashMap<String, String>();
                                    paramater1.put("user_id", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.user_id, ""));
                                    paramater1.put("device_id", SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken());

                                    CustomRequest customRequest1 = new CustomRequest(Request.Method.POST, API.Token_URL + "/save_device", paramater1, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            Log.e("FROM LOGIN2", response.toString());

                                            String errorCode = response.optString("success");
                                            if (errorCode.equalsIgnoreCase("1")) {

                                                Toast.makeText(LoginActivity.this, "Token Updated.. " + response, Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, LeaveActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                            alertDialog.dismiss();
                                        }

                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("RESPONSE ERRR", error.toString());
                                        }
                                    });

                                    customRequest1.setRetryPolicy(new DefaultRetryPolicy(30000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                    VolleyService.getInstance(LoginActivity.this).getRequestQueue().add(customRequest1);



                                } else {
                                    String message = response.getString("error_msg");
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                                Log.e("FROM LOGIN", e.toString());
                                e.printStackTrace();
                            }
                            alertDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("RESPONSE ERR", error.toString());
                        }
                    });

                    customRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    VolleyService.getInstance(LoginActivity.this).getRequestQueue().add(customRequest);
                }
            }

        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etemail.getWindowToken(), 0);
    }

    private boolean isValidEmailLogin(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}