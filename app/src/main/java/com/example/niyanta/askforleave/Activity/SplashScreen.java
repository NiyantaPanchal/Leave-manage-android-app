package com.example.niyanta.askforleave.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.niyanta.askforleave.R;
import com.example.niyanta.askforleave.Common.PrefsUtils;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = PrefsUtils.getPreferences(getApplicationContext());

                if (sharedPreferences.getBoolean(PrefsUtils.isLogin, false)) {
                    Intent intent = new Intent(SplashScreen.this, LeaveActivity.class);
                    startActivity(intent);
                } else {
                    Intent LoginActivity = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(LoginActivity);
                }
                finish();
            }
        }, 2000);
    }
}