package openjade.setting;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BootHandler extends DefaultHandler {

	private static final String TAG = "container";
	private long countSubSample;
	private boolean isAgent;
	private List<String[]> args = null;

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		countSubSample = 0;
		args = new ArrayList<String[]>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase(TAG)) {
			isAgent = true;
			countSubSample++;
		} else {
			isAgent = false;
		}
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		if (isAgent) {
			args.add(new String(ch, start, length).trim().split(" "));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ((qName.equalsIgnoreCase(TAG)) && countSubSample > 0) {
			countSubSample--;
			isAgent = false;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	public List<String[]> getArgs() {
		return args;
	}
}
