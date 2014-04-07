package pso;


import tools.Tools;

public class Particle {
	
	boolean extended = true;
	boolean random = false;
	int size;
	double[] position, velocity, personalBestPosition, globalBestPosition, communicationPersonalBestPosition, communicationGlobalBestPosition;
	protected double personalBestFitness, globalBestFitness, communicationPersonalBestFitness, communicationGlobalBestFitness;
	private double maxSpeed;
	private double minValue, maxValue;
	private int timestep = 0;
	private int lastCommunicationTimestep = 0;
	public double communicationRange = 15;
	public int targetCommunicationSteps;

	/**
	 * Create a new particle 
	 * 
	 * @param size		The search space for the particle.
	 * @param minValue	The minimum value accessible by the particle.
	 * @param maxValue	The maximum value accessible by the particle.
	 */
	public Particle(int size, double minValue, double maxValue, double maxSpeed, int targetCommunicationSteps) {
		this.size = size;
		this.maxSpeed = maxSpeed;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.targetCommunicationSteps = targetCommunicationSteps;
		position = new double[size];
		velocity = new double[size];
		for (int i = 0; i < size; i++) {
			position[i] = Tools.getRandomDouble(minValue, maxValue);
			velocity[i] = Tools.getRandomDouble(minValue/100, maxValue/100);
		}
		personalBestPosition = new double[position.length];
		globalBestPosition = new double[position.length];
		communicationPersonalBestPosition = new double[position.length];
		communicationGlobalBestPosition = new double[position.length];
		setRandom();
	}
	
	/**
	 * Sets best locations to random positions for exploration if no reward has been found yet
	 */
	private void setRandom() {
		if (personalBestFitness == 0) {
			for (int i = 0; i < position.length; i++) {
				personalBestPosition[i] = Tools.getRandomDouble(minValue, maxValue);
			}
		}
		if (globalBestFitness == 0) {
			for (int i = 0; i < position.length; i++) {
				globalBestPosition[i] = Tools.getRandomDouble(minValue, maxValue);
			}
		}
		if (communicationPersonalBestFitness == 0) {
			for (int i = 0; i < position.length; i++) {
				communicationPersonalBestPosition[i] = Tools.getRandomDouble(minValue, maxValue);
			}
		}
		if (communicationGlobalBestFitness == 0) {
			for (int i = 0; i < position.length; i++) {
				communicationGlobalBestPosition[i] = Tools.getRandomDouble(minValue, maxValue);
			}
		}
	}
	
	public void runIteration() {
		//System.out.println("SERVER POSITION: ["+communicationPersonalBestPosition[0]+","+communicationPersonalBestPosition[1]+"]");
		timestep++;
		setRandom();
		if (random) {
			velocityUpdateRandom();
		} else {
			if (extended)
				velocityUpdateExtended();
			else
				velocityUpdate();
		}
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
			double momentum = PSO.momentum * velocity[i];
			double update = momentum + goal(i);
			velocity[i] += update;
			clampSpeed();
		}
	}
	
	private void velocityUpdateExtended() {
		// for each component in the vectors
		//double communicationWeight = ((double)(timestep - lastCommunicationTimestep) / targetCommunicationSteps);
		double communicationWeight = (Math.pow(Math.E,(timestep - lastCommunicationTimestep)) / Math.pow(Math.E,targetCommunicationSteps));
		//System.out.println(timestep+" - "+lastCommunicationTimestep+" / "+targetCommunicationSteps+" = "+communicationWeight);
		for (int i = 0; i < velocity.length; i++) {
			double momentum = PSO.momentum * velocity[i];
			double goal = goal(i);
			double communication = communication(i);
			//System.out.println(momentum + " + " + (1 - communicationWeight)+" * "+goal+" + "+communicationWeight +" * "+communication);
			//System.out.println(Tools.round((1 - communicationWeight),3)+" + "+Tools.round(communicationWeight,3));
			double update = momentum + (1 - communicationWeight) * goal + communicationWeight * communication;
			velocity[i] += update;
			clampSpeed();
		}
	}
	
	private void velocityUpdateRandom() {
		// for each component in the vectors
		for (int i = 0; i < velocity.length; i++) {
			velocity[i] = Tools.getRandomDouble(-maxSpeed, maxSpeed);
		}
	}
	
	public double goal(int index) {
		double cognitive = Tools.getRandomDouble(0, PSO.cognitiveInfluence) * (personalBestPosition[index] - position[index]);
		double social = Tools.getRandomDouble(0, PSO.socialInfluence) * (globalBestPosition[index] - position[index]);
		return cognitive + social;
	}
	
	public double communication(int index) {
		double cognitive = Tools.getRandomDouble(0, PSO.cognitiveInfluence) * (communicationPersonalBestPosition[index] - position[index]);
		double social = Tools.getRandomDouble(0, PSO.socialInfluence) * (communicationGlobalBestPosition[index] - position[index]);
		return cognitive + social;
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
	
	public double[] position() {
		return position;
	}
	
	public double getPosition(int i) {
		return position[i];
	}
	
	public double getVelocity(int i) {
		return velocity[i];
	}
	
	public String stringPersonalBestPosition() {
		return "["+personalBestPosition[0]+","+personalBestPosition[1]+"]";
	}
	
	public String stringGlobalBestPosition() {
		return "["+globalBestPosition[0]+","+globalBestPosition[1]+"]";
	}
	
	public void serverUpdateSolution(double[] globalPosition, double newFitness) {
		if (newFitness >= globalBestFitness) {
			lastCommunicationTimestep = timestep;
			if (newFitness > globalBestFitness) {
				globalBestFitness = newFitness;
				for (int i = 0; i < globalPosition.length; i++)
					globalBestPosition[i] = globalPosition[i];
			}
		}
	}
	
	public void serverUpdatePosition(double[] serverPosition) {
		communicationGlobalBestFitness = 1.0;
		communicationPersonalBestFitness = 1.0;
		for (int i = 0; i < serverPosition.length; i++) {
			communicationGlobalBestPosition[i] = serverPosition[i];
			communicationPersonalBestPosition[i] = serverPosition[i];
		}
	}
	
	public void updatePersonalBestPosition(double fitness) {
		if (fitness > personalBestFitness) {
			personalBestFitness = fitness;
			for (int i = 0; i < position.length; i++)
				personalBestPosition[i] = position[i];
		}
	}
	
	public void updateGlobalBestPosition(double[] globalPosition, double newFitness) {
		if (newFitness > globalBestFitness) {
			globalBestFitness = newFitness;
			for (int i = 0; i < globalPosition.length; i++)
				globalBestPosition[i] = globalPosition[i];
		}
	}
	
	public void updateCommunicationPersonalBestPosition(double newFitness) {
		if (newFitness > 0) {
			lastCommunicationTimestep = timestep;
			if (newFitness > communicationPersonalBestFitness) {
				communicationPersonalBestFitness = newFitness;
				for (int i = 0; i < position.length; i++)
					communicationPersonalBestPosition[i] = position[i];
			}
		}
	}
	
	public void updateCommunicationGlobalBestPosition(double[] communicationPosition, double newFitness) {
		if (newFitness > communicationGlobalBestFitness) {
			communicationGlobalBestFitness = newFitness;
			for (int i = 0; i < communicationPosition.length; i++)
				communicationGlobalBestPosition[i] = communicationPosition[i];
		}
	}
	
	public double getPersonalBestFitness() {
		return personalBestFitness;
	}
	
	public double[] getPersonalBestPosition() {
		return personalBestPosition;
	}
	
	public double getGlobalBestFitness() {
		return globalBestFitness;
	}
	
	public double[] getGlobalBestPosition() {
		return globalBestPosition;
	}
	
	public double getCommunicationPersonalBestFitness() {
		return communicationPersonalBestFitness;
	}
	
	public double[] getCommunicationPersonalBestPosition() {
		return communicationPersonalBestPosition;
	}
	
	public double getCommunicationGlobalBestFitness() {
		return communicationGlobalBestFitness;
	}
	
	public double[] getCommunicationGlobalBestPosition() {
		return communicationGlobalBestPosition;
	}
	
	protected void degradeFitness(double degradeFactor) {
		personalBestFitness *= degradeFactor;
		globalBestFitness *= degradeFactor;
	}

}
