package openjade.core;

import java.util.HashMap;

public class DataProvider {

	private static DataProvider dt = new DataProvider();

	private HashMap<String, Object> values = new HashMap<String, Object>();

	private DataProvider() {
	}

	public static DataProvider getInstance() {
		return dt;
	}

	public Object put(String key, Object value) {
		return values.put(key, value);
	}

	public Object get(String key) {
		return values.get(key);
	}

}
