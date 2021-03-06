<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">

<incompleteQuan>${incompleteQuan}</incompleteQuan>
<minStartTime>${minStartTime}</minStartTime>
<maxEndTime>${maxEndTime}</maxEndTime>

<qaList>
<#list qaList as qaData>
<qaItem>
<charId>${qaData.charId}</charId>
<charName>${qaData.charName}</charName>
<prompt>${qaData.prompt}</prompt>
<resultColumnName>${qaData.resultColumnName}</resultColumnName>
<datatypeMeaning>${qaData.datatypeMeaning}</datatypeMeaning>
<derivedFlag>${qaData.derivedFlag}</derivedFlag>
<mandatoryFlag>${qaData.mandatoryFlag}</mandatoryFlag>
<readOnlyFlag>${qaData.readOnlyFlag}</readOnlyFlag>
</qaItem>
</#list>
</qaList>
</pes>