package ru.der2shka.util.database.hibernate;

import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;
import ru.der2shka.entity.ClassEntity;
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

    public static SessionFactory getSessionFactory(@NotNull String profile) {

        if (databaseProperties.isEmpty()) throw new PropertiesIsEmptyException("Database settings file is empty");

        if (Objects.isNull(sessionFactory)) {
            try {
                sessionFactory = createSessionFactory(profile)
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
                System.out.println("Is sessionFactory null?: " + Objects.isNull(sessionFactory));
            }

        }

        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        Objects.requireNonNull(sessionFactory, "Session factory wasn't initialized with profile before!");

        return sessionFactory;
    }

    private static Optional<SessionFactory> createSessionFactory(@NotNull String profile) {
        try {
            Configuration config = new Configuration();
            Properties databaseSettings = new Properties();

            System.out.println(databaseProperties.getProperty("database.url"));

            databaseSettings.put(Environment.DRIVER, databaseProperties.getProperty("database.driver"));

            if (profile.equals("prod") && DotEnvReader.isExist) {
                System.out.println("Set PROD - TRUE profile settings");

                System.out.println("URL: " + DotEnvReader.getValue("DB_URL"));
                System.out.println("USERNAME: " + DotEnvReader.getValue("DB_USERNAME"));

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
            }
            else if (profile.equals("prod") && !DotEnvReader.isExist) {
                System.out.println("Set PROD - FALSE profile settings");

                System.out.println("URL: " + System.getenv("DB_URL"));
                System.out.println("USERNAME: " + System.getenv("DB_USERNAME"));

                databaseSettings.put(Environment.URL, System.getenv("DB_URL"));
                databaseSettings.put(Environment.USER, System.getenv("DB_USERNAME"));
                databaseSettings.put(Environment.PASS, System.getenv("DB_PASSWORD"));
            }
            else if (profile.equals("dev")) {
                System.out.println("Set DEV profile settings");

                System.out.println("URL: " + databaseProperties.getProperty("database.url"));
                System.out.println("USERNAME: " + databaseProperties.getProperty("database.username"));

                databaseSettings.put(Environment.URL, databaseProperties.getProperty("database.url"));
                databaseSettings.put(Environment.USER, databaseProperties.getProperty("database.username"));
                databaseSettings.put(Environment.PASS, databaseProperties.getProperty("database.password"));
            }
            else {
                throw new IllegalArgumentException("Incorrect ARG profile for start value!: " + profile);
            }

            databaseSettings.put(Environment.DIALECT, databaseProperties.getProperty("database.dialect"));

            databaseSettings.put(Environment.SHOW_SQL, databaseProperties.getProperty("database.show_sql"));
            databaseSettings.put(Environment.FORMAT_SQL, databaseProperties.getProperty("database.format_sql"));

            databaseSettings.put(Environment.HBM2DDL_AUTO, databaseProperties.getProperty("database.hbm2ddl"));

            config.setProperties(databaseSettings);

            addAnnotatedClasses(config);

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

    /**
     * Add all entity classes to Hibernate configuration.
     * @param config Hibernate {@link Configuration}.
     * **/
    private static void addAnnotatedClasses(Configuration config) {
        config.addAnnotatedClass(ClassEntity.class);
    }
}
