/*-----------------------------------------------------------

-- PURPOSE

--    工序的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item;

public class DataProcessItem extends DataItem {

	public String jobId;			//工程单ID
	public String productId;		//成品ID
	public String scheduleDate;		//开单日期
	public String primaryUomCode;		//成品单位
	public String jobDesc;		//工程名称


	public String projectNum;		//工程单号
	public String processNum;		//生产单号
	public String publishCode;	//VER_NO 版次

	public String productCode;	//成品编码
	public String productDesc;	//成品描述
	public String wipId;			//生产单ID
	public String opCode;			//工序代码
	public String seqNum;			//工序号
	public String seqDesc;		//工序描述
	public String planNum;		//排期数量
	public String timeNeeded;		//需用工时(hr)
	public String reportedQty;	//好货数/坏货数
	public String isReport;		//是否已报数

	public String getProjectNum() {
		return projectNum;
	}

	public String getProcessNum() {
		return processNum;
	}

	public String getPublishCode() {
		return publishCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public String getWipId() {
		return wipId;
	}

	public String getOpCode() {
		return opCode;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public String getSeqDesc() {
		return seqDesc;
	}

	public String getPlanNum() {
		return planNum;
	}

	public String getTimeNeeded() {
		return timeNeeded;
	}

	public String getReportedQty() {
		return reportedQty;
	}

	public String getJobId() {
		return jobId;
	}

	public String getProductId() {
		return productId;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public String getPrimaryUomCode() {
		return primaryUomCode;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public String getIsReport() {
		return isReport;
	}
}
