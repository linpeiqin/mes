package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.MeCodeMsgAdapter;

//<pes code="${code}" message="${message}">
//</pes>

public class MeCodeMsgParser extends DefaultBasicParser<MeCodeMsgAdapter> {
	

	public MeCodeMsgParser(MeCodeMsgAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		super.endElement(uri, localName, qName);
	}
	
}
