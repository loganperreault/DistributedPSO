package pso;

import java.util.ArrayList;
import java.util.List;

public class PSO {
	
	protected static double momentum = 0.2;
	public static double cognitiveInfluence = 0.5;
	public static double socialInfluence = 0.5;
	Fitness fitnessEvaluation;
	public static Particle globalBest;
	double avgFitness, globalBestFitness = 0.0;
	
	public List<Particle> particles = new ArrayList<>();
	int particleDimension = 2;
	
	public PSO(int swarmSize, double minValue, double maxValue, Fitness fitnessEvaluation) {
		for (int i = 0; i < swarmSize; i++)
			particles.add(new Particle(particleDimension, minValue, maxValue));
		this.fitnessEvaluation = fitnessEvaluation;
		globalBest = particles.get(0);
	}
	
	public Particle get(int i) {
		return particles.get(i);
	}
	
	public int numParticles() {
		return particles.size();
	}
	
	public void evaluateParticles() {
		// evaluate all particles
		double minFitness = Double.MAX_VALUE;
		double avgFitness = 0.0;
		for (Particle particle : particles) {
			double fitness = fitnessEvaluation.evaluate(particle);
			if (fitness > globalBestFitness) {
				globalBestFitness = fitness;
				globalBest = particle;
			}
			avgFitness += fitness;
			minFitness = Math.min(fitness, minFitness);
		}
		avgFitness /= particles.size();
		double range = globalBestFitness - minFitness;
		//System.out.format("FITNESS: (%10f , %10f)   %f\n", minFitness, globalBestFitness, avgFitness);
		System.out.format("RANGE: %10f    AVG: %10f    BEST: %10f\n", 
				range, 
				avgFitness,
				globalBestFitness);
	}
	
	public void runIteration() {
		for (Particle particle : particles) {
			particle.runIteration();
		}
	}

}
