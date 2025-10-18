package ru.der2shka;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import ru.der2shka.entity.ClassEntity;
import ru.der2shka.repository.Repository;
import ru.der2shka.repository.StudyClassRepository;
import ru.der2shka.service.StudyClassService;
import ru.der2shka.util.database.hibernate.HibernateUtil;
import ru.der2shka.util.dotenv.DotEnvReader;
import ru.der2shka.util.telegrambot.NatkTelegramBot;

public class Main {
    private static StudyClassService studyClassService;
    private static Repository<ClassEntity, Long> studyClassRepository;

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

        studyClassRepository = new StudyClassRepository();
        studyClassService = new StudyClassService(studyClassRepository);

        registerBot();
    }

    private static void registerBot() {
        final String token = System.getenv("TELEGRAM_TOKEN");

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(token, new NatkTelegramBot(token, studyClassService));

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