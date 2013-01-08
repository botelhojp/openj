package openjade.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import openjade.ontology.Rating;

import org.bouncycastle.crypto.RuntimeCryptoException;

public class RatingCache implements Serializable {

	private static final long serialVersionUID = 1L;

	private int cacheTime;
	private int minTime;
	private int iteration;

	private Hashtable<Integer, List<Rating>> cache = new Hashtable<Integer, List<Rating>>();

	public RatingCache(int _currentTime, int _timeCache) {
		setTimeCache(_timeCache);
		setCurrentTime(_currentTime);
		minTime = (_currentTime - _timeCache);
		minTime = (minTime <= 1) ? 1 : minTime;
	}

	public void add(Rating sa) {
		if (sa.getIteration() > (this.iteration)) {
			throw new RuntimeCryptoException("Invalid iteration [" + sa.getIteration() + "]. Must be less than or equal to [" + this.iteration + "]");
		}
		if (sa.getIteration() <= (this.iteration - this.cacheTime)) {
			throw new RuntimeCryptoException("Invalid iteration [" + sa.getIteration() + "].Must be greater than [" + (this.iteration - this.cacheTime) + "]");
		}
		List<Rating> list = cache.get(sa.getIteration());
		if (list == null) {
			list = new ArrayList<Rating>();
			cache.put(sa.getIteration(), list);
		}
		list.add(sa);
	}

	public void setCurrentTime(int _currentTime) {
		iteration = _currentTime;
		clean();
	}

	private void clean() {
		while (minTime <= (iteration - cacheTime)) {
			cache.remove(minTime);
			minTime++;
		}
	}

	private void setTimeCache(int _timeCache) {
		if (_timeCache <= 0) {
			throw new RuntimeCryptoException("Invalid iteration");
		}
		this.cacheTime = _timeCache;
	}

	public int size() {
		int size = 0;
		Enumeration<List<Rating>> elements = this.cache.elements();
		while (elements.hasMoreElements()) {
			List<Rating> list = (List<Rating>) elements.nextElement();
			size += list.size();
		}
		return size;
	}

	public Float getValue() {
		float count = 0.0F;
		float sum = 0.0F;
		Enumeration<Integer> keys = cache.keys();
		while (keys.hasMoreElements()) {
			Integer key = (Integer) keys.nextElement();
			List<Rating> list = cache.get(key);
			for (Rating satisfactionAction : list) {
				count++;
				sum += satisfactionAction.getValue();
			}
			if (count == 0.0F) {
				throw new RuntimeException("Empty Cache");
			}
			return (sum == 0.0F) ? 0.0F : sum / count;
		}
		return null;
	}

	public Float getValue(int time, String model) {
		List<Rating> list = cache.get(time);
		if (list == null)
			return null;
		float count = 0.0F;
		float sum = 0.0F;
		for (Rating satisfactionAction : list) {
			if (satisfactionAction.getTerm().equals(model)) {
				count++;
				sum += satisfactionAction.getValue();
			}
		}
		if (count == 0.0F) {
			throw new RuntimeException("Term [" + model + "] not find for iteration [" + time + "]");
		}
		return (sum == 0.0F) ? 0.0F : sum / count;
	}

	public boolean isCompleted() {
		return (iteration - minTime) >= (cacheTime - 1);
	}

	public int getMin() {
		return this.minTime;
	}
}
