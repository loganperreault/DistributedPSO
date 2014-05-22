package driver;

import java.util.ArrayList;
import java.util.List;

import problem.Room;
import problem.Server;
import problem.Target;
import pso.Fitness;
import pso.FitnessCommunication;
import pso.FitnessTarget;
import pso.PSO;
import tools.Tools;
import evaluation.Evaluation;

public class Driver {
	
	static int numRobots = 8;
	static double roomSize = 100;
	static double maxSpeed = 1.0;
	static double degradeFactor = 0.95;
	static boolean mobile = false;
	static boolean animate = false;
	static boolean anneal = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// NOTE: had to randomly set bestLocations until bestFitness was greater than 0
		// NOTE: fitness has a falloff until it gets small enough, then imperceptible
		// NOTE: communication velocity update has an exponential dropoff when timesteps are up so a solution is maintained
		
		// NOTE: generated a figure using serverRange = 50, particleRange = 15, targets 1 and 2, focused on first particle, stopped a t = 128
		
		//manualTest();
		
		Evaluation eval;
		//eval.setTimesteps(128);
		
		int timesteps = 2000;
		int epochs = 1000;
		
		double avgFinalServerError = 0.0;
		String avgSolutionValue = "";
		String avgCommunicationError = "";
		String finalServerError = "";
		double[] solutionValue = new double[timesteps];
		double[] communicationError = new double[timesteps];
		double[] serverError = new double[timesteps];
		double[] serverErrorExample = new double[timesteps];
		for (int i = 0; i < epochs; i++) {
			System.out.println("EPOCH: "+i);
			Room room = getRandomRoom();
			if (i == 1)
				room.animate(false);
			eval = new Evaluation(room);
			eval.test(timesteps);
			avgSolutionValue += eval.getAverageSolutionValue()+"\t";
			avgCommunicationError += eval.getAverageCommunicationError()+"\t";
			finalServerError += eval.serverError[serverError.length-1]+"\t";
			avgFinalServerError += eval.serverError[serverError.length-1];
			for (int j = 0; j < timesteps; j++) {
				solutionValue[j] += eval.solutionValue[j];
				communicationError[j] += eval.communicationError[j];
				serverError[j] += eval.serverError[j];
				if (i==0)
					serverErrorExample[j] = eval.serverError[j];
			}
		}
		avgFinalServerError /= epochs;
		for (int i = 0; i < timesteps; i++) {
			solutionValue[i] /= epochs;
			communicationError[i] /= epochs;
			serverError[i] /= epochs;
		}
		String solutionValues = "";
		String communicationErrors = "";
		String serverErrors = "";
		String serverErrorExamples = "";
		for (int i = 0; i < timesteps; i++) {
			solutionValues += solutionValue[i]+"\t";
			communicationErrors += communicationError[i]+"\t";
			serverErrors += serverError[i]+"\t";
			serverErrorExamples += serverErrorExample[i]+"\t";
		}
		
		/*
		System.out.println("AVG SOLUTION VALUE\t"+avgSolutionValue);
		System.out.println("AVG COMMUNICATION ERROR\t"+avgCommunicationError);
		System.out.println();
		System.out.println("SOLUTION VALUES\t"+solutionValues);
		System.out.println("COMMUNICATION ERRORS\t"+communicationErrors);
		*/
		
		System.out.println("SERVER ERRORS\t"+finalServerError);
		System.out.println("AVERAGE ERROR\t"+avgFinalServerError);
		System.out.println("TIME SERIES AGGREGATE\t"+serverErrors);
		System.out.println("TIME SERIES SINGLE\t"+serverErrorExamples);
		
		
	}
	
	private static Room getRandomRoom() {
		Room room = new Room(roomSize);
		
		// create server object
		Server server = new Server(Tools.random.nextInt((int) roomSize), Tools.random.nextInt((int) roomSize));
		
		// add targets
		//int numTargets = Tools.random.nextInt(2) + 1;
		int numTargets = 1;
		List<Target> targets = new ArrayList<>(numTargets);
		for (int i = 0; i < numTargets; i++) {
			Target target = new Target(Tools.random.nextInt((int) roomSize), Tools.random.nextInt((int) roomSize));
			while (Tools.euclidean(server.position(), target.position()) < server.communicationRange) {
				target = new Target(Tools.random.nextInt((int) roomSize), Tools.random.nextInt((int) roomSize));
			}
			targets.add(target);
		}
		
		// Use mobile targets instead
		if (mobile) {
			for (Target target : targets)
				target.setVelocity(Tools.getRandomDouble(-0.1, 0.1), Tools.getRandomDouble(-0.1, 0.1));
			if (anneal)
				server.setDegradeFactor(degradeFactor);
		}
		
		// add noise
		int numNoise = Tools.random.nextInt(100);
		List<Target> noise = getRandomTargets(numNoise, 0, 1);
		for (Target t : noise)
			targets.add(t);
		
		Fitness fitnessEvaluation = new FitnessTarget(room, targets);
		Fitness communicationEvaluation = new FitnessCommunication(room, server);
		PSO swarm = new PSO(numRobots, 0, roomSize, maxSpeed, fitnessEvaluation, communicationEvaluation);
		
		if (mobile && anneal)
			swarm.setDegradeFactor(degradeFactor);
		
		server.addSwarm(swarm);
		
		room.addSwarm(swarm);
		room.addTargets(targets);
		room.addServer(server);
		
		return room;
	}
	
	private static void manualTest() {
		List<Target> targets = getTargets();
		
		// add noise
		List<Target> noise = getRandomTargets(100, 0, 1);
		for (Target t : noise)
			targets.add(t);
		
		// create server object
		Server server = new Server(90, 10);
		
		// Use mobile targets instead
		if (mobile) {
			targets = getMobileTargets();
			server.setDegradeFactor(degradeFactor);
		}
		
		// create the problem area
		Room room = new Room(roomSize);
		room.animate(animate);
		
		Fitness fitnessEvaluation = new FitnessTarget(room, targets);
		Fitness communicationEvaluation = new FitnessCommunication(room, server);
		PSO swarm = new PSO(numRobots, 0, roomSize, maxSpeed, fitnessEvaluation, communicationEvaluation);
		
		if (mobile)
			swarm.setDegradeFactor(degradeFactor);
		
		server.addSwarm(swarm);
		
		room.addSwarm(swarm);
		room.addTargets(targets);
		room.addServer(server);
		
		Evaluation eval = new Evaluation(room);
		eval.test(1000);
		
		System.out.println("        VALID TIMESTEPS: "+eval.getValidTimesteps());
		System.out.println("     TIMESTEP CONVERGED: "+eval.getTimestepConverged());
		System.out.println("      CONVERGENCE VALUE: "+eval.getConvergenceValue());
		System.out.println("     AVG SOLUTION VALUE: "+eval.getAverageSolutionValue());
		System.out.println("AVG COMMUNICATION ERROR: "+eval.getAverageCommunicationError());
		System.out.println();
	}
	
	private static List<Target> getRandomTargets(int number, double minStrength, double maxStrength) {
		List<Target> targets = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			Target target = new Target(Tools.random.nextInt((int) roomSize), Tools.random.nextInt((int) roomSize));
			target.setIntensity(Tools.getRandomDouble(minStrength, maxStrength));
			target.setVisibleRadius(1);
			targets.add(target);
		}
		return targets;
	}
	
	private static List<Target> getRandomTargets(int number, double strength) {
		List<Target> targets = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			Target target = new Target(Tools.random.nextInt((int) roomSize), Tools.random.nextInt((int) roomSize));
			target.setIntensity(strength);
			targets.add(target);
		}
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
		//targets.add(target2);
		//targets.add(target3);
		
		return targets;
	}
	
	private static List<Target> getMobileTargets() {
		List<Target> targets = new ArrayList<>();
		
		Target target1 = new Target(10, 40);
		target1.setVelocity(0.1, 0.05);
		
		targets.add(target1);
		
		return targets;
	}

}
