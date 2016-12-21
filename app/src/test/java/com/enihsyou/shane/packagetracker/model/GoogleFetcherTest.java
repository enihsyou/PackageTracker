package com.enihsyou.shane.packagetracker.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GoogleFetcherTest {
    private GoogleFetcher fetcher;
    @Before
    public void setUp() throws Exception {
        fetcher = new GoogleFetcher();
    }

    @Test
    public void locationResult() throws Exception {
        String lat = "31.2933738";
        String lng = "121.5566791";
        CurrentLocationResult result = fetcher.locationResult(lat, lng);
        assertNotNull(result);
        assertEquals("OK", result.status, "OK");
    }
}
