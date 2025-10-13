package ru.der2shka.model;

import lombok.Data;

@Data
public sealed abstract class Class permits OneLineClass, MultiLineClass {
    private String date;
}
