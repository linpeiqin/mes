<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">

	<completeQty>${descInfo.completeQty?c}</completeQty>
	<scrapQty>${descInfo.scrapQty?c}</scrapQty>
	<display><![CDATA[${descInfo.display}]]></display>
	<errCode>${descInfo.errCode}</errCode>
	<errMsg><![CDATA[${descInfo.errMsg}]]></errMsg>
	
	<activeList>
	<#list descInfo.activeList as active>
		<activeItem>
			<activity>${active.activity}</activity>
			<description><![CDATA[${active.description}]]></description>
		</activeItem>
	</#list>
	</activeList>
	
	<reasonList>
	<#list descInfo.reasonList as reason>
		<reasonItem>${reason}</reasonItem>
	</#list>
	</reasonList>
</pes>