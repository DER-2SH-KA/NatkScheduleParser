package ru.der2shka;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.der2shka.exception.SettingsPropertiesIsEmptyException;
import ru.der2shka.model.Class;
import ru.der2shka.util.parser.Parser;
import ru.der2shka.util.properties.PropertiesReader;
import ru.der2shka.util.telegrambot.NatkTelegramBot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class Main {
    private static Properties properties = new Properties();
    private static final Parser parser = Parser.getInstance();

    private static final String natkPr22101ScheduleUrl = "https://natk.ru/stud-grad/schedule/187?gid=244";

    public static void main(String[] args) {
        loadSettingsProperties();

        /*System.out.println(properties.getProperty("hello.world"));
        System.out.println(System.getenv("TELEGRAM_TOKEN"));

        Optional<Document> document = parseDocument(natkPr22101ScheduleUrl);
        Optional<Elements> table = parser.getTable(document.get());
        Optional<Elements> rows = parser.getRows(table.get());

        List<Class> classes = parser.getClasses(rows.get());

        System.out.println(Arrays.deepToString(classes.toArray()));*/

        System.out.println(System.getenv("TELEGRAM_TOKEN"));

        registerBot();
    }

    private static void loadSettingsProperties() {
        try {
            properties = PropertiesReader.getSettingsProperties();
        }
        catch (URISyntaxException ex) {
            System.err.println("URI syntax of settings file exception");
            ex.printStackTrace();
        }
        catch (SettingsPropertiesIsEmptyException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        catch (FileNotFoundException ex) {
            System.err.println("File of settings not found.");
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.err.println("Failed to load content from settings file");
            ex.printStackTrace();
        }
        /*catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }

    private static Optional<Document> parseDocument(String url) {
        Optional<Document> document = Optional.empty();

        try {
            document = Optional.of(parser.getDocument(url));
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        catch (KeyManagementException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.err.println("IOException when connect to " + natkPr22101ScheduleUrl);
            ex.printStackTrace();
        }

        return document;
    }

    private static void registerBot() {
        final String token = System.getenv("TELEGRAM_TOKEN");

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(token, new NatkTelegramBot(token));
            System.out.println("MyAmazingBot successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}