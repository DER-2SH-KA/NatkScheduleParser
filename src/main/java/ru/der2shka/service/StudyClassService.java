package ru.der2shka.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.der2shka.entity.ClassEntity;
import ru.der2shka.model.Class;
import ru.der2shka.model.Subject;
import ru.der2shka.repository.Repository;
import ru.der2shka.repository.StudyClassRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class StudyClassService {
    private final Repository<ClassEntity, Long> repository;

    /**
     * Check {@link Class} is exist in database.
     * @param studyClass {@link Class} object to check.
     * @return {@code boolean} is entity with data from {@link Class} exist.
     * @throws RuntimeException when checking was failed.
     * **/
    public boolean existsClassInDB(@NotNull Class studyClass) {
        try {
            Objects.requireNonNull(studyClass, "studyClass arg must be not null!");

            ClassEntity entityFromClass = new ClassEntity();

            entityFromClass.setDate(studyClass.getDate());
            entityFromClass.setSeqNum(studyClass.getSubject().sequenceNumber());
            entityFromClass.setTimePeriod(studyClass.getSubject().timePeriod());
            entityFromClass.setName(studyClass.getSubject().name());
            entityFromClass.setTeacherFio(studyClass.getSubject().teacherFIO());
            entityFromClass.setAddress(studyClass.getSubject().address());

            List<ClassEntity> entities = repository.findAll();

            List<ClassEntity> filteredEntities = entities.stream()
                    .filter(ce -> ce.equalsByFieldsExcludeId(entityFromClass))
                    .toList();

            return !filteredEntities.isEmpty();
        }
        catch (NullPointerException ex) {
            System.err.println("NullPointerException in existsClassInDB(): " + ex.getMessage());
            ex.printStackTrace();

            throw new RuntimeException("Error to check existing class in DB using service", ex);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Save {@link Class} in database.
     * @param studyClass {@link Class} object to save.
     * @return saved in database {@link Class} object.
     * @throws RuntimeException when save was failed.
     * **/
    public Optional<Class> save(@NotNull Class studyClass) {
        try {
            Objects.requireNonNull(studyClass, "studyClass arg must be not null!");

            ClassEntity entityFromClass = new ClassEntity();

            entityFromClass.setDate(studyClass.getDate());
            entityFromClass.setSeqNum(studyClass.getSubject().sequenceNumber());
            entityFromClass.setTimePeriod(studyClass.getSubject().timePeriod());
            entityFromClass.setName(studyClass.getSubject().name());
            entityFromClass.setTeacherFio(studyClass.getSubject().teacherFIO());
            entityFromClass.setAddress(studyClass.getSubject().address());

            ClassEntity savedEntity = repository.save(entityFromClass)
                    .orElseThrow(() -> new EntityNotFoundException("Class entity was not saved!"));

            Class savedClass = new Class();
            Subject savedClassSubject = new Subject(
                    savedEntity.getSeqNum(),
                    savedEntity.getTimePeriod(),
                    savedEntity.getName(),
                    savedEntity.getTeacherFio(),
                    savedEntity.getAddress()
            );

            savedClass.setDate(savedEntity.getDate());
            savedClass.setSubject(savedClassSubject);

            return Optional.of(savedClass);
        }
        catch (NullPointerException ex) {
            System.err.println("NullPointerException in save(): " + ex.getMessage());
            ex.printStackTrace();

            throw new RuntimeException("Error to save in DB using service", ex);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }
}
