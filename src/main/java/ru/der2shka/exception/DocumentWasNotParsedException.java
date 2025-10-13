package ru.der2shka.exception;

public class DocumentWasNotParsedException extends RuntimeException {
    public DocumentWasNotParsedException(String message) {
        super(message);
    }
}
