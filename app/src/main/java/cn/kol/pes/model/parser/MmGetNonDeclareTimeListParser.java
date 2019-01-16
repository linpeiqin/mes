package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetNonDeclareTimesBackItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetDeclareTimeListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetNonDeclareTimeListAdapter;

public class MmGetNonDeclareTimeListParser extends DefaultBasicParser<MmGetNonDeclareTimeListAdapter> {



	final String NON_DECLARE_TIME_ITEM = "nonDeclareTimeItem";

	final String ORGANIZATION_ID = "organizationId";
	final String JOB_TRANSACTION_ID = "jobTransactionId";
	final String MOVE_TRANSACTION_ID = "moveTransactionId";
	final String JOB_ID = "jobId";
	final String JOB_NO = "jobNo";
	final String WIP_ENTITY_ID = "wipEntityId";
	final String WIP_ENTITY_NAME = "wipEntityName";
	final String INVENTORY_ITEM_ID = "inventoryItemId";
	final String SCHEDULE_TRANSACTION_ID = "scheduleTransactionId";
	final String QUANTITY = "quantity";
	final String WORK_TIME = "workTime";
	final String GOODS_QUANTITY = "goodsQuantity";
	final String GOODS_WASTE_QUANTITY = "goodsWasteQuantity";
	final String REASON_CODE = "reasonCode";
	final String REASON_REMARK = "reasonRemark";

	public MmGetNonDeclareTimeListParser(MmGetNonDeclareTimeListAdapter adapter) {
		this.adapter = adapter;
	}

	MmGetNonDeclareTimesBackItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(NON_DECLARE_TIME_ITEM)){
			item  = new MmGetNonDeclareTimesBackItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(NON_DECLARE_TIME_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(ORGANIZATION_ID)){
			item.organizationId = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB_TRANSACTION_ID)){
			item.jobTransactionId = (mBuffer.toString().trim());
		}
		else if(localName.equals(MOVE_TRANSACTION_ID)){
			item.moveTransactionId = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB_ID)){
			item.jobId = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB_NO)){
			item.jobNo = (mBuffer.toString().trim());
		}
		else if(localName.equals(WIP_ENTITY_ID)){
			item.wipEntityId = (mBuffer.toString().trim());
		}
		else if(localName.equals(WIP_ENTITY_NAME)){
			item.wipEntityName = (mBuffer.toString().trim());
		}
		else if(localName.equals(INVENTORY_ITEM_ID)){
			item.inventoryItemId = (mBuffer.toString().trim());
		}
		else if(localName.equals(SCHEDULE_TRANSACTION_ID)){
			item.scheduleTransactionId = (mBuffer.toString().trim());
		}
		else if(localName.equals(QUANTITY)){
			item.quantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(WORK_TIME)){
			item.workTime = (mBuffer.toString().trim());
		}
		else if(localName.equals(GOODS_QUANTITY)){
			item.goodsQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(GOODS_WASTE_QUANTITY)){
			item.goodsWasteQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(REASON_CODE)){
			item.reasonCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(REASON_REMARK)){
			item.reasonRemark = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
