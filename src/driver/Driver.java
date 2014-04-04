package driver;

import problem.Room;
import problem.Target;
import pso.Fitness;
import pso.FitnessTarget;
import pso.PSO;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO: remove global knowledge from particles
		// TODO: clamp search area
		// TODO: write server code
		// TODO: write communication-based velocity update
		
		int numRobots = 8;
		double roomSize = 100;
		double maxSpeed = 1.0;
		
		Target target = new Target(15, 50);
		
		Room area = new Room(roomSize);
		
		Fitness fitnessEvaluation = new FitnessTarget(area, target);
		PSO swarm = new PSO(numRobots, 0, roomSize, maxSpeed, fitnessEvaluation);
		
		area.addSwarm(swarm);
		area.addTarget(target);
		
		area.runIterations(1000);
	}

}
