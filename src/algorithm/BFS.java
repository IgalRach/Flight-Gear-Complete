package algorithm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

 public class BFS<Solution> extends CommonSearcher<Solution> {

	@Override
	public Solution search(Searchable s) {
		openList.add(s.getInitialState());
		HashSet<State<Point>> closedSet = new HashSet<>();

		while (openList.size() > 0) {
			State<Point> state = (State<Point>)  popOpenList();
			closedSet.add(state);

			if (state.equals(s.getGoalState())) {
				return (Solution) s.backtrace(state);
			}

			ArrayList<State<Point>> succesors = s.getAllPossibleStates(state);
			for (State<Point> successor : succesors) {
				if (!closedSet.contains(successor) && !openList.contains(successor)) {
					successor.setParentState(state);
					openList.add(successor);
				} else if ((state.getCost() + (successor.getCost() - successor.getParentState().getCost())) < successor.getCost()){
					if(openList.contains(successor)){
						successor.setParentState(state);
					}
					else{
						successor.setParentState(state);
						closedSet.remove(successor);
						openList.add(successor);
					}
				}
			}
		}
		return (Solution) s.backtrace(s.getGoalState());
	}

	 @Override
	 public int getNumberOfNodesEvaluated() { return evluateNodes; }
 }