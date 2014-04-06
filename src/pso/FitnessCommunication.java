package pso;

import java.util.ArrayList;
import java.util.List;

import problem.Room;
import problem.Server;
import problem.Target;
import tools.Tools;

public class FitnessCommunication extends Fitness {
	
	List<Server> servers;
	
	public FitnessCommunication(Room room, List<Server> servers) {
		super(room);
		this.servers = servers;
	}
	
	public FitnessCommunication(Room room, Server server) {
		super(room);
		if (this.servers == null)
			this.servers = new ArrayList<>();
		servers.add(server);
	}
	
	public double evaluate(Particle particle) {
		double intensity = 0.0;
		for (Server server : servers) {
			double distance = Tools.euclidean(particle.position, server.position());
			if (distance <= particle.communicationRange) {
				intensity = 1.0;
				// TODO: this transmittion should be done elsewhere
				System.out.println("TRANSMIT");
				server.transmitSolution(particle.getGlobalBestPosition(), particle.getGlobalBestFitness());
			}
		}
		return intensity;
	}

}
