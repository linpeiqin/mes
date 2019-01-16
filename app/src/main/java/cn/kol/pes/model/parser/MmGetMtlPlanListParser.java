package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmMtlPlanItem;
import cn.kol.pes.model.item.femaleworker.MmRoutingItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetMtlPlanListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetRoutingListAdapter;

public class MmGetMtlPlanListParser extends DefaultBasicParser<MmGetMtlPlanListAdapter> {



	final String MTL_PLAN_ITEM = "mtlPlanItem";
	final String WIP_ENTITY_ID = "wipEntityId";
	final String OPERATION_SEQ_NUM = "operationSeqNum";
	final String ORGANIZATION_ID = "organizationId";
	final String INVENTORY_ITEM_ID = "inventoryItemId";
	final String CONCATENATED_SEGMENTS = "concatenatedSegments";
	final String ITEM_DESCRIPTION = "itemDescription";
	final String REQUIRED_QUANTITY = "requiredQuantity";
	final String ITEM_PRIMARY_UOM_CODE = "itemPrimaryUomCode";
	final String ISSUED = "issued";
	final String AVAILABLE = "available";

	public MmGetMtlPlanListParser(MmGetMtlPlanListAdapter adapter) {
		this.adapter = adapter;
	}

	MmMtlPlanItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(MTL_PLAN_ITEM)){
			item  = new MmMtlPlanItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(MTL_PLAN_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(WIP_ENTITY_ID)){
			item.wipEntityId = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPERATION_SEQ_NUM)){
			item.operationSeqNum = (mBuffer.toString().trim());
		}
		else if(localName.equals(ORGANIZATION_ID)){
			item.organizationId = (mBuffer.toString().trim());
		}
		else if(localName.equals(INVENTORY_ITEM_ID)){
			item.inventoryItemId = (mBuffer.toString().trim());
		}
		else if(localName.equals(CONCATENATED_SEGMENTS)){
			item.concatenatedSegments = (mBuffer.toString().trim());
		}
		else if(localName.equals(ITEM_DESCRIPTION)){
			item.itemDescription = (mBuffer.toString().trim());
		}
		else if(localName.equals(REQUIRED_QUANTITY)){
			item.requiredQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(ITEM_PRIMARY_UOM_CODE)){
			item.itemPrimaryUomCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(ISSUED)){
			item.issued = (mBuffer.toString().trim());
		}
		else if(localName.equals(AVAILABLE)){
			item.available = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
