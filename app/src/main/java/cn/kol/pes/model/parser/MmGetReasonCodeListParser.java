package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmReasonCodeItem;
import cn.kol.pes.model.item.femaleworker.MmRoutingItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetReasonCodeListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetRoutingListAdapter;

public class MmGetReasonCodeListParser extends DefaultBasicParser<MmGetReasonCodeListAdapter> {



	final String REASON_CODE_ITEM = "reasonCodeItem";
	final String REASON_CODE = "reasonCode";
	final String REASON_NAME = "reasonName";
	final String REASON_DESC = "reasonDesc";

	public MmGetReasonCodeListParser(MmGetReasonCodeListAdapter adapter) {
		this.adapter = adapter;
	}

	MmReasonCodeItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(REASON_CODE_ITEM)){
			item  = new MmReasonCodeItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(REASON_CODE_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(REASON_CODE)){
			item.reasonCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(REASON_NAME)){
			item.reasonName = (mBuffer.toString().trim());
		}
		else if(localName.equals(REASON_DESC)){
			item.reasonDesc = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
