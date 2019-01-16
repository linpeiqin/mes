package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeTimeReportSeqListItem;
import cn.kol.pes.model.parser.adapter.MeTimeReportSeqListAdapter;

//<pes code="${code}" message="${message}">
//<trxQty>${descInfo.trxQty?c}</trxQty>
//<scrapQty>${descInfo.scrapQty?c}</scrapQty>
//<seqList>
//<#list seqList as seq>
//	<seqItem>
//		<opCode>${seq.operationCode}</opCode>
//		<seqNum>${seq.operationDescription}</seqNum>
//	</seqItem>
//</#list>
//</seqList>
//</pes>

public class MeTimeReportSeqListParser extends DefaultBasicParser<MeTimeReportSeqListAdapter> {
	
	final String TRXQTY = "trxQty";
	final String SCRAPQTY = "scrapQty";
	
	final String SEQLIST = "seqList";

	final String SEQITEM = "seqItem";
	
	final String OPCODE = "opCode";
	final String SEQNUM = "seqNum";
	final String OPDESC = "opDesc";
	
	public MeTimeReportSeqListParser(MeTimeReportSeqListAdapter adapter){
		this.adapter = adapter;
	}
	
	MeTimeReportSeqListItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(SEQLIST)){
			
		}
		else if(localName.equals(SEQITEM)){
			item  = new MeTimeReportSeqListItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(SEQITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(TRXQTY)){
			adapter.trxQty = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(SCRAPQTY)){
			adapter.scrapQty = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(OPCODE)){
			item.opCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(SEQNUM)){
			item.seqNum = mBuffer.toString().trim();
		}
		else if(localName.equals(OPDESC)){
			item.opDesc = (mBuffer.toString().trim());
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
