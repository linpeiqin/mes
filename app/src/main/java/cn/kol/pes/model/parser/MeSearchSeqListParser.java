package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeSearchSeqListItem;
import cn.kol.pes.model.parser.adapter.MeSearchSeqListAdapter;

//<pes code="${code}" message="${message}">
//<seqList>
//<#list seqList as seq>
//	<seqItem>
//		<trxId>${seq.trxId}</trxId>
//		<opCode>${seq.opCode}</opCode>
//		<trxQty>${seq.trxQty}</trxQty>
//		<scrapQty>${seq.scrapQty}</scrapQty>
//		<updateDate>${seq.updateDate}</updateDate>
//	</seqItem>
//</#list>
//</seqList>
//</pes>

public class MeSearchSeqListParser extends DefaultBasicParser<MeSearchSeqListAdapter> {
	
	final String SEQLIST = "seqList";

	final String SEQITEM = "seqItem";
	
	final String TRXID = "trxId";
	final String OPCODE = "opCode";
	final String TRXQTY = "trxQty";
	final String SCRAPQTY = "scrapQty";
	final String UPDATEDATE = "updateDate";
	
	public MeSearchSeqListParser(MeSearchSeqListAdapter adapter){
		this.adapter = adapter;
	}
	
	MeSearchSeqListItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(SEQLIST)){
			
		}
		else if(localName.equals(SEQITEM)){
			item  = new MeSearchSeqListItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(SEQITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(TRXID)){
			item.trxId = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPCODE)){
			item.opCode = mBuffer.toString().trim();
		}
		else if(localName.equals(TRXQTY)){
			item.trxQty = mBuffer.toString().trim();
		}
		else if(localName.equals(SCRAPQTY)){
			item.scrapQty = mBuffer.toString().trim();
		}
		else if(localName.equals(UPDATEDATE)){
			item.updateDate = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
