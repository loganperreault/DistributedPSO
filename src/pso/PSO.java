package pso;

import java.util.ArrayList;
import java.util.List;

import tools.Tools;

public class PSO {
	
	protected static double momentum = 0.9;
	public static double cognitiveInfluence = 2.5;
	public static double socialInfluence = 0.1;
	Fitness fitnessEvaluation;
	Fitness communicationEvaluation;
	public static Particle globalBestParticle;
	double avgFitness, globalBestFitness = 0.0;
	public static int targetCommunicationSteps = 50;
	
	public List<Particle> particles = new ArrayList<>();
	int particleDimension = 2;
	
	public PSO(int swarmSize, double minValue, double maxValue, double maxSpeed, Fitness fitnessEvaluation, Fitness communicationEvaluation) {
		for (int i = 0; i < swarmSize; i++)
			particles.add(new Particle(particleDimension, minValue, maxValue, maxSpeed));
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
			particle.updatePersonalBestPosition(fitness);
			particle.updateGlobalBestPosition(particle.position, fitness);
			// evaluate all particles and update personal communication fitnesses
			double communication = communicationEvaluation.evaluate(particle);
			particle.updateCommunicationPersonalBestPosition(communication);
			particle.updateCommunicationGlobalBestPosition(particle.position, communication);
		}
		
		// find the best position anyone knows about within communication range for each particle
		for (Particle particle : particles) {
			globalBestFitness = particle.getGlobalBestFitness();
			globalBestParticle = particle;
			List<Particle> neighbors = getNeighbors(particle);
			//System.out.println(neighbors.size());
			for (Particle neighbor : neighbors) {
				if (neighbor.getGlobalBestFitness() > globalBestFitness) {
					globalBestFitness = neighbor.getGlobalBestFitness();
					globalBestParticle = neighbor;
				}
			}
			particle.updateGlobalBestPosition(globalBestParticle.getGlobalBestPosition(), globalBestParticle.getGlobalBestFitness());
		}
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
	
	public void runIteration() {
		for (Particle particle : particles) {
			particle.runIteration();
			evaluateParticles();
		}
	}

}
