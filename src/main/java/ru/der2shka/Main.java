package ru.der2shka;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.der2shka.util.telegrambot.NatkTelegramBot;

public class Main {

    public static void main(String[] args) {
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
}