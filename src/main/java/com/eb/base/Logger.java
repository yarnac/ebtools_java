package com.eb.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekkart on 06.01.18.
 */

public class Logger {
    private static List<String> loggedMessages = new ArrayList<>();
    private static String lastMessage;

    public static void logErrors(String localizedMessage) {
        logError(localizedMessage);
    }

    public static void logError(String message)
    {
        log(message);
    }

    public static void logError(Throwable message)
    {
        log(message);
    }

    public static void log(String message) {
        lastMessage = message;
        loggedMessages.add(lastMessage);
    }

    public static void log(Throwable message) {
        if (message.getMessage()==null)
            log (message.toString());
        else
            log(message.getMessage());
    }


    public static List<String> getLoggedMessages() {
        return loggedMessages;
    }

    public static void setLoggedMessages(List<String> loggedMessages) {
        Logger.loggedMessages = loggedMessages;
    }

    public static String getLastMessage() {
        return lastMessage;
    }

    public static void setLastMessage(String lastMessage) {
        Logger.lastMessage = lastMessage;
    }


}
