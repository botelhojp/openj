package openjade.hi.run;

import org.apache.log4j.Logger;

public class Boot {

	protected static Logger log = Logger.getLogger(Boot.class);

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			openjade.Boot.main(args);
		} else {
			openjade.Boot.loadXml();
		}
	}
}
