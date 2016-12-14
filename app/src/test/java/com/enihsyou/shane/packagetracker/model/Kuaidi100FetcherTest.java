package com.enihsyou.shane.packagetracker.model;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Kuaidi100FetcherTest {
    private Kuaidi100Fetcher fetcher;


    @Test
    public void buildNetworkSearchURL() throws Exception {
        String excepted = "https://www.kuaidi100.com/network/www/searchapi.do";
        String actual = fetcher.buildNetworkSearchURL().toString();
        assertEquals("Test network search URL", excepted, actual);
    }

    @Before
    public void setUp() throws Exception {
        fetcher = new Kuaidi100Fetcher();
    }

    @Test
    public void buildNumberSearchURL() throws Exception {
        String excepted = "http://www.kuaidi100.com/autonumber/autoComNum?text=560522058285";
        String actual = fetcher.buildNumberSearchURL("560522058285").toString();
        assertEquals("Test number search URL", excepted, actual);
    }

    @Test
    public void buildPackageSearchURL() throws Exception {
        String excepted = "http://www.kuaidi100.com/query?type=yuantong&postid=560522058285";
        String actual = fetcher.buildPackageSearchURL("560522058285", "yuantong").toString();
        assertEquals("Test package search URL", excepted, actual);
    }

    @Test
    public void parseCompanyJson() throws Exception {
        CompanyAutoSearchResult searchResult =
            fetcher.parseCompanyJson(
                fetcher.getJson(
                    fetcher.buildNumberSearchURL("560522058285")));
        assertThat(searchResult, new IsInstanceOf(CompanyAutoSearchResult.class));
    }

    @Test
    public void parsePackageJson() throws Exception {
        /*标准正常的解析*/
        PackageTrafficSearchResult searchResult =
            fetcher.parsePackageJson(
                fetcher.getJson(
                    fetcher.buildPackageSearchURL("560522058285", "yuantong")));
        assertThat(searchResult, new IsInstanceOf(PackageTrafficSearchResult.class));
    }

    @Test
    public void parseWrongPackageJson() throws Exception {
        /*不存在的单号*/
        PackageTrafficSearchResult searchResult =
            fetcher.parsePackageJson(
                fetcher.getJson(
                    fetcher.buildPackageSearchURL("56052205828", "yuantong")));
        assertNull(searchResult);
    }

    @Test
    public void parseNetworkJson() throws Exception {
        RequestBody requestBody = new FormBody.Builder()
            .addEncoded("area", "上海 - 上海市")
            .addEncoded("keyword", "")
            .addEncoded("method", "searchnetwork")
            .addEncoded("offset", "0")
            .addEncoded("size", "8")
            .build();
        NetworkSearchResult searchResult =
            fetcher.parseNetworkJson(
                fetcher.getJson(
                    fetcher.buildNetworkSearchURL(), requestBody));
        assertThat(searchResult, new IsInstanceOf(NetworkSearchResult.class));
    }
}
