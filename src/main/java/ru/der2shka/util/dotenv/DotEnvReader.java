package ru.der2shka.util.dotenv;

import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class DotEnvReader {
    @Nullable
    private static Dotenv dotenv = null;
    public static boolean isExist = false;

    static {
        System.out.println("DotEnvReader is called!");
    }

    public static Dotenv getEnv() {
        if (Objects.isNull(dotenv)) {
            dotenv = Dotenv.load();

            isExist = true;

            System.out.println(
                    Objects.nonNull(dotenv) ?
                            "DotEnv was loaded successfully!" :
                            "DotEnv wasn't loaded!"
            );
        }

        return dotenv;
    }

    public static Optional<String> getValue(@NotNull String key) {
        return Optional.ofNullable(dotenv.get(key));
    }
}
