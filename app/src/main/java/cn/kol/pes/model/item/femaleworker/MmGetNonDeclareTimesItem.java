package cn.kol.pes.model.item.femaleworker;

import java.io.Serializable;

import cn.kol.pes.model.item.Item;

public class MmGetNonDeclareTimesItem extends Item implements Serializable {

	public String organizationId;		//组织ID
	public String jobId;		//工程单ID
	public String wipEntityId;		//生产单ID
	public String inventoryItemId;		//成品ID
	public String scheduleDate;		//计划日期
	public String workGroup;		//组别
	public String workMonitor;		//组长
	public String workClassCode;		//班次(DAY/NIGHT)

}
