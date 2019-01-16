package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesBackItem;
import cn.kol.pes.model.item.femaleworker.MmReasonCodeItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetDeclareTimeListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetReasonCodeListAdapter;

public class MmGetDeclareTimeListParser extends DefaultBasicParser<MmGetDeclareTimeListAdapter> {



	final String DECLARE_TIME_ITEM = "declareTimeItem";
	final String ORGANIZATION_ID = "organizationId";
	final String JOB_TRANSACTION_ID = "jobTransactionId";
	final String MOVE_TRANSACTION_ID = "moveTransactionId";
	final String OPERATION_SEQ_NUM = "operationSeqNum";
	final String OPERATION_DESC = "operationDesc";
	final String NI_OPERATION_CODE = "niOperationCode";
	final String NI_OPERATION_DESC = "niOperationDesc";
	final String QUANTITY = "quantity";
	final String START_PULL_TIME = "startPullTime";
	final String END_PULL_TIME = "endPullTime";
	final String WORK_TIME = "workTime";
	final String TRANSACTION_UOM = "transactionUom";
	final String PER_QUANTITY = "perQuantity";
	final String SUBGOODS_QUANTITY = "subgoodsQuantity";
	final String GOODS_QUANTITY = "goodsQuantity";
	final String GOODS_WASTE_QUANTITY = "goodsWasteQuantity";
	final String RETURN_WASTE_QUANTITY = "returnWasteQuantity";
	final String INPUT_QUANTITY = "inputQuantity";
	final String WASTE_INPUT_QUANTITY = "wasteInputQuantity";
	final String REASON_CODE = "reasonCode";
	final String REASON_REMARK = "reasonRemark";

	public MmGetDeclareTimeListParser(MmGetDeclareTimeListAdapter adapter) {
		this.adapter = adapter;
	}

	MmGetDeclareTimesBackItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(DECLARE_TIME_ITEM)){
			item  = new MmGetDeclareTimesBackItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(DECLARE_TIME_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(ORGANIZATION_ID)){
			item.organizationId = (mBuffer.toString().trim());
		}else if(localName.equals(JOB_TRANSACTION_ID)){
			item.jobTransactionId = (mBuffer.toString().trim());
		}else if(localName.equals(MOVE_TRANSACTION_ID)){
			item.moveTransactionId = (mBuffer.toString().trim());
		}else if(localName.equals(OPERATION_SEQ_NUM)){
			item.operationSeqNum = (mBuffer.toString().trim());
		}else if (localName.equals(OPERATION_DESC)){
			item.operationDesc = (mBuffer.toString().trim());
		}else if(localName.equals(NI_OPERATION_CODE)){
			item.niOperationCode = (mBuffer.toString().trim());
		}else if(localName.equals(NI_OPERATION_DESC)){
			item.niOperationDesc = (mBuffer.toString().trim());
		}else if(localName.equals(QUANTITY)){
			item.quantity = (mBuffer.toString().trim());
		}else if(localName.equals(START_PULL_TIME)){
			item.startPullTime = (mBuffer.toString().trim());
		}else if(localName.equals(END_PULL_TIME)){
			item.endPullTime = (mBuffer.toString().trim());
		}else if(localName.equals(WORK_TIME)){
			item.workTime = (mBuffer.toString().trim());
		}else if(localName.equals(TRANSACTION_UOM)){
			item.transactionUom = (mBuffer.toString().trim());
		}else if(localName.equals(PER_QUANTITY)){
			item.perQuantity = (mBuffer.toString().trim());
		}else if(localName.equals(SUBGOODS_QUANTITY)){
			item.subgoodsQuantity = (mBuffer.toString().trim());
		}else if(localName.equals(GOODS_QUANTITY)){
			item.goodsQuantity = (mBuffer.toString().trim());
		}else if(localName.equals(GOODS_WASTE_QUANTITY)){
			item.goodsWasteQuantity = (mBuffer.toString().trim());
		}else if(localName.equals(RETURN_WASTE_QUANTITY)){
			item.returnWasteQuantity = (mBuffer.toString().trim());
		}else if(localName.equals(INPUT_QUANTITY)){
			item.inputQuantity = (mBuffer.toString().trim());
		}else if(localName.equals(WASTE_INPUT_QUANTITY)){
			item.wasteInputQuantity = (mBuffer.toString().trim());
		}else if(localName.equals(REASON_CODE)){
			item.reasonCode = (mBuffer.toString().trim());
		}else if(localName.equals(REASON_REMARK)){
			item.reasonRemark = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
