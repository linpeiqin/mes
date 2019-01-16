<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<materialsNumList>
<#list materialsNumList as numItem>
	<numItem>
		<item>${numItem.item}</item>
		<itemId>${numItem.itemId}</itemId>
		<desc>${numItem.desc}</desc>
	</numItem>
</#list>
</materialsNumList>
</pes>