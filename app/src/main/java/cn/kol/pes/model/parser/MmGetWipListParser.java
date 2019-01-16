package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmOrgItem;
import cn.kol.pes.model.item.femaleworker.MmWipItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetOrgListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetWipListAdapter;

public class MmGetWipListParser extends DefaultBasicParser<MmGetWipListAdapter> {



	final String WIP_ITEM = "wipItem";
	final String JOB_ID = "jobId";
	final String JOB_NO = "jobNo";
	final String JOB_NAME = "jobName";
	final String WIP_ENTITY_NAME = "wipEntityName";
	final String WIP_ENTITY_ID = "wipEntityId";
	final String INVENTORY_ITEM_ID = "inventoryItemId";
	final String ITEM_NUMBER = "itemNumber";
	final String ITEM_DESC = "itemDesc";
	final String OPERATION_CODE = "operationCode";
	final String OPERATION_SEQ_NUM = "operationSeqNum";
	final String OPERATION_DESC = "operationDesc";
	final String JOB_DESC = "jobDesc";

	public MmGetWipListParser(MmGetWipListAdapter adapter) {
		this.adapter = adapter;
	}
	
	MmWipItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(WIP_ITEM)){
			item  = new MmWipItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(WIP_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(JOB_ID)){
			item.jobId = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB_NO)){
			item.jobNo = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB_NAME)){
			item.jobName = (mBuffer.toString().trim());
		}
		else if(localName.equals(WIP_ENTITY_NAME)){
			item.wipEntityName = (mBuffer.toString().trim());
		}
		else if(localName.equals(WIP_ENTITY_ID)){
			item.wipEntityId = (mBuffer.toString().trim());
		}
		else if(localName.equals(INVENTORY_ITEM_ID)){
			item.inventoryItemId = (mBuffer.toString().trim());
		}
		else if(localName.equals(ITEM_NUMBER)){
			item.itemNumber = (mBuffer.toString().trim());
		}
		else if(localName.equals(ITEM_DESC)){
			item.itemDesc = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPERATION_CODE)){
			item.operationCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPERATION_SEQ_NUM)){
			item.operationSeqNum = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPERATION_DESC)){
			item.operationDesc = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB_DESC)){
			item.jobDesc = (mBuffer.toString().trim());
		}
		super.endElement(uri, localName, qName);
	}
	
}
