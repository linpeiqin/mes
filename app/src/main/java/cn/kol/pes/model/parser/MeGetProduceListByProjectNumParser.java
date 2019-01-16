package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeProduceItem;
import cn.kol.pes.model.parser.adapter.MeGetProduceListByProjectNumAdapter;

//<pes code="${code}" message="${message}">
//<produceList>
//<#list produceList as produce>
//	<produceItem>
//		<wipEntityId>${produce.wipEntityId}</wipEntityId>
//		<jobDesc>${produce.jobDesc}</jobDesc>
//	</produceItem>
//</#list>
//</produceList>
//</pes>

public class MeGetProduceListByProjectNumParser extends DefaultBasicParser<MeGetProduceListByProjectNumAdapter> {
	
	final String PRODUCELIST = "produceList";

	final String PRODUCEITEM = "produceItem";
	
	final String WIPENTITYID = "wipEntityId";
	final String JOBDESC = "jobDesc";
	
	public MeGetProduceListByProjectNumParser(MeGetProduceListByProjectNumAdapter adapter){
		this.adapter = adapter;
	}
	
	MeProduceItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(PRODUCELIST)){
		}
		else if(localName.equals(PRODUCEITEM)){
			item  = new MeProduceItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(PRODUCEITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(WIPENTITYID)){
			item.wipEntityId = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOBDESC)){
			item.jobDesc = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
