package matrix;

import java.awt.Point;
import java.util.ArrayList;

import algorithm.Searchable;
import algorithm.State;

public class MatrixSearch implements Searchable<Point> {

	Matrix matrix;

	public MatrixSearch(Matrix matrix) {
		this.matrix = matrix;
	}

	@Override
	public State<Point> getInitialState() {
		return matrix.entrance;
	}

	@Override
	public ArrayList<State<Point>> getAllPossibleStates(State<Point> s) {
		ArrayList<State<Point>> successors = new ArrayList<>();
		isInMatrix((int) s.getState().getX() + 1, (int) s.getState().getY(), s, successors);
		isInMatrix((int) s.getState().getX() - 1, (int) s.getState().getY(), s, successors);
		isInMatrix((int) s.getState().getX(), (int) s.getState().getY() + 1, s, successors);
		isInMatrix((int) s.getState().getX(), (int) s.getState().getY() - 1, s, successors);
		return successors;
	}

	private void isInMatrix(int row, int column, State<Point> s, ArrayList<State<Point>> successors) {
		if (row >= 0 && row < matrix.rows && column >= 0 && column < matrix.cols) {
			State<Point> state = new State<>(new Point(row, column));
			state.setParentState(s);
			state.setCost(s.getCost() + matrix.getData()[row][column]);
			successors.add(state);
		}

	}

	@Override
	public State<Point> getGoalState() {
		return matrix.exit;
	}

	@Override
	public String backtrace(State goal) {
		State<Point> current = goal;
		State<Point> parentState;
		String directions = "";
		while (!current.equals(this.getInitialState())) {
			parentState = current.getParentState();
			if (current.getState().getY() == parentState.getState().getY() + 1)
				directions = "Right," + directions;
			if (current.getState().getY() == parentState.getState().getY() - 1)
				directions = "Left," + directions;
			if (current.getState().getX() == parentState.getState().getX() + 1)
				directions = "Down," + directions;
			if (current.getState().getX() == parentState.getState().getX() - 1)
				directions = "Up," + directions;

			current = current.getParentState();
		}
		return directions.substring(0, directions.length() - 1);
	}

	@Override
	public String getString() {
		StringBuilder sb = new StringBuilder();
		int[][] matrixx = matrix.getData();
		int cols = matrix.getCols();
		int rows = matrix.getRows();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				sb.append(matrixx[i][j] + ",");
			}
		}
		sb.append(matrix.getEntrance().getState().getX() + "," + matrix.getEntrance().getState().getY() + ",");
		sb.append(matrix.getExit().getState().getX() + "," + matrix.getExit().getState().getY());
		return sb.toString();
	}

	@Override
	public <Solution> Solution backTrace(State goalState) {
		return null;
	}

}
