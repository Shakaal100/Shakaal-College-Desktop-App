package org.shakaal.collegemanagementapp.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class AppDataManager {

    private static final File APP_DATA_FOLDER =
            new File(System.getProperty("user.dir"));

    private static final File BROWSE_LOCATIONS_FILE = new File(APP_DATA_FOLDER, "browse-locations.properties");


    private AppDataManager() {
        // Prevent creating objects of this utility class
    }


    // ================= DATABASE =================

    public static File getDatabaseFolder() {

        File folder = new File(APP_DATA_FOLDER, "databaseFiles");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }


    public static File getDatabaseFile() {

        return new File(getDatabaseFolder(), "CollegeManagement.db");
    }


    // ================= COURSE PDFs =================

    public static File getCoursePdfFolder() {

        File folder = new File(APP_DATA_FOLDER, "CoursePDFs");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }


    // ================= TEACHER PICTURES =================

    public static File getTeacherPicturesFolder() {

        File folder = new File(APP_DATA_FOLDER, "TeacherPictures");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }


    // ================= STUDENT PICTURES =================

    public static File getStudentPicturesFolder() {

        File folder = new File(APP_DATA_FOLDER, "StudentPictures");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }


    // =========================================================
    // LAST BROWSE LOCATIONS
    // =========================================================

    private static Properties loadBrowseLocations() {

        Properties properties = new Properties();

        if (!BROWSE_LOCATIONS_FILE.exists()) {
            return properties;
        }

        try {

            properties.load(Files.newInputStream(BROWSE_LOCATIONS_FILE.toPath()));

        } catch (IOException e) {

            e.printStackTrace();
        }

        return properties;
    }


    private static void saveBrowseLocation(
            String key,
            File folder
    ) {

        if (folder == null || !folder.exists() || !folder.isDirectory()) {
            return;
        }

        Properties properties = loadBrowseLocations();

        properties.setProperty(key, folder.getAbsolutePath());

        try {

            properties.store(Files.newOutputStream(BROWSE_LOCATIONS_FILE.toPath()), "College Management App - Last Browse Locations");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    private static File getLastBrowseLocation(String key) {

        Properties properties = loadBrowseLocations();

        String path = properties.getProperty(key);

        if (path == null || path.isBlank()) {
            return null;
        }

        File folder = new File(path);

        if (folder.exists() && folder.isDirectory()) {
            return folder;
        }

        return null;
    }


    // =========================================================
    // COURSE BROWSE LOCATION
    // =========================================================

    public static File getLastCourseBrowseFolder() {

        return getLastBrowseLocation("course");
    }


    public static void setLastCourseBrowseFolder(File folder) {

        saveBrowseLocation("course", folder);
    }


    // =========================================================
    // TEACHER PICTURE BROWSE LOCATION
    // =========================================================

    public static File getLastTeacherPictureBrowseFolder() {

        return getLastBrowseLocation("teacher-picture");
    }


    public static void setLastTeacherPictureBrowseFolder(File folder) {

        saveBrowseLocation("teacher-picture", folder);
    }


    // =========================================================
    // STUDENT PICTURE BROWSE LOCATION
    // =========================================================

    public static File getLastStudentPictureBrowseFolder() {

        return getLastBrowseLocation("student-picture");
    }


    public static void setLastStudentPictureBrowseFolder(File folder) {

        saveBrowseLocation("student-picture", folder);
    }
}