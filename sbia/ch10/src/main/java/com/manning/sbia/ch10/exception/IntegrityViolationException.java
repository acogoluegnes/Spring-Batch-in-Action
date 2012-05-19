/**
 * 
 */
package com.manning.sbia.ch10.exception;

/**
 * Exception thrown when integrity checks for extracted files from an archive have failed.
 * @author acogoluegnes
 *
 */
public class IntegrityViolationException extends RuntimeException {

	public IntegrityViolationException() {
		super();
	}

	public IntegrityViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IntegrityViolationException(String message) {
		super(message);
	}

	public IntegrityViolationException(Throwable cause) {
		super(cause);
	}
	
}
