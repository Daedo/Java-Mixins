package de.daedo.mixin.example;

import de.daedo.mixin.example.mixins.fly.IFlyMixinSupport;
import de.daedo.mixin.example.mixins.talk.ITalkMixinSupport;

/**
 * The {@link Parrot} class supports both talking with the {@link ITalkMixinSupport} and flying
 * with the {@link IFlyMixinSupport}.
 */
public class Parrot extends MixinSupportingBase implements ITalkMixinSupport, IFlyMixinSupport{
	
	/**
	 * Class Constructor
	 */
	public Parrot() {
		this.setMessage("Polly wants a cracker!");
		this.setMaxHeight(3);
	}

}
