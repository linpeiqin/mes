<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<orgList>
<#list orgList as org>
<organizationItem>
<organizationCode>${org.organizationCode}</organizationCode>
<organizationName>${org.organizationName}</organizationName>
<organizationId>${org.organizationId}</organizationId>
</organizationItem>
</#list>
</orgList>
</pes>