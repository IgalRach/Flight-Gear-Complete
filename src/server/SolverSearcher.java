package server;

import algorithm.Searchable;
import algorithm.Searcher;

public class SolverSearcher<Problem, Solution> implements Solver<Problem, Solution> {

	Searcher<Solution> searcher;

	public SolverSearcher(Searcher<Solution> searcher) {
		this.searcher = searcher;
	}

	@Override
	public Solution solve(Problem p) {
		return (Solution) searcher.search((Searchable) p);
	}

}
