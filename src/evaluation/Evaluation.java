package evaluation;

import java.util.ArrayList;
import java.util.List;

import problem.Room;
import problem.Server;
import problem.Target;
import pso.Particle;
import tools.Tools;

public class Evaluation {
	
	Room room;
	double targetIntensityThreshold = 1;
	int timesteps = 1000;
	int invalidTimesteps = 0;
	double[] solutionValue = new double[timesteps];
	double exactSolutionDistance = 1.0;
	
	public Evaluation(Room room) {
		this.room = room;
	}
	
	public void test() {
		for (int i = 0; i < timesteps; i++) {
			room.runIteration();
			testCommunication();
			testSolution();
		}
	}
	
	public double getValidTimesteps() {
		return 1 - ((double)invalidTimesteps / timesteps);
	}
	
	public int getTimestepConverged() {
		int i;
		for (i = 0; i < timesteps; i++)
			if (solutionValue[i]==1)
				break;
		return i;
	}
	
	public double getConvergenceValue() {
		return (double) getTimestepConverged() / timesteps;
	}
	
	private void testCommunication() {
		
		double actualFitness = 0;
		double[] actualPosition = new double[2];
		// get actual global best solution
		for (Particle particle : room.swarm.particles) {
			if (particle.getGlobalBestFitness() >= actualFitness) {
				actualFitness = particle.getGlobalBestFitness();
				actualPosition = particle.getGlobalBestPosition();
			}
		}
		
		double serverFitness = 0;
		double[] serverPosition = new double[2];
		// get server best solution
		for (Server server : room.servers) {
			if (server.getSolutionFitness() > serverFitness) {
				serverFitness = server.getSolutionFitness();
				serverPosition = server.getSolutionPosition();
			}
		}
		
		if (actualFitness != serverFitness)
			invalidTimesteps++;
		
	}
	
	private void testSolution() {
		
		// for each target that is above a minimum intensity (not noise)
		double maxValue = 0;
		for (Target target : room.targets) {
			if (target.getIntensity() > targetIntensityThreshold) {
				double[] closest;
				double closestDistance = Double.MAX_VALUE;
				for (Particle particle : room.swarm.particles) {
					closestDistance = Math.min(closestDistance, Tools.euclidean(particle.getPersonalBestPosition(), target.position()));
				}
				solutionValue[room.getTimestep() - 1] += target.getIntensity() * Math.min((exactSolutionDistance / closestDistance), exactSolutionDistance);
				maxValue += target.getIntensity() * exactSolutionDistance;
			}
		}
		// normalize to [0,1]
		solutionValue[room.getTimestep() - 1] /= maxValue;
		
	}
	
	public void setTimesteps(int timesteps) {
		this.timesteps = timesteps;
	}

}
