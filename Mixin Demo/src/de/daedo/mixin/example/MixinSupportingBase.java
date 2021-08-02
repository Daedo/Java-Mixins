package de.daedo.mixin.example;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import de.daedo.mixin.base.IMixinSupport;
import de.daedo.mixin.base.Mixin;

/**
 * A simple example implementation of a class supporting {@link Mixin}s.
 */
public class MixinSupportingBase implements IMixinSupport {

	private HashMap<Class<? extends Mixin>, Mixin> mixinMap;
	
	/**
	 * Class Constructor
	 */
	public MixinSupportingBase() {
		this.mixinMap = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <M extends Mixin> M getMixin(Class<M> clazz) {
		if(clazz == null) {
			throw new NullPointerException("Mixin Class may not be null");
		}

		if(!this.mixinMap.containsKey(clazz)) {
			try {
				M mixin = clazz.getConstructor().newInstance();
				this.mixinMap.put(clazz, mixin);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new UnsupportedOperationException(
						"Could not instantiate mixin of type"+ clazz.getSimpleName(), e);
			}
		}
		return (M) this.mixinMap.get(clazz);
	}

}
