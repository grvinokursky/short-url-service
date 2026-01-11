package com.example.shorturl.service;

import com.example.shorturl.config.ApplicationProperties;
import com.example.shorturl.model.UrlModel;
import com.example.shorturl.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final YandexClickerUrlShorteningService yandexClickerUrlShorteningService;

    private final int defaultMaxClicks;
    private final int defaultExistTimeInHours;

    public UrlService(UrlRepository urlRepository, YandexClickerUrlShorteningService shortener, ApplicationProperties applicationProperties) {
        this.urlRepository = urlRepository;
        this.yandexClickerUrlShorteningService = shortener;
        this.defaultMaxClicks = applicationProperties.getShortUrlSettings().getMaxClicks();
        this.defaultExistTimeInHours = applicationProperties.getShortUrlSettings().getExistTimeInHours();
    }

    public UrlModel create(UUID userId, String baseUrl, int maxClicks) throws Exception {
        var shortUrl = yandexClickerUrlShorteningService.getShortUrlAsync(baseUrl).join();

        UrlModel user = new UrlModel();
        user.setUserId(userId);
        user.setBaseUrl(baseUrl);
        user.setMaxClickCount(maxClicks);
        user.setExistTimeInHours(defaultExistTimeInHours);
        user.setShortUrl(shortUrl);

        urlRepository.save(user);

        return user;
    }

    public UrlModel create(UUID userId, String baseUrl) throws Exception {
        var shortUrl = yandexClickerUrlShorteningService.getShortUrlAsync(baseUrl).join();

        UrlModel user = new UrlModel();
        user.setUserId(userId);
        user.setBaseUrl(baseUrl);
        user.setMaxClickCount(defaultMaxClicks);
        user.setExistTimeInHours(defaultExistTimeInHours);
        user.setShortUrl(shortUrl);

        urlRepository.save(user);

        return user;
    }

    public List<UrlModel> listByUser(UUID userId) {
        return urlRepository.findByUser(userId);
    }

    public void open(UUID userId, String shortUrl) {
        var url = urlRepository.findByShortUrl(shortUrl);
        if (url.isEmpty()) {
            return;
        }

        urlRepository.incrementClickCount(userId, shortUrl);
    }

    public int cleanup(Instant now) {
        return urlRepository.deleteExpiredOrExceeded(now);
    }
}
