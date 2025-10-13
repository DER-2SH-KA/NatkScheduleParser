package ru.der2shka.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class MultiLineClass extends Class {
    private final List<Subject> subjects = new ArrayList<>();

    @Override
    public String toString() {
        return String.format(
                "Class: %s%nDate: %s%nSubject: %s%nCount of subjects: %d%n",
                this.getClass().getName(),
                this.getDate(),
                this.subjects.size()
        );
    }
}
