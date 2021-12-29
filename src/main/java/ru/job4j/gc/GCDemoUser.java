package ru.job4j.gc;

import org.openjdk.jol.info.GraphLayout;

import java.util.ArrayList;
import java.util.List;

public class GCDemoUser {
    private static final long KB = 1024;
    private static final long MB = KB * KB;
    private static final Runtime ENVIRONMENT = Runtime.getRuntime();

    public static void info() {
        final long freeMemory = ENVIRONMENT.freeMemory();
        final long totalMemory = ENVIRONMENT.totalMemory();
        final long maxMemory = ENVIRONMENT.maxMemory();
        System.out.println("=== Environment state ===");
        System.out.printf("Free: %d%n", freeMemory / MB);
        System.out.printf("Total: %d%n", totalMemory / MB);
        System.out.printf("Max: %d%n", maxMemory / MB);
    }
    public static void main(String[] args) {
        info();
        User user1 = new User("ivan", "1v@n", 33, true);
        User user2 = new User("", "", 0, false);
        User user3 = new User();
        Job job = new Job();
        info();
        System.out.println(GraphLayout.parseInstance((Object) user1).toPrintable());
        System.out.println(GraphLayout.parseInstance((Object) user1).toFootprint());
        System.out.println(GraphLayout.parseInstance((Object) user2).toPrintable());
        System.out.println(GraphLayout.parseInstance((Object) user2).toFootprint());
        System.out.println(GraphLayout.parseInstance((Object) user3).toPrintable());
        System.out.println(GraphLayout.parseInstance((Object) user3).toFootprint());
        System.out.println(GraphLayout.parseInstance((Object) job).toPrintable());
        System.out.println(GraphLayout.parseInstance((Object) job).toFootprint());

        for (int i = 0; i < 1000000; i++) {
            createUser();
        }
        info();

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            users.add(new User("Oryngali", "Karimzhan", 25, true));
        }
        info();
        System.gc();
    }

    public static void createUser() {
        new User();
    }
}
