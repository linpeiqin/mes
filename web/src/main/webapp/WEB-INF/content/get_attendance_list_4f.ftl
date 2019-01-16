<?xml version="1.0" encoding="UTF-8" ?>
<pes code="${code}" message="${message}">
<attendanceList>
<#list attendanceList as attendance>
<attendanceItem>
<forecastWorkMan>${attendance.forecastWorkMan}</forecastWorkMan>
<factWorkMan>${attendance.factWorkMan}</factWorkMan>
<leaveQuantity>${attendance.leaveQuantity}</leaveQuantity>
<absentQuantity>${attendance.absentQuantity}</absentQuantity>
<yearOfRestDay>${attendance.yearOfRestDay}</yearOfRestDay>
<turnOfRestDay>${attendance.turnOfRestDay}</turnOfRestDay>
<autoAemissionQuantity>${attendance.autoAemissionQuantity}</autoAemissionQuantity>
<fireQuantity>${attendance.fireQuantity}</fireQuantity>
<outQuantity>${attendance.outQuantity}</outQuantity>
<newInQuantity>${attendance.newInQuantity}</newInQuantity>
<jobTime>${attendance.jobTime}</jobTime>
<jobOvertime>${attendance.jobOvertime}</jobOvertime>
<nonjobTime>${attendance.nonjobTime}</nonjobTime>
<nonjobOvertime>${attendance.nonjobOvertime}</nonjobOvertime>
<remark>${attendance.remark}</remark>
</attendanceItem>
</#list>
</attendanceList>
</pes>