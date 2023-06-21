package top.liaoxin.StarDB.backend.utils;

import static java.lang.System.out;

public class Panic {
    public static void panic(Exception e) {
        e.printStackTrace();
        System.exit(1);
    }
}
