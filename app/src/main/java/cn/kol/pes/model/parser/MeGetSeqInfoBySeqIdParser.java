package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MePafteropItem;
import cn.kol.pes.model.parser.adapter.MeGetSeqInfoBySeqIdAdapter;

//<pes code="${code}" message="${message}">
//
//<operationCode>${seqInfo.operationCode}</operationCode>
//<operationDescription>${seqInfo.operationDescription}</operationDescription>
//
//</pes>

public class MeGetSeqInfoBySeqIdParser extends DefaultBasicParser<MeGetSeqInfoBySeqIdAdapter> {
	
	final String OPLIST = "opList";
	final String SEQITEM = "seqItem";

	final String SEQNUM = "seqNum";
	final String OPCODE = "opCode";
	final String DESC = "desc";

	
	public MeGetSeqInfoBySeqIdParser(MeGetSeqInfoBySeqIdAdapter adapter){
		this.adapter = adapter;
	}
	
	MePafteropItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equals(OPLIST)){
			
		}
		else if(localName.equals(SEQITEM)){
			item  = new MePafteropItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(SEQITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(SEQNUM)){
			item.seqNum = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPCODE)){
			item.opCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(DESC)){
			item.desc = (mBuffer.toString().trim());
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
