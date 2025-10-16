package ru.der2shka.util.database.hibernate;

import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import ru.der2shka.exception.PropertiesFileNotFoundException;
import ru.der2shka.util.properties.PropertiesReader;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static Properties databaseProperties = PropertiesReader
            .loadProperties(PropertiesReader.databaseSettingsFileName);

    public static SessionFactory getSessionFactory() {
        if (Objects.isNull(sessionFactory)) {
            try {
                sessionFactory = createSessionFactory()
                        .orElseThrow(() ->
                                new SessionException("Failed to create SessionFactory object")
                        );
            }
            catch (Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }

        }

        return sessionFactory;
    }

    private static Optional<SessionFactory> createSessionFactory() {
        try {
            Configuration config = new Configuration();
            Properties databaseSettings = new Properties();

            databaseSettings.put(Environment.DRIVER, databaseProperties.getProperty("database.driver"));
            databaseSettings.put(Environment.URL, databaseProperties.getProperty("database.url"));
            databaseSettings.put(Environment.USER, databaseProperties.getProperty("database.username"));
            databaseSettings.put(Environment.PASS, databaseProperties.getProperty("database.password"));

            databaseSettings.put(Environment.DIALECT, databaseProperties.getProperty("database.dialect"));

            databaseSettings.put(Environment.SHOW_SQL, databaseProperties.getProperty("database.show_sql"));
            databaseSettings.put(Environment.FORMAT_SQL, databaseProperties.getProperty("database.format_sql"));

            config.setProperties(databaseSettings);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties())
                    .build();

            return Optional.ofNullable(
                    config.buildSessionFactory(serviceRegistry)
            );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }
}
