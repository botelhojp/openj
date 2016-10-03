package br.pucpr.ppgia.redo;

import org.apache.log4j.Logger;

public class Boot {

	protected static Logger log = Logger.getLogger(Boot.class);

	public static void main(String[] args) {
		log.debug("load start");
		openjade.Boot.loadXml();
		log.debug("load end");
	}
}
