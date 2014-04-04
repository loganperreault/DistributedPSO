package pso;

import problem.Room;

public abstract class Fitness {
	
	Room area;
	
	public Fitness(Room area) {
		this.area = area;
	}
	
	public abstract double evaluate(Particle particle);

}
