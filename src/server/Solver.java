package server;

public interface Solver<Problem, Solution> {
	public Solution solve(Problem p);
}