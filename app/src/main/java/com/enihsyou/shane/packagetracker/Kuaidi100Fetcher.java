package com.enihsyou.shane.packagetracker;

import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Kuaidi100Fetcher {
    public static final String SEARCH_NUMBER = "autonumber/autoComNum";
    public static final String SEARCH_PACKAGE = "query";
    private static final String TAG = "Kuaidi100Fetcher";
    private static final HttpUrl ENDPOINT = HttpUrl.parse("http://www.kuaidi100.com");

    /**
     * 获得json
     *
     * @param url 传递过来的URL路径
     *
     * @return 获得的Json文本
     */
    private Gson getJson(HttpUrl url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).build();
        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        gson.fromJson(response.body().charStream(), PackageTraffic.class);
        return gson;
    }

    public HttpUrl makeNumberSearch(String number) {
        return ENDPOINT.newBuilder()
                .addEncodedPathSegments(SEARCH_NUMBER)
                .addEncodedQueryParameter("text", number)
                .build();
    }

    public HttpUrl makePackageSearch(String number, String type) {
        return ENDPOINT.newBuilder()
                .addEncodedPathSegment(SEARCH_PACKAGE)
                .addEncodedQueryParameter("type", type)
                .addEncodedQueryParameter("postid", number)
                .build();
    }
}
