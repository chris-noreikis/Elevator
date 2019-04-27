package exceptions;

import java.lang.Exception;

// This exception is a recoverable exception.
// It's triggered by invalid input to our elevator simulator, like a floor request to a non-existent floor.
// In this case, we don't want to halt the system, so program flow should continue.
public class InvalidValueException extends Exception {
	public InvalidValueException(String message) {
		super(message);
	}
}
