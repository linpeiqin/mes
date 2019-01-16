/*-----------------------------------------------------------

-- PURPOSE

--    插入图片路径的解析器.

-- History

--	  11-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoSetPasswordAdapter;

//<pes code="${code}" message="${message}">
//</pes>

public class KoSetPasswordParser extends DefaultBasicParser<KoSetPasswordAdapter> {
	
	
	public KoSetPasswordParser(KoSetPasswordAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		
		super.endElement(uri, localName, qName);
	}
	
}
