package ${packageName};

import org.apache.log4j.Logger;

public class Boot {

	protected static Logger log = Logger.getLogger(Boot.class);

	public static void main(String[] args) {
		openjade.Boot.loadXml();
	}
}
