package ru.der2shka.util.properties;

import ru.der2shka.Main;
import ru.der2shka.exception.PropertiesFileNotFoundException;
import ru.der2shka.exception.SettingsPropertiesFileNotFoundException;
import ru.der2shka.exception.SettingsPropertiesIsEmptyException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Properties;

public class PropertiesReader {
    public static final String settingsPropertiesFileName = "settings.properties";
    public static final String databaseSettingsFileName = "database.properties";

    /**
     * Get {@link InputStream} object by file name.
     * @param fileName file name (include path to file).
     * @return {@link InputStream} object from file.
     * @throws NullPointerException if fileName is {@code null} or resource not found.
     * **/
    private static Optional<InputStream> getPropertiesInputStream(String fileName) {
        return Optional.ofNullable(
                Main.class
                        .getResourceAsStream(fileName)
        );
    }

    /**
     * Get {@link InputStream} object by {@value settingsPropertiesFileName} file.
     * @return {@link InputStream}.
     * **/
    private static Optional<InputStream> getSettingsPropertiesInputStream() {
        return Optional.ofNullable(
                Main.class.getResourceAsStream(settingsPropertiesFileName)
        );
    }

    /**
     * Get {@link Properties} collection from file by file name.
     * @param fileName properties file name.
     * @return {@link Properties} collection with content from file.
     * @throws FileNotFoundException file wasn't found.
     * @throws IOException failed to load content from file to {@link Properties}.
     * **/
    private static Properties getProperties(String fileName)
            throws IOException, FileNotFoundException, PropertiesFileNotFoundException, URISyntaxException {
        Properties properties = new Properties();

        try (InputStream inputStream =
                getPropertiesInputStream(fileName)
                        .orElseThrow(() ->
                                new PropertiesFileNotFoundException(fileName + " file was not found!")
                        )
        ) {
            properties.load(inputStream);
        }

        return properties;
    }

    /**
     * Get {@link Properties} collection from {@value  settingsPropertiesFileName} {@link InputStream} content.
     * @return {@link Properties} collection with content from file.
     * @throws FileNotFoundException file wasn't found.
     * @throws IOException failed to load content from file to {@link Properties}.
     * **/
    private static Properties getSettingsProperties()
            throws IOException, FileNotFoundException, SettingsPropertiesFileNotFoundException, URISyntaxException {
        Properties properties = new Properties();

        try (InputStream inputStream =
                     getSettingsPropertiesInputStream()
                             .orElseThrow(() -> new SettingsPropertiesFileNotFoundException(
                                     settingsPropertiesFileName + " file not found exception"
                             ))
        ) {
            properties.load(inputStream);
        }

        if (properties.isEmpty())
            throw new SettingsPropertiesIsEmptyException("Setting properties collection is empty after load!");

        return properties;
    }

    /**
     * Load settings properties.
     * @return {@link Properties} collection by {@value settingsPropertiesFileName}.
     * **/
    public static Properties loadSettingsProperties() {
        Properties properties = new Properties();

        try {
            properties = getSettingsProperties();
        }
        catch (URISyntaxException ex) {
            System.err.println("URI syntax of settings file exception");
            ex.printStackTrace();
        }
        catch (SettingsPropertiesFileNotFoundException ex) {
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
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return properties;
    }

    /**
     * Load properties from file.
     * @param fileName file name.
     * @return {@link Properties} collection by file.
     * **/
    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();

        try {
            properties = getProperties(fileName);
        }
        catch (URISyntaxException ex) {
            System.err.println("URI syntax of " + fileName + " file exception");
            ex.printStackTrace();
        }
        catch (PropertiesFileNotFoundException ex) {
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
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return properties;
    }
}
