package ru.der2shka;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.der2shka.util.database.hibernate.HibernateUtil;
import ru.der2shka.util.properties.PropertiesReader;
import ru.der2shka.util.telegrambot.NatkTelegramBot;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties db = PropertiesReader.loadProperties(PropertiesReader.databaseSettingsFileName);

        System.out.println("\n\nDB url: " + db.getProperty("database.url"));
        System.out.println("DB username: " + db.getProperty("database.username"));
        System.out.println("DB password: " + db.getProperty("database.password") + "\n\n");

        getInstanceOfSessionFactory();
        registerBot();
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

    private static void getInstanceOfSessionFactory() {
        HibernateUtil.getSessionFactory();
    }
}