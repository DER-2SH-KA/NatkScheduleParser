package ru.der2shka;

import ru.der2shka.exception.SettingsPropertiesIsEmptyException;
import ru.der2shka.util.properties.PropertiesReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Main {
    private static Properties properties = new Properties();

    public static void main(String[] args) {
        loadSettingsProperties();

        System.out.println(properties.getProperty("hello.world"));
        System.out.println(System.getenv("TELEGRAM_TOKEN"));
    }

    private static void loadSettingsProperties() {
        try {
            properties = PropertiesReader.getSettingsProperties();
        }
        catch (URISyntaxException ex) {
            System.err.println("URI syntax of settings file exception");
            ex.printStackTrace();
        }
        catch (SettingsPropertiesIsEmptyException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        catch (FileNotFoundException ex) {
            System.err.println("File of settings not found.");
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.err.println("Failed to load content from settings file");
            ex.printStackTrace();
        }
    }
}