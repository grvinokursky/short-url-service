package com.example.shorturl;

import com.example.shorturl.config.ApplicationProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class ShortUrlApplication {
    public static void main(String[] args) {
        // Раскомментировать вызов метода, если в консоли кракозябры.
        setOutEncoding();
        new SpringApplicationBuilder(ShortUrlApplication.class).headless(false).run(args);
    }

    private static void setOutEncoding() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }
}