package ru.der2shka.exception;

public class TableWasNotParsedException extends RuntimeException {
    public TableWasNotParsedException(String message) { super(message); }
}
