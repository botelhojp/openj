package openjade.trust;

import jade.core.AID;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import openjade.core.OpenAgent;
import openjade.ontology.Rating;

public interface ITrustModel extends Serializable {

	/** Agent */

	public void setAgent(OpenAgent taskAgent);

	/** Rating */

	public void addRating(Rating rating, boolean direct);

	public Boolean test(AID aid);
	
//	public void reset();
	
	/** Witness */

	public void addWitness(AID sender);

	/** Serializar */

	public void serialize();

	public void loadSerialize();

	/** Propriedades */
	
	public Properties getProperties();

	public void setProperties(Properties properties);

	public void findReputation(AID server);

	public List<Rating> getDossie();
	
}
