package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmRoutingItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetRoutingListAdapter;

public class MmGetRoutingListParser extends DefaultBasicParser<MmGetRoutingListAdapter> {



	final String ROUTING_ITEM = "routingItem";
	final String ROUTING_CODE = "routingCode";
	final String ROUTING_NAME = "routingName";
	final String ROUTING_TYPE = "routingType";

	public MmGetRoutingListParser(MmGetRoutingListAdapter adapter) {
		this.adapter = adapter;
	}
	
	MmRoutingItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(ROUTING_ITEM)){
			item  = new MmRoutingItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ROUTING_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(ROUTING_CODE)){
			item.routingCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(ROUTING_NAME)){
			item.routingName = (mBuffer.toString().trim());
		}
		else if(localName.equals(ROUTING_TYPE)){
			item.routingType = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
