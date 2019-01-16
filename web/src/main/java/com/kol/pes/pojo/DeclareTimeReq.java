package com.kol.pes.pojo;

public class DeclareTimeReq {

    public String organizationId;		//组织ID
    public String jobTransactionId;		//生产任务ID
    public String operationSeqNum;		//标准工序
    public String niOperationCode;		//女工工序编号
    public String niOperationDesc;		//女工工序描述
    public String assetCode;		//设备编号
    public String quantity;				//人数
    public String startPullTime;				//开拉时间
    public String endPullTime;				//收拉时间
    public String workTime;				//总工时
    public String transactionUom;				//事务单位
    public String perQuantity;				//每小时产量
    public String subgoodsQuantity;				//半制品数
    public String goodsQuantity;				//成品数
    public String goodsWasteQuantity;				//生产坏货数
    public String returnWasteQuantity;				//返工坏货数
    public String inputQuantity;				//已导入好货数
    public String wasteInputQuantity;		//已导入坏货数
    public String reasonCode;					//坏货原因代码
    public String reasonRemark;				//坏货原因说明
}
