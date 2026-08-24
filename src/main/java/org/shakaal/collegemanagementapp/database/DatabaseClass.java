package org.shakaal.collegemanagementapp.database;

import org.shakaal.collegemanagementapp.storage.AppDataManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ==========================================================
 * DatabaseConnection.java
 * ==========================================================

 * Creates and provides a connection to the SQLite database.

 * Every DAO class will use this class to communicate
 * with the database.

 * Author: Shakaal
 * Project: College Management System
 * ==========================================================
 */

public final class DatabaseClass {

    // =====================================================
    // Database Path
    // =====================================================

    private static final String url = "jdbc:sqlite:" + AppDataManager.getDatabaseFile().getAbsolutePath();

    // =====================================================
    // Private Constructor
    // =====================================================

    /**
     * Prevents object creation.

     * This is a utility class.
     */
    private DatabaseClass() {

    }

    // =====================================================
    // Database Connection
    // =====================================================

    /**
     * Creates and returns a connection to SQLite.
     *
     * @return Connection object
     * @throws SQLException if a connection cannot be created
     */
    public static Connection getConnection() throws SQLException {


        return DriverManager.getConnection(url);



    }

}
