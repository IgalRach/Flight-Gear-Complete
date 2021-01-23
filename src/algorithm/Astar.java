package algorithm;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import algorithm.CommonSearcher;
import algorithm.Searchable;
import algorithm.State;

public class Astar<Solution,heuristic> extends CommonSearcher<Solution> {
    // Inner interface - Heuristic.
    public interface Heuristic {
        public double cost(State<Point> s, State<Point> goalState);
    }

    // Data member.
    Heuristic heuristic;

    // CTOR.
    public Astar(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Solution search(Searchable s) {
        openList.add(s.getInitialState());
        HashSet<State<Point>> closedSet = new HashSet<>();

        while (openList.size() > 0) {
            State state = popOpenList();

            closedSet.add(state);

            ArrayList<State> successors = s.getAllPossibleStates(state);
            state.setCost(state.getCost() + heuristic.cost(state, s.getGoalState()));

            if (state.equals(s.getGoalState())) {
                return (Solution) s.backtrace(state);
            }

            for (State successor : successors) {
                successor.setCost(successor.getCost() + heuristic.cost(successor, s.getGoalState()));
                if (!closedSet.contains(successor) && (!openList.contains(successor))) {
                    successor.setParentState(state);
                    openList.add(successor);
                } else if ((state.getCost() + (successor.getCost() - successor.getParentState().getCost())) < successor.getCost()) {
                    if (openList.contains(successor)) {
                        successor.setParentState(state);
                    } else {
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
    public int getNumberOfNodesEvaluated() {
        return evluateNodes;
    }
}