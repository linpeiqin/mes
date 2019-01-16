<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<nonDeclareTimeList>
<#list nonDeclareTimeList as nonDeclareTime>
<nonDeclareTimeItem>
<organizationId>${nonDeclareTime.organizationId}</organizationId>
<jobTransactionId>${nonDeclareTime.jobTransactionId}</jobTransactionId>
<moveTransactionId>${nonDeclareTime.moveTransactionId}</moveTransactionId>

<jobId>${nonDeclareTime.jobId}</jobId>
<jobNo>${nonDeclareTime.jobNo}</jobNo>
<wipEntityId>${nonDeclareTime.wipEntityId}</wipEntityId>
<wipEntityName>${nonDeclareTime.wipEntityName}</wipEntityName>
<inventoryItemId>${nonDeclareTime.inventoryItemId}</inventoryItemId>
<scheduleTransactionId>${nonDeclareTime.scheduleTransactionId}</scheduleTransactionId>

<quantity>${nonDeclareTime.quantity}</quantity>
<workTime>${nonDeclareTime.workTime}</workTime>
<goodsQuantity>${nonDeclareTime.goodsQuantity}</goodsQuantity>
<goodsWasteQuantity>${nonDeclareTime.goodsWasteQuantity}</goodsWasteQuantity>
<reasonCode>${nonDeclareTime.reasonCode}</reasonCode>
<reasonRemark>${nonDeclareTime.reasonRemark}</reasonRemark>
</nonDeclareTimeItem>
</#list>
</nonDeclareTimeList>
</pes>