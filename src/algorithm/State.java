package algorithm;

public class State<T> {

	public T state;
	public double cost;
	public State<T> parentState;

	public State(T state) {
		this.state = state;
	}

	public boolean hasParentState() {
		return parentState != null;
	}

	public T getState() {
		return state;
	}

	public void setState(T state) {
		this.state = state;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public State<T> getParentState() {
		return parentState;
	}

	public void setParentState(State<T> parentState) {
		this.parentState = parentState;
	}

	@Override
	public boolean equals(Object o) {
		return this.state.equals(((State<T>) o).getState());
	}

	@Override
	public int hashCode() {
		return state.hashCode();
	}

	public boolean contains(Object o) {
		System.out.println("Hello");
		return false;
	}
}