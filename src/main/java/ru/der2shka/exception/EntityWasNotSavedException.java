package ru.der2shka.exception;

public class EntityWasNotSavedException extends RuntimeException {
    public EntityWasNotSavedException(String message) { super(message); }
}
