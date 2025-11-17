package ru.der2shka.util.telegrambot;

import org.jetbrains.annotations.NotNull;
import ru.der2shka.model.Class;

import java.util.List;

public class TelegramMessagesGenerator {
    public static @NotNull String generateMessageWhenSegodnya(@NotNull List<Class> classes) {
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
                    sb.append(String.format("%nВремя: %s%n", classObj.getSubject().timePeriod()));
                    sb.append(String.format("%s пара%n", classObj.getSubject().sequenceNumber()));
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
