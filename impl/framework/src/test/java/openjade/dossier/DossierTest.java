package openjade.dossier;

import static org.junit.Assert.*;


import static openjade.util.OpenJadeUtil.*;

import org.junit.Test;

import openjade.ontology.Dossier;
import openjade.ontology.Rating;
import openjade.util.OpenJadeUtil;

public class DossierTest {
	
	Dossier dossier = new Dossier();

	@Test
	public void insert1() throws Exception {
		Rating rt = OpenJadeUtil.makeRating("a1", "a2", 0, "great");
		String hashRoot = md5(rt);
		OpenJadeUtil.insert(dossier, rt);
		assertEquals(1, dossier.getRatings().size());
		assertEquals(0, rt.getIndex());
		assertNotNull(dossier.getTree());
		assertNotNull(dossier.getTree().getNode());
		assertEquals(hashRoot, dossier.getTree().getNode().getValue());
	}
	
	@Test
	public void insert2() throws Exception {
		Rating rt1 = OpenJadeUtil.makeRating("a1", "a2", 1, "great");
		Rating rt2 = OpenJadeUtil.makeRating("a1", "a2", 2, "great");
		OpenJadeUtil.insert(dossier, rt1);
		OpenJadeUtil.insert(dossier, rt2);
		String hashRoot = md5(md5(rt1) + md5(rt2));
		assertEquals(2, dossier.getRatings().size());
		assertEquals(hashRoot, dossier.getTree().getNode().getValue());
	}
	
	@Test
	public void insertImpar() throws Exception {
		Rating rt1 = OpenJadeUtil.makeRating("a1", "a2", 1, "great");
		Rating rt2 = OpenJadeUtil.makeRating("a1", "a2", 2, "great");
		Rating rt3 = OpenJadeUtil.makeRating("a1", "a2", 3, "great");
		OpenJadeUtil.insert(dossier, rt1);
		OpenJadeUtil.insert(dossier, rt2);
		OpenJadeUtil.insert(dossier, rt3);
		
		
		String hashRoot = md5(md5(md5(rt1) + md5(rt2)) + md5(md5(rt3) + md5(rt3)));
		assertEquals(3, dossier.getRatings().size());
		assertEquals(hashRoot, dossier.getTree().getNode().getValue());
	}
	
	
	@Test
	public void valid() throws Exception {
		Rating rt1 = OpenJadeUtil.makeRating("a1", "a2", 1, "great");
		Rating rt2 = OpenJadeUtil.makeRating("a1", "a2", 2, "great");
		Rating rt3 = OpenJadeUtil.makeRating("a1", "a2", 3, "great");
		Rating rt4 = OpenJadeUtil.makeRating("a1", "a2", 4, "great");
		Rating rt5 = OpenJadeUtil.makeRating("a1", "a2", 5, "great");
		
		OpenJadeUtil.insert(dossier, rt1);
		OpenJadeUtil.insert(dossier, rt2);
		OpenJadeUtil.insert(dossier, rt3);
		OpenJadeUtil.insert(dossier, rt4);
		OpenJadeUtil.insert(dossier, rt5);
		
		assertTrue(OpenJadeUtil.valid(dossier, rt4));
		
		assertEquals(4, rt5.getIndex());
		assertEquals(5, dossier.getRatings().size());
//		assertEquals(hashRoot, dossier.getTree().getNode().getValue());
	}

}
