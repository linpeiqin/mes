package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.MeAssetItem;
import cn.kol.pes.model.item.femaleworker.MmGroupItem;
import cn.kol.pes.model.parser.DefaultBasicParser;
import cn.kol.pes.model.parser.adapter.MeGetAssetListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetGroupListAdapter;

public class MmGetGroupListParser extends DefaultBasicParser<MmGetGroupListAdapter> {



	final String GROUP_ITEM = "groupItem";
	final String GROUP_CODE = "groupCode";
	final String GROUP_NAME = "groupName";

	public MmGetGroupListParser(MmGetGroupListAdapter adapter) {
		this.adapter = adapter;
	}
	
	MmGroupItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(GROUP_ITEM)){
			item  = new MmGroupItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(GROUP_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(GROUP_CODE)){
			item.groupCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(GROUP_NAME)){
			item.groupName = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
