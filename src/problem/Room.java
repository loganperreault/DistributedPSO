package problem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pso.PSO;
import pso.Particle;
import tools.Tools;
import visualizer.Node;
import visualizer.Shape;
import visualizer.Visualize;

public class Room {
	
	double width, height;
	public List<Target> targets = new ArrayList<>();
	public List<Server> servers = new ArrayList<>();
	public PSO swarm;
	Visualize vis;
	int visualizerSize = 800;
	Map<Particle, Node> particleMap = new HashMap<>();
	Map<Target, Node> targetMap = new HashMap<>();
	Map<Server, Node> serverMap = new HashMap<>();
	int timestep = 0;
	boolean draw = false;
	int pause = 100;
	
	public Room(double size) {
		initializeArea(size);
	}
	
	private void initializeArea(double size) {
		this.width = size;
		this.height = size;
		vis = new Visualize("Area", Tools.toInt(width), Tools.toInt(height));
		vis.setScale(visualizerSize/size);
		if (draw)
			vis.draw();
	}
	
	public void addSwarm(PSO swarm) {
		this.swarm = swarm;
		for (int i = 0; i < swarm.particles.size(); i++) {
			Particle particle = swarm.get(i);
			Node node = new Node(Tools.toInt(particle.getPosition(0)), Tools.toInt(particle.getPosition(1)), 2, Color.BLUE);
			node.setShape(Shape.TRIANGLE);
			if (i == 0) {
				node.setCircledCenter(Tools.toInt(particle.communicationRange));
				node.setShape(Shape.TRIANGLE2);
			}
			particleMap.put(particle, node);
			vis.addNode(node);
		}
	}
	
	public void addTarget(Target target) {
		if (target.x > width || target.x < 0 ||
			target.y > height || target.y < 0) {
			System.out.println("ERROR: Target is out of bounds");
			System.exit(1);
		}
		targets.add(target);
		Node node = new Node(Tools.toInt(target.x), Tools.toInt(target.y), target.visibleRadius, target.color);
		targetMap.put(target, node);
		vis.addNode(node);
	}
	
	public void addTargets(List<Target> targets) {
		for (Target target : targets)
			addTarget(target);
	}
	
	public void addServer(Server server) {
		if (server.x > width || server.x < 0 ||
			server.y > height || server.y < 0) {
			System.out.println("ERROR: Server is out of bounds");
			System.exit(1);
		}
		servers.add(server);
		Node node = new Node(server.x, server.y, 3, server.color);
		node.setShape(Shape.SQUARE);
		node.setCircledCenter(Tools.toInt(server.communicationRange));
		serverMap.put(server, node);
		vis.addNode(node);
	}
	
	public void addServer(List<Server> servers) {
		for (Server server : servers)
			addServer(server);
	}
	
	public void runIterations(int timesteps) {
		for (int i = 0; i < timesteps; i++) {
			runIteration();
		}
	}
	
	public void runIteration() {
		timestep++;
		swarm.runIteration();
		for (Target target : targets) {
			target.runIteration();
			double correctX = correct(target.x, 0, width);
			double correctY = correct(target.y, 0, height);
			if (correctX != 0) {
				target.x += correctX;
				target.vx *= -1;
			}
			if (correctY != 0) {
				target.y += correctY;
				target.vy *= -1;
			}
		}
		for (Server server : servers)
			server.runIteration();
		updateArea();
		if (draw) {
			draw();
			try {	
				Thread.sleep(pause);	
			} catch (InterruptedException e) {	e.printStackTrace();	}
		}
	}
	
	public double correct(double val, double min, double max) {
		if (val > max) {
			return 2*(max - val);
		} else if (val < min) {
			return 2*(min - val);
		} else {
			return 0;
		}
	}
	
	public int getTimestep() {
		return timestep;
	}
	
	private void updateArea() {
		//swarm.get(0).printVelocity();
		for (Particle particle : swarm.particles) {
			Node node = particleMap.get(particle);
			node.setPosition(Tools.toInt(particle.getPosition(0)), Tools.toInt(particle.getPosition(1)));
		}
		for (Target target : targets) {
			Node node = targetMap.get(target);
			node.setPosition(Tools.toInt(target.x), Tools.toInt(target.y));
		}
		for (Server server : servers) {
			Node node = serverMap.get(server);
			node.setPosition(Tools.toInt(server.x), Tools.toInt(server.y));
		}
	}
	
	public void draw() {
		if (draw) {
			vis.draw();
			vis.removeAll();
			vis.updateUI();
			vis.repaint();
		}
	}
	
	public void animate(boolean draw) {
		this.draw = draw;
	}

}
