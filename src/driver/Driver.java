package driver;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
		
		List<Target> targets = getTargets();
		
		Room room = new Room(roomSize);
		
		Fitness fitnessEvaluation = new FitnessTarget(room, targets);
		PSO swarm = new PSO(numRobots, 0, roomSize, maxSpeed, fitnessEvaluation);
		
		room.addSwarm(swarm);
		room.addTargets(targets);
		
		room.runIterations(1000);
	}
	
	private static List<Target> getTargets() {
		List<Target> targets = new ArrayList<>();
		
		Target target1 = new Target(15, 50);
		//target1.setIntensity(500000);
		
		Target target2 = new Target(50, 60);
		
		Target target3 = new Target(70, 20);
		
		targets.add(target1);
		//targets.add(target2);
		//targets.add(target3);
		
		return targets;
	}

}
