package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeMaterialsReportNumItem;
import cn.kol.pes.model.parser.adapter.MeMaterialsReportNumAdapter;

//<pes code="${code}" message="${message}">
//<materialsNumList>
//<#list materialsNumList as numItem>
//	<numItem>
//		<item>${numItem.item}</item>
//		<itemId>${numItem.itemId}</itemId>
//	</numItem>
//</#list>
//</materialsNumList>
//</pes>

public class MeMaterialsReportNumParser extends DefaultBasicParser<MeMaterialsReportNumAdapter> {
	
	final String MATERIALSNUMLIST = "materialsNumList";

	final String NUMITEM = "numItem";
	
	final String ITEM = "item";
	final String ITEMID = "itemId";
	
	public MeMaterialsReportNumParser(MeMaterialsReportNumAdapter adapter){
		this.adapter = adapter;
	}
	
	MeMaterialsReportNumItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(MATERIALSNUMLIST)){
			
		}
		else if(localName.equals(NUMITEM)){
			item  = new MeMaterialsReportNumItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(NUMITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(ITEM)){
			item.item = (mBuffer.toString().trim());
		}
		else if(localName.equals(ITEMID)){
			item.itemId = mBuffer.toString().trim();
		}
		else if(localName.equals("desc")){
			item.desc = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
