package com.davidremington.stormy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.davidremington.stormy.R;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static Gson gson = new Gson();
    private static OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
