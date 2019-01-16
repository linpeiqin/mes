package cn.kol.pes.model.param;

public class MeHolidayOnOffParams extends KoHttpParams {
	
	public MeHolidayOnOffParams(String staffNo) {
		
		setParam("uri", "holiday_on_off");
		
		setParam("staffNo", staffNo);
	}
}
