package exceptions;

import java.lang.Exception;

// This exception is an unrecoverable internal elevator error.
// It means that something bad happened that we don't want to try to recover from.
// I.E. if the elevator is moving floors while the doors are open.
// Throwing this exception should stop program execution.
public class InvalidStateException extends Exception {
    public InvalidStateException(String message) {
        super(message);
    }
}