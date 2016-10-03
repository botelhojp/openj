package openjade.dossier;

import static openjade.util.OpenJadeUtil.md5;
import static openjade.util.OpenJadeUtil.makeRating;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import openjade.composite.DossierModel;
import openjade.ontology.Rating;
import openjade.util.OpenJadeUtil;

public class DossierModelTest {
	
	DossierModel dm = new DossierModel();
	
	@Test
	public void insert1() throws Exception {
		Rating rt = OpenJadeUtil.makeRating("a1", "a2", 0, "great");
		String hashRoot = md5(rt);
		dm.insert(rt);
		assertEquals(1, dm.getRatings().size());
		assertEquals(0, rt.getIndex());
		assertNotNull(dm.getTree());
		assertNotNull(dm.getTree().getNode());
		assertEquals(hashRoot, dm.getTree().getNode().getValue());
		assertEquals(hashRoot, dm.getNode(hashRoot).getValue());
	}
	
	@Test
	public void insert2() throws Exception {
		Rating rt1 = makeRating("a1", "a2", 1, "great");
		Rating rt2 = makeRating("a1", "a2", 2, "great");
		dm.insert(rt1);
		dm.insert(rt2);
		String hashRoot = md5(md5(rt1) + md5(rt2));
		assertEquals(2, dm.getRatings().size());
		assertEquals(hashRoot, dm.getTree().getNode().getValue());
	}
	
	@Test
	public void insertImpar() throws Exception {
		Rating rt1 = makeRating("a1", "a2", 1, "great");
		Rating rt2 = makeRating("a1", "a2", 2, "great");
		Rating rt3 = makeRating("a1", "a2", 3, "great");
		dm.insert(rt1);
		dm.insert(rt2);
		dm.insert(rt3);
		
		String hashRoot = md5(md5(md5(rt1) + md5(rt2)) + md5(md5(rt3) + md5(rt3)));
		assertEquals(md5(rt3), dm.getNode(md5(rt3)).getValue());
		assertEquals(3, dm.getRatings().size());
		assertEquals(hashRoot, dm.getTree().getNode().getValue());
	}
	
	
	@Test
	public void valid() throws Exception {
		Rating rt1 = makeRating("a1", "a2", 1, "great");
		Rating rt2 = makeRating("a1", "a2", 2, "great");
		Rating rt3 = makeRating("a1", "a2", 3, "great");
		Rating rt4 = makeRating("a1", "a2", 4, "great");
		Rating rt5 = makeRating("a1", "a2", 5, "great");
		
		dm.insert(rt1);
		dm.insert(rt2);
		dm.insert(rt3);
		dm.insert(rt4);
		dm.insert(rt5);
		
		//rt4 = OpenJadeUtil.makeRating("a1", "a2", 4, "great");
		
		assertTrue(dm.valid(rt4));
		
		assertEquals(4, rt5.getIndex());
		assertEquals(5, dm.getRatings().size());
//		assertEquals(hashRoot, dossier.getTree().getNode().getValue());
	}

}
