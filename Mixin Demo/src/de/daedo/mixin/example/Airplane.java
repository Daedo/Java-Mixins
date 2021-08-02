package de.daedo.mixin.example;

import de.daedo.mixin.example.mixins.fly.IFlyMixinSupport;

/**
 * the {@link Airplane} can fly with the {@link IFlyMixinSupport}
 */
public class Airplane extends MixinSupportingBase implements IFlyMixinSupport {
	
	/**
	 * Class Constructor
	 */
	public Airplane() {
		this.setMaxHeight(100_000);
	}
}
