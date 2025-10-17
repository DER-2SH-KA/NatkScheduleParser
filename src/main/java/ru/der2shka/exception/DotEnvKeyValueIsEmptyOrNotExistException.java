package ru.der2shka.exception;

public class DotEnvKeyValueIsEmptyOrNotExistException extends RuntimeException {
    public DotEnvKeyValueIsEmptyOrNotExistException(String message, String key) {
        super(String.format("%s%sKey: %s%s", message, key));
    }
}
