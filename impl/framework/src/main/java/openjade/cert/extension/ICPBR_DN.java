package openjade.cert.extension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * DN properties for ICP-BRASIL Patterns  
 * 

*/
public class ICPBR_DN extends Properties {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dn = null;

	/**
	 * @param dn
	 * @throws IOException
	 */
	
	public ICPBR_DN(String dn) throws IOException {
		super();
		this.dn = dn;
		ByteArrayInputStream bis = new ByteArrayInputStream(dn.replaceAll(",", "\n").getBytes());
		load(bis);
		bis.close();
	}
	
	/**
	 * 
	 * @return String
	 */
	public String toString(){
		return dn;
	}

}
