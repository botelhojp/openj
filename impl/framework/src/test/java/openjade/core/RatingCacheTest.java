package openjade.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import openjade.ontology.Rating;

import org.junit.Test;

public class RatingCacheTest {

	@Test
	public void testTimeCache() {
		try {
			new RatingCache(1, 0);
			fail("excetion not throw");
		} catch (Exception e) {
			assertEquals("Invalid iteration", e.getMessage());
		}
	}

	@Test
	public void testAddInvalid1() {
		try {
			RatingCache mc = new RatingCache(1, 1);
			mc.add(newRating(2, "a", 2.0F));
			fail("exception don't throwed");
		} catch (Exception e) {
			assertEquals("Invalid iteration [2]. Must be less than or equal to [1]", e.getMessage());
		}
	}

	@Test
	public void testAddInvalid2() {
		try {
			RatingCache mc = new RatingCache(10, 2);
			mc.setIteration(10);
			mc.add(newRating(8, "a", 2.0F));
			fail("exception don't throwed");
		} catch (Exception e) {
			assertEquals("Invalid iteration [8].Must be greater than [8]", e.getMessage());
		}
	}

	@Test
	public void testSize_1() {
		try {
			RatingCache mc = new RatingCache(1, 1);
			mc.setIteration(1);

			mc.add(newRating(1, "a", 2.0F));
			mc.add(newRating(1, "a", 2.0F));
			mc.add(newRating(1, "a", 2.0F));

			assertEquals(3, mc.size());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSize_2() {
		try {
			RatingCache mc = new RatingCache(1, 2);
			mc.setIteration(1);

			mc.add(newRating(1, "a", 2.0F));
			mc.add(newRating(1, "a", 2.0F));
			mc.add(newRating(1, "a", 2.0F));

			mc.setIteration(2);

			mc.add(newRating(1, "a", 2.0F));
			mc.add(newRating(2, "a", 2.0F));
			mc.add(newRating(2, "a", 2.0F));

			assertEquals(6, mc.size());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testClean() {
		try {
			RatingCache mc = new RatingCache(5, 3);

			mc.add(newRating(3, "a", 2.0F));
			mc.add(newRating(3, "a", 2.0F));

			mc.add(newRating(4, "a", 2.0F));
			mc.add(newRating(4, "a", 2.0F));

			mc.add(newRating(5, "a", 2.0F));
			mc.add(newRating(5, "a", 2.0F));

			assertEquals(6, mc.size());

			mc.setIteration(6);
			assertEquals(4, mc.size());
			mc.setIteration(7);
			assertEquals(2, mc.size());
			mc.setIteration(8);
			assertEquals(0, mc.size());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCalcException() {
		RatingCache mc = new RatingCache(1, 3);

		mc.add(newRating(1, "a", 2.0F));
		mc.add(newRating(1, "b", 2.0F));

		assertNull(mc.getValue(5, "a"));
	}

	@Test
	public void testCalcException2() {
		try {
			RatingCache mc = new RatingCache(2, 3);

			mc.add(newRating(1, "a", 10.0F));
			mc.add(newRating(1, "a", 20.0F));
			mc.add(newRating(1, "a", 60.0F));

			assertEquals(mc.getValue(1, "b"), 0.0F, 0F);
			fail("");
		} catch (Exception e) {
			assertEquals("Term [b] not find for iteration [1]", e.getMessage());
		}
	}

	@Test
	public void testCalc01() {
		try {
			RatingCache mc = new RatingCache(1, 3);
			mc.add(newRating(1, "a", 2.0F));
			assertEquals(mc.getValue(1, "a"), 2.0F, 0F);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCalc02() {
		try {
			RatingCache mc = new RatingCache(2, 3);

			mc.add(newRating(1, "a", 10.0F));
			mc.add(newRating(1, "a", 20.0F));
			mc.add(newRating(1, "a", 60.0F));

			mc.add(newRating(2, "a", 1.0F));
			mc.add(newRating(2, "b", 2.0F));
			mc.add(newRating(2, "b", 6.0F));

			assertEquals(mc.getValue(1, "a"), 30.0F, 0F);
			assertEquals(mc.getValue(2, "a"), 1.0F, 0F);
			assertEquals(mc.getValue(2, "b"), 4.0F, 0F);
			
			
			
			assertEquals(15.07256031036377, mc.getValue(), 0F);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetValue() {
		try {
			RatingCache mc = new RatingCache(1, 3);

			mc.setIteration(1);
			mc.add(newRating(1, "a", 3.0F));
			mc.add(newRating(1, "b", 7.0F));
			assertEquals(mc.getValue(), 5.0F, 0F);
			
			mc.setIteration(2);
			mc.add(newRating(2, "a", 4.0F));
			mc.add(newRating(2, "b", 10.0F));
			assertEquals(5.762093544006348, mc.getValue(), 0F);

			mc.setIteration(3);
			mc.add(newRating(3, "a", 0F));
			mc.add(newRating(3, "b", 0F));
			assertEquals(3.4758384227752686F, mc.getValue(), 0F);

			//sera desconsiderados a iteracao 1
			mc.setIteration(4);
			mc.add(newRating(4, "a", 4F));
			mc.add(newRating(4, "b", 0F));
			assertEquals(2.577038526535034F, mc.getValue(), 0F);

			

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	
	@Test
	public void testIsFull() {
		RatingCache mc = new RatingCache(1, 4);
		mc.add(newRating(1, "a", 10.0F));
		assertFalse(mc.isCompleted());

		mc.setIteration(2);
		mc.add(newRating(2, "a", 10.0F));
		assertFalse(mc.isCompleted());

		mc.setIteration(3);
		mc.add(newRating(3, "a", 10.0F));
		assertFalse(mc.isCompleted());

		mc.setIteration(4);
		mc.add(newRating(4, "a", 10.0F));
		assertTrue(mc.isCompleted());
	}

	private Rating newRating(int time, String trustmodel, float value) {
		Rating rating = new Rating();
		rating.setIteration(time);
		rating.setTerm(trustmodel);
		rating.setValue(value);
		return rating;
	}

}
