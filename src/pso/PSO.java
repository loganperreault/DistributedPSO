package pso;

import java.util.ArrayList;
import java.util.List;

import tools.Tools;

public class PSO {
	
	protected static double momentum = 0.9;
//	public static double cognitiveInfluence = 2.5;
//	public static double socialInfluence = 0.1;
	public static double cognitiveInfluence = 2.1;
	public static double socialInfluence = 0.3;
	Fitness fitnessEvaluation;
	Fitness communicationEvaluation;
	public static Particle globalBestParticle, communicationGlobalBestParticle;
	double globalBestFitness, communicationGlobalBestFitness;
	private int targetCommunicationSteps = 50;
	public int communicationOffset = 10;
	double degradeFactor = 1.0;
	double fitnessThreshold = 2;
	
	public List<Particle> particles = new ArrayList<>();
	int particleDimension = 2;
	
	public PSO(int swarmSize, double minValue, double maxValue, double maxSpeed, Fitness fitnessEvaluation, Fitness communicationEvaluation) {
		for (int i = 0; i < swarmSize; i++) {
			Particle particle = new Particle(particleDimension, minValue, maxValue, maxSpeed, targetCommunicationSteps + i * communicationOffset);
			particles.add(particle);
		}
		this.fitnessEvaluation = fitnessEvaluation;
		this.communicationEvaluation = communicationEvaluation;
		globalBestParticle = particles.get(0);
	}
	
	public Particle get(int i) {
		return particles.get(i);
	}
	
	public int numParticles() {
		return particles.size();
	}
	
	public void evaluateParticles() {
		
		for (Particle particle : particles) {
			// evaluate all particles and update personal fitnesses
			double fitness = fitnessEvaluation.evaluate(particle);
			if (fitness > fitnessThreshold) {
				particle.updatePersonalBestPosition(fitness);
				particle.updateGlobalBestPosition(particle.position, fitness);
			}
			// evaluate all particles and update personal communication fitnesses
			double communication = communicationEvaluation.evaluate(particle);
			particle.updateCommunicationPersonalBestPosition(communication);
			particle.updateCommunicationGlobalBestPosition(particle.position, communication);
		}
		
		// find the best position anyone knows about within communication range for each particle
		for (Particle particle : particles) {
			double globalBestFitness = particle.getGlobalBestFitness();
			Particle globalBestParticle = particle;
			double communicationGlobalBestFitness = particle.getCommunicationGlobalBestFitness();
			Particle communicationGlobalBestParticle = particle;
			List<Particle> neighbors = getNeighbors(particle);
			//System.out.println(neighbors.size());
			// force exploration if no solution found yet
			double[] exploration = new double[particle.globalBestPosition.length];
			for (int i = 0; i < exploration.length; i++)
				exploration[i] = 1;
			for (Particle neighbor : neighbors) {
				if (neighbor.getGlobalBestFitness() > globalBestFitness) {
					globalBestFitness = neighbor.getGlobalBestFitness();
					globalBestParticle = neighbor;
				}
				if (neighbor.getCommunicationGlobalBestFitness() > communicationGlobalBestFitness) {
					communicationGlobalBestFitness = neighbor.getCommunicationGlobalBestFitness();
					communicationGlobalBestParticle = neighbor;
				}
				// force exploration if no solution found yet
				for (int i = 0; i < neighbor.position.length; i++)
					exploration[i] *= -neighbor.position[i];
			}
			/*
			if (particle.globalBestFitness == 0)
				for (int i = 0; i < exploration.length; i++)
					particle.globalBestPosition[i] = exploration[i];
			if (particle.globalBestFitness == 0)
				for (int i = 0; i < exploration.length; i++)
					particle.personalBestPosition[i] = exploration[i];
			*/
			
			// update global best solutions
			particle.updateGlobalBestPosition(globalBestParticle.getGlobalBestPosition(), globalBestParticle.getGlobalBestFitness());
			particle.updateCommunicationGlobalBestPosition(communicationGlobalBestParticle.getCommunicationGlobalBestPosition(), 
															communicationGlobalBestParticle.getCommunicationGlobalBestFitness());
			
			// update server's belief of the solution
			for (Particle neighbor : neighbors) {
				neighbor.serverUpdateSolution(particle.globalBestPosition, particle.globalBestFitness, particle.timestep - particle.lastCommunicationTimestep + 1);
			}
			
		}
		
		//System.out.println(particles.get(0).globalBestFitness+" vs "+particles.get(1).globalBestFitness+" vs "+particles.get(2).globalBestFitness);
		System.out.println(particles.get(0).globalBestFitness+" at ("+particles.get(0).globalBestPosition[0]+","+particles.get(0).globalBestPosition[1]+")");
		
	}
	
	public List<Particle> getNeighbors(Particle particle) {
		List<Particle> neighbors = new ArrayList<>();
		for (Particle p : particles) {
			double distance = Tools.euclidean(particle.position, p.position);
			if (distance < particle.communicationRange && p != particle)
				neighbors.add(p);
		}
		return neighbors;
	}
	
	public void setDegradeFactor(double degradeFactor) {
		if (degradeFactor < 0 || degradeFactor > 1) {
			System.out.println("ERROR: degrade factor must be between 0 and 1.");
			System.exit(1);
		}
		this.degradeFactor = degradeFactor;
	}
	
	public void runIteration() {
		for (Particle particle : particles) {
			particle.runIteration();
			particle.degradeFitness(degradeFactor);
		}
		evaluateParticles();
	}

}
