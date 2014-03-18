package graph;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Node {

	// initialize parameters
	private Point2D.Double location;
	private Color color = Color.BLUE;

	/**
	 * Create a new node at a specific location in space.
	 * 
	 * @param location	The 2D location in virtual space.
	 */
	public Node(Point2D.Double location) {
		this.location = location;
	}

	/**
	 * @return	The location of the node in virtual 2D space.
	 */
	public Point2D.Double getLocation() {
		return location;
	}
	
	/**
	 * @return	The virtual location of the node in list form.
	 */
	public List<Double> getLocationVector() {
		List<Double> vector = new ArrayList<Double>(2);
		vector.add(location.x);
		vector.add(location.y);
		return vector;
	}

	/**
	 * @param location	The location of the node in virtual 2D space.
	 */
	public void setLocation(Point2D.Double location) {
		this.location = location;
	}
	
	/**
	 * @param color	The color to display the node as in the visualizer.
	 */
	public void setColor(Color color) {
		double alpha = getAlpha();
		this.color = color;
		setAlpha(alpha);
	}
	
	/**
	 * @return	The color that the node is displayed with in the visualizer.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * @param alpha	The transparency of the node in the visualizer.
	 */
	public void setAlpha(double alpha) {
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
	}
	
	/**
	 * @return	The transparency of the node in the visualizer.
	 */
	public double getAlpha() {
		return (double)Math.round(((double)color.getAlpha()/255) * 100) / 100;
	}

}
