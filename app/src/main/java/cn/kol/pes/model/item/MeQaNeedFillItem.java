/*-----------------------------------------------------------

-- PURPOSE

--    需要填写的质量管理计划的数据封装类

-- History

--	  09-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;

import java.util.ArrayList;
import java.util.List;

public class MeQaNeedFillItem extends Item {

	public String charId;
	public String charName;
	public String prompt;
	public String resultColumnName;
	public String datatypeMeaning;
	public String derivedFlag;
	public String mandatoryFlag;
	public String readOnlyFlag;
	public String planId;
	
	private List<MeQaValueItem> qaValueList = null;
	
	public String getCharId() {
		return charId;
	}
	
	public String getCharName() {
		return charName;
	}
	
	public String getPrompt() {
		return prompt;
	}
	
	public String getResultColumnName() {
		return resultColumnName;
	}
	
	public List<MeQaValueItem> getQaValueList() {
		return qaValueList;
	}
	
	public void addQaValueList(String shortCode, String desc) {
		if(qaValueList == null) {
			qaValueList = new ArrayList<MeQaValueItem>();
		}
		qaValueList.add(new MeQaValueItem(shortCode, desc));
	}
	
	
}
