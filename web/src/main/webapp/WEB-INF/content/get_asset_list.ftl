<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<assetList>
<#list assetList as asset>
<assetItem>
<assetId>${asset.resourceId?c}</assetId>
<assetCode>${asset.assetNumber}</assetCode>
<desc>${asset.description}</desc>
</assetItem>
</#list>
</assetList>
</pes>