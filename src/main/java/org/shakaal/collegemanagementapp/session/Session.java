package org.shakaal.collegemanagementapp.session;

import org.shakaal.collegemanagementapp.models.User;

public class Session {

    private static User currentUser;

    private Session() {

        // Prevent creating Session objects

    }

    public static User getCurrentUser() {

        return currentUser;

    }

    public static void setCurrentUser(User user) {

        currentUser = user;

    }

    public static void clear() {

        currentUser = null;

    }

}
