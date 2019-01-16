<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<seqList>
<#list seqList as seq>
	<seqItem>
		<wipId>${seq.wipId}</wipId>
		<transactionId>${seq.transactionId}</transactionId>
		<job>${seq.job}</job>
		<fmOperationCode>${seq.fmOperationCode}</fmOperationCode>
		<seqNum>${seq.seqNum}</seqNum>
		<opStart>${seq.opStart}</opStart>
		<trxQuantity>${seq.trxQuantity}</trxQuantity>
	</seqItem>
</#list>
</seqList>
</pes>