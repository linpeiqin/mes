<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<seqList>
<#list seqList as seq>
	<seqItem>
		<seqNum>${seq.seqNum}</seqNum>
		<opCode>${seq.operationCode}</opCode>
		<desc><![CDATA[${seq.operationDescription}]]></desc>
	</seqItem>
</#list>
</seqList>
</pes>