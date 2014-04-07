package problem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import pso.PSO;
import pso.Particle;
import tools.Tools;

public class Server {
	
	// TODO: keep track of each particle and when they last communicated
	
	public Color color = Color.GREEN;
	public int x, y;
	double communicationRange = 50;
	List<PSO> swarms;
	double solutionFitness;
	double[] solutionPosition;
	double degradeFactor = 1.0;
	
	public Server(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double[] position() {
		return new double[] {x, y};
	}
	
	public void runIteration() {
		degradeSolution();
		broadcastPosition();
		broadcastSolution();
	}
	
	public void setCommunicationRange(double communicationRange) {
		this.communicationRange = communicationRange;
	}
	
	public void transmitSolution(double[] position, double fitness) {
		if (fitness > solutionFitness) {
			// update solution
			solutionPosition = position;
			solutionFitness = fitness;
			// broadcast solution
			broadcastSolution();
		}
	}
	
	private void degradeSolution() {
		solutionFitness *= degradeFactor;
	}
	
	private void broadcastSolution() {
		for (PSO swarm : swarms) {
			for (Particle particle : swarm.particles) {
				double distance = Tools.euclidean(particle.position(), this.position());
				if (distance < communicationRange)	{
					particle.serverUpdateSolution(solutionPosition, solutionFitness);
				}
			}
		}
	}
	
	public void broadcastPosition() {
		for (PSO swarm : swarms) {
			for (Particle particle : swarm.particles) {
				double distance = Tools.euclidean(particle.position(), this.position());
				if (distance < communicationRange)
					particle.serverUpdatePosition(position());
			}
		}
	}
	
	public void addSwarms(List<PSO> swarms) {
		this.swarms = swarms;
	}
	
	public void addSwarm(PSO swarm) {
		if (swarms == null)
			swarms = new ArrayList<>();
		swarms.add(swarm);
	}
	
	public double getSolutionFitness() {
		return solutionFitness;
	}
	
	public double[] getSolutionPosition() {
		return solutionPosition;
	}
	
	public void setDegradeFactor(double degradeFactor) {
		if (degradeFactor < 0 || degradeFactor > 1) {
			System.out.println("ERROR: degrade factor must be between 0 and 1.");
			System.exit(1);
		}
		this.degradeFactor = degradeFactor;
	}

}
