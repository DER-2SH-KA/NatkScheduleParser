package ru.der2shka.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Class {
    @NotNull
    private String date;

    @NotNull
    private Subject subject;

    @Override
    public String toString() {
        return String.format(
                "Class: %s%nDate: %s%nSubject: %s%n",
                this.getClass().getName(),
                this.getDate(),
                this.getSubject().toString()
        );
    }
}
