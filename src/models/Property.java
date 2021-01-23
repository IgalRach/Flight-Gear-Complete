package models;

import java.util.Observable;
import java.util.Observer;

public class Property<V> extends Observable implements Observer {

	V v;

	public void set(V v) {
		this.v = v;
		setChanged();
		notifyObservers();
	}

	public V get() {
		return v;
	}

	public void bindTo(Property<V> p) {
		p.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		@SuppressWarnings("unchecked")
		Property<V> other = (Property<V>) o;
		if (other.v != v || v == null)
			this.set(other.v);
	}

}
