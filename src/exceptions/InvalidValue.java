package exceptions;

import java.lang.Exception;

public class InvalidValue extends Exception {
	public InvalidValue(String message) {
		super(message);
	}
}
