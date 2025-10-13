package ru.der2shka;

import org.jsoup.nodes.Document;
import ru.der2shka.exception.SettingsPropertiesIsEmptyException;
import ru.der2shka.util.parser.Parser;
import ru.der2shka.util.properties.PropertiesReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Properties;

public class Main {
    private static Properties properties = new Properties();
    private static final Parser parser = Parser.getInstance();

    private static final String natkGroupsUrl = "https://natk.ru/stud-grad/schedule";

    public static void main(String[] args) {
        loadSettingsProperties();

        System.out.println(properties.getProperty("hello.world"));
        System.out.println(System.getenv("TELEGRAM_TOKEN"));

        Optional<Document> document = parseDocument(natkGroupsUrl);

        document.ifPresent(System.out::println);
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

    private static Optional<Document> parseDocument(String url) {
        Optional<Document> document = Optional.empty();

        try {
            document = Optional.of(parser.getDocument(url));
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        catch (KeyManagementException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.err.println("IOException when connect to " + natkGroupsUrl);
            ex.printStackTrace();
        }

        return document;
    }
}