package driver;

import problem.Area;
import problem.Target;
import pso.Fitness;
import pso.PSO;
import visualizer.Visualize;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int numRobots = 5;
		double minValue = 0;
		double maxValue = 100;
		
		Target target = new Target(15, 50);
		
		Area area = new Area((maxValue - minValue));
		
		Fitness fitnessEvaluation = new Fitness(area);
		PSO swarm = new PSO(numRobots, minValue, maxValue, fitnessEvaluation);
		
		area.addSwarm(swarm);
		area.addTarget(target);
		
		area.runIterations(10);
	}

}
