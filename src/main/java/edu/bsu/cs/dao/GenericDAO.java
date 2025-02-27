package edu.bsu.cs.dao;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Generic DAO (Data Access Object) interface.
 * Defines common operations for database access.
 *
 * @param <T> The entity type
 * @param <ID> The type of the entity's ID
 */
public interface GenericDAO<T, ID extends Serializable> {

    /**
     * Saves an entity to the database.
     *
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);

    /**
     * Updates an existing entity.
     *
     * @param entity The entity to update
     * @return The updated entity
     */
    T update(T entity);

    /**
     * Finds an entity by its ID.
     *
     * @param id The entity ID
     * @return An Optional containing the entity if found, or empty if not found
     */
    Optional<T> findById(ID id);

    /**
     * Finds all entities.
     *
     * @return A list of all entities
     */
    List<T> findAll();

    /**
     * Deletes an entity.
     *
     * @param entity The entity to delete
     */
    void delete(T entity);

    /**
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to delete
     * @return true if the entity was deleted, false if it wasn't found
     */
    boolean deleteById(ID id);
}