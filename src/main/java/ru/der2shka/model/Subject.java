package ru.der2shka.model;

import org.jetbrains.annotations.NotNull;

public record Subject(
        @NotNull Integer sequenceNumber,
        @NotNull String timePeriod,
        @NotNull String name,
        @NotNull String teacherFIO,
        @NotNull String address
) {}
