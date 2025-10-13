package ru.der2shka.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class OneLineClass extends Class {
    private String subject;

    @Override
    public String toString() {
        return String.format(
                "Class: %s%nDate: %s%nSubject: %s%n",
                this.getClass().getName(),
                this.getDate(),
                this.getSubject()
        );
    }
}
