package openjade.certbuilder;

import java.io.InputStream;
import java.util.Hashtable;

import org.apache.log4j.Logger;

public class CertBuilder {

	private static Logger logger = Logger.getLogger(CertBuilder.class);
	private static X509CertificateBuilder builder = null;

	public static void main(String[] args) {
		logger.info("-----------------------------");
		logger.info("Open Jade Builder - v 1.0.0");
		logger.info("-----------------------------");
		if (args.length < 1){
			showHelp();
		}else{
			if (args[0].equals("-test")){
				buildCertTest();
			} else if (args[0].equals("-c")){
				buildCert(args);
			}else{
				showHelp();
			}
		}
	}

	private static void buildCert(String[] args) {
		Hashtable<String, String> table = new Hashtable<String, String>();
		for (int i = 1; i < args.length; i++) {
			String arg = args[i];
			String[] param = arg.split("=");
			if (param != null && param.length == 2){
				table.put(param[0].toLowerCase(), param[1].replace("\"", "").toLowerCase());
			}
		}
		logger.info("generating custom certificate...");
		InputStream keystore = CertBuilder.class.getResourceAsStream("/certs/keystore_open_jade.p12");
		String alias = "alias_ca";		
		String passwordKeystore = 	X509Functions.getValue(table.get("certpassword"), "");
		String personID = 			X509Functions.getValue(table.get("personid"), "000.000.000-00");
		String personName = 		X509Functions.getValue(table.get("personname"), ""); 
		String personEmail = 		X509Functions.getValue(table.get("personemail"), "");
		String url = 				X509Functions.getValue(table.get("url"), "");
		String agentID = 			X509Functions.getValue(table.get("agentid"), "");
		String newFileName = 		agentID + ".pfx";
		try {
			builder = new X509CertificateBuilder(keystore, alias, true, personID, agentID, personEmail, url);
			StringBuffer cn = new StringBuffer();
			cn.append(personName + ":" + agentID);
			builder.createCertificate(cn.toString(), 1460, newFileName, passwordKeystore);
		} catch (Throwable e) {
			throw new CertBuilderException(e);
		}		
	}

	private static void buildCertTest() {
		logger.info("generating certificate...");
		InputStream keystore = CertBuilder.class.getResourceAsStream("/certs/keystore_open_jade.p12");
		String alias = "alias_ca";
		String passwordKeystore = "123456";
		String personID = "00000000001";
		String personName = "Leonard";
		String personEmail = "agent@openjade.org";
		String url = "http://openjade.org/lcr/acopenjade.crl";
		String agentID = "agent_00001";
		String newFileName = agentID + ".pfx";
		try {
			builder = new X509CertificateBuilder(keystore, alias, true, personID, agentID, personEmail, url);
			StringBuffer cn = new StringBuffer();
			cn.append(personName + ":" + agentID);
			builder.createCertificate(cn.toString(), 1460, newFileName, passwordKeystore);
		} catch (Throwable e) {
			throw new CertBuilderException(e);
		}		
	}

	private static void showHelp() {
		InputStream is = CertBuilder.class.getResourceAsStream("/help.txt");
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		while (s.hasNext()) {
			logger.info(s.next());
		}
	}
}
