package com.example.shorturl.repository;

import com.example.shorturl.model.UrlModel;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.time.Instant;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUrlRepository implements UrlRepository {
    private final Map<UUID, UrlModel> storage = new ConcurrentHashMap<>();

    @Override
    public List<UrlModel> findByUser(UUID userId) {
        var result = new ArrayList<UrlModel>();
        for (var u : storage.values()) {
            if (u.getUserId().equals(userId)) result.add(u);
        }

        return result;
    }

    @Override
    public Optional<UrlModel> findByShortUrl(String shortUrl) {
        return storage.values().stream().filter(u -> u.getShortUrl().equals(shortUrl)).findFirst();
    }

    @Override
    public void save(UrlModel url) throws Exception {
        if (storage.values().stream().anyMatch(u -> Objects.equals(u.getShortUrl(), url.getShortUrl()))) {
            throw new Exception(String.format("URL для данного адреса уже существует - %s.", url.getShortUrl()));
        }

        storage.put(url.getId(), url);
    }

    @Override
    public int deleteExpiredOrExceeded(Instant now) {
        var toRemove = new ArrayList<UUID>();

        for (var e : storage.entrySet()) {
            var u = e.getValue();
            var expired = now.isAfter(u.getCreateDate().plusSeconds((long) u.getExistTimeInHours() * 60 * 60));
            var exceeded = u.getMaxClickCount() > 0 && u.getClickCount() >= u.getMaxClickCount();
            if (expired || exceeded) {
                toRemove.add(e.getKey());
            }
        }

        for (var id : toRemove) {
            storage.remove(id);
        }

        return toRemove.size();
    }

    @Override
    public void incrementClickCount(UUID userId, String shortUrl) {
        var urlModel = storage.values().stream()
                .filter(u -> u.getUserId().equals(userId) && u.getShortUrl().equals(shortUrl))
                .findFirst();

        urlModel.ifPresent(model -> model.setClickCount(model.getClickCount() + 1));
    }
}