package com.example.shorturl.worker;

import com.example.shorturl.config.ApplicationProperties;
import com.example.shorturl.service.UrlService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.*;

@Component
public class WorkerForCleaningService {
    private final UrlService urlService;
    private final int periodPerMinutes;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public WorkerForCleaningService(UrlService urlService, ApplicationProperties props) {
        this.urlService = urlService;
        this.periodPerMinutes = props.getWorkerForCleaning().getPeriodPerMinutes();

        System.out.println("Выход из конструктора воркера для очистки");
    }

    @PostConstruct
    public void start() {
        System.out.println("Запуск воркера для очистки");

        scheduler.scheduleWithFixedDelay(this::doWork, 0, periodPerMinutes, TimeUnit.MINUTES);

        System.out.println("Запущен воркер для очистки, период выполнения в минутах: " + periodPerMinutes);
    }

    private void doWork() {
        try {
            var now = Instant.now();
            var removed = urlService.cleanup(now);
            if (removed > 0) {
                System.out.println("Очистка: удалено ссылок: " + removed + ".");
            }
        } catch (Throwable t) {
            System.out.println("Ошибка при очистке: " + t.getMessage());
            t.printStackTrace(System.out);
        }
    }

    @PreDestroy
    public void stop() {
        System.out.println("Остановка воркера для очистки...");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) scheduler.shutdownNow();
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Воркер для очистки остановлен.");
    }
}