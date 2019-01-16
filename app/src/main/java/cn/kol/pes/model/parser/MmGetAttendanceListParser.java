package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.femaleworker.MmGetAttendancesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesBackItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetAttendanceListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetDeclareTimeListAdapter;

public class MmGetAttendanceListParser extends DefaultBasicParser<MmGetAttendanceListAdapter> {



	final String ATTENDANCE_ITEM = "attendanceItem";
	final String FORECAST_WORK_MAN = "forecastWorkMan";
	final String FACT_WORK_MAN = "factWorkMan";
	final String LEAVE_QUANTITY = "leaveQuantity";
	final String ABSENT_QUANTITY = "absentQuantity";
	final String YEAR_OF_REST_DAY = "yearOfRestDay";
	final String TURN_OF_REST_DAY = "turnOfRestDay";
	final String AUTO_AEMISSION_QUANTITY = "autoAemissionQuantity";
	final String FIRE_QUANTITY = "fireQuantity";
	final String OUT_QUANTITY = "outQuantity";
	final String NEW_IN_QUANTITY = "newInQuantity";
	final String JOB_TIME = "jobTime";
	final String JOB_OVERTIME = "jobOvertime";
	final String NONJOB_TIME = "nonjobTime";
	final String NONJOB_OVERTIME = "nonjobOvertime";
	final String REMARK = "remark";


	public MmGetAttendanceListParser(MmGetAttendanceListAdapter adapter) {
		this.adapter = adapter;
	}

	MmGetAttendancesBackItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		 if(localName.equals(ATTENDANCE_ITEM)){
			item  = new MmGetAttendancesBackItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ATTENDANCE_ITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(FORECAST_WORK_MAN)){
			item.forecastWorkMan = (mBuffer.toString().trim());
		}
		else if(localName.equals(FACT_WORK_MAN)){
			item.factWorkMan = (mBuffer.toString().trim());
		}
		else if(localName.equals(LEAVE_QUANTITY)){
			item.leaveQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(ABSENT_QUANTITY)){
			item.absentQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(YEAR_OF_REST_DAY)){
			item.yearOfRestDay = (mBuffer.toString().trim());
		}
		else if(localName.equals(TURN_OF_REST_DAY)){
			item.turnOfRestDay = (mBuffer.toString().trim());
		}
		else if(localName.equals(AUTO_AEMISSION_QUANTITY)){
			item.autoAemissionQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(FIRE_QUANTITY)){
			item.fireQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(OUT_QUANTITY)){
			item.outQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(NEW_IN_QUANTITY)){
			item.newInQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB_TIME)){
			item.jobTime = (mBuffer.toString().trim());
		}
		else if(localName.equals(JOB_OVERTIME)){
			item.jobOvertime = (mBuffer.toString().trim());
		}
		else if(localName.equals(NONJOB_TIME)){
			item.nonjobTime = (mBuffer.toString().trim());
		}
		else if(localName.equals(NONJOB_OVERTIME)){
			item.nonjobOvertime = (mBuffer.toString().trim());
		}
		else if(localName.equals(REMARK)){
			item.remark = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
