package com.enihsyou.shane.packagetracker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Kuaidi100FetcherTest {
    private Kuaidi100Fetcher fetcher;

    @Before
    public void setUp() throws Exception {
        fetcher = new Kuaidi100Fetcher();
    }

    @Test
    public void makeNumberSearch() throws Exception {
        String excepted = "http://www.kuaidi100.com/autonumber/autoComNum?text=560522058285";
        String actual = fetcher.makeNumberSearch("560522058285").toString();
        assertEquals("Test number search", excepted, actual);
    }

    @Test
    public void makePackageSearch() throws Exception {
        String excepted = "http://www.kuaidi100.com/query?type=yuantong&postid=560522058285";
        String actual = fetcher.makePackageSearch("560522058285", "yuantong").toString();
        assertEquals("Test number search", excepted, actual);
    }
}
