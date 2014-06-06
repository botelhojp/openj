package openjade.trust;

import openjade.core.OpenJadeException;

public class TrustModelFactory {

	@SuppressWarnings("rawtypes")
	public static ITrustModel create(String clazz) {
		try {
			Class c = Class.forName(clazz);
			return (ITrustModel) c.newInstance();
		} catch (ClassNotFoundException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}
}
