package com.example.shorturl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private WorkerForCleaning workerForCleaning = new WorkerForCleaning();
    private ConnectionStrings connectionStrings = new ConnectionStrings();
    private ShortUrlSettings shortUrlSettings = new ShortUrlSettings();

    public WorkerForCleaning getWorkerForCleaning() { return workerForCleaning; }
    public void setWorkerForCleaning(WorkerForCleaning w) { this.workerForCleaning = w; }

    public ConnectionStrings getConnectionStrings() { return connectionStrings; }
    public void setConnectionStrings(ConnectionStrings c) { this.connectionStrings = c; }

    public ShortUrlSettings getShortUrlSettings() {
        return shortUrlSettings;
    }

    public void setShortUrlSettings(ShortUrlSettings shortUrlSettings) {
        this.shortUrlSettings = shortUrlSettings;
    }

    public static class WorkerForCleaning {
        @Min(1)
        private int periodPerMinutes = 1;
        public int getPeriodPerMinutes() { return periodPerMinutes; }
        public void setPeriodPerMinutes(int p) { this.periodPerMinutes = p; }
    }

    public static class ConnectionStrings {
        @NotBlank
        private String yandexClickerBaseAddress;
        public String getYandexClickerBaseAddress() { return yandexClickerBaseAddress; }
        public void setYandexClickerBaseAddress(String y) { this.yandexClickerBaseAddress = y; }
    }

    public static class ShortUrlSettings {
        @Min(1)
        private int maxClicks = 1;
        @Min(1)
        private int existTimeInHours = 1;

        public int getMaxClicks() {
            return maxClicks;
        }

        public void setMaxClicks(int maxClicks) {
            this.maxClicks = maxClicks;
        }

        public int getExistTimeInHours() {
            return existTimeInHours;
        }

        public void setExistTimeInHours(int existTimeInHours) {
            this.existTimeInHours = existTimeInHours;
        }
    }
}