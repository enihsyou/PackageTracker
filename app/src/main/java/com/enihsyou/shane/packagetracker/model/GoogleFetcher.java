package com.enihsyou.shane.packagetracker.model;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GoogleFetcher {
    private static HttpUrl ENDPOINT = HttpUrl.parse("https://maps.google.cn/maps/api/geocode/json");
    private final Gson gson;
    private final OkHttpClient client;

    public GoogleFetcher() {
        this.gson = new GsonBuilder().create();
        this.client = new OkHttpClient();
    }

    public CurrentLocationResult locationResult(String lat, String lng) throws IOException {
        HttpUrl request = buildLocationUrl(lat, lng);
        Response response = getJson(request);
        return parseLocationJson(response);
    }

    @NonNull
    private HttpUrl buildLocationUrl(String lat, String lng) {
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
