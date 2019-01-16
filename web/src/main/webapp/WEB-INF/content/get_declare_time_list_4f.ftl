<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<declareTimeList>
<#list declareTimeList as declareTime>
<declareTimeItem>
<organizationId>${declareTime.organizationId}</organizationId>
<jobTransactionId>${declareTime.jobTransactionId}</jobTransactionId>
<moveTransactionId>${declareTime.moveTransactionId}</moveTransactionId>
<operationSeqNum>${declareTime.operationSeqNum}</operationSeqNum>
<operationDesc>${declareTime.operationDesc}</operationDesc>
<niOperationCode>${declareTime.niOperationCode}</niOperationCode>
<niOperationDesc>${declareTime.niOperationDesc}</niOperationDesc>
<quantity>${declareTime.quantity}</quantity>
<startPullTime>${declareTime.startPullTime}</startPullTime>
<endPullTime>${declareTime.endPullTime}</endPullTime>
<workTime>${declareTime.workTime}</workTime>
<transactionUom>${declareTime.transactionUom}</transactionUom>
<perQuantity>${declareTime.perQuantity}</perQuantity>
<subgoodsQuantity>${declareTime.subgoodsQuantity}</subgoodsQuantity>
<goodsQuantity>${declareTime.goodsQuantity}</goodsQuantity>
<goodsWasteQuantity>${declareTime.goodsWasteQuantity}</goodsWasteQuantity>
<returnWasteQuantity>${declareTime.returnWasteQuantity}</returnWasteQuantity>
<inputQuantity>${declareTime.inputQuantity}</inputQuantity>
<wasteInputQuantity>${declareTime.wasteInputQuantity}</wasteInputQuantity>
<reasonCode>${declareTime.reasonCode}</reasonCode>
<reasonRemark>${declareTime.reasonRemark}</reasonRemark>
</declareTimeItem>
</#list>
</declareTimeList>
</pes>