package cn.kol.pes.model.item.femaleworker;

import cn.kol.pes.model.item.Item;

public class MmGetAttendancesBackItem extends Item {

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

	public String showMsg(){

		String msg = "[预计出勤人数:" + forecastWorkMan +"|实际出勤人数:"+factWorkMan + "|请假工时:"+  leaveQuantity+
				    "|旷工工时:"+absentQuantity+"|年休:"+yearOfRestDay+"|轮休:"+turnOfRestDay+
					"|自离:"+autoAemissionQuantity+"|辞工解雇:"+fireQuantity+"|出差"+outQuantity+"|新进员工数:"+newInQuantity+
					"|生产正常工时:"+jobTime+"|生产加班工时:"+jobOvertime+"|非生产正常工时:"+nonjobTime+"|非生产加班工时:"+nonjobOvertime+
					"|备注:"+remark+"]<br>";
		return msg;
	}

}
