package cn.kol.pes.model.parser.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.kol.pes.model.item.MeTimeReportActiveItem;

public class MeTimeReportGetDescInfoAdapter extends ItemAdapter<MeTimeReportActiveItem> {
	
	public String display;
    public int completeQty;
    public int scrapQty;
    public int errorCode;
    public String errorMsg;
    
    public List<String> reasonList = new ArrayList<String>();
}
