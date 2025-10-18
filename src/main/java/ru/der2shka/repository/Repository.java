package ru.der2shka.repository;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface Repository<Entity, Id> {
    Optional<Entity> save(@NotNull Entity entity);

    List<Entity> findAll();

    Optional<Entity> findById(@NotNull Id id);

    void delete(@NotNull Entity entity);

    void deleteById(@NotNull Id id);
}
