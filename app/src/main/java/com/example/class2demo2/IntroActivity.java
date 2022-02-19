package com.example.class2demo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.class2demo2.feed.MainDrawerActivity;
import com.example.class2demo2.login.LoginActivity;
import com.example.class2demo2.model.Model;

import java.util.concurrent.Executors;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Model.instance.executor.execute(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Model.instance.isSignedIn()){
                Log.d("LOGGEDUSER:",Model.instance.getUid());
                Model.instance.mainThread.post(()->{
                    toFeedActivity();
                });
            }else{
                Model.instance.mainThread.post(()->{
                    toLoginActivity();
                });
            }
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, MainDrawerActivity.class);
        startActivity(intent);
        finish();
    }
}