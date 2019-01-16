<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<seqList>
<#list seqList as seq>
	<seqItem>
		<trxId>${seq.trxId}</trxId>
		<opCode>${seq.opCode}</opCode>
		<trxQty>${seq.trxQty}</trxQty>
		<scrapQty>${seq.scrapQty}</scrapQty>
		<updateDate>${seq.updateDate}</updateDate>
	</seqItem>
</#list>
</seqList>
</pes>