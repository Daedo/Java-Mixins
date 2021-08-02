package de.daedo.mixin.example.mixins.talk;

import de.daedo.mixin.base.Mixin;

/**
 * Example Mixin: The {@link TalkMixin} allows printing a predefined 
 * message using the {@link TalkMixin#talk()} method .
 */
public class TalkMixin extends Mixin{
	private String message;
	
	/**
	 * Class Constructor
	 */
	public TalkMixin() {
		this.message = "";
	}
	
	/**
	 * Getter
	 * @return the message, never null
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Setter
	 * @param message the new message, may not be null
	 * @throws NullPointerException should the argument be null
	 */
	public void setMessage(String message) {
		if (this.message == null) {
			throw new NullPointerException("message may not be null");
		}
		this.message = message;
	}
	
	/**
	 * prints the current message
	 */
	public void talk() {
		System.out.println(this.message);
	}
}
