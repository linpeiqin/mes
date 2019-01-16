package cn.kol.pes.model.item.femaleworker;

import cn.kol.pes.model.item.Item;

public class MmGetNonDeclareTimesBackItem extends Item {

	public String organizationId;		//组织ID
	public String jobTransactionId;		//生产任务ID
	public String moveTransactionId;		//非生产状况ID
	public String jobId;		//工程单ID
	public String jobNo;		//工程单编码
	public String wipEntityId;		//生产单ID
	public String wipEntityName;		//生成单编码
	public String inventoryItemId ;		//物料（成品）ID
	public String scheduleTransactionId;		//班次ID
	public String quantity;		//人数
	public String workTime;		//耗用工时
	public String goodsQuantity;		//非生产总数
	public String goodsWasteQuantity;		//非生产坏货数
	public String reasonCode;		//原因代码
	public String reasonRemark;		//原因说明
	public String showMsg(){
		String msg = "<td align='center'>"+reasonCode+"</td>"+
				"<td align='center'>"+reasonRemark+"</td>"+
				"<td align='center'>"+quantity+"</td>"+
				"<td align='center'>"+workTime+"</td>"+
				"<td align='center'>"+goodsQuantity+"</td>"+
				"<td align='center'>"+goodsWasteQuantity+"</td>"+
				"<td align='center'>"+jobNo+"</td>"+
				"<td align='center'>"+wipEntityName+"</td>";
		return msg;
	}

}
