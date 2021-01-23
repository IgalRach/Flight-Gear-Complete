package models;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Observable;

import algorithm.State;
import matrix.Matrix;

public class MatrixModel extends Observable {

	Matrix resultMatrix;
	double startX;
	double startY;
	PrintWriter outToSolver;
	BufferedReader in;
	String shortestPath;

	public Matrix getMatrix() {
		return resultMatrix;
	}

	public void buildMatrix(String[] csv, int rows, int cols) {
		startX = Double.parseDouble(csv[0]);
		startY = Double.parseDouble(csv[1]);
		csv = Arrays.copyOfRange(csv, 3, csv.length);
		int[][] mat = new int[rows][cols];
		int c = 0;
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				mat[i][j] = Integer.parseInt(csv[c++]);
			}
		}
		resultMatrix = new Matrix(mat, new State<>(new Point(0, 0)), null);
		setChanged();
		notifyObservers("matrix");
	}

	public double getStartCooX() {
		return startX;
	}

	public double getStartCooY() {
		return startY;
	}

	public void connect(String ip, int port) {
		Socket server = null;
		try {
			server = new Socket(ip, port);
			in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			outToSolver = new PrintWriter(server.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client connected to the solver.");
	}

	public void setStartPosition(Point start) {
		resultMatrix.setEntrance(new State<>(start));
	}

	public void setExitPosition(Point exit) {
		resultMatrix.setExit(new State<>(exit));
	}

	public String getShortestPath() {
		return shortestPath;
	}

	public void sendToServer(String[] output) {
		sendProblemToServer(resultMatrix);
		for (String o : output) {
			outToSolver.println(o);
		}
		outToSolver.flush();
	}

	public void getPathFromServer() {
		if (outToSolver == null) {
			System.out.println("you are not connected to a solver.");
			setChanged();
			notifyObservers("not connected");
			return;
		}
		String entrance = resultMatrix.getEntrance().getState().x + "," + resultMatrix.getEntrance().getState().y;
		String exit = resultMatrix.getExit().getState().x + "," + resultMatrix.getExit().getState().y;
		String[] totalOutput = { "end", entrance, exit };
		sendToServer(totalOutput);
		try {
			shortestPath = in.readLine();
			setChanged();
			notifyObservers("shortest path");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendProblemToServer(Matrix matrix) {
		int i, j;
		for (i = 0; i < matrix.getData().length; i++) {
			for (j = 0; j < matrix.getData()[0].length-1; j++) {
				outToSolver.print(matrix.getData()[i][j] + ",");
			}
			outToSolver.println(matrix.getData()[i][j] + ",");
		}
	}

}
