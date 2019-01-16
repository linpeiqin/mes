<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<groupList>
<#list groupList as group>
<groupItem>
<groupCode>${group.groupCode}</groupCode>
<groupName>${group.groupName}</groupName>
</groupItem>
</#list>
</groupList>
</pes>