package application.model.algorithms;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

// Thanks to https://stackoverflow.com/a/5669034
public class RandomSet<E> extends AbstractSet<E> {

	private List<E> dta = new ArrayList<>();
	private Map<E, Integer> idx = new HashMap<>();

	public RandomSet() {}

	public RandomSet(Collection<E> items) {
		for (E item : items) {
			idx.put(item, dta.size());
			dta.add(item);
		}
	}

	@Override
	public boolean add(E item) {
		if (idx.containsKey(item)) {
			return false;
		}
		idx.put(item, dta.size());
		dta.add(item);
		return true;
	}

	public E get(int id) {
		return dta.get(id);
	}

	public E getRandom(Random rnd) {
		if (dta.isEmpty()) {
			return null;
		}
		int id = rnd.nextInt(dta.size());
		return get(id);
	}

	@Override
	public Iterator<E> iterator() {
		return dta.iterator();
	}

	public E pollRandom(Random rnd) {
		if (dta.isEmpty()) {
			return null;
		}
		int id = rnd.nextInt(dta.size());
		return removeAt(id);
	}

	@Override
	public boolean remove(Object item) {
		Integer id = idx.get(item);
		if (id == null) {
			return false;
		}
		removeAt(id);
		return true;
	}

	public E removeAt(int id) {
		if (id >= dta.size()) {
			return null;
		}
		E res = dta.get(id);
		idx.remove(res);
		E last = dta.remove(dta.size() - 1);
		if (id < dta.size()) {
			idx.put(last, id);
			dta.set(id, last);
		}
		return res;
	}

	@Override
	public int size() {
		return dta.size();
	}
}
