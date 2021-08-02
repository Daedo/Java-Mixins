package de.daedo.mixin.example.mixins.fly;

import de.daedo.mixin.base.Mixin;

/**
 * Example Mixin: The {@link FlyMixin} give the class a current and maximal flight height.
 * It provides methods for flying and landing.
 */
public class FlyMixin extends Mixin{
	int currentHeight;
	int maxHeight;
	
	/**
	 * Class Constructor
	 */
	public FlyMixin() {
		this.currentHeight = 0;
		this.maxHeight = 100;
	}
	
	/**
	 * Sets the current height to the maximal height
	 */
	public void fly() {
		this.currentHeight = this.maxHeight;
	}
	
	/**
	 * Sets the current height to zero.
	 */
	public void land() {
		this.currentHeight = 0;
	}
	
	/**
	 * Getter
	 * @return the current height
	 */
	public int getCurrentHeight() {
		return currentHeight;
	}
	
	/**
	 * Getter
	 * @return the maximal flight height
	 */
	public int getMaxHeight() {
		return maxHeight;
	}
	
	/**
	 * Setter
	 * @param maxHeight the new maximal flight height, may not be less than zero.
	 */
	public void setMaxHeight(int maxHeight) {
		if(maxHeight < 0) {
			throw new IllegalArgumentException("maxHeight may not be negative");
		}
		this.maxHeight = maxHeight;
	}
	
}
