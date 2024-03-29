package com.squirrelcandy.silverlocker.acts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.squirrelcandy.silverlocker.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        Postponer postpone = new Postponer();
        postpone.start();
    }

    private class Postponer extends Thread {
        @Override
        public void run() {
            try {
                sleep(2000);

            }catch (InterruptedException e) {
                //TODO handle exception
            }

            Intent lgnTnt = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(lgnTnt);
            SplashActivity.this.finish();
        }
    }
}
