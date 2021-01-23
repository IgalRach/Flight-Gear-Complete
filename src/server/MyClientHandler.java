package server;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import algorithm.*;
import matrix.Matrix;
import matrix.MatrixSearch;

public class MyClientHandler implements ClientHandler {

	//Solver<Searchable, String> solver;
	Solver solver;
	CacheManager<String, String> cm;


	public MyClientHandler() {
		solver = new SolverSearcher(new BFS<String>());
		cm = new FileCacheManager<>();
	}


	@Override
	public void handleClient(InputStream in, OutputStream out) {

		BufferedReader clientInput = new BufferedReader(new InputStreamReader(in));
		PrintWriter outToClient = new PrintWriter(out);

		try {
			String line;
			String[] stringMatrix;
			int rows = 0;
			int cols = 0;
			ArrayList<String[]> arr = new ArrayList<>();
			while ((line = clientInput.readLine())!=null && !line.equals("end") ) {
				stringMatrix = line.split(",");
				arr.add(stringMatrix);
			}
			rows = arr.size();
			cols = arr.get(0).length;
			int[][] matrix = new int[rows][cols];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					matrix[i][j] = Integer.parseInt(arr.get(i)[j]);
				}
			}
			String[] entrance = clientInput.readLine().split(",");
			Point entrancePoint = new Point(Integer.parseInt(entrance[0]), Integer.parseInt(entrance[1]));
			String[] exit = clientInput.readLine().split(",");
			Point exitPoint = new Point(Integer.parseInt(exit[0]), Integer.parseInt(exit[1]));


			Astar.Heuristic heuristic = new Astar.Heuristic() {
				@Override
				public double cost(State state, State goalState) {
					String start = cleanLine(String.valueOf(state.getState()));
					String[] tmp =start.split(",");

					double x1 = Integer.parseInt(tmp[2]);
					double y1 = Integer.parseInt(tmp[4]);
					String end = cleanLine(String.valueOf(goalState.getState()));
					tmp = end.split(",");
					double x2 = Integer.parseInt(tmp[2]);
					double y2 = Integer.parseInt(tmp[4]);
					return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1- y2, 2));
				}
			};



			Searcher searcher = new Astar(heuristic);

			solver = new SolverSearcher<>(searcher);

			Searchable problem = new MatrixSearch(
					new Matrix(matrix, new State<>(entrancePoint), new State<>(exitPoint)));
			String problemData = problem.getString();
			String solution;
			if (cm.doesSolutionExists(problemData)) {
				solution = cm.getSolution(problemData);
			} else {
				solution =(String)solver.solve(problem);
				cm.saveSolution(problemData, solution);
			}
			outToClient.println(solution);
			outToClient.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static String cleanLine(String line) {
		if (line != null) {
			return line.replace("[",",")
					.replace("=",",")
					.replace("]",",");

		}
		return null;
	}



}
