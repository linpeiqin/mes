package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.MeMaterialsReportDisplayAdapter;

//<pes code="${code}" message="${message}">
//
//<qty>${seqInfo.operationCode}</qty>
//
//</pes>

public class MeMaterialsReportDisplayParser extends DefaultBasicParser<MeMaterialsReportDisplayAdapter> {
	
	final String DISPLAY = "display";
	
	public MeMaterialsReportDisplayParser(MeMaterialsReportDisplayAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(DISPLAY)) {
			adapter.display = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
