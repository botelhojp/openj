package openjade.cert.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import openjade.cert.CertificateValidatorException;



public class RepositoryUtil {

	public static String urlToMD5(String url) {
		try {
			String ret;
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(url.getBytes());
			BigInteger bigInt = new BigInteger(1, md.digest());
			ret = bigInt.toString(16);
			return ret;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static void saveURL(String sUrl, File destinationFile) {
		URL url;
		byte[] buf;
		int byteRead/*, byteWritten*/ = 0;
		BufferedOutputStream outStream = null;
		URLConnection uCon = null;
		InputStream is = null;
		try {
			url = new URL(sUrl);
			outStream = new BufferedOutputStream(new FileOutputStream(destinationFile));			
			uCon = url.openConnection();			
			uCon.setConnectTimeout(5000);
			is = uCon.getInputStream();
			buf = new byte[1024];
			while ((byteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, byteRead);
				//byteWritten += byteRead;
			}
		} catch (MalformedURLException e) {
			throw new CertificateValidatorException("URL [" + sUrl + "] is Malformed", e);
		} catch (FileNotFoundException e) {
			throw new CertificateValidatorException("File [" + sUrl + "] is not found", e);
		} catch (IOException e) {
			throw new CertificateValidatorException("Error in  url openConnection [" + sUrl + "]", e);
		} finally {
			try {
				is.close();
				outStream.close();
			} catch (Throwable e) {
				throw new CertificateValidatorException("Is not possible close conection [" + sUrl + "]", e);
			}
		}
	}

}
