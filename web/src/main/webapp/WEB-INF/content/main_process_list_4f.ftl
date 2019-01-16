<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<processList>
<#list processList as processData>
	<processItem>
		<jobId>${processData.jobId}</jobId>
		<productId>${processData.productId}</productId>
		<scheduleDate>${processData.scheduleDate}</scheduleDate>
		<primaryUomCode>${processData.primaryUomCode}</primaryUomCode>
		<jobDesc>${processData.jobDesc}</jobDesc>
		<projectNum>${processData.projectNum}</projectNum>
		<processNum>${processData.processNum}</processNum>
		<publishCode>${processData.publishCode}</publishCode>
		<productCode>${processData.productCode}</productCode>
		<productDesc>${processData.productDesc}</productDesc>
		<wipId>${processData.wipId}</wipId>
		<opCode>${processData.opCode}</opCode>
		<seqNum>${processData.seqNum}</seqNum>
		<seqDesc>${processData.seqDesc}</seqDesc>
		<planNum>${processData.planNum}</planNum>
		<timeNeeded>${processData.timeNeeded}</timeNeeded>
		<reportedQty>${processData.reportedQty}</reportedQty>
	</processItem>
</#list>
</processList>
</pes>