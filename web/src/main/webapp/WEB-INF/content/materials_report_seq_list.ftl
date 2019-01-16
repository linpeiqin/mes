<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<seqList>
<#list seqList as seq>
	<seqItem>
		<opCode>${seq.opCode}</opCode>
		<seqNum><![CDATA[${seq.desc}]]></seqNum>
	</seqItem>
</#list>
</seqList>
</pes>