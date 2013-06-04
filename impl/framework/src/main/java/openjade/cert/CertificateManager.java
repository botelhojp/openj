package openjade.cert;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

import openjade.cert.validator.CRLValidator;
import openjade.cert.validator.PeriodValidator;



public class CertificateManager {
	
	private X509Certificate x509;
	private Collection<IValidator> validators;
	
	
	public CertificateManager(X509Certificate x509, IValidator... validators) throws CertificateValidatorException  {
		this(x509, true, validators);
	}
	
	public CertificateManager(String pinNumber, IValidator... validators)  throws CertificateValidatorException  {
		this(pinNumber, true, validators);
	}
	
	public CertificateManager(File fileX509, IValidator... validators)  throws CertificateValidatorException  {
		this(fileX509, true, validators);
	}

	public CertificateManager(X509Certificate x509, boolean loadDefaultValidators, IValidator... validators)  throws CertificateValidatorException  {
		this.init(x509, loadDefaultValidators, validators);
	}
	
	public CertificateManager(String pinNumber, boolean loadDefaultValidators, IValidator... validators)  throws CertificateValidatorException  {
		CertificateLoader loader = new CertificateLoaderImpl();
		X509Certificate x509 = loader.loadFromToken(pinNumber);
		this.init(x509, loadDefaultValidators, validators);
	}

	public CertificateManager(File fileX509, boolean loadDefaultValidators, IValidator... validators)  throws CertificateValidatorException  {
		CertificateLoader loader = new CertificateLoaderImpl();
		X509Certificate x509 = loader.load(fileX509);
		this.init(x509, loadDefaultValidators, validators);
	}

	private void init(X509Certificate x509, boolean loadDefaultValidators, IValidator... validators)  throws CertificateValidatorException  {
		this.x509 = x509;
		this.validators = new ArrayList<IValidator>();
		
		if (loadDefaultValidators) {
			loadDefaultValidators();
		}
		
		for (IValidator validator : validators) {
			this.validators.add(validator);
		}
		
		for (IValidator validator : this.validators) {
			validator.validate(x509);
		}
	}
	
	public void load(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			for (Annotation annotation : field.getAnnotations()) {
				if (annotation.annotationType().isAnnotationPresent(OIDExtension.class)) {
					OIDExtension oid = annotation.annotationType().getAnnotation(OIDExtension.class);
					
					Class<? extends IOIDExtensionLoader> loaderClass = oid.loader();
					try {
						IOIDExtensionLoader loader = loaderClass.newInstance();
						loader.load(object, field, x509);
					} catch (Exception e) {
						throw new CertificateException("Error: Could not initialize atribute \""+field.getName()+"\"", e);
					}
				}
			}
		}
	}
	
	public <T> T load(Class<T> clazz) {
		T object;
		try {
			object = clazz.newInstance();
		} catch (Exception e) {
			throw new CertificateException("Error on new instance for "+clazz.getName(), e); 
		}
		load(object);
		return object;
	}
	
	private void loadDefaultValidators() {
		validators.add(new PeriodValidator());
		validators.add(new CRLValidator());		
	}
	
}
