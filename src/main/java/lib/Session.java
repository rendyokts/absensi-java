package lib;

public class Session {
    private static String currentUser = "Admin HR";

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String user) {
        currentUser = user;
    }
}

