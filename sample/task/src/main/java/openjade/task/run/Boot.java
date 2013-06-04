package openjade.task.run;

import org.apache.log4j.Logger;

public class Boot {
	
	protected static Logger log = Logger.getLogger(Boot.class);

	public static void main(String[] args) {
		try {
			if (args != null && args.length > 0) {
				openjade.Boot.main(args);
			} else {
				openjade.Boot.loadXml();
			}			
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
}
