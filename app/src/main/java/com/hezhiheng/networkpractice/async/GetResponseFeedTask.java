package com.hezhiheng.networkpractice.async;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetResponseFeedTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = strings[0];
        String result = null;
        Request request = new Request.Builder().url(url).build();
        try(Response response = okHttpClient.newCall(request).execute()) {
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
