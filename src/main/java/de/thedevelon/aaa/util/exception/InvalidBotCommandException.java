package de.thedevelon.aaa.util.exception;

public class InvalidBotCommandException extends Exception {
	
	public InvalidBotCommandException() { super(); }
	public InvalidBotCommandException(String message) { super(message); }
	public InvalidBotCommandException(String message, Throwable cause) { super(message, cause); }
	public InvalidBotCommandException(Throwable cause) { super(cause); }

}
