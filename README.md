# Java-Mixins
[Mixins](https://en.wikipedia.org/wiki/Mixin) are a really useful tool to have. They allow you to basically insert a pre-defined snippit of functionality to any class. 
Due to the lack of multi-inheritance or interfaces with fields, Java doesn't natively support mixins on the same level as other languages.  However we can use interfaces and default methods to create something similar with only minimal boilerplate.
 
# Going beyond the interface
While Java does not have support for multi-inheritance a class can implement arbitrarily many interface.
An interface can also define actual implementations using the `default` keyword. The problem is, that interfaces are not able to access local variables of a class.

Take the following example:
```java
public interface ITalk {
    public String getMessage();

    public void setMessage(String message);

    public default void talk() {
        System.out.println(this.getMessage());
    }
}
```
This interface should provide the capability of a `talk` method that prints a pre-defined message. This message should be accessible using a getter and setter. Unfortunately due to the lack of local variables the implementation of the getter, setter and local field has to be done by the user.  While we could turn this interface into an abstract class this would defeat our purpose since we can only extend one class but want to be able to add arbitrarily many mixins. What we would need is a way for an interface to store arbitrary data in its class.

# Interfaces with state
Consider this class implementation of the same interface: 
```java
public class TalkState {
    private String message;
    
    public TalkState() {
        this.message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (this.message == null) {
            throw new NullPointerException("message may not be null");
        }
        this.message = message;
    }
    
    public void talk() {
        System.out.println(this.message);
    }
}
```
It can easily implement the missing capabilities. Now, let's combine these two methods:


```java
public interface ITalkWithState {
    TalkState getState();

    public default String getMessage() {
        return this.getState().getMessage();
    }

    public default void setMessage(String message) {
        this.getState().setMessage(message);
    }

    public default void talk() {
        return this.getState().talk();
    }
}
```
As you can see by adding the `getState` method to our interface we can now simply delegate all methods to the `TalkState` now our user only has to provide a simple getter for the state and all methods are free.

# Full Mixins
Now with just one state this is hardly impressive, so let me show you the full magic of java mixins:

```java
public interface IMixinSupport {
    <M extends Mixin> M getMixin(Class<M> clazz);
}

public abstract class Mixin {}
```
These two mostly harmless thingies are what makes the magic work: The `Mixin` class is the abstraction of the `TalkState` from above while the `getMixin` method is the abstraction of the `getState` method.
An Implementation of the `ITalk` Interface could look like this:


```java
public class TalkMixin extends Mixin {
    private String message;
    
    public TalkState() {
        this.message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (this.message == null) {
            throw new NullPointerException("message may not be null");
        }
        this.message = message;
    }
    
    public void talk() {
        System.out.println(this.message);
    }
}

public interface ITalkMixinSupport extends IMixinSupport {
    public default String getMessage() {
        return this.getMixin(TalkMixin.class).getMessage();
    }
    
    public default void setMessage(String message) {
        this.getMixin(TalkMixin.class).setMessage(message);
    }
    
    public default void talk() {
        this.getMixin(TalkMixin.class).talk();
    }
}
```

Notice the similarities to the example from before essentially we just replaced the `getState()` with `getMixin(TalkMixin.class)` but this is all it took. Now look at this:

```java
public class FlyMixin extends Mixin{
    int currentHeight;
    int maxHeight;
    
    public FlyMixin() {
        this.currentHeight = 0;
        this.maxHeight = 100;
    }
    
    public void fly() {
        this.currentHeight = this.maxHeight;
    }
    
    public void land() {
        this.currentHeight = 0;
    }
    
    public int getCurrentHeight() {
        return currentHeight;
    }
}

public interface IFlyMixinSupport extends IMixinSupport {
    public default void fly() {
        this.getMixin(FlyMixin.class).fly();
    }

    public default void land() {
        this.getMixin(FlyMixin.class).land();
    }

    public default int getCurrentHeight() {
        return this.getMixin(FlyMixin.class).getCurrentHeight();
    }
}
```
This is a second mixin using the same pattern, and as before our user only need to implement the `getMixin` method and everything else is free.

## How does `getMixin` work
`getMixin` is at its core just a hashMap... Really...

Look at this:
```java
public class MixinSupportingBase implements IMixinSupport {

    private HashMap<Class<? extends Mixin>, Mixin> mixinMap;

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
```

All we do is look up the class in the `mixinMap` if it is there we return it, otherwise we create a new instance.

# Okay... But why is this useful?
Well now that we have a class that implemented the `IMixinSupport`we can do stuff like this:
```java

public class Parrot extends MixinSupportingBase implements IFlyMixinSupport, ITalkMixinSupport {
    // Nothing to do, mixins do all the work
}
```
And now we can call `parrot.fly()` or `parrot.talk()` without having to implement **anything**. I thinks that is pretty cool.

You can arbitrarily extend this pattern:
1. Create a class that extends `Mixin`, that implements the functionality and stores the state
2. Implement an interface that extends `IMixinSupport` that delegates to the `Mixin``
3. Let any class that has an implement version of `getMixin` implement the new interface
4. Profit: Your class now supports all the mixin functionality

# `getMixin` Variations
The most difficult part of this entire construct is the `getMixin` method. It has to instantiate the mixin if it isn't present. I used reflection for it and simply assumed that each mixin has a zero argument constructor. Personally I think this is a reasonable thing to assume but If you don't want to use reflection there is another other option:

```java
public interface IMixinSupport {
    <M extends Mixin> M getMixinOrElse(Class<M> clazz, Supplier<M> mixinGenerator);
}

public <M extends Mixin> M getMixinOrElse(Class<M> clazz, Supplier<M> mixinGenerator) {
        if(clazz == null) {
            throw new NullPointerException("Mixin Class may not be null");
        }

        if(!this.mixinMap.containsKey(clazz)) {
            this.mixinMap.put(clazz, mixinGenerator.get())
        }
        return (M) this.mixinMap.get(clazz);
    }
```
Now the only difference is that you mixin support interfaces have to provide a supplier:

```java
public interface IFlyMixinSupport extends IMixinSupport {
    public default void fly() {
        this.getMixinOrElse(FlyMixin.class, FlyMixin::new).fly();
    }

    public default void land() {
        this.getMixinOrElse(FlyMixin.class, FlyMixin::new).land();
    }

    public default int getCurrentHeight() {
        return this.getMixinOrElse(FlyMixin.class, FlyMixin::new).getCurrentHeight();
    }
}
```
And like this we no longer need any reflection. I personally don't mind the reflection since it saves me having to add those suppliers and as I said, I think having a mixin with zero argument constructor is fine by me. If you want to configure it you can still do this in the constructor of the mixin implementing class:
```java
public class Human extends MixinSupportingBase implements ITalkMixinSupport {
    public Human() {
        this.setMessage("Would anyone like some stew?");
    }
}
```

# Self-aware mixins
If you want your mixin to be able to access the class it is attached to this can also be easily done:

```
public abstract class Mixin {
    protected IMixinSupport parent;

    public Mixin(IMixinSupport parent) {
        this.parent = parent;
    }
}
```
Using the `getMixin` function you could now even access other mixins with your mixin. If you were to add a `boolean hasMixin(Class<M> clazz)` to the mixin support you have full mixin introspection. In this case you would have to slightly change the reflection to pass `this` to the constuctor but that is simple.

Anyway I think I've gone on long enough about mixins. If you want to checkout a documented example you can simply clone the repo and play with it. 

Have a nice day!   
-- Dominik
