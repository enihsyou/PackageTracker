package com.enihsyou.shane.packagetracker.helper;

import android.support.annotation.NonNull;
import android.util.Log;
import com.enihsyou.shane.packagetracker.model.CurrentLocationResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GoogleFetcher {
    private static final String TAG = "GoogleFetcher";
    private static HttpUrl ENDPOINT = HttpUrl.parse("https://maps.google.cn/maps/api/geocode/json");
    private final Gson gson;
    private final OkHttpClient client;

    public GoogleFetcher() {
        gson = new GsonBuilder().create();
        client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();
    }

    /**
     * @param lat latitude 纬度
     * @param lng longitude 经度
     *
     * @return 解析结果
     *
     * @throws IOException 网络错误
     */
    public CurrentLocationResult locationResult(String lat, String lng) throws IOException {
        HttpUrl request = buildLocationUrl(lat, lng);
        Log.v(TAG, "locationResult: 发送的请求地址 " + request);
        Response response = getJson(request);
        return parseLocationJson(response);
    }

    @NonNull
    private static HttpUrl buildLocationUrl(String lat, String lng) {
        return ENDPOINT.newBuilder()
            .addEncodedQueryParameter("language", "zh-CN")
            .addEncodedQueryParameter("latlng", lat.concat(",").concat(lng)).build();
    }

    /**
     * 获得json GET
     *
     * @param url 传递过来的URL路径
     *
     * @return 获得的Json文本
     */
    private Response getJson(HttpUrl url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }

    private CurrentLocationResult parseLocationJson(Response response) {
        return gson.fromJson(response.body().charStream(), CurrentLocationResult.class);
    }
}
