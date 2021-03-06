/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data;

/**
 * @author nunocastro
 */
public interface Persistable {

    /**
     * Save an objet to the database.
     * @param databaseConnection the database's connection
     * @param object the object to be added
     * @return Operation success.
     */
    boolean save(DatabaseConnection databaseConnection, Object object);

    /**
     * Delete an object from the database.
     * @param databaseConnection the database's connection
     * @param object the object to be deleted
     * @return Operation success.
     */
    boolean delete(DatabaseConnection databaseConnection, Object object);
}
