package com.lotr.demo.userlist.utils;

public class LogUtils {

    private static final String LOG_PREFIX = "!_";

    public static String makeLogTag(Class className) {
        return LOG_PREFIX + className.getSimpleName();
    }
}
