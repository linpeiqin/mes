<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<mtlPlanList>
<#list mtlPlanList as mtlPlan>
<mtlPlanItem>
<wipEntityId>${mtlPlan.wipEntityId}</wipEntityId>
<operationSeqNum>${mtlPlan.operationSeqNum}</operationSeqNum>
<organizationId>${mtlPlan.organizationId}</organizationId>
<inventoryItemId>${mtlPlan.inventoryItemId}</inventoryItemId>
<concatenatedSegments>${mtlPlan.concatenatedSegments}</concatenatedSegments>
<itemDescription>${mtlPlan.itemDescription}</itemDescription>
<requiredQuantity>${mtlPlan.requiredQuantity}</requiredQuantity>
<itemPrimaryUomCode>${mtlPlan.itemPrimaryUomCode}</itemPrimaryUomCode>
<issued>${mtlPlan.issued}</issued>
<available>${mtlPlan.available}</available>
</mtlPlanItem>
</#list>
</mtlPlanList>
</pes>