package visualizer;

import java.awt.Color;

public class Node {
	
	protected Color color = Color.BLACK;
	protected int x, y;
	protected int size = 5;
	protected Shape shape = Shape.CIRCLE;
	protected int circledRadius = 0;
	
	public Node(int x, int y) {
		initialize(x, y, size, color);
	}
	
	public Node(int x, int y, int size) {
		initialize(x, y, size, color);
	}
	
	public Node(int x, int y, Color color) {
		initialize(x, y, size, color);
	}
	
	public Node(int x, int y, int size, Color color) {
		initialize(x, y, size, color);
	}
	
	private void initialize(int x, int y, int size, Color color) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
	public void setCircledCenter(int radius) {
		this.circledRadius = radius;
	}

}
