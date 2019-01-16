package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmGroupItem;
import cn.kol.pes.model.item.femaleworker.MmOrgItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetGroupListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetOrgListAdapter;

public class MmGetOrgListParser extends DefaultBasicParser<MmGetOrgListAdapter> {



	final String ORGANIZATION_ITEM = "organizationItem";
	final String ORGANIZATION_CODE = "organizationCode";
	final String ORGANIZATION_NAME = "organizationName";
	final String ORGANIZATION_ID = "organizationId";

	public MmGetOrgListParser(MmGetOrgListAdapter adapter) {
		this.adapter = adapter;
	}
	
	MmOrgItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(ORGANIZATION_ITEM)){
			item  = new MmOrgItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ORGANIZATION_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(ORGANIZATION_CODE)){
			item.organizationCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(ORGANIZATION_NAME)){
			item.organizationName = (mBuffer.toString().trim());
		}
		else if(localName.equals(ORGANIZATION_ID)){
			item.organizationId = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
