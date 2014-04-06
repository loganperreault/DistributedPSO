package problem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import pso.PSO;
import pso.Particle;

public class Server {
	
	// TODO: keep track of each particle and when they last communicated
	
	public Color color = Color.GREEN;
	public int x, y;
	double communicationRange = 1000;
	List<PSO> swarms;
	double solutionFitness;
	double[] solutionPosition;
	
	public Server(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double[] position() {
		return new double[] {x, y};
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
			for (PSO swarm : swarms) {
				for (Particle particle : swarm.particles) {
					particle.serverUpdate(solutionPosition, solutionFitness);
				}
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

}
