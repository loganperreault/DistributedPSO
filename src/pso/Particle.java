package pso;


import tools.Tools;

public class Particle {
	
	boolean extended = false;
	int size;
	double[] position, velocity, personalBestPosition, globalBestPosition;
	private double personalBestFitness, globalBestFitness;
	private double maxSpeed;
	private double minValue, maxValue;

	/**
	 * Create a new particle 
	 * 
	 * @param size		The search space for the particle.
	 * @param minValue	The minimum value accessible by the particle.
	 * @param maxValue	The maximum value accessible by the particle.
	 */
	public Particle(int size, double minValue, double maxValue, double maxSpeed) {
		this.size = size;
		this.maxSpeed = maxSpeed;
		this.minValue = minValue;
		this.maxValue = maxValue;
		position = new double[size];
		velocity = new double[size];
		for (int i = 0; i < size; i++) {
			position[i] = Tools.getRandomDouble(minValue, maxValue);
			velocity[i] = Tools.getRandomDouble(minValue/100, maxValue/100);
		}
		personalBestPosition = new double[position.length];
		globalBestPosition = new double[position.length];
		for (int i = 0; i < position.length; i++) {
			personalBestPosition[i] = position[i];
			globalBestPosition[i] = position[i];
		}
	}
	
	public void runIteration() {
		if (extended)
			velocityUpdateExtended();
		else
			velocityUpdate();
		move();
	}
	
	private void move() {
		double margin = 2;
		for (int i = 0; i < size; i++) {
			position[i] += velocity[i];
			if (position[i] < minValue) {
				position[i] = minValue + margin;
				velocity[i] *= -0.5;
			}
			if (position[i] > maxValue - margin) {
				position[i] = maxValue - margin*2;
				velocity[i] *= -0.5;
			}
		}
	}
	
	private void velocityUpdate() {
		// for each component in the vectors
		for (int i = 0; i < velocity.length; i++) {
			double term1 = PSO.momentum * velocity[i];
			double term2 = Tools.getRandomDouble(0, PSO.cognitiveInfluence) * (personalBestPosition[i] - position[i]);
			double term3 = Tools.getRandomDouble(0, PSO.socialInfluence) * (globalBestPosition[i] - position[i]);
			double update = term1 + term2 + term3;
			velocity[i] += update;
			clampSpeed();
		}
	}
	
	private void velocityUpdateExtended() {
		
	}
	
	private void clampSpeed() {
		for (int i = 0; i < velocity.length; i++) {
			velocity[i] = Math.max(Math.min(velocity[i], maxSpeed), -maxSpeed);
		}
	}
	
	public String toString() {
		String str = "[";
		for (int i = 0; i < position.length; i++) {
			if (i > 0)
				str += ",";
			str += position[i];
		}
		str += "]";
		return str;
	}
	
	public void printPosition() {
		System.out.println(this);
	}
	
	public void printVelocity() {
		System.out.print("[");
		for (int i = 0; i < position.length; i++) {
			if (i > 0)
				System.out.print(",");
			System.out.print(velocity[i]);
		}
		System.out.println("]");
	}
	
	public double getPosition(int i) {
		return position[i];
	}
	
	public double getVelocity(int i) {
		return velocity[i];
	}
	
	public String getBestPosition() {
		return "["+personalBestPosition[0]+","+personalBestPosition[1]+"]";
	}
	
	public void updatePersonalBestPosition(double fitness) {
		if (fitness > personalBestFitness) {
			personalBestFitness = fitness;
			for (int i = 0; i < position.length; i++)
				personalBestPosition[i] = position[i];
		}
	}
	
	public void updateGlobalBestPosition(double[] globalPosition, double fitness) {
		if (fitness > globalBestFitness) {
			globalBestFitness = fitness;
			for (int i = 0; i < globalPosition.length; i++)
				personalBestPosition[i] = globalPosition[i];
		}
	}

}
