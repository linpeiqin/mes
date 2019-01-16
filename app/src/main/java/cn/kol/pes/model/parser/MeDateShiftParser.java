/*-----------------------------------------------------------

-- PURPOSE

--    登录返回员工信息的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.MeGetDateShiftAdapter;

//<pes code="${code}" message="${message}">
//<staff>
//<staffNo>${loginData.staffNo}</staffNo>
//<staffName>${loginData.staffName}</staffName>
//<cardNo>${loginData.cardNo}</cardNo>
//<levelCode>${loginData.levelCode}</levelCode>
//</staff>
//</pes>

public class MeDateShiftParser extends DefaultBasicParser<MeGetDateShiftAdapter> {
	
	final String DATE = "date";
	final String SHIFT = "shift";

	public MeDateShiftParser(MeGetDateShiftAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(DATE)){
			adapter.date = mBuffer.toString().trim();
		}
		else if(localName.equals(SHIFT)){
			adapter.shift = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
