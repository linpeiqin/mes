package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeAssetItem;
import cn.kol.pes.model.parser.adapter.MeGetAssetListAdapter;

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

public class MGetAssetListParser extends DefaultBasicParser<MeGetAssetListAdapter> {
	
	final String ASSETLIST = "assetList";

	final String ASSETITEM = "assetItem";
	
	final String ASSETCODE = "assetCode";
	final String DESC = "desc";

	
	public MGetAssetListParser(MeGetAssetListAdapter adapter) {
		this.adapter = adapter;
	}
	
	MeAssetItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(ASSETLIST)){
			
		}
		else if(localName.equals(ASSETITEM)){
			item  = new MeAssetItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ASSETITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals("assetId")){
			item.assetId = (mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETCODE)){
			item.assetCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(DESC)){
			item.desc = (mBuffer.toString().trim());
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
