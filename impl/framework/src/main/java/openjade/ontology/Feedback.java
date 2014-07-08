package openjade.ontology;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
 * Protege name: Feedback
 * 
 * @author ontology bean generator
 * @version 2014/07/8, 11:38:13
 */
public class Feedback implements Concept {

	private static final long serialVersionUID = 1L;
	/**
	 * Protege name: rating
	 */
	private List rating = new ArrayList();

	public void addRating(Rating elem) {
		List oldList = this.rating;
		rating.add(elem);
	}

	public boolean removeRating(Rating elem) {
		List oldList = this.rating;
		boolean result = rating.remove(elem);
		return result;
	}

	public void clearAllRating() {
		List oldList = this.rating;
		rating.clear();
	}

	public Iterator getAllRating() {
		return rating.iterator();
	}

	public List getRating() {
		return rating;
	}

	public void setRating(List l) {
		rating = l;
	}

}
