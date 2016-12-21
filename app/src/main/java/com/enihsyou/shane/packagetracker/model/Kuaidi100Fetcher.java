package com.enihsyou.shane.packagetracker.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.view.TrafficCardView;
import com.google.gson.*;
import okhttp3.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Kuaidi100Fetcher {
    private static final String SEARCH_NUMBER = "autonumber/autoComNum";
    private static final String SEARCH_PACKAGE = "query";
    private static final String SEARCH_NETWORK = "network/www/searchapi.do";
    private static final String SEARCH_COURIER = "courier";

    private static HttpUrl ENDPOINT = HttpUrl.parse("http://www.kuaidi100.com");

    private final Gson gson;
    private final JsonParser jsonParser;
    private final SimpleDateFormat dateFormatter;
    private final OkHttpClient client;

    Kuaidi100Fetcher() {
        gson = new GsonBuilder().create();
        jsonParser = new JsonParser();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        client = new OkHttpClient();
    }

    /**
     * 从packageTrafficSearchResult中获取信息，生成一张卡片，添加到cardContainer上，如果成功 关闭键盘
     *
     * @param searchResult  提取信息的对象
     * @param trafficHeader 插入卡片的容器
     *
     * @return 带有信息的卡片
     */
    public static LinearLayout generateCard(PackageTrafficSearchResult searchResult,
        LinearLayout trafficHeader) {
        /*下面创建快递信息卡片*/
        // 获得卡片部件，之后的每个小部件都添加到这张卡片里面，这卡片有两部分:头部信息和主体信息
        TrafficCardView detailContainer =
            (TrafficCardView) trafficHeader.findViewById(R.id.header_detail_container);
        /*设置头部*/
        setUpDetailCardHeader(searchResult, detailContainer);
        /*设置主体*/
        setUPDetailCardBody(searchResult, detailContainer);
        return trafficHeader;
    }

    private static TrafficCardView setUpDetailCardHeader(PackageTrafficSearchResult searchResult,
        TrafficCardView detailContainer) {
        // 设置CardView各个部件
        detailContainer.setCompanyName(searchResult.getCompanyString());
        detailContainer.setPackageNumber(searchResult.getNumber());
        // detailContainer.setCompanyHead(null);
        return detailContainer;
    }

    private static TrafficCardView setUPDetailCardBody(PackageTrafficSearchResult searchResult,
        TrafficCardView detailContainer) {
        detailContainer.addTraffics(searchResult.getTraffics());
        return detailContainer;
    }

    /**
     * 获取网点信息
     *
     * @param area    地区
     * @param keyword 搜索关键词
     * @param offset  偏移量，用于翻页
     * @param size    获取大小
     *
     * @return 解析结果
     *
     * @throws IOException 网络错误
     */
    public NetworkSearchResult networkResult(String area, String keyword, String offset, String size)
        throws IOException {
        HttpUrl request = buildNetworkSearchUrl();
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("area", area)
            .addEncoded("keyword", keyword)
            .addEncoded("method", "searchnetwork")
            .addEncoded("offset", offset)
            .addEncoded("size", size)
            .build();
        Response response = getJson(request, requestBody);
        return parseNetworkJson(response);
    }

    @NonNull
    private HttpUrl buildNetworkSearchUrl() {
        return ENDPOINT.newBuilder()
            .addPathSegments(SEARCH_NETWORK)
            .build();
    }

    /**
     * 获得json POST
     *
     * @param url 传递过来的URL路径
     *
     * @return 获得的Json文本
     */
    private Response getJson(HttpUrl url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
            .url(url)
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .post(body).build();
        return client.newCall(request).execute();
    }

    private NetworkSearchResult parseNetworkJson(Response response) {
        NetworkSearchResult result =
            gson.fromJson(response.body().charStream(), NetworkSearchResult.class);
        for (NetworkNetListResult listResult : result.getNetLists()) {
            listResult.cleanHtml();
        }
        return result;
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

    @NonNull
    private HttpUrl buildPackageSearchURL(String number, String type) {
        return ENDPOINT.newBuilder()
            .addPathSegment(SEARCH_PACKAGE)
            .addEncodedQueryParameter("type", type)
            .addEncodedQueryParameter("postid", number)
            .build();
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

    /**
     * 解析处理响应的数据，适用于kuaidi100
     * 获取快递单的结果
     *
     * @param response 获得的响应
     *
     * @return 解析结果，null为失败
     */
    @Nullable
    private PackageTrafficSearchResult parsePackageJson(Response response) {
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

    @NonNull
    private HttpUrl buildNumberSearchURL(String number) {
        return ENDPOINT.newBuilder()
            .addPathSegments(SEARCH_NUMBER)
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
    private CompanyAutoSearchResult parseCompanyJson(Response response) {
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
     * 获取输入地区的城市列表
     *
     * @param cityCode 城市代码
     *
     * @return 解析结果
     *
     * @throws IOException 网络错误
     */
    public NetworkCityResult[] networkCityResult(String cityCode) throws IOException {
        HttpUrl request = buildNetworkSearchUrl();
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("method", "getcounty")
            .addEncoded("city", cityCode)
            .build();
        Response response = getJson(request, requestBody);
        return parseNetworkCityJson(response);
    }

    private NetworkCityResult[] parseNetworkCityJson(Response response) {
        return gson.fromJson(response.body().charStream(), NetworkCityResult[].class);
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
}
