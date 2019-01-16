/*-----------------------------------------------------------

-- PURPOSE

--    KoJobItem工序的数据封装类。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;

public class MeMainProcessItem extends Item {

	public String jobId;		//工程单ID
	public String productId;		//成品ID
	public String scheduleDate;		//开单日期

	public String primaryUomCode;		//成品单位
	public String jobDesc;		//工程单名称


	public String projectNum;		//工程单编号
	public String processNum;		//生产单编号
	public String publishCode;	//VER_NO 版次
	public String productCode;	//成品编码
	public String productDesc;	//成品描述
	public String wipId;			//生产单ID
	public String opCode;			//工序代码
	public String seqNum;			//工序号
	public String seqDesc;		//工序描述
	public String planNum;		//排期数量
	public String timeNeeded;		//需用工时(hr)
	public String reportedQty;
	public String isReport;		//是否已经报数
}
