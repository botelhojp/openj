package openjade.cert.oid;

/**
 * 
 * Classe Generica   para   tratamento   de   atributos   de  alguns   atributos   de   Pessoa 
 * Fisica, Pessoa Juridica   e   Equipamento   de   acordo   com   os   padroes  definidos   no 
 * DOC­ICP­04 v2.0 de 18/04/2006, pela ICP­BRASIL
 * 

 */

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BERConstructedOctetString;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DLSequence;

import sun.security.util.DerValue;
import sun.security.x509.OtherName;

@SuppressWarnings("all")
public class OIDGeneric {
	
	private String oid  = null;
	private String data = null;
	protected Map<String, String> properties = new HashMap<String, String>();
	
	protected OIDGeneric() {
	}
	
	/**
	 * Instance for object.
	 * @param data -> byte array with certificate content.
	 * @return Object GenericOID
	 * @throws IOException
	 * @throws Exception
	 */
	public static OIDGeneric getInstance(byte[] data) throws IOException, Exception {
		ASN1InputStream     is       = new ASN1InputStream(data);		
		DLSequence         sequence = (DLSequence) is.readObject();
		DERObjectIdentifier oid      = (DERObjectIdentifier) sequence.getObjectAt(0);
		DERTaggedObject     tag      = (DERTaggedObject) sequence.getObjectAt(1);
		ASN1OctetString       octet    = null;
		DERPrintableString  print    = null;
		DERUTF8String		utf8	 = null;
		
		is.close();

		try {
			octet = makeDERUTF8String(tag);
		} catch (Exception e) {
			e.printStackTrace();
			try{
				print    = (DERPrintableString)DERPrintableString.getInstance(tag);
			}catch (Exception e1) {
				utf8 	= (DERUTF8String) DERUTF8String.getInstance(tag);
			}			
		}

		String className = "openjade.cert.oid.OID_" + oid.getId().replaceAll("[.]", "_");
		OIDGeneric oidGenerico;
		try {
			oidGenerico = (OIDGeneric)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			throw new Exception("Can not instace class '" + className + "'.", e);
		} catch (IllegalAccessException e) {
			throw new Exception("Was not possible instace class '" + className + "'.", e);
		} catch (ClassNotFoundException e) {
			oidGenerico = new OIDGeneric();
		}

		oidGenerico.oid  = oid.getId();
		
		if(octet != null) {
			oidGenerico.data = new String(octet.getOctets());
		} else {
			if (print != null){
				oidGenerico.data = print.getString();
			}else{
				oidGenerico.data = utf8.getString();
			}
		}
		
		oidGenerico.initialize();
		
		return oidGenerico;
	}
	
	/**
	 * 
	 * @param der -> Certificate DER (sun.security.util.DerValue)
	 * @return OIDGenerico
	 * @throws IOException
	 * @throws Exception
	 */
	public static OIDGeneric getInstance(DerValue der) throws IOException, Exception{
		OtherName on = new OtherName(der);
		String className = "openjade.cert.oid.OID_" + on.getOID().toString().replaceAll("[.]", "_");
		
		OIDGeneric oidGenerico;
		try {
			oidGenerico = (OIDGeneric)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			throw new Exception("Was not possible instance class '" + className + "'.", e);
		} catch (IllegalAccessException e) {
			throw new Exception("Was not possible instance class '" + className + "'.", e);
		} catch (ClassNotFoundException e) {
			oidGenerico = new OIDGeneric();
		}
		
		oidGenerico.oid  = on.getOID().toString();
		oidGenerico.data = new String(on.getNameValue()).substring(6);
		oidGenerico.initialize();

		return oidGenerico;
	}

	protected void initialize(){
		//Inicializa as propriedades do conteudo DATA
	}
	
	/**
	 * 
	 * @param fields -> fields from certificate
	 */
	protected void initialize(Object[] fields){
		int tmp = 0;

		for(int i=0 ; i<fields.length ; i+=2) {
			String key = (String)fields[i];
			int size = ((Integer)fields[i+1]).intValue();
			
			properties.put(key, data.substring(tmp, Math.min(tmp + size, data.length())));
			
			tmp += size;
		}
	}

	/**
	 * 
	 * @return set of OID on String format
	 */
	public String getOid(){
		return oid;
	}
	
	/**
	 * 
	 * @return content on String format
	 */
	public String getData(){
		return data;
	}
	
	@SuppressWarnings("all")
    private static ASN1OctetString makeDERUTF8String(
            Object  obj)
        {
            if (obj == null || obj instanceof ASN1OctetString)
            {
                return (ASN1OctetString)obj;
            }

            if (obj instanceof ASN1TaggedObject)
            {
                return makeDERUTF8String(((ASN1TaggedObject)obj).getObject());
            }
            if (obj instanceof ASN1Sequence)
            {
               
				Vector      v = new Vector();
                Enumeration e = ((ASN1Sequence)obj).getObjects();

                while (e.hasMoreElements())
                {
                    v.addElement(e.nextElement());
                }

                return new BERConstructedOctetString(v);
            }

            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
        }
}