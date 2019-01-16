/*-----------------------------------------------------------

-- PURPOSE

--    设备维修信息的适配类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter.femaleworker;


import cn.kol.pes.model.parser.adapter.DefaultBasicAdapter;

public class MmGetMtlPlanInfoAdapter extends DefaultBasicAdapter {

	public String wipEntityId;			//生产单id
	public String operationSeqNum;		//工序号
	public String organizationId;		//组织id
	public String inventoryItemId;		//物料id
	public String concatenatedSegments;//物料编码
	public String itemDescription;		//物料描述
	public String requiredQuantity;		//申请用量
	public String itemPrimaryUomCode;	//申请单位
	public String issued;	//已报数量
	public String available;	//可报数量


}
