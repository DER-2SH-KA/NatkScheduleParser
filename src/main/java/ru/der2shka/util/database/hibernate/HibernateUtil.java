package ru.der2shka.util.database.hibernate;

import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import ru.der2shka.entity.ClassEntity;
import ru.der2shka.exception.DocumentWasNotParsedException;
import ru.der2shka.exception.DotEnvKeyValueIsEmptyOrNotExistException;
import ru.der2shka.exception.PropertiesIsEmptyException;
import ru.der2shka.util.dotenv.DotEnvReader;
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
            finally {
                System.out.println("Create instance of session factory");
                System.out.println("Is null?: " + Objects.isNull(sessionFactory));
            }

        }

        if (databaseProperties.isEmpty()) throw new PropertiesIsEmptyException("Database settings file is empty");

        return sessionFactory;
    }

    private static Optional<SessionFactory> createSessionFactory() {
        try {
            Configuration config = new Configuration();
            Properties databaseSettings = new Properties();

            System.out.println(databaseProperties.getProperty("database.url"));

            databaseSettings.put(Environment.DRIVER, databaseProperties.getProperty("database.driver"));
            databaseSettings.put(
                    Environment.URL, DotEnvReader.getValue("DB_URL")
                            .orElseThrow(() ->
                                    new DotEnvKeyValueIsEmptyOrNotExistException("Value by key is empty!", "DB_URL")
                            )
            );
            databaseSettings.put(
                    Environment.USER, DotEnvReader.getValue("DB_USERNAME")
                            .orElseThrow(() ->
                                    new DotEnvKeyValueIsEmptyOrNotExistException("Value by key is empty!", "DB_USERNAME")
                            )
            );
            databaseSettings.put(
                    Environment.PASS, DotEnvReader.getValue("DB_PASSWORD")
                            .orElseThrow(() ->
                                    new DotEnvKeyValueIsEmptyOrNotExistException("Value by key is empty!", "DB_PASSWORD")
                            )
            );
            databaseSettings.put(Environment.DIALECT, databaseProperties.getProperty("database.dialect"));

            databaseSettings.put(Environment.SHOW_SQL, databaseProperties.getProperty("database.show_sql"));
            databaseSettings.put(Environment.FORMAT_SQL, databaseProperties.getProperty("database.format_sql"));

            databaseSettings.put(Environment.HBM2DDL_AUTO, databaseProperties.getProperty("database.hbm2ddl"));

            config.setProperties(databaseSettings);

            // Here all entity classes.
            config.addAnnotatedClass(ClassEntity.class);

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
