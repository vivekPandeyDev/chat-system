// ...new file...
package com.loop.troop.chat.infrastructure.jpa.audit;

public final class CurrentUserHolder {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    private CurrentUserHolder() {}

    public static void setCurrentUser(String userId) {
        currentUser.set(userId);
    }

    public static String getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}

