package com.enihsyou.shane.packagetracker;

import android.support.annotation.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Kuaidi100Fetcher {
    private static final String SEARCH_NUMBER = "autonumber/autoComNum";
    private static final String SEARCH_PACKAGE = "query";
    private static final String TAG = "Kuaidi100Fetcher";
    private static final HttpUrl ENDPOINT = HttpUrl.parse("http://www.kuaidi100.com");
    private final JsonParser jsonParser;
    private final SimpleDateFormat dateFormatter;

    public Kuaidi100Fetcher() {
        jsonParser = new JsonParser();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    /**
     * 获得json
     *
     * @param url 传递过来的URL路径
     *
     * @return 获得的Json文本
     */
    public Response getJson(HttpUrl url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).build();
        Response response = client.newCall(request).execute();
        return response;

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

    @Nullable
    public CompanyAutoSearchResult parseCompanyJson(Response response) {
        CompanyAutoSearchResult searchResult = null;
        JsonElement jsonElement = jsonParser.parse(response.body().charStream());
        if (jsonElement.isJsonObject()) {
            searchResult = new CompanyAutoSearchResult();
            ArrayList<CompanyEachAutoSearch> auto = new ArrayList<>();

            JsonObject jsonObject = jsonElement.getAsJsonObject(); //Json本体
            String num = jsonObject.get("num").getAsString(); //获取单号

            JsonArray autoArray = jsonObject.getAsJsonArray("auto"); //获取公司信息列表
            for (JsonElement element : autoArray) {
                CompanyEachAutoSearch eachCompany = new CompanyEachAutoSearch();
                JsonObject detail = element.getAsJsonObject();

                eachCompany.setCompanyCode(detail.get("comCode").getAsString());
                eachCompany.setNumberCount(detail.get("noCount").getAsString());
                eachCompany.setNumberPrefix(detail.get("noPre").getAsString());
                auto.add(eachCompany);
            }
            searchResult.setNumber(num);
            searchResult.setCompanies(auto);
        }
        return searchResult;
    }

    @Nullable
    public PackageTrafficSearchResult parsePackageJson(Response response) {
        PackageTrafficSearchResult searchResult = null;
        JsonElement jsonElement = jsonParser.parse(response.body().charStream());
        if (jsonElement.isJsonObject()) {
            searchResult = new PackageTrafficSearchResult();
            ArrayList<PackageEachTraffic> data = new ArrayList<>();

            JsonObject jsonObject = jsonElement.getAsJsonObject(); //Json本体
            String nu = jsonObject.get("nu").getAsString();
            String com = jsonObject.get("com").getAsString();
            int state = jsonObject.get("state").getAsInt();

            JsonArray dataArray = jsonObject.getAsJsonArray("data");
            for (JsonElement element : dataArray) {
                PackageEachTraffic eachTraffic = new PackageEachTraffic();
                JsonObject detail = element.getAsJsonObject();

                try {
                    eachTraffic.setTime(dateFormatter.parse(detail.get("time").getAsString()));
                } catch (ParseException never) {
                    //won't happen
                }
                JsonElement location = detail.get("location");
                eachTraffic.setLocation(location.isJsonNull() ? "" : location.getAsString());
                eachTraffic.setContext(detail.get("context").getAsString());
                data.add(eachTraffic);
            }
            searchResult.setNumber(nu);
            searchResult.setCompany(com);
            searchResult.setStatus(state);
            searchResult.setTraffics(data);
        }
        return searchResult;
    }
}
