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

public class MmDeclareTimeFinishParser extends DefaultBasicParser<MmDeclareTimeFinishAdapter> {

	final String DECLARE_TIME_ID = "declareTimeId";

	public MmDeclareTimeFinishParser(MmDeclareTimeFinishAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	if(localName.equals(DECLARE_TIME_ID)){
			adapter.declareTimeId = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
