package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.MeReceiveOrderGetQtyByIdAdapter;

//<pes code="${code}" message="${message}">
//
//<qty>${seqInfo.operationCode}</qty>
//
//</pes>

public class MeReceiveOrderGetQtyByIdParser extends DefaultBasicParser<MeReceiveOrderGetQtyByIdAdapter> {
	
	final String QTY = "qty";
	
	public MeReceiveOrderGetQtyByIdParser(MeReceiveOrderGetQtyByIdAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(QTY)){
			adapter.qty = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
