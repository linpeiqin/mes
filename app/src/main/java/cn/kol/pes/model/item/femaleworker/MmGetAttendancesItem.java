package cn.kol.pes.model.item.femaleworker;

import java.io.Serializable;

import cn.kol.pes.model.item.Item;

public class MmGetAttendancesItem extends Item implements Serializable {

	public String organizationId;		//组织ID
	public String workGroup;		//组别
	public String workMonitor;		//组长
	public String workClassCode;		//班次(DAY/NIGHT)
	public String scheduleDate;		//计划日期


}
