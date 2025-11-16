package ru.der2shka.util.telegrambot;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.der2shka.exception.EntityWasNotSavedException;
import ru.der2shka.model.Class;
import ru.der2shka.service.ParserService;
import ru.der2shka.service.StudyClassService;
import ru.der2shka.util.properties.PropertiesReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class NatkTelegramBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    // private final StudyClassService studyClassService;

    private static final ParserService parser = new ParserService();

    @NotNull
    private Properties properties = new Properties();

    // public NatkTelegramBot(@NotNull String token, @NotNull StudyClassService studyClassService) {
    public NatkTelegramBot(@NotNull String token) {

        telegramClient = new OkHttpTelegramClient(token);
        //this.studyClassService = studyClassService;

        this.loadProperties();
        System.out.println("From NatkTelegramBot natk.url: " + properties.getProperty("natk.url"));
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            long chatId = update.getMessage().getChatId();
            String messageText =
                    "Не понимаю тебя:(\nНапиши \"сегодня\", чтобы посмотреть расписание на сегодня ПР-22.101!";

            String updateText = update.getMessage().getText();

            if (updateText.toLowerCase().equals("сегодня")) {
                List<Class> classes = parser.getClasses(
                        properties.getProperty("natk.url")
                );

                System.out.println(Arrays.deepToString(classes.toArray()));

                messageText = setMessageWhenSegodnya(classes);

                // DataBase work.
                /*classes.forEach(c -> {
                    if (!studyClassService.existsClassInDB(c)) {
                        studyClassService.save(c)
                                .orElseThrow(() ->
                                        new EntityWasNotSavedException("Study class " + c + " was not saved!")
                                );
                    }
                });*/
            }

            SendMessage sendMessage = createSendMessage(chatId, messageText);

            try {
                sendMessage.setReplyMarkup(getCustomKeyboard("Сегодня", "Завтра (не работает)"));

                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private ReplyKeyboardMarkup getCustomKeyboard(@NotNull String... rowsText) {

        final List<KeyboardRow> keyboard = new ArrayList<>();

        for (String text : rowsText) {
            KeyboardRow row = new KeyboardRow();
            row.add(text);

            keyboard.add(row);
        }

        return new ReplyKeyboardMarkup(keyboard);
    }

    private void loadProperties() {
        properties = PropertiesReader.loadSettingsProperties();
    }

    /**
     * Create new {@link SendMessage} message for sending in Telegram.
     * @param chatId chat's ID.
     * @return {@link SendMessage}
     * **/
    private @NotNull SendMessage createSendMessage(long chatId, @NotNull String message) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
    }

    private @NotNull String setMessageWhenSegodnya(@NotNull List<Class> classes) {
        String messageText = "";

        if (!classes.isEmpty()) {
            String groupName = "ПР-22.101";
            String currentDate = classes.getFirst().getDate();

            StringBuilder sb = new StringBuilder();

            sb.append(String.format("Группа: %s%n", groupName));
            sb.append(String.format("Дата: %s%n", currentDate));
            sb.append("\nРасписание:");

            for (Class classObj : classes) {
                if (classObj.getSubject().sequenceNumber() == -1) {
                    sb.append("\n" + classObj.getSubject().name());
                }
                else {
                    sb.append(String.format("%n%s пара%n", classObj.getSubject().sequenceNumber()));
                    sb.append(String.format("Предмет: %s%n", classObj.getSubject().name()));
                    sb.append(String.format("Преподаватель: %s%n", classObj.getSubject().teacherFIO()));
                    sb.append(String.format("Место: %s%n", classObj.getSubject().address()));
                }
            }
            messageText = sb.toString();
        }

        return messageText;
    }
}
