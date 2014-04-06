package driver;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import problem.Room;
import problem.Target;
import pso.Fitness;
import pso.FitnessTarget;
import pso.PSO;
import tools.Tools;

public class Driver {
	
	static int numRobots = 8;
	static double roomSize = 100;
	static double maxSpeed = 1.0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// NOTE: had to randomly set bestLocations until bestFitness was greater than 0
		// NOTE: fitness has a falloff until it gets small enough, then imperceptible
		
		// TODO: write server code
		// TODO: write communication-based velocity update
		
		List<Target> targets = getTargets();
		
		Room room = new Room(roomSize);
		
		Fitness fitnessEvaluation = new FitnessTarget(room, targets);
		PSO swarm = new PSO(numRobots, 0, roomSize, maxSpeed, fitnessEvaluation);
		
		room.addSwarm(swarm);
		room.addTargets(targets);
		
		room.runIterations(1000);
	}
	
	private static List<Target> getRandomTargets(int number, int strength) {
		List<Target> targets = new ArrayList<>();
		for (int i = 0; i < number; i++)
			targets.add(new Target(Tools.random.nextInt((int) roomSize), Tools.random.nextInt((int) roomSize)));
		return targets;
	}
	
	private static List<Target> getTargets() {
		List<Target> targets = new ArrayList<>();
		
		Target target1 = new Target(15, 50);
		
		Target target2 = new Target(50, 60);
		//target2.setIntensity(5);
		
		Target target3 = new Target(70, 20);
		//target3.setIntensity(300);
		
		targets.add(target1);
		targets.add(target2);
		//targets.add(target3);
		
		return targets;
	}

}
