package org.shakaal.collegemanagementapp.database;

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

public final class DatabaseConnection {

    // =====================================================
    // Database Path
    // =====================================================

    private static final String DATABASE_URL =
            "jdbc:sqlite:databaseFiles/student_registration.db";

    // =====================================================
    // Private Constructor
    // =====================================================

    /**
     * Prevents object creation.

     * This is a utility class.
     */
    private DatabaseConnection() {

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

        java.io.File file = new java.io.File("jdbc:sqlite:databaseFiles/student_registration.db");

        System.out.println("Exists: " + file.exists());
        System.out.println("Absolute Path: " + file.getAbsolutePath());

        return DriverManager.getConnection(DATABASE_URL);

    }

}
