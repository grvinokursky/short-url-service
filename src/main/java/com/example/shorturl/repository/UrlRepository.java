package com.example.shorturl.repository;

import com.example.shorturl.model.UrlModel;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UrlRepository {
    List<UrlModel> findByUser(UUID userId);
    Optional<UrlModel> findByShortUrl(String shortUrl);
    void save(UrlModel url) throws Exception;
    int deleteExpiredOrExceeded(Instant now);
    void incrementClickCount(UUID userId, String shortUrl);
}
