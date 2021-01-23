package algorithm;

import java.awt.Point;
import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class CommonSearcher<Solution> implements Searcher {

	protected PriorityQueue<State<Point>> openList;
	protected int evluateNodes; //checked

	protected State popOpenList() {
		evluateNodes++;
		return openList.poll();
	}

	public CommonSearcher() {
		this.openList = new PriorityQueue<>(((o1, o2) -> (int) (o1.getCost() - o2.getCost())));
		evluateNodes = 0;
	}

	@Override
	public abstract Solution search(Searchable s);

}
