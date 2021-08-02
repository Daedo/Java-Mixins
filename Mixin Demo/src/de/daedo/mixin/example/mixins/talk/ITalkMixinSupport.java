package de.daedo.mixin.example.mixins.talk;

import de.daedo.mixin.base.IMixinSupport;

/**
 * Implementing the {@link ITalkMixinSupport} enables support for the {@link TalkMixin}
 */
public interface ITalkMixinSupport extends IMixinSupport {
	/**
	 * See {@link TalkMixin#getMessage()}
	 */
	public default String getMessage() {
		return this.getMixin(TalkMixin.class).getMessage();
	}
	
	/**
	 * See {@link TalkMixin#setMessage(String)}
	 */
	public default void setMessage(String message) {
		this.getMixin(TalkMixin.class).setMessage(message);
	}
	
	/**
	 * See {@link TalkMixin#talk()}
	 */
	public default void talk() {
		this.getMixin(TalkMixin.class).talk();
	}
}
