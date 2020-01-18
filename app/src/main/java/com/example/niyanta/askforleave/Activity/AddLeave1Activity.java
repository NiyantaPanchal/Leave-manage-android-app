package com.example.niyanta.askforleave.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niyanta.askforleave.Common.API;
import com.example.niyanta.askforleave.Common.PrefsUtils;
import com.example.niyanta.askforleave.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddLeave1Activity extends AppCompatActivity  {


    ArrayList<String> leaveTypeArrayList;
    ArrayList<String> leaveTypeIdArray;
    ArrayAdapter leaveAdapter;

    ArrayList<String> leaveDayArrayList;
    ArrayList<String> leaveDayIdArray;
    ArrayAdapter leaveDayAdapter;

    private Spinner spinnerDay;
    private Spinner spinnerType;
    private EditText edtLeaveReason;
    private EditText edtFromDate;
    private RelativeLayout rlvFromDate;
    private ImageView ivFromCalender;
    private EditText edtToDate;
    private RelativeLayout rlvToDate;
    private ImageView ivToCalender;
    private EditText edtDays;
    private Button btnApply;
    private Button btnClose;

    Calendar myCalendar;
    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addleave_1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView ivBack = toolbar.findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//Leave Type
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        leaveAdapter = ArrayAdapter.createFromResource(this, R.array.leave_arrays, android.R.layout.simple_selectable_list_item);
        leaveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(leaveAdapter);
        spinnerType.setPrompt("-Choose-");
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) spinnerType.getChildAt(position);
                if (position == 0) {

                    if (tv != null) {
                        tv.setText("Select Leave Type");
                        tv.setTextColor(Color.GRAY);
                    }

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }});

//Leave Day
        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        leaveDayAdapter = ArrayAdapter.createFromResource(this, R.array.leaveday_arrays, android.R.layout.simple_selectable_list_item);
        leaveDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(leaveDayAdapter);
        spinnerDay.setPrompt("Choose");
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) spinnerDay.getChildAt(position);
                if (position == 0) {

                    if (tv != null) {
                        tv.setText("Select Leave Day");
                        tv.setTextColor(Color.GRAY);
                    }
                }
                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            } });

        edtLeaveReason = (EditText) findViewById(R.id.edtLeaveReason);
        edtFromDate = (EditText) findViewById(R.id.edtFromDate);
        rlvFromDate = (RelativeLayout) findViewById(R.id.rlvFromDate);
        ivFromCalender = (ImageView) findViewById(R.id.ivFromCalender);
        edtToDate = (EditText) findViewById(R.id.edtToDate);
        rlvToDate = (RelativeLayout) findViewById(R.id.rlvToDate);
        ivToCalender = (ImageView) findViewById(R.id.ivToCalender);
        edtDays = (EditText) findViewById(R.id.edtDays);
        edtDays.setText("" + PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.leave_count, ""));
        btnApply = (Button) findViewById(R.id.btnApply);
        btnClose = (Button) findViewById(R.id.btnClose);

        myCalendar = Calendar.getInstance();
        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        month = myCalendar.get(Calendar.MONTH);
        year = myCalendar.get(Calendar.YEAR);


        rlvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLeave1Activity.this, dateListenerFrom, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        ivFromCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLeave1Activity.this, dateListenerFrom, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        rlvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLeave1Activity.this, dateListenerTo, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        ivToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLeave1Activity.this, dateListenerTo, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });



        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLeaveDetails();
            }

        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddLeave1Activity.this, LeaveActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }



        private void saveLeaveDetails() {
                    String leavetypeid=spinnerType.getSelectedItem().toString();
                    String leaveday=spinnerDay.getSelectedItem().toString();
                    String reason = edtLeaveReason.getText().toString();
                    String from_date = edtFromDate.getText().toString();
                    String to_date = edtToDate.getText().toString();

                    if (!isConnectd(getApplicationContext())) {
                        Toast.makeText(AddLeave1Activity.this, "Check your network connection", Toast.LENGTH_SHORT).show();

                    }if (reason.isEmpty()) {
                        edtLeaveReason.setError("Enter reason");
                        edtLeaveReason.requestFocus();
                        return;
                    }
                    if (from_date.isEmpty()) {
                        edtFromDate.setError("Choose From Date");
                        edtFromDate.requestFocus();
                        return;
                    }
                    if (to_date.isEmpty()) {
                        edtToDate.setError("Choose To Date");
                        edtToDate.requestFocus();
                        return;
                    }
                    if (isConnectd(AddLeave1Activity.this)) {

                     StringRequest mRequest = new StringRequest(Request.Method.POST, API.COMMON_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddLeave1Activity.this, "Response : " + response, Toast.LENGTH_SHORT).show();
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
                        mParam.put("TAG", "leave_request");
                        mParam.put("leavetypeid", spinnerType.getSelectedItem().toString());
                        mParam.put("leaveday", spinnerDay.getSelectedItem().toString());
                        mParam.put("reason", edtLeaveReason.getText().toString());
                        mParam.put("from_date", edtFromDate.getText().toString());
                        mParam.put("to_date", edtToDate.getText().toString());
                        mParam.put("leave_count", edtDays.getText().toString());
                        mParam.put("created_by", "");
                        mParam.put("modified_by", "");
                        mParam.put("user_id", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.user_id, ""));
                        mParam.put("rep_mang_id", PrefsUtils.getPreferenceValue(getApplicationContext(), PrefsUtils.rep_mang_id, ""));
                        return mParam;

                    }

                };

                Volley.newRequestQueue(AddLeave1Activity.this).add(mRequest);
            }
            {

                Toast.makeText(AddLeave1Activity.this, "Request Successfull", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddLeave1Activity.this, LeaveActivity.class);
                startActivity(intent);
            }
        }






    public static boolean isConnectd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    DatePickerDialog.OnDateSetListener dateListenerFrom = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            edtFromDate.setText(format.format(myCalendar.getTime()));
        }

    };

    DatePickerDialog.OnDateSetListener dateListenerTo = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            edtToDate.setText(format.format(myCalendar.getTime()));
        }

    };

}