package server;

public interface CacheManager<Problem, Solution> {
	public boolean doesSolutionExists(Problem problem);

	public Solution getSolution(Problem problem);

	public void saveSolution(Problem problem, Solution solution);
}
