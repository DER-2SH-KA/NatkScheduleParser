package ru.der2shka.repository;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import ru.der2shka.entity.ClassEntity;
import ru.der2shka.util.database.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StudyClassRepository implements Repository<ClassEntity, Long> {

    /**
     * Save entity in database.
     * @param entity entity to save.
     * @return {@link ClassEntity} as {@link Optional} object.
     * @throws NullPointerException if entity param is null.
     * @throws RuntimeException if transaction is failed (with rollback).
     * **/
    @Override
    public Optional<ClassEntity> save(@NotNull ClassEntity entity) {

        Objects.requireNonNull(entity, "Entity must be not null!");

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            Transaction transaction =  session.beginTransaction();

            ClassEntity savedEntity = session.merge(entity);

            transaction.commit();

            return Optional.ofNullable(savedEntity);
        }
        catch (Exception ex) {
            ex.printStackTrace();

            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }

            throw new RuntimeException("Error saving entity", ex);
        }
        finally {
            session.close();
        }
    }

    /**
     * Find entities in database.
     * **/
    @Override
    public List<ClassEntity> findAll() {

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            final String request = "SELECT ce FROM ClassEntity ce";

            TypedQuery<ClassEntity> query = session
                    .createQuery(request, ClassEntity.class);

            return query.getResultList();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ArrayList<ClassEntity>();
    }

    /**
     * Find entity in database by ID.
     * @param id entity's ID.
     * @throws NullPointerException if id param is null.
     * **/
    @Override
    public Optional<ClassEntity> findById(@NotNull Long id) {

        Objects.requireNonNull(id, "Entities' ID must be not null!");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            ClassEntity foundedEntity = session.find(ClassEntity.class, id);

            return Optional.ofNullable(foundedEntity);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Delete entity from database.
     * @param entity entity to save.
     * @throws NullPointerException if entity param is null.
     * @throws RuntimeException if transaction is failed (with rollback).
     * **/
    @Override
    public void delete(@NotNull ClassEntity entity) {

        Objects.requireNonNull(entity, "Entity must be not null!");

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {

            Transaction transaction = session.beginTransaction();

            session.load(ClassEntity.class, entity.getId());

            session.remove(entity);

            transaction.commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();

            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }

            throw new RuntimeException("Error deleting entity", ex);
        }
        finally {
            session.close();
        }
    }

    @Override
    public void deleteById(@NotNull Long id) {
        Objects.requireNonNull(id, "Entity's ID must be not null!");

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {

            Transaction transaction = session.beginTransaction();

            final String request = "DELETE FROM ClassEntity ce WHERE ce.id = :id";
            TypedQuery<ClassEntity> query = session.createQuery(request);
            query.setParameter("id", id);

            int rowDeleted = query.executeUpdate();

            transaction.commit();

            if (rowDeleted == 0) {
                System.out.println("Zero rows was deleted!");
            }
            else {
                System.out.println(rowDeleted + " row was deleted!");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();

            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }

            throw new RuntimeException("Error deleting entity by ID", ex);
        }
        finally {
            session.close();
        }
    }
}
