package de.daedo.mixin.example;

import de.daedo.mixin.example.mixins.talk.ITalkMixinSupport;

/**
 * The {@link Human} can talk with the {@link ITalkMixinSupport}
 */
public class Human extends MixinSupportingBase implements ITalkMixinSupport {
	
	/**
	 * Class Constructor
	 */
	public Human() {
		this.setMessage("Would anyone like some stew?");
	}
	
}
