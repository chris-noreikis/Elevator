package models;

import java.lang.Exception;

public class InvalidValueException extends Exception {
	public InvalidValueException(String message) {
		super(message);
	}
}
