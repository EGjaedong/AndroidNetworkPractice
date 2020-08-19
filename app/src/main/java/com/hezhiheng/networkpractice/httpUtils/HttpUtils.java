package com.hezhiheng.networkpractice.httpUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    public static String getResponse(String url){
        OkHttpClient client = new OkHttpClient();
        String result = null;
        Request request = new Request.Builder().url(url).build();
        try(Response response = client.newCall(request).execute()) {
            result = response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
