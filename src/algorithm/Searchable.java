package algorithm;

import java.util.ArrayList;

public interface Searchable<T> {
	public State<T> getInitialState();

	public State<T> getGoalState();

	public ArrayList<State<T>> getAllPossibleStates(State<T> s);

	public <Solution> Solution backtrace(State goal);

	public String getString();

	<Solution> Solution backTrace(State goalState);
}
