package ru.der2shka;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.der2shka.util.database.hibernate.HibernateUtil;
import ru.der2shka.util.dotenv.DotEnvReader;
import ru.der2shka.util.properties.PropertiesReader;
import ru.der2shka.util.telegrambot.NatkTelegramBot;

import java.util.Optional;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        DotEnvReader.getEnv();

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