package com.example.shorturl.console;

import com.example.shorturl.model.UserModel;
import com.example.shorturl.service.UrlService;
import com.example.shorturl.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;
import java.util.*;

@Component
public class ConsoleApplication implements CommandLineRunner {
    private final UserService userService;
    private final UrlService urlService;

    private UserModel currentUser;

    public ConsoleApplication(UserService userService, UrlService urlService) {
        this.userService = userService;
        this.urlService = urlService;
    }

    @Override
    public void run(String... args) {
        while (true) {
            authorization();
            startShortUrlService();
        }
    }

    public void openUrlInBrowser(String url) {
        try {
            // Проверяем, поддерживается ли Desktop API
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    // Открываем ссылку в браузере по умолчанию
                    desktop.browse(new URI(url));
                    System.out.println("Открываю ссылку в браузере: " + url);
                } else {
                    System.out.println("Действие BROWSE не поддерживается");
                }
            } else {
                System.out.println("Desktop не поддерживается");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при открытии браузера: " + e.getMessage());
        }
    }

    private void startShortUrlService() {
        System.out.println();
        System.out.println("Запущен сервис генерации коротких URL");

        printShortUrlServiceHelp();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("Введите команду:");

            var commandString = sc.nextLine();
            if (commandString.isEmpty()) {
                System.out.println("Пустая строка не является командой.");
                continue;
            }

            if (commandString.equalsIgnoreCase("exit")) {
                return;
            }

            var commandWords = commandString.split(" ");
            executeShortUrlServiceCommand(commandWords);
        }
    }

    private void executeShortUrlServiceCommand(String[] commandWords) {
        if (commandWords.length == 1 && Objects.equals(commandWords[0], "list")) {
            printUrlList();
            return;
        } else if (commandWords.length == 4 && Objects.equals(commandWords[0], "create") && Objects.equals(commandWords[2], "-maxClicks")) {
            createUrl(commandWords[1], commandWords[3]);
            return;
        } else if (commandWords.length == 2 && Objects.equals(commandWords[0], "create")) {
            createUrl(commandWords[1]);
            return;
        } else if (commandWords.length == 2 && Objects.equals(commandWords[0], "open")) {
            openUrl(commandWords[1]);
            return;
        }

        System.out.println("Невалидная команда.");
    }

    private void openUrl(String shortUrl) {
        try {
            Desktop.getDesktop().browse(new URI(shortUrl));
        } catch (Exception e) {
            System.out.printf("В процессе перехода по короткому URL-адресу произошла ошибка. %s", e.getMessage());
            return;
        }

        urlService.open(currentUser.getId(), shortUrl);
    }

    private void createUrl(String baseUrl) {
        try {
            var shortUrl = urlService.create(currentUser.getId(), baseUrl);
            System.out.printf("Короткая ссылка была успешно создана - %s", shortUrl.getShortUrl());
        } catch (Exception e) {
            System.out.printf("Невалидная команда. %s", e.getMessage());
        }
    }

    private void createUrl(String baseUrl, String maxClicksString) {
        try {
            var maxClicks = Integer.parseInt(maxClicksString);

            var shortUrl = urlService.create(currentUser.getId(), baseUrl, maxClicks);
            System.out.printf("Короткая ссылка была успешно создана - %s", shortUrl.getShortUrl());
        } catch (Exception e) {
            System.out.printf("Невалидная команда. %s", e.getMessage());
        }
    }

    private void printUrlList() {
        try {
            var urls = urlService.listByUser(currentUser.getId());

            if (urls.isEmpty()) {
                System.out.println("Список коротких ссылок пуст.");
                return;
            }

            for (var i = 0; i < urls.size(); i++) {
                var url = urls.get(i);

                System.out.printf("%d. Идентификатор %s. Короткий адрес %s. Полный адрес %s. Максимальное кол-во переходов %d. Текущее кол-во переходов %d. Дата окончания действия %s.%n",
                        i + 1,
                        url.getId(),
                        url.getShortUrl(),
                        url.getBaseUrl(),
                        url.getMaxClickCount(),
                        url.getClickCount(),
                        url.getCreateDate().plusSeconds((long) url.getExistTimeInHours() * 60 * 60));
            }
        } catch (Exception e) {
            System.out.printf("Не удалось получить список URL. %s", e.getMessage());
        }
    }

    private void authorization() {
        System.out.println("Выполните авторизацию для входа в сервис генерации коротких URL");
        printAuthorizationHelp();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("Введите команду:");

            var commandString = sc.nextLine();
            if (commandString.isEmpty()) {
                System.out.println("Пустая строка не является командой.");
                continue;
            }

            var commandWords = commandString.split(" ");
            if (tryExecuteAuthorizationCommand(commandWords) && (Objects.equals(commandWords[0], "login")) || Objects.equals(commandWords[0], "l")) {
                return;
            }
        }
    }

    private boolean tryExecuteAuthorizationCommand(String[] commandWords) {
        if (commandWords.length == 2 && (Objects.equals(commandWords[0], "login") || Objects.equals(commandWords[0], "l"))) {
            return tryLoginCommand(commandWords[1]);
        } else if (commandWords.length == 2 && (Objects.equals(commandWords[0], "registration") || Objects.equals(commandWords[0], "reg"))) {
            return tryRegistrationCommand(commandWords[1]);
        }

        System.out.println("Невалидная команда.");
        return false;
    }

    private boolean tryLoginCommand(String username) {
        try {
            var loginResult = userService.getByName(username);
            if (loginResult.isEmpty()) {
                System.out.println("При попытке авторизации произошла ошибка.");
                return false;
            }

            currentUser = loginResult.get();

            System.out.println("Авторизация выполнена успешно.");
            return true;
        } catch (Exception e) {
            System.out.printf("При попытке авторизации произошла ошибка. %s", e.getMessage());
            return false;
        }
    }

    private boolean tryRegistrationCommand(String username) {
        try {
            userService.register(username);

            System.out.println("Регистрация выполнена успешно.");
            System.out.println("Для входа выполните авторизация (команда login)");

            return true;
        } catch (Exception e) {
            System.out.printf("При попытке регистрации произошла ошибка. %s%n", e.getMessage());

            return false;
        }
    }

    private void printAuthorizationHelp() {
        System.out.println("Доступные команды:");
        System.out.println("register (reg) <username> - Зарегистрировать пользователя");
        System.out.println("login (l) <username> - Войти как пользователь");
    }

    private void printShortUrlServiceHelp() {
        System.out.println("Доступные команды:");
        System.out.println("list - Показать ссылки текущего пользователя");
        System.out.println("create <baseUrl> - Создать короткую ссылку (опционально -maxClicks <maxClicksValue>)");
        System.out.println("open <shortUrl> - Открыть короткую ссылку");
        System.out.println("exit - Выйти из приложения");
    }
}
