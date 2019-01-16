package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.MeGetAssetSeqInfoByAssetIdAdapter;

//<pes code="${code}" message="${message}">
//<resourceId>${assetInfo.resourceId}</resourceId>
//<resourceCode>${assetInfo.resourceCode}</resourceCode>
//<description>${assetInfo.description}</description>
//<attribute4>${assetInfo.attribute4}</attribute4>
//
//</pes>

public class MeGetAssetSeqInfoByAssetIdParser extends DefaultBasicParser<MeGetAssetSeqInfoByAssetIdAdapter> {
	
	final String RESOURCEID = "resourceId";
	final String RESOURCECODE = "resourceCode";
	final String DESCRIPTION = "description";
	final String ATTRIBUTE4 = "attribute4";
	
	public MeGetAssetSeqInfoByAssetIdParser(MeGetAssetSeqInfoByAssetIdAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(RESOURCEID)){
			adapter.resourceId = mBuffer.toString().trim();
		}
		else if(localName.equals(RESOURCECODE)){
			adapter.resourceCode = mBuffer.toString().trim();
		}
		else if(localName.equals(DESCRIPTION)){
			adapter.description = mBuffer.toString().trim();
		}
		else if(localName.equals(ATTRIBUTE4)){
			adapter.attribute4 = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
