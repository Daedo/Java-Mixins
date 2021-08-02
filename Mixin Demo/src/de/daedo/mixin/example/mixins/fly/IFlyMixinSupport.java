package de.daedo.mixin.example.mixins.fly;

import de.daedo.mixin.base.IMixinSupport;

public interface IFlyMixinSupport extends IMixinSupport {
	/**
	 * See {@link FlyMixin#fly()}
	 */
	public default void fly() {
		this.getMixin(FlyMixin.class).fly();
	}
	
	/**
	 * See {@link FlyMixin#land()}
	 */
	public default void land() {
		this.getMixin(FlyMixin.class).land();
	}
	
	/**
	 * See {@link FlyMixin#getCurrentHeight()}
	 */
	public default int getCurrentHeight() {
		return this.getMixin(FlyMixin.class).getCurrentHeight();
	}
	
	/**
	 * See {@link FlyMixin#getMaxHeight()}
	 */
	public default int getMaxHeight() {
		return this.getMixin(FlyMixin.class).getMaxHeight();
	}
	
	/**
	 * See {@link FlyMixin#setMaxHeight(int)}
	 */
	public default void setMaxHeight(int maxHeight) {
		this.getMixin(FlyMixin.class).setMaxHeight(maxHeight);
	}
}
