<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<declareMtlList>
<#list declareMtlList as declareMtl>
<declareMtlItem>
<mtlTransactionId>${declareMtl.mtlTransactionId}</mtlTransactionId>
<scheduleTransactionId>${declareMtl.scheduleTransactionId}</scheduleTransactionId>
<inventoryItemId>${declareMtl.inventoryItemId}</inventoryItemId>
<concatenatedSegments>${declareMtl.concatenatedSegments}</concatenatedSegments>
<description>${declareMtl.description}</description>
<transactionQuantity>${declareMtl.transactionQuantity}</transactionQuantity>
<transactionUom>${declareMtl.transactionUom}</transactionUom>
<transactionItemType>${declareMtl.transactionItemType}</transactionItemType>
<transactionType>${declareMtl.transactionType}</transactionType>
<remark>${declareMtl.remark}</remark>
</declareMtlItem>
</#list>
</declareMtlList>
</pes>