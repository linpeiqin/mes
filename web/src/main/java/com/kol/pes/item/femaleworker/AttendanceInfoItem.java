/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class AttendanceInfoItem extends DataItem {

    public String forecastWorkMan;		//预计出勤人数
    public String factWorkMan;		//实际出勤人数
    public String leaveQuantity;		//请假工时
    public String absentQuantity ;		//旷工工时
    public String yearOfRestDay;		//年休
    public String turnOfRestDay;		//轮休
    public String autoAemissionQuantity;		//自离
    public String fireQuantity;		//辞工解雇
    public String outQuantity;		//出差
    public String newInQuantity;		//新进员工数
    public String jobTime;		//生产正常工时
    public String jobOvertime;		//生产加班工时
    public String nonjobTime;		//非生产正常工时
    public String nonjobOvertime;		//非生产加班工时
    public String remark;		//备注

    public String getForecastWorkMan() {
        return forecastWorkMan;
    }

    public String getFactWorkMan() {
        return factWorkMan;
    }

    public String getLeaveQuantity() {
        return leaveQuantity;
    }

    public String getAbsentQuantity() {
        return absentQuantity;
    }

    public String getYearOfRestDay() {
        return yearOfRestDay;
    }

    public String getTurnOfRestDay() {
        return turnOfRestDay;
    }

    public String getAutoAemissionQuantity() {
        return autoAemissionQuantity;
    }

    public String getFireQuantity() {
        return fireQuantity;
    }

    public String getOutQuantity() {
        return outQuantity;
    }

    public String getNewInQuantity() {
        return newInQuantity;
    }

    public String getJobTime() {
        return jobTime;
    }

    public String getJobOvertime() {
        return jobOvertime;
    }

    public String getNonjobTime() {
        return nonjobTime;
    }

    public String getNonjobOvertime() {
        return nonjobOvertime;
    }

    public String getRemark() {
        return remark;
    }
}
