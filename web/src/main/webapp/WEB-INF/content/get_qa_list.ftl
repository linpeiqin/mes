<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<qaList>
<#list qaList as qa>
	<qaItem>
		<charId>${qa.charId}</charId>
		<charName>${qa.charName}</charName>
		<prompt>${qa.prompt}</prompt>
		<resultColumnName>${qa.resultColumnName}</resultColumnName>
		<datatypeMeaning>${qa.datatypeMeaning}</datatypeMeaning>
		<derivedFlag>${qa.derivedFlag}</derivedFlag>
		<mandatoryFlag>${qa.mandatoryFlag}</mandatoryFlag>
		<readOnlyFlag>${qa.readOnlyFlag}</readOnlyFlag>
		<planId>${qa.planId}</planId>
		<qaValueList>
			<#list qa.qaValueList as qaValue>
			<qaValue shortCode="${qaValue.shortCode}" description="${qaValue.description}"></qaValue>
			</#list>
		</qaValueList>
	</qaItem>
</#list>
</qaList>
</pes>