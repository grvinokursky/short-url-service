package com.example.shorturl.model;

import java.time.Instant;
import java.util.UUID;

public class UrlModel {
    private UUID id = UUID.randomUUID();
    private UUID userId;
    private String baseUrl;
    private String shortUrl;
    private int maxClickCount; // 0 = no limit
    private int clickCount = 0;
    private int existTimeInHours;
    private Instant createDate = Instant.now();

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID u) { this.userId = u; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String b) { this.baseUrl = b; }
    public String getShortUrl() { return shortUrl; }
    public void setShortUrl(String s) { this.shortUrl = s; }
    public int getMaxClickCount() { return maxClickCount; }
    public void setMaxClickCount(int m) { this.maxClickCount = m; }
    public int getClickCount() { return clickCount; }
    public void setClickCount(int c) { this.clickCount = c; }
    public int getExistTimeInHours() { return existTimeInHours; }
    public void setExistTimeInHours(int e) { this.existTimeInHours = e; }
    public Instant getCreateDate() { return createDate; }
}