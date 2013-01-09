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

	private int cacheIteration;
	private int minIteration;
	private int iteration;

	private Hashtable<Integer, List<Rating>> cache = new Hashtable<Integer, List<Rating>>();

	public RatingCache(int _initialIteration, int _sizeCache) {
		setSizeCache(_sizeCache);
		setIteration(_initialIteration);
		minIteration = (_initialIteration - _sizeCache);
		minIteration = (minIteration <= 1) ? 1 : minIteration;
	}

	public void add(Rating rating) {
		if (rating.getIteration() > (this.iteration)) {
			throw new OpenJadeException("Invalid iteration [" + rating.getIteration() + "]. Must be less than or equal to [" + this.iteration + "]");
		}
		if (rating.getIteration() <= (this.iteration - this.cacheIteration)) {
			throw new RuntimeCryptoException("Invalid iteration [" + rating.getIteration() + "].Must be greater than [" + (this.iteration - this.cacheIteration) + "]");
		}
		List<Rating> ratings = cache.get(rating.getIteration());
		if (ratings == null) {
			ratings = new ArrayList<Rating>();
			cache.put(rating.getIteration(), ratings);
		}
		ratings.add(rating);
	}

	public void setIteration(int _iteration) {
		iteration = _iteration;
		clean();
	}

	private void clean() {
		while (minIteration <= (iteration - cacheIteration)) {
			cache.remove(minIteration);
			minIteration++;
		}
	}

	private void setSizeCache(int _timeCache) {
		if (_timeCache <= 0) {
			throw new RuntimeCryptoException("Invalid iteration");
		}
		this.cacheIteration = _timeCache;
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
		return (iteration - minIteration) >= (cacheIteration - 1);
	}

	public int getMin() {
		return this.minIteration;
	}
}
