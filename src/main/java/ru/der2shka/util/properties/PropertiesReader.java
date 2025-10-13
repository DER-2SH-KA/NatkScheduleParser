package ru.der2shka.util.properties;

import ru.der2shka.Main;
import ru.der2shka.exception.SettingsPropertiesFileNotFoundException;
import ru.der2shka.exception.SettingsPropertiesIsEmptyException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class PropertiesReader {
    private static final String settingsPropertiesFileName = "settings.properties";

    /**
     * Get {@link File} object by file name.
     * @param fileName file name (include path to file).
     * @return {@link File} object.
     * @throws NullPointerException if fileName is {@code null} or resource not found.
     * **/
    public static Optional<File> getPropertiesFile(String fileName) throws URISyntaxException {
        return Optional.of(
                new File(
                        Objects.requireNonNull(
                                Main.class.getResource(fileName)
                        ).toURI()
                )
        );
    }

    /**
     * Get {@link InputStream} object by {@value settingsPropertiesFileName} file.
     * @return {@link InputStream}.
     * **/
    public static Optional<InputStream> getSettingsPropertiesInputStream() {
        return Optional.ofNullable(
                Main.class.getResourceAsStream(settingsPropertiesFileName)
        );
    }

    /**
     * Get {@link Properties} collection from {@link File} content.
     * @param file {@link File} object.
     * @return {@link Properties} collection with content from file.
     * @throws FileNotFoundException file wasn't found.
     * @throws IOException failed to load content from file to {@link Properties}.
     * **/
    public static Properties getProperties(File file) throws IOException, FileNotFoundException {
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        return properties;
    }

    /**
     * Get {@link Properties} collection from {@value  settingsPropertiesFileName} {@link InputStream} content.
     * @return {@link Properties} collection with content from file.
     * @throws FileNotFoundException file wasn't found.
     * @throws IOException failed to load content from file to {@link Properties}.
     * **/
    public static Properties getSettingsProperties()
            throws IOException, FileNotFoundException, SettingsPropertiesIsEmptyException, URISyntaxException {
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

    public static void printSettingsResourceURI() {
        System.out.println(Main.class.getResource(settingsPropertiesFileName));
    }
}
