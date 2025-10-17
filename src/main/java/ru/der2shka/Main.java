package ru.der2shka;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.der2shka.util.database.hibernate.HibernateUtil;
import ru.der2shka.util.dotenv.DotEnvReader;
import ru.der2shka.util.telegrambot.NatkTelegramBot;

public class Main {

    public static void main(String[] args) {
        String profile = args[0].trim().toLowerCase();

        if (profile.isBlank()) throw new IllegalArgumentException("ARG profile (args[0]) for start is empty!");

        try {
            DotEnvReader.getEnv();
        }
        catch (Exception ex) {
            System.err.println(".env file wasn't loaded");
            ex.printStackTrace();
            DotEnvReader.isExist = false;
        }

        getInstanceOfSessionFactory(profile);

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

    private static void getInstanceOfSessionFactory(String profile) {
        HibernateUtil.getSessionFactory(profile);
    }
}