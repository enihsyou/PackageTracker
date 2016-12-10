package com.enihsyou.shane.packagetracker.model;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Kuaidi100Fetcher {
    private static final String SEARCH_NUMBER = "autonumber/autoComNum";
    private static final String SEARCH_PACKAGE = "query";

    private static HttpUrl ENDPOINT = HttpUrl.parse("http://www.kuaidi100.com");

    private final JsonParser jsonParser;
    private final SimpleDateFormat dateFormatter;
    private final OkHttpClient client;

    Kuaidi100Fetcher() {
        jsonParser = new JsonParser();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        client = new OkHttpClient();
    }

    /**
     * 在运行中修改API服务器的地址，用于测试目的
     * 在设置面板中添加文字框选项，输入新的地址和端口号，调用此方法
     *
     * @param baseURL 要设置的网址
     */
    public void setBaseURL(String baseURL) {
        ENDPOINT = HttpUrl.parse(baseURL);
    }

    /**
     * 获取封装好的结果
     *
     * @param number 请求单号
     * @param type   请求公司名
     *
     * @return 解析结果
     *
     * @throws IOException 网络错误
     */
    public PackageTrafficSearchResult packageResult(String number, String type) throws IOException {
        HttpUrl request = buildPackageSearchURL(number, type);
        Response response = getJson(request);
        return parsePackageJson(response);
    }

    /**
     * 获得json
     *
     * @param url 传递过来的URL路径
     *
     * @return 获得的Json文本
     */
    Response getJson(HttpUrl url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }

    HttpUrl buildPackageSearchURL(String number, String type) {
        return ENDPOINT.newBuilder()
                .addEncodedPathSegment(SEARCH_PACKAGE)
                .addEncodedQueryParameter("type", type)
                .addEncodedQueryParameter("postid", number)
                .build();
    }

    /**
     * 解析处理响应的数据，适用于kuaidi100
     * 获取快递单的结果
     *
     * @param response 获得的响应
     *
     * @return 解析结果，null为失败
     */
    @Nullable
    PackageTrafficSearchResult parsePackageJson(Response response) {
        /*获取JSON*/
        JsonObject jsonObject = jsonParser.parse(response.body().charStream()).getAsJsonObject();
        /*判断结果正常*/
        int status = jsonObject.get("status").getAsInt();
        if (status != HttpURLConnection.HTTP_OK) return null; //请求错误
        /*初始化变量*/
        PackageTrafficSearchResult searchResult;//返回结果
        searchResult = new PackageTrafficSearchResult();
        ArrayList<PackageEachTraffic> data = new ArrayList<>();
        /*解析结果*/
        String nu = jsonObject.get("nu").getAsString();
        String com = jsonObject.get("com").getAsString();
        int state = jsonObject.get("state").getAsInt();

        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        for (JsonElement element : dataArray) {
            PackageEachTraffic eachTraffic = new PackageEachTraffic();
            JsonObject detail = element.getAsJsonObject();

            JsonElement location = detail.get("location"); //处理空值
            eachTraffic.setLocation(location.isJsonNull() ? "" : location.getAsString());
            eachTraffic.setTime(detail.get("time").getAsString(), dateFormatter);
            eachTraffic.setContext(detail.get("context").getAsString());

            data.add(eachTraffic);
        }
        searchResult.setNumber(nu);
        searchResult.setCompany(com);
        searchResult.setStatus(state);
        searchResult.setTraffics(data);

        return searchResult;
    }

    /**
     * 获取封装好的结果，用于单号自动完成
     *
     * @param number 请求单号
     *
     * @return 解析结果
     *
     * @throws IOException 网络错误
     */
    public CompanyAutoSearchResult companyResult(String number) throws IOException {
        HttpUrl request = buildNumberSearchURL(number);
        Response response = getJson(request);
        return parseCompanyJson(response);
    }

    HttpUrl buildNumberSearchURL(String number) {
        return ENDPOINT.newBuilder()
                .addEncodedPathSegments(SEARCH_NUMBER)
                .addEncodedQueryParameter("text", number)
                .build();
    }

    /**
     * 解析处理响应的数据，适用于kuaidi100，使用GET方法没问题
     * 预测快递单对应的快递公司
     *
     * @param response 获得的响应
     *
     * @return 解析结果，null为失败
     */
    @Nullable
    CompanyAutoSearchResult parseCompanyJson(Response response) {
        /*获取JSON*/
        JsonObject jsonObject = jsonParser.parse(response.body().charStream()).getAsJsonObject();
        /*判断结果正常*/
        int status = response.code();
        if (status != HttpURLConnection.HTTP_OK) return null; //请求错误
        /*初始化变量*/
        CompanyAutoSearchResult searchResult;//返回结果
        searchResult = new CompanyAutoSearchResult();
        ArrayList<CompanyEachAutoSearch> auto = new ArrayList<>();
        /*解析结果*/
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

        return searchResult;
    }

    /**
     * 从packageTrafficSearchResult中获取信息，生成一张卡片，添加到cardContainer上，如果成功 关闭键盘
     *
     * @param packageTrafficSearchResult 提取信息的对象
     * @param cardContainer              插入卡片的容器
     * @param layoutInflater             LayoutInflater
     *
     * @return 带有信息的卡片
     */
    public View generateCard(PackageTrafficSearchResult packageTrafficSearchResult,
            LinearLayout cardContainer, LayoutInflater layoutInflater) {
        /*下面创建快递信息卡片*/
        // 卡片的根布局
        View detailCard = layoutInflater
                .inflate(R.layout.card_package, cardContainer, false);
        // 获得卡片部件，之后的每个小部件都添加到这张卡片里面
        CardView detailContainer = (CardView) detailCard.findViewById(R.id.detail_container);
        // 获得卡片里面的详细跟踪信息的展示布局，之后的每个详细跟踪信息都添加到这里面
        LinearLayout eachDetailContainer =
                (LinearLayout) detailContainer.findViewById(R.id.each_detail_container);
        // 获得各个部件
        ImageView companyHead =
                (ImageView) detailContainer.findViewById(R.id.company_head_card_view);
        TextView packageNumber =
                (TextView) detailContainer.findViewById(R.id.package_number_card_view);
        TextView companyName =
                (TextView) detailContainer.findViewById(R.id.company_name_card_view);
        // 设置CardView各个部件
        packageNumber.setText(packageTrafficSearchResult.getNumber());
        companyName.setText(CompanyCodeToString
                .valueOf(packageTrafficSearchResult.getCompany())
                .toString());
        /*创建每一条信息的显示*/
        for (PackageEachTraffic eachTraffic : packageTrafficSearchResult.getTraffics()) {
            // 详细跟踪信息的根布局
            View trafficLayout = layoutInflater
                    .inflate(R.layout.traffic_detail, eachDetailContainer, false);

            TextView detailDatetime =
                    (TextView) trafficLayout.findViewById(R.id.detail_datetime);
            TextView detailContext = (TextView) trafficLayout.findViewById(R.id.detail_context);
            /*文字框赋值*/
            detailDatetime.setText(eachTraffic.getTimeString());
            detailContext.setText(eachTraffic.getContext());

            eachDetailContainer.addView(trafficLayout);
        }
        return detailCard;
    }
}
