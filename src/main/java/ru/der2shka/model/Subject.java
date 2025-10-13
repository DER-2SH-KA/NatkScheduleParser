package ru.der2shka.model;

public record Subject(
        Integer sequenceNumber,
        String timePeriod,
        String name,
        String teacherFIO,
        String address
) {}
