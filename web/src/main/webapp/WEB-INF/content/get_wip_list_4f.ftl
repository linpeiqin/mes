<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<wipList>
<#list wipList as wip>
<wipItem>
<jobId>${wip.jobId}</jobId>
<jobNo>${wip.jobNo}</jobNo>
<jobName>${wip.jobName}</jobName>
<wipEntityName>${wip.wipEntityName}</wipEntityName>
<wipEntityId>${wip.wipEntityId}</wipEntityId>
<inventoryItemId>${wip.inventoryItemId}</inventoryItemId>
<itemNumber>${wip.itemNumber}</itemNumber>
<itemDesc>${wip.itemDesc}</itemDesc>
<operationCode>${wip.operationCode}</operationCode>
<operationSeqNum>${wip.operationSeqNum}</operationSeqNum>
<operationDesc>${wip.operationDesc}</operationDesc>
<jobDesc>${wip.jobDesc}</jobDesc>
</wipItem>
</#list>
</wipList>
</pes>