package com.web4enterprise.pdf.layout.exception;

public class BadOperationException extends RuntimeException {
    /**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
	public BadOperationException(String message) {
        super(message);
    }
}
