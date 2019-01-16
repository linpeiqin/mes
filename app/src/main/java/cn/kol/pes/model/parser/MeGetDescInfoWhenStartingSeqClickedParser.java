package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.MeGetDescInfoWhenStartingSeqClickedAdapter;

//<pes code="${code}" message="${message}">
//
//<defaultStartDate>${descInfo.defaultStartDate}</defaultStartDate>
//<display>${descInfo.display}</display>
//<availQty>${descInfo.availQty?c}</availQty>
//<errorCode>${descInfo.errorCode?c}</errorCode>
//<errorMsg>${descInfo.errorMsg}</errorMsg>
//
//</pes>

public class MeGetDescInfoWhenStartingSeqClickedParser extends DefaultBasicParser<MeGetDescInfoWhenStartingSeqClickedAdapter> {
	
	final String DEFAULTSTARTDATE = "defaultStartDate";
	final String DISPLAY = "display";
	final String AVAILQTY = "availQty";
	final String ERRORCODE = "errorCode";
	final String ERRORMSG = "errorMsg";
	
	final String DATABASETIME = "databaseTime";
	
	public MeGetDescInfoWhenStartingSeqClickedParser(MeGetDescInfoWhenStartingSeqClickedAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(DEFAULTSTARTDATE)){
			adapter.defaultStartDate = mBuffer.toString().trim();
		}
		else if(localName.equals(DISPLAY)){
			adapter.display = mBuffer.toString().trim();
		}
		else if(localName.equals(AVAILQTY)){
			adapter.availQty = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ERRORCODE)){
			adapter.errorCode = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ERRORMSG)){
			adapter.errorMsg = mBuffer.toString().trim();
		}
		else if(localName.equals(DATABASETIME)){
			adapter.databaseTime = mBuffer.toString().trim();
		}
		
		
		super.endElement(uri, localName, qName);
	}
	
}
