package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmGetDeclareMtlsBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesBackItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetDeclareMtlListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetDeclareTimeListAdapter;

public class MmGetDeclareMtlListParser extends DefaultBasicParser<MmGetDeclareMtlListAdapter> {



	final String DECLARE_MTL_ITEM = "declareMtlItem";

	final String MTL_TRANSACTION_ID = "mtlTransactionId";
	final String SCHEDULE_TRANSACTION_ID = "scheduleTransactionId";
	final String INVENTORY_ITEM_ID = "inventoryItemId";
	final String CONCATENATED_SEGMENTS = "concatenatedSegments";
	final String DESCRIPTION = "description";
	final String TRANSACTION_QUANTITY = "transactionQuantity";
	final String TRANSACTION_UOM = "transactionUom";
	final String TRANSACTION_ITEM_TYPE = "transactionItemType";
	final String TRANSACTION_TYPE = "transactionType";
	final String REMARK = "remark";


	public MmGetDeclareMtlListParser(MmGetDeclareMtlListAdapter adapter) {
		this.adapter = adapter;
	}

	MmGetDeclareMtlsBackItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(DECLARE_MTL_ITEM)){
			item  = new MmGetDeclareMtlsBackItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(DECLARE_MTL_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(MTL_TRANSACTION_ID)){
			item.mtlTransactionId = (mBuffer.toString().trim());
		}
		else if(localName.equals(SCHEDULE_TRANSACTION_ID)){
			item.scheduleTransactionId = (mBuffer.toString().trim());
		}else if(localName.equals(INVENTORY_ITEM_ID)){
			item.inventoryItemId = (mBuffer.toString().trim());
		}else if(localName.equals(MTL_TRANSACTION_ID)){
			item.mtlTransactionId = (mBuffer.toString().trim());
		}else if(localName.equals(CONCATENATED_SEGMENTS)){
			item.concatenatedSegments = (mBuffer.toString().trim());
		}else if(localName.equals(DESCRIPTION)){
			item.description = (mBuffer.toString().trim());
		}else if(localName.equals(TRANSACTION_QUANTITY)){
			item.transactionQuantity = (mBuffer.toString().trim());
		}else if(localName.equals(TRANSACTION_UOM)){
			item.transactionUom = (mBuffer.toString().trim());
		}else if(localName.equals(TRANSACTION_ITEM_TYPE)){
			item.transactionItemType = (mBuffer.toString().trim());
		}
		else if(localName.equals(TRANSACTION_TYPE)){
			item.transactionType = (mBuffer.toString().trim());
		}
		else if(localName.equals(REMARK)){
			item.remark = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
