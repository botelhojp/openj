package openjade.cert.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

import openjade.cert.CertificateValidatorException;
import openjade.cert.util.RepositoryUtil;


/**
 * Representa o aplicativo de gerenciamneot dos arquivos de CRL. Recomenda-ser
 * criar um serviço que periodimente chame este aplicação para atulização das
 * CRL cadastrada no arquivo de índice.
 */
public class RepositoryService {

	private static final String UPDATE = "update-crl-list";
	private static final String ADD = "add-crl";
	private static String rt = "";

	/**
	 * Método principal
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			println(" Error: number of invalid arguments.\n " + "Use: java -jar security-certificate.jar [operation] <[url]> <[fileindex]>\n " + "Sample for update: java -jar security-certificate.jar " + UPDATE + " /tmp/crls/crl_index.txt\n " + "Sample for add url of crl: java -jar security-certificate.jar " + ADD + " http://www.domain.org/file.clr /tmp/crls/crl_index.txt");
		} else {

			String op = args[0];
			if (op.equalsIgnoreCase(ADD)) {

				String url = args[1];
				String file_index = args[2];
				File file = new File(file_index);
				Configuration.getInstance().setCrlIndex(file.getName());
				Configuration.getInstance().setCrlPath(file.getParent());
				OffLineCRLRepository rp = new OffLineCRLRepository();
				rp.addFileIndex(url);
				update(url);

			} else if (op.equalsIgnoreCase(UPDATE)) {

				String file_index = args[1];
				File fileIndex = new File(file_index);
				Configuration.getInstance().setCrlIndex(file_index);

				if (!fileIndex.exists()) {
					println("Index file [" + file_index + "] not found");

				} else {
					Properties prop = new Properties();

					try {
						prop.load(new FileInputStream(fileIndex));
					} catch (Exception e) {
						throw new CertificateValidatorException("Error on load index file " + fileIndex, e);
					}

					Enumeration<Object> keys = prop.keys();
					while (keys.hasMoreElements()) {
						Object key = keys.nextElement();
						String url = (String) prop.get(key);
						update(url);
					}

					try {
						prop.store(new FileOutputStream(fileIndex), null);
					} catch (Exception e) {
						throw new CertificateValidatorException("Error on load index file " + fileIndex, e);
					}
				}

			} else {
				println("Invalid operation [" + op + "]");
			}
		}

	}

	private static void update(String url) {
		try {
			Configuration config = Configuration.getInstance();
			File fileCLR = new File(config.getCrlPath(), RepositoryUtil.urlToMD5(url));
			print(" Downloading [" + url + "]...");
			RepositoryUtil.saveURL(url, fileCLR);
			println("...[Ok]");
		} catch (CertificateValidatorException e) {
			println("...[Fail]");
			println("\tCause: " + e.getMessage());
		}
	}

	public static String getReturn() {
		return rt;
	}

	private static void println(String msg) {
		rt = msg;
		System.out.println(msg);
	}

	private static void print(String msg) {
		rt = msg;
		System.out.print(msg);
	}

}
