package com.hezhiheng.networkpractice.httpUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    public static String getResponse(String url){
        OkHttpClient client = new OkHttpClient();
        String result;
        Request request = new Request.Builder().url(url).build();
        try(Response response = client.newCall(request).execute()) {
            result = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
        return result;
    }
}
