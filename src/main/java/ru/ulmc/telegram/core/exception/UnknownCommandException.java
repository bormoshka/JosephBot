package ru.ulmc.telegram.core.exception;

public class UnknownCommandException extends Exception {

    public UnknownCommandException(String command) {
        super("Received unknown command: " + command);
    }
}
