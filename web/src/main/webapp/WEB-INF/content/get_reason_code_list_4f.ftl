<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<reasonCodeList>
<#list reasonCodeList as reasonCode>
<reasonCodeItem>
<reasonCode>${reasonCode.reasonCode}</reasonCode>
<reasonName>${reasonCode.reasonName}</reasonName>
<reasonDesc>${reasonCode.reasonDesc}</reasonDesc>
</reasonCodeItem>
</#list>
</reasonCodeList>
</pes>