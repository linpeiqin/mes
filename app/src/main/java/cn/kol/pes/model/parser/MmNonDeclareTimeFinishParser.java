/*-----------------------------------------------------------

-- PURPOSE

--    登录返回员工信息的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareTimeFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmNonDeclareTimeFinishAdapter;

public class MmNonDeclareTimeFinishParser extends DefaultBasicParser<MmNonDeclareTimeFinishAdapter> {

	final String NON_DECLARE_TIME_ID = "nonDeclareTimeId";

	public MmNonDeclareTimeFinishParser(MmNonDeclareTimeFinishAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	if(localName.equals(NON_DECLARE_TIME_ID)){
			adapter.nonDeclareTimeId = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
