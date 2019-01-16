package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeStartedSeqItem;
import cn.kol.pes.model.parser.adapter.MeGetStartedSeqListAdapter;

//<pes code="${code}" message="${message}">
//<seqList>
//<#list seqList as seq>
//	<seqItem>
//transactionId
//		<job>${seq.job}</job>
//		<fmOperationCode>${seq.jobDesc}</fmOperationCode>
//		<opStart>${seq.opStart}</opStart>
//		<trxQuantity>${seq.trxQuantity}</trxQuantity>
//	</seqItem>
//</#list>
//</seqList>
//</pes>

public class MGetStartedSeqListParser extends DefaultBasicParser<MeGetStartedSeqListAdapter> {
	
	final String SEQLIST = "seqList";

	final String SEQITEM = "seqItem";
	
	final String WIPID = "wipId";
	final String TRANSACTIONID = "transactionId";
	final String JOB = "job";
	final String FMOPERATIONCODE = "fmOperationCode";
	final String SEQNUM = "seqNum";
	final String OPSTART = "opStart";
	final String TRXQUANTITY = "trxQuantity";
	
	public MGetStartedSeqListParser(MeGetStartedSeqListAdapter adapter){
		this.adapter = adapter;
	}
	
	MeStartedSeqItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(SEQLIST)){
			
		}
		else if(localName.equals(SEQITEM)){
			item  = new MeStartedSeqItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(SEQITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(WIPID)){
			item.wipId = (mBuffer.toString().trim());
		}
		else if(localName.equals(TRANSACTIONID)){
			item.transactionId = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB)){
			item.job = (mBuffer.toString().trim());
		}
		else if(localName.equals(FMOPERATIONCODE)){
			item.fmOperationCode = mBuffer.toString().trim();
		}
		else if(localName.equals(SEQNUM)){
			item.seqNum = mBuffer.toString().trim();
		}
		else if(localName.equals(OPSTART)){
			item.opStart = mBuffer.toString().trim();
		}
		else if(localName.equals(TRXQUANTITY)){
			item.trxQuantity = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
