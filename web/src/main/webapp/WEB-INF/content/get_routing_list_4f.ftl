<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<routingList>
<#list routingList as routing>
<routingItem>
<routingCode>${routing.routingCode}</routingCode>
<routingName>${routing.routingName}</routingName>
<routingType>${routing.routingType}</routingType>
</routingItem>
</#list>
</routingList>
</pes>