package ru.der2shka.model;

import lombok.Data;

@Data
public class Class {
    private String date;
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
