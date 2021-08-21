package ru.javabegins.springboot.util;

import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class MyLogger {
    public static void logInfo(String msg) {
        log.log(Level.INFO, msg);
    }
}
