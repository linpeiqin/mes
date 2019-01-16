package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeTimeReportActiveItem;
import cn.kol.pes.model.parser.adapter.MeTimeReportGetDescInfoAdapter;

//<pes code="${code}" message="${message}">
//
//<completeQty>${completeQty}</completeQty>
//<scrapQty>${scrapQty}</scrapQty>
//<display>${display}</display>
//<errCode>${errCode}</errCode>
//<errMsg>${errMsg}</errMsg>
//
//<activeList>
//<#list activeList as active>
//	<activeItem>
//		<activity>${active.activity}</activity>
//		<description>${active.description}</description>
//	</activeItem>
//</#list>
//</activeList>
//</pes>

public class MeTimeReportGetDescInfoParser extends DefaultBasicParser<MeTimeReportGetDescInfoAdapter> {
	
	final String ACTIVELIST = "activeList";

	final String ACTIVEITEM = "activeItem";
	final String ACTIVITY = "activity";
	final String DESCRIPTION = "description";
	
	final String COMPLETEQTY = "completeQty";
	final String SCRAPQTY = "scrapQty";
	final String DISPLAY = "display";
	final String ERRCODE = "errCode";
	final String ERRMSG = "errMsg";
	
	public MeTimeReportGetDescInfoParser(MeTimeReportGetDescInfoAdapter adapter){
		this.adapter = adapter;
	}
	
	MeTimeReportActiveItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(ACTIVELIST)){
			
		}
		else if(localName.equals(ACTIVEITEM)){
			item  = new MeTimeReportActiveItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ACTIVEITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(ACTIVITY)){
			item.activity = (mBuffer.toString().trim());
		}
		else if(localName.equals(DESCRIPTION)){
			item.desc = (mBuffer.toString().trim());
		}
		
		else if(localName.equals(COMPLETEQTY)){
			adapter.completeQty = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(SCRAPQTY)){
			adapter.scrapQty = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(DISPLAY)){
			adapter.display = (mBuffer.toString().trim());
		}
		else if(localName.equals(ERRCODE)){
			adapter.errorCode = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ERRMSG)){
			adapter.errorMsg = (mBuffer.toString().trim());
		}
		else if(localName.equals("reasonItem")){
			adapter.reasonList.add(mBuffer.toString().trim());
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
