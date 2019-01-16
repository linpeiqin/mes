<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<trxQty>${descInfo.trxQty?c}</trxQty>
<scrapQty>${descInfo.scrapQty?c}</scrapQty>
<seqList>
<#list descInfo.seqList as seq>
	<seqItem>
		<opCode>${seq.operationCode}</opCode>
		<seqNum>${seq.seqNum}</seqNum>
		<opDesc><![CDATA[${seq.operationDescription}]]></opDesc>
	</seqItem>
</#list>
</seqList>
</pes>