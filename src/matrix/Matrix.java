package matrix;

import java.awt.Point;

import algorithm.State;

public class Matrix {
	public int[][] data;
	public State<Point> entrance;
	public State<Point> exit;
	public int rows;
	public int cols;

	public Matrix(int[][] data, State<Point> entrance, State<Point> exit) {
		this.data = data;
		this.entrance = entrance;
		this.exit = exit;
		this.cols = data[0].length;
		this.rows = data.length;
	}

	public int getRows() {
		return data.length;
	}

	public int getCols() {
		return data[0].length;
	}

	public void setEntrance(State<Point> entrance) {
		this.entrance = entrance;
	}

	public State<Point> getEntrance() {
		return entrance;
	}

	public void setExit(State<Point> exit) {
		this.exit = exit;
	}

	public State<Point> getExit() {
		return exit;
	}

	public int[][] getData() {
		return data;
	}

}
