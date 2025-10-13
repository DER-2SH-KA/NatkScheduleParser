package ru.der2shka.exception;

public class SettingsPropertiesFileNotFoundException extends RuntimeException {
    public SettingsPropertiesFileNotFoundException(String message) {
        super(message);
    }
}
