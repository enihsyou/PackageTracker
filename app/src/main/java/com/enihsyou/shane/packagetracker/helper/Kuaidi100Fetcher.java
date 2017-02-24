package com.enihsyou.shane.packagetracker.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.model.*;
import com.enihsyou.shane.packagetracker.view.TrafficCardView;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Kuaidi100Fetcher {
    private static final String TAG = "Kuaidi100Fetcher";
    private static final String SEARCH_NUMBER = "autonumber/autoComNum";
    private static final String SEARCH_PACKAGE = "query";
    private static final String SEARCH_NETWORK = "network/www/searchapi.do";
    private static final String SEARCH_COURIER = "courier/searchapi.do";
    private static final String SEARCH_PRICE = "order/unlogin/price.do";
    private static final String SEARCH_TIME = "time";
    private static final String LOGO_URL = "order/images/company/"; //小的很的那种
    private static final String LOGO_URL_144 = "images/all/144/"; //144*144方形
    private static final String LOGO_URL_148 = "images/all/148x48/"; //148*48长方形
    /*使用HTTPS*/
    private static HttpUrl ENDPOINT = HttpUrl.parse("https://www.kuaidi100.com");
    private static HttpUrl CDN_ENDPOINT = HttpUrl.parse("https://cdn.kuaidi100.com");

    private final Gson gson;
    private final JsonParser jsonParser;
    private final SimpleDateFormat dateFormatter;
    private final OkHttpClient client;

    public Kuaidi100Fetcher() {
        gson = new GsonBuilder().create();
        jsonParser = new JsonParser();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        client = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .build();
    }

    public static HttpUrl resolveRelativeUrl(String URL) {
        return ENDPOINT.resolve(URL);
    }

    public static HttpUrl buildCompanyLongLogoUrl(String companyCode) {
        return CDN_ENDPOINT.newBuilder()
            .addPathSegments(LOGO_URL_148)
            .addPathSegment(companyCode + "_logo.png")
            .build();
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
        Picasso.with(detailContainer.getContext()).load(Kuaidi100Fetcher.buildCompanyLogoUrl(searchResult.getCompanyCode()).toString())
            .placeholder(R.drawable.package_variant_closed)
            .into(detailContainer.getCompanyHead());
        return detailContainer;
    }

    private static TrafficCardView setUPDetailCardBody(PackageTrafficSearchResult searchResult,
        TrafficCardView detailContainer) {
        detailContainer.addTraffics(searchResult.getTraffics());
        return detailContainer;
    }

    /**
     * 获取公司图标，大号的那种 144px
     *
     * @param companyCode 公司代码
     *
     * @return 图片的URL
     */
    @NonNull
    public static HttpUrl buildCompanyLogoUrl(String companyCode) {
        return CDN_ENDPOINT.newBuilder()
            .addPathSegments(LOGO_URL_144)
            .addPathSegment(companyCode + ".png")
            .build();
    }

    /**
     * 获取公司图标，小号的那种
     *
     * @param companyCode 公司代码
     * @param imageExt    图片格式
     *
     * @return 图片的URL
     */
    @NonNull
    public static HttpUrl buildCompanyLogoUrl(String companyCode, String imageExt) {
        return ENDPOINT.newBuilder()
            .addPathSegments(LOGO_URL)
            .addPathSegment(companyCode + imageExt)
            .build();
    }

    public PriceSearchResult priceResult(String startPlaceCode, String endPlaceCode, String street, String weight, String  currentPage, String pageSize) throws
        IOException {
        HttpUrl request = buildPriceSearchUrl(currentPage, pageSize);
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("startPlace", startPlaceCode)
            .addEncoded("endPlace", endPlaceCode)
            .addEncoded("street", street)
            .addEncoded("weight", weight)
            .build();
        Log.d(TAG, String.format("priceResult: 发送价格查询URL: %s 内容: %s %s %s %s", request, startPlaceCode, endPlaceCode, street, weight));
        Response response = getJson(request, requestBody);
        return parsePriceJson(response);
    }

    @NonNull
    private static HttpUrl buildPriceSearchUrl(String currentPage, String pageSize) {
        return ENDPOINT.newBuilder()
            .addPathSegments(SEARCH_PRICE)
            .addQueryParameter("action", "searchOrderPrice")
            .addQueryParameter("currentPage", currentPage)
            .addQueryParameter("pageSize", pageSize)
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

    private PriceSearchResult parsePriceJson(Response response) {
        return gson.fromJson(response.body().charStream(), PriceSearchResult.class);
    }

    public TimeSearchResult timeResult(String from, String to) throws IOException {
        HttpUrl request = buildTimeSearchUrl(from, to);
        Log.d(TAG, "timeResult: 发送时效查询URL " + request);
        Response response = getHtml(request);
        return parseTimeHtml(response);
    }

    @NonNull
    private static HttpUrl buildTimeSearchUrl(String from, String to) {
        return ENDPOINT.newBuilder()
            .addPathSegment(SEARCH_TIME)
            .addEncodedPathSegment(String.format("timecost_%s_%s", from, to))
            .build();
    }

    private Response getHtml(HttpUrl url) throws IOException {
        Request request = new Request.Builder()
            .url(url).build();
        return client.newCall(request).execute();
    }

    private static TimeSearchResult parseTimeHtml(Response response) {
        try {
            Document document = Jsoup.parse(response.body().string());
            Elements parent =
                document.select("body > div.container.w960 > div.col_1 > table > tbody > tr");
            TimeSearchResult result = new TimeSearchResult();
            for (int i = 1; i < parent.size(); i++) {
                Element element = parent.get(i);
                String companyName = element.child(0).text();
                String companyLogoUrl = element.child(0).child(0).attr("src");
                String time = element.child(1).text();
                result.addEntries(companyName, companyLogoUrl, time);
            }
            return result;
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "parseTimeHtml: 解析失败 API改了？？？", e);
        } catch (IOException e) {
            Log.wtf(TAG, "parseTimeHtml: ···解析HTML失败", e);
        }
        return null;
    }

    /**
     * 获取网点信息
     *
     * @param area    县区
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
        Log.d(TAG, "networkResult() called with: area = [" + area + "], keyword = [" + keyword
            + "], offset = [" + offset + "], size = [" + size + "]");
        HttpUrl request = buildNetworkSearchUrl();
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("method", "searchnetwork")
            .addEncoded("area", area)
            .addEncoded("keyword", keyword)
            .addEncoded("offset", offset)
            .addEncoded("size", size)
            .build();
        Log.d(TAG, String.format("networkResult: 发送网点查询URL: %s 内容: %s %s %s %s", request, area, keyword, offset, size));
        Response response = getJson(request, requestBody);
        return parseNetworkJson(response);
    }

    @NonNull
    private static HttpUrl buildNetworkSearchUrl() {
        return ENDPOINT.newBuilder()
            .addPathSegments(SEARCH_NETWORK)
            .build();
    }

    private NetworkSearchResult parseNetworkJson(Response response) {
        NetworkSearchResult result =
            gson.fromJson(response.body().charStream(), NetworkSearchResult.class);
        for (NetworkSearchResult.NetworkNetList listResult : result.getNetLists()) {
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
        Log.d(TAG, "packageResult: 发送包裹查询URL " + request);
        Response response = getJson(request);
        return parsePackageJson(response);
    }

    @NonNull
    private static HttpUrl buildPackageSearchURL(String number, String type) {
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
        Log.d(TAG, "companyResult: 发送公司查询URL: " + request);
        Response response = getJson(request);
        return parseCompanyJson(response);
    }

    @NonNull
    private static HttpUrl buildNumberSearchURL(String number) {
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
        if (autoArray == null) return null;
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

    public void DownloadImage(SetImage callback, final HttpUrl url, final ImageView view) {
        if (url == null) return;
        final Request request = new Request.Builder().url(url).build();
        Log.d(TAG, "DownloadImage: 发送下载图片请求 " + url);
        client.newCall(request).enqueue(new DownLoad(callback, view));
    }

    /**
     * 获取输入县区的城市列表
     *
     * @param cityCode 城市代码
     *
     * @return 解析结果
     *
     * @throws IOException 网络错误
     */
    public List<NetworkCityResult> networkCityResult(String cityCode) throws IOException {
        HttpUrl request = buildNetworkSearchUrl();
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("method", "getcounty")
            .addEncoded("city", cityCode)
            .build();
        Log.d(TAG, String.format("networkCityResult: 发送三级城市查询URL: %s 内容: %s", request, cityCode));
        Response response = getJson(request, requestBody);
        return parseNetworkCityJson(response);
    }

    private List<NetworkCityResult> parseNetworkCityJson(Response response) {
        return gson.fromJson(response.body().charStream(), new TypeToken<List<NetworkCityResult>>() {}.getType());
    }

    public CourierSearchResult courierResult(String area, String keyword) throws IOException {
        HttpUrl url = buildCourierSearchUrl();
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("method", "courieraround")
            .addEncoded("json", gson.toJson(new CourierJson(area, keyword), CourierJson.class))
            .build();
        Response response = getJson(url, requestBody);
        return parseCourierJson(response);
    }

    @Nullable
    private static HttpUrl buildCourierSearchUrl() {
        return ENDPOINT.newBuilder()
            .addPathSegments(SEARCH_COURIER)
            .build();
    }

    private CourierSearchResult parseCourierJson(Response response) {
        return gson.fromJson(response.body().charStream(), CourierSearchResult.class);
    }

    public CourierDetailSearchResult courierDetailResult(String guid) throws IOException {
        HttpUrl url = buildCourierSearchUrl();
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("method", "courierdetail")
            .addEncoded("json", gson.toJson(new CourierDetailJson(guid), CourierDetailJson.class))
            .build();
        Response response = getJson(url, requestBody);
        return parseCourierDetailJson(response);
    }

    private CourierDetailSearchResult parseCourierDetailJson(Response response) {
        return gson.fromJson(response.body().charStream(), CourierDetailSearchResult.class);
    }

    /**
     * 设置ImageView的图片
     */
    public interface SetImage {
        void SetBitmap(Bitmap bitmap, ImageView view);
    }

    private static class CourierJson {
        @SerializedName("xzqnamae")
        private String area;
        @SerializedName("keywords")
        private String keyword;

        public CourierJson(String area, String keyword) {
            this.area = area;
            this.keyword = keyword;
        }
    }

    private static class CourierDetailJson {
        @SerializedName("guid")
        private String guid;

        public CourierDetailJson(String guid) {
            this.guid = guid;
        }
    }

    private static class DownLoad implements Callback {
        private SetImage listener;
        private ImageView image;

        public DownLoad(SetImage callback, ImageView view) {
            listener = callback;
            image = view;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Log.w(TAG, "onFailure: 下载失败", e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.v(TAG, "onResponse: 下载成功 " + response.request().url());
            byte[] bytes = response.body().bytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            listener.SetBitmap(bitmap, image);
        }
    }
}
