<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<machineReportTime><![CDATA[${machineReportTime}]]></machineReportTime>
<processList>
<#list processList as processData>
	<processItem>
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