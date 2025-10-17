package ru.der2shka.util.dotenv;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;
import java.util.Optional;

public class DotEnvReader {
    private static Dotenv dotenv = Dotenv.load();

    static {
        System.out.println("DotEnvReader is called!");
    }

    public static Dotenv getEnv() {
        if (Objects.isNull(dotenv)) {
            dotenv = Dotenv.load();

            System.out.println(
                    Objects.nonNull(dotenv) ?
                            "DotEnv was loaded successfully!" :
                            "DotEnv wasn't loaded!"
            );
        }

        return dotenv;
    }

    public static Optional<String> getValue(String key) {
        return Optional.ofNullable(dotenv.get(key));
    }
}
