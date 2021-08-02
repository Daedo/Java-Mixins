package de.daedo.mixin.example;

public class Main {

	public static void main(String[] args) {
		Human human = new Human();
		human.talk();

		Airplane airplaine = new Airplane();
		airplaine.fly();
		assert(airplaine.getMaxHeight() ==  airplaine.getCurrentHeight());
		
		Parrot polly = new Parrot();
		polly.talk();
		polly.fly();
		assert(polly.getMaxHeight() ==  polly.getCurrentHeight());
	}

}
