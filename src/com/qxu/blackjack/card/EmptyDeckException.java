package com.qxu.blackjack.card;

public class EmptyDeckException extends RuntimeException {
	private static final long serialVersionUID = -5869847119205132420L;

	/**
	 * Constructs a new <code>EmptyStackException</code>
	 */
	public EmptyDeckException() {
		super();
	}

	public EmptyDeckException(String message) {
		super(message);
	}
}
