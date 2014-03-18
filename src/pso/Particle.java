package pso;


import tools.Tools;

public class Particle {
	
	boolean extended = false;
	int size;
	double[] position, velocity, personalBest;

	/**
	 * Create a new particle 
	 * 
	 * @param size		The search space for the particle.
	 * @param minValue	The minimum value accessible by the particle.
	 * @param maxValue	The maximum value accessible by the particle.
	 */
	public Particle(int size, double minValue, double maxValue) {
		this.size = size;
		position = new double[size];
		velocity = new double[size];
		for (int i = 0; i < size; i++) {
			position[i] = Tools.getRandomDouble(minValue, maxValue);
			velocity[i] = Tools.getRandomDouble(minValue, maxValue);
		}
		personalBest = position;
	}
	
	public void runIteration() {
		if (extended)
			velocityUpdateExtended();
		else
			velocityUpdate();
		move();
	}
	
	private void move() {
		for (int i = 0; i < size; i++)
			position[i] += velocity[i];
	}
	
	private void velocityUpdate() {
		
	}
	
	private void velocityUpdateExtended() {
		
	}

}
