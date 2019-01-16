package cn.kol.pes.model.param;

public class MeGetProduceListByProjectNumParams extends KoHttpParams {
	
	public MeGetProduceListByProjectNumParams(String projectNum){
		
		setParam("uri", "get_produce_list_by_project_num");
		
		setParam("projectNum", projectNum);
	}
}
