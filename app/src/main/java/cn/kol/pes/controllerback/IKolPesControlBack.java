/*-----------------------------------------------------------

-- PURPOSE

--    IKolPesControlBack是所有网络请求回调的父类.由于 此应用的网络请求数量相对较少，所以我们采用一个类接受所有网络请求回调。每个界面只需复写自己需要的回调即可。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.controllerback;

import java.util.HashMap;
import java.util.List;

import cn.kol.pes.model.item.KoAssetCheckAssetItem;
import cn.kol.pes.model.item.KoAssetCheckCheckItem;
import cn.kol.pes.model.item.KoAssetCheckItem;
import cn.kol.pes.model.item.KoAssetCheckSetAssetCheckItem;
import cn.kol.pes.model.item.KoAssetMachineFailItem;
import cn.kol.pes.model.item.KoChangedPartItem;
import cn.kol.pes.model.item.KoOpItem;
import cn.kol.pes.model.item.KoOpStartedItem;
import cn.kol.pes.model.item.KoPushMsgItem;
import cn.kol.pes.model.item.KoWeekItem;
import cn.kol.pes.model.item.MeAssetItem;
import cn.kol.pes.model.item.MeMaterialsReportNumItem;
import cn.kol.pes.model.item.MeProduceItem;
import cn.kol.pes.model.item.MeQaNeedFillItem;
import cn.kol.pes.model.item.MeSearchSeqListItem;
import cn.kol.pes.model.item.MeStartedSeqItem;
import cn.kol.pes.model.item.MeTimeReportSeqListItem;
import cn.kol.pes.model.item.femaleworker.MmGetAttendancesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareMtlsBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetNonDeclareTimesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGroupItem;
import cn.kol.pes.model.item.femaleworker.MmMtlPlanItem;
import cn.kol.pes.model.item.femaleworker.MmOrgItem;
import cn.kol.pes.model.item.femaleworker.MmReasonCodeItem;
import cn.kol.pes.model.item.femaleworker.MmRoutingItem;
import cn.kol.pes.model.item.femaleworker.MmWipItem;
import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;
import cn.kol.pes.model.parser.adapter.KoUpdateApkAdapter;
import cn.kol.pes.model.parser.adapter.MeGetAssetSeqInfoByAssetIdAdapter;
import cn.kol.pes.model.parser.adapter.MeGetDescInfoWhenSeqSelectedAdapter;
import cn.kol.pes.model.parser.adapter.MeGetDescInfoWhenStartingSeqClickedAdapter;
import cn.kol.pes.model.parser.adapter.MeGetPafteropWhenSeqSelectedAdapter;
import cn.kol.pes.model.parser.adapter.MeGetSeqInfoBySeqIdAdapter;
import cn.kol.pes.model.parser.adapter.MeLoginAdapter;
import cn.kol.pes.model.parser.adapter.MeMainProcessListAdapter;
import cn.kol.pes.model.parser.adapter.MeTimeReportGetDescInfoAdapter;
import cn.kol.pes.model.parser.adapter.MeTimeReportSeqListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmAttendanceFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareJobAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareMtlFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareTimeFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetMachineReportTimeAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmNonDeclareTimeFinishAdapter;


public interface IKolPesControlBack extends IControlBack {
	
	public void dateEnableBack(boolean isSuc, HashMap<String, KoWeekItem> weekMap, String msg);
    
	public void loginBack(boolean isSuc, MeLoginAdapter userData, String msg);
	
	public void getAssetInfoBack(boolean isSuc, KoAssetGetAssetInfoAdapter assetInfo, String msg);
	
	public void getMainProcessListBack(boolean isSuc, MeMainProcessListAdapter processData, String msg);

	public void getMachineReportTimeBack(boolean isSuc, MmGetMachineReportTimeAdapter processData, String msg);

	public void declareJobBack(boolean b, MmDeclareJobAdapter mDeclareJobAdapter, String message);

	public void setPasswordBack(boolean isSuc, String msg);
	
	public void getProduceListByProjectNumBack(boolean isSuc, List<MeProduceItem> produceList, String msg);
	
	public void getAssetSeqInfoByAssetIdBack(boolean isSuc, MeGetAssetSeqInfoByAssetIdAdapter assetInfo, String msg);
	
	public void getSeqInfoBySeqIdBack(boolean isSuc, MeGetSeqInfoBySeqIdAdapter assetInfo, String msg);
	
	public void getDescInfoWhenSeqSelectedBack(boolean isSuc, MeGetDescInfoWhenSeqSelectedAdapter descInfo, String msg);
	
	public void getDescInfoWhenStartingSeqClicked(boolean isSuc, MeGetDescInfoWhenStartingSeqClickedAdapter descInfo, String msg);
	
	public void getPafteropWhenStartingSeqClicked(boolean isSuc, MeGetPafteropWhenSeqSelectedAdapter descInfo, String msg);
	
	public void startSeqBack(boolean isSuc, String msg);
	
	public void getStartedSeqListBack(boolean isSuc, List<MeStartedSeqItem> produceList, String msg);
	
	public void getQaListBack(boolean isSuc, List<MeQaNeedFillItem> qaList, String msg);
	
	public void endSeqBack(boolean isSuc, String msg);
	
	public void receiveOrderGetQtyByIdBack(boolean isSuc, String qty, String msg);
	public void receiveOrderReceiveBack(boolean isSuc, String msg);
	public void receiveOrderRejectBack(boolean isSuc, String msg);
	
	
	
	
	
	
	
	
	public void getOpBack(boolean isSuc, List<KoOpItem> gongXuList, String msg);
	
	public void assetCheckAddBack(boolean isSuc, String checkId, String msg);
	
	public void assetInsertPicPathBack(boolean isSuc, String msg);
	
	public void assetCheckGetErrorListBack(boolean isSuc, String msg, List<KoAssetCheckItem> assetList);
	
	public void assetGetMachineFailListBack(boolean isSuc, String msg, List<KoAssetMachineFailItem> failList);
	
	public void assetCheckRepairBack(boolean isSuc, String msg);
	
	public void assetGetAssetInfoBack(boolean isSuc, String msg, KoAssetGetAssetInfoAdapter adapter);
	
	public void opGetMaxQuanBack(boolean isSuc, String maxQuan, List<KoAssetCheckItem> opAssetList, String msg);
	
	public void opStartBack(boolean isSuc, String maxQuan, String msg);
	
	public void opEndBack(boolean isSuc, String maxQuan, String msg);
	
	public void opStartedListBack(boolean isSuc, List<KoOpStartedItem> opList, boolean isOpCompleted, String curWorkingOpCode, String msg);
	public void opMoveAllListBack(boolean isSuc, List<KoOpStartedItem> opList, String msg);
	
	
	public void qaListByPlanIdBack(boolean isSuc, String incompleteQuan, String minStart, String maxEnd, List<MeQaNeedFillItem> qaList, String msg);
	
	public void qaListManualAddBack(boolean isSuc, String incompleteQuan, String minStart, String maxEnd, List<MeQaNeedFillItem> qaList, String msg);
	
	public void submitQaDataBack(boolean isSuc, String msg);
	
	public void getPushMsgListBack(boolean isSuc, List<KoPushMsgItem> msgList, String msg);
	
	public void getUpdateApkBack(boolean isSuc, KoUpdateApkAdapter updateData, String msg);
	
	public void getOpCheckBeforeStartBack(boolean isSuc, String timeBuffer, String msg);
	public void deleteStartedSeqBack(boolean isSuc, String msg);
	
	public void searchSeqListBack(boolean isSuc, List<MeSearchSeqListItem> seqList, String msg);
	
	public void timeReportDescInfoAndActiveListBack(boolean isSuc, MeTimeReportGetDescInfoAdapter info, String msg);
	public void timeReportGetSeqListBack(boolean isSuc, MeTimeReportSeqListAdapter info, String msg);
	public void timeReportCompleteBack(boolean isSuc, String msg);
	
	
	public void materialsReportSeqListBack(boolean isSuc, List<MeTimeReportSeqListItem> list, String msg);
	public void materialsReportGetDescBack(boolean isSuc, String display, String msg);
	public void materialsReportGetStandardNumBack(boolean isSuc, List<MeMaterialsReportNumItem> list, String msg);
	public void materialsReportGetNewNumBack(boolean isSuc, List<MeMaterialsReportNumItem> list, String msg);
	
	
	public void materialsReportGetMaterialDescBack(boolean isSuc, String display, String msg);
	public void materialsReportCompleteBack(boolean isSuc, String msg);
	public void holidayOnOffBack(boolean isSuc, String msg);
	
	public void getAssetListBack(boolean isSuc, List<MeAssetItem> list, String msg);

	public void getGroupListBack(boolean isSuc, List<MmGroupItem> list, String msg);

	public void getDateShiftBack(boolean isSuc, String date, String shift, String msg);
	
	
	
	
	
	
	public void getCheckItemListDataBack(boolean isSuc, List<KoAssetCheckCheckItem> checkList, String msg);
	
	public void assetCheckGetAssetInfoBack(boolean isSuc, String msg, KoAssetGetAssetInfoAdapter adapter);
	
	public void assetCheckSubmitCheckBack(boolean isSuc, String msg);
	
	public void assetCheckAssetListBack(boolean isSuc, List<KoAssetCheckAssetItem> assetList, String msg);
	
	public void assetCheckGetSetAssetCheckListBack(boolean isSuc, List<KoAssetCheckSetAssetCheckItem> assetList, String msg);
	
	public void assetCheckUpdateSetAssetCheckListBack(boolean isSuc, String msg);
	
	public void assetCheckGetChangedPartsBack(boolean isSuc, List<KoChangedPartItem> partList, String msg);
	
	public void assetCheckCancelCheckAssetBack(boolean isSuc, String msg);


    void getRoutingListBack(boolean equals, List<MmRoutingItem> list, String message);


	void getAttendanceFinishBack(boolean equals, MmAttendanceFinishAdapter mMmAttendanceFinishAdapter, String message);

    void getDeclareTimeFinishBack(boolean equals, MmDeclareTimeFinishAdapter mMmDeclareTimeFinishAdapter, String message);

    void getReasonCodeListBack(boolean equals, List<MmReasonCodeItem> list, String message);

    void getNonDeclareTimeFinishBack(boolean equals, MmNonDeclareTimeFinishAdapter mMmNonDeclareTimeFinishAdapter, String message);

    void getMtlPlanListBack(boolean equals, List<MmMtlPlanItem> list, String message);

    void getDeclareTimeListBack(boolean equals, List<MmGetDeclareTimesBackItem> list, String message);

    void getDeclareMtlFinishBack(boolean equals, MmDeclareMtlFinishAdapter mMmDeclareMtlFinishAdapter, String message);

    void getNonDeclareTimeListBack(boolean equals, List<MmGetNonDeclareTimesBackItem> list, String message);

    void getDeclareMtlListBack(boolean equals, List<MmGetDeclareMtlsBackItem> list, String message);

    void getAttendanceListBack(boolean equals, List<MmGetAttendancesBackItem> list, String message);

    void getOrgListBack(boolean equals, List<MmOrgItem> list, String message);

    void getWipListBack(boolean equals, List<MmWipItem> list, String message);

    void getDeclareTimeListToMtlBack(boolean equals, List<MmGetDeclareTimesBackItem> list, String message);
}
