package com.hezhiheng.networkpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.hezhiheng.networkpractice.async.GetResponseFeedTask;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkActivity extends AppCompatActivity {
    public static final String URL = "https://twc-android-bootcamp.github.io/fake-data/data/default.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get_response)
    public void btnGetResponseClick(Button button) {
        GetResponseFeedTask task = new GetResponseFeedTask();
        try {
            String result = task.execute(URL).get();
            Toast.makeText(NetworkActivity.this, result, Toast.LENGTH_SHORT).show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(NetworkActivity.this, "Response is empty", Toast.LENGTH_SHORT).show();
        }
    }
}