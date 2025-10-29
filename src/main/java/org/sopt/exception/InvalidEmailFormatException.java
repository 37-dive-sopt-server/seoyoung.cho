package org.sopt.exception;

public class InvalidEmailFormatException extends IllegalArgumentException {
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
