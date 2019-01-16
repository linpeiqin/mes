/*-----------------------------------------------------------

-- PURPOSE

--    工序列表的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeMainProcessItem;
import cn.kol.pes.model.parser.adapter.MeMainProcessListAdapter;

//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="${code}" message="${message}">
//<processList>
//<#list processList as processData>
//	<processItem>
//		<projectNum>${processData.projectNum}</projectNum>
//		<processNum>${processData.processNum}</processNum>
//		<publishCode>${processData.publishCode}</publishCode>
//		<productCode>${processData.productCode}</productCode>
//		<productDesc>${processData.productDesc}</productDesc>
//		<seqNum>${processData.seqNum}</seqNum>
//		<seqDesc>${processData.seqDesc}</seqDesc>
//		<planNum>${processData.planNum}</planNum>
//		<timeNeeded>${processData.timeNeeded}</timeNeeded>
//	</processItem>
//</#list>
//</processList>
//</pes>

public class MeMainProcessListParser extends DefaultBasicParser<MeMainProcessListAdapter> {
	
	final String MACHINEREPORTTIME = "machineReportTime";
	
	final String PROCESSLIST = "processList";
	final String PROCESSITEM = "processItem";
	
	final String JOB_ID = "jobId";
	final String PRODUCT_ID = "productId";
	final String SCHEDULE_DATE = "scheduleDate";
	final String PRIMARY_UOM_CODE = "primaryUomCode";
	final String JOB_DESC = "jobDesc";


	final String PROJECTNUM = "projectNum";
	final String PROCESSNUM = "processNum";
	final String PUBLISHCODE = "publishCode";
	final String PRODUCTCODE = "productCode";
	final String PRODUCTDESC = "productDesc";
	final String WIPID = "wipId";
	final String OPCODE = "opCode";
	final String SEQNUM = "seqNum";
	final String SEQDESC = "seqDesc";
	final String PLANNUM = "planNum";
	final String TIMENEEDED = "timeNeeded";
	final String REPORTEDQTY = "reportedQty";
	final String ISREPORT = "isReport";

	public MeMainProcessListParser(MeMainProcessListAdapter adapter){
		this.adapter = adapter;
	}
	
	MeMainProcessItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equals(PROCESSLIST)){
			
		}else if(localName.equals(PROCESSITEM)){
			item  = new MeMainProcessItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		
		if(localName.equals(MACHINEREPORTTIME)){
			adapter.machineReportTime = mBuffer.toString().trim();
		}
		else if(localName.equals(PROCESSITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(JOB_ID)){
			item.jobId = (mBuffer.toString().trim())!=null?(mBuffer.toString().trim()):"";
		}
		else if(localName.equals(PRODUCT_ID)){
			item.productId = (mBuffer.toString().trim())!=null?(mBuffer.toString().trim()):"";
		}
		else if(localName.equals(SCHEDULE_DATE)){
			item.scheduleDate = (mBuffer.toString().trim())!=null?(mBuffer.toString().trim()):"";
		}
		else if(localName.equals(PRIMARY_UOM_CODE)){
			item.primaryUomCode = (mBuffer.toString().trim())!=null?(mBuffer.toString().trim()):"";
		}
		else if(localName.equals(JOB_DESC)){
			item.jobDesc = (mBuffer.toString().trim())!=null?(mBuffer.toString().trim()):"";
		}
		else if(localName.equals(PROJECTNUM)){
			item.projectNum = (mBuffer.toString().trim());
		}
		else if(localName.equals(PROCESSNUM)){
			item.processNum = (mBuffer.toString().trim());
		}
		else if(localName.equals(PUBLISHCODE)) {
			item.publishCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(PRODUCTCODE)){
			item.productCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(PRODUCTDESC)){
			item.productDesc = (mBuffer.toString().trim());
		}
		else if(localName.equals(WIPID)){
			item.wipId = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPCODE)){
			item.opCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(SEQNUM)){
			item.seqNum = (mBuffer.toString().trim());
		}
		else if(localName.equals(SEQDESC)){
			item.seqDesc = (mBuffer.toString().trim());
		}
		else if(localName.equals(PLANNUM)){
			item.planNum = (mBuffer.toString().trim());
		}
		else if(localName.equals(TIMENEEDED)){
			item.timeNeeded = (mBuffer.toString().trim());
		}
		else if(localName.equals(REPORTEDQTY)){
			item.reportedQty = (mBuffer.toString().trim());
		}
		else if(localName.equals(ISREPORT)){
			item.isReport = (mBuffer.toString().trim());
		}
		super.endElement(uri, localName, qName);
	}
	
}
