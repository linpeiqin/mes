/*-----------------------------------------------------------

-- PURPOSE

--    KolPesNetReqControl是所有网络请求帮助类.由于 此应用的网络请求数量相对较少，所以我们采用一个帮助类管理所有网络请求。每个界面调用自己需要的请求即可。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.controller;

import android.util.Xml;
import android.util.Xml.Encoding;

import java.io.InputStream;
import java.util.List;

import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.controllerback.IKolPesControlBack;
import cn.kol.pes.model.NetworkManager;
import cn.kol.pes.model.NetworkManager.OnDataParseListener;
import cn.kol.pes.model.NetworkManager.OnHttpDownloadListener;
import cn.kol.pes.model.item.KoAssetCheckCheckItem;
import cn.kol.pes.model.item.KoAssetCheckSetAssetCheckItem;
import cn.kol.pes.model.item.femaleworker.MmAttendanceItem;
import cn.kol.pes.model.item.femaleworker.MmDeclareMtlItem;
import cn.kol.pes.model.item.femaleworker.MmDeclareTimeItem;
import cn.kol.pes.model.item.femaleworker.MmGetAttendancesItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareMtlsItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesItem;
import cn.kol.pes.model.item.femaleworker.MmGetNonDeclareTimesItem;
import cn.kol.pes.model.item.femaleworker.MmMainListItem;
import cn.kol.pes.model.item.femaleworker.MmNonDeclareTimeItem;
import cn.kol.pes.model.param.KoAssetCheckAddParams;
import cn.kol.pes.model.param.KoAssetCheckAssetListParams;
import cn.kol.pes.model.param.KoAssetCheckCancelCheckAssetParams;
import cn.kol.pes.model.param.KoAssetCheckCheckItemParams;
import cn.kol.pes.model.param.KoAssetCheckGetAssetInfoParams;
import cn.kol.pes.model.param.KoAssetCheckGetChangedPartsParams;
import cn.kol.pes.model.param.KoAssetCheckGetSetAssetListParams;
import cn.kol.pes.model.param.KoAssetCheckSubmitCheckParams;
import cn.kol.pes.model.param.KoAssetCheckUpdateSetAssetListParams;
import cn.kol.pes.model.param.KoAssetGetAssetInfoParams;
import cn.kol.pes.model.param.KoAssetGetCheckErrorListParams;
import cn.kol.pes.model.param.KoAssetInsertPicPathParams;
import cn.kol.pes.model.param.KoAssetMachineFailListParams;
import cn.kol.pes.model.param.KoAssetRepairParams;
import cn.kol.pes.model.param.KoDataEnableParams;
import cn.kol.pes.model.param.KoGetMainProcessListParams;
import cn.kol.pes.model.param.KoGetOpMoveAllListParams;
import cn.kol.pes.model.param.KoGetOpParams;
import cn.kol.pes.model.param.KoGetStartedOpListParams;
import cn.kol.pes.model.param.KoHttpParams;
import cn.kol.pes.model.param.KoLoginParams;
import cn.kol.pes.model.param.KoOpCheckBeforeStartParams;
import cn.kol.pes.model.param.KoOpEndParams;
import cn.kol.pes.model.param.KoOpMaxQuanParams;
import cn.kol.pes.model.param.KoOpStartParams;
import cn.kol.pes.model.param.KoPushMsgParams;
import cn.kol.pes.model.param.KoQaInsertDataParams;
import cn.kol.pes.model.param.KoQaListByPlanIdParams;
import cn.kol.pes.model.param.KoQaListManualAddParams;
import cn.kol.pes.model.param.KoSetPasswordParams;
import cn.kol.pes.model.param.KoUpdateApkParams;
import cn.kol.pes.model.param.MeDeleteStartedSeqParams;
import cn.kol.pes.model.param.MeEndSeqParams;
import cn.kol.pes.model.param.MeGetAssetInfoParams;
import cn.kol.pes.model.param.MeGetAssetListParams;
import cn.kol.pes.model.param.MeGetAssetSeqInfoByAssetIdParams;
import cn.kol.pes.model.param.MeGetDescInfoWhenSeqSelectedParams;
import cn.kol.pes.model.param.MeGetDescInfoWhenStartingSeqClickedParams;
import cn.kol.pes.model.param.MeGetPAfterOpWhenSeqSelectedParams;
import cn.kol.pes.model.param.MeGetProduceListByProjectNumParams;
import cn.kol.pes.model.param.MeGetSeqInfoBySeqIdParams;
import cn.kol.pes.model.param.MeGetStartedSeqListParams;
import cn.kol.pes.model.param.MeHolidayOnOffParams;
import cn.kol.pes.model.param.MeMaterialsReportCompleteParams;
import cn.kol.pes.model.param.MeMaterialsReportGetDescParams;
import cn.kol.pes.model.param.MeMaterialsReportGetNewMaterialDescParams;
import cn.kol.pes.model.param.MeMaterialsReportGetNewNumParams;
import cn.kol.pes.model.param.MeMaterialsReportGetStandardNumParams;
import cn.kol.pes.model.param.MeMaterialsReportSeqListParams;
import cn.kol.pes.model.param.MeQaListNeedFillParams;
import cn.kol.pes.model.param.MeReceiveOrderGetQtyByIdParams;
import cn.kol.pes.model.param.MeReceiveOrderReceiveParams;
import cn.kol.pes.model.param.MeReceiveOrderRejectParams;
import cn.kol.pes.model.param.MeSearchSeqListParams;
import cn.kol.pes.model.param.MeStartSeqParams;
import cn.kol.pes.model.param.MeTimeReportCompleteParams;
import cn.kol.pes.model.param.MeTimeReportGetDescInfoWhenStartParams;
import cn.kol.pes.model.param.MeTimeReportGetProduceInfoAndSeqListParams;
import cn.kol.pes.model.param.MegetDateShiftParams;
import cn.kol.pes.model.param.femaleworker.MmAttendanceFinishParams;
import cn.kol.pes.model.param.femaleworker.MmDeclareJobParams;
import cn.kol.pes.model.param.femaleworker.MmDeclareMtlFinishParams;
import cn.kol.pes.model.param.femaleworker.MmDeclareTimeFinishParams;
import cn.kol.pes.model.param.femaleworker.MmGetAssetListParams;
import cn.kol.pes.model.param.femaleworker.MmGetAttendanceListParams;
import cn.kol.pes.model.param.femaleworker.MmGetDeclareMtlListParams;
import cn.kol.pes.model.param.femaleworker.MmGetDeclareTimeListParams;
import cn.kol.pes.model.param.femaleworker.MmGetGroupListParams;
import cn.kol.pes.model.param.femaleworker.MmGetMachineReportTimeParams;
import cn.kol.pes.model.param.femaleworker.MmGetMainProcessListParams;
import cn.kol.pes.model.param.femaleworker.MmGetMtlNewListParams;
import cn.kol.pes.model.param.femaleworker.MmGetMtlPlanListParams;
import cn.kol.pes.model.param.femaleworker.MmGetNonDeclareTimeListParams;
import cn.kol.pes.model.param.femaleworker.MmGetNonReasonCodeListParams;
import cn.kol.pes.model.param.femaleworker.MmGetOrgListParams;
import cn.kol.pes.model.param.femaleworker.MmGetReasonCodeListParams;
import cn.kol.pes.model.param.femaleworker.MmGetRoutingListParams;
import cn.kol.pes.model.param.femaleworker.MmGetWipListParams;
import cn.kol.pes.model.param.femaleworker.MmNonDeclareTimeFinishParams;
import cn.kol.pes.model.parser.KoAssetCheckAddParser;
import cn.kol.pes.model.parser.KoAssetCheckAssetItemParser;
import cn.kol.pes.model.parser.KoAssetCheckChangedPartsListParser;
import cn.kol.pes.model.parser.KoAssetCheckCheckItemParser;
import cn.kol.pes.model.parser.KoAssetCheckRepairParser;
import cn.kol.pes.model.parser.KoAssetCheckSetAssetCheckItemParser;
import cn.kol.pes.model.parser.KoAssetCheckSubmitCheckParser;
import cn.kol.pes.model.parser.KoAssetCheckUpdateSetAssetCheckParser;
import cn.kol.pes.model.parser.KoAssetGetAssetInfoParser;
import cn.kol.pes.model.parser.KoAssetGetCheckErrorListParser;
import cn.kol.pes.model.parser.KoAssetInsertPicPathParser;
import cn.kol.pes.model.parser.KoAssetMachineFailListParser;
import cn.kol.pes.model.parser.KoCheckAssetCancelCheckAssetParser;
import cn.kol.pes.model.parser.KoDataEnableParser;
import cn.kol.pes.model.parser.KoGetOpListParser;
import cn.kol.pes.model.parser.KoGetOpStartedListParser;
import cn.kol.pes.model.parser.KoOpCheckBeforeStartParser;
import cn.kol.pes.model.parser.KoOpEndParser;
import cn.kol.pes.model.parser.KoOpMaxQuanParser;
import cn.kol.pes.model.parser.KoPushMsgListParser;
import cn.kol.pes.model.parser.KoQaInsertDataParser;
import cn.kol.pes.model.parser.KoSetPasswordParser;
import cn.kol.pes.model.parser.KoUpdateApkParser;
import cn.kol.pes.model.parser.MGetAssetListParser;
import cn.kol.pes.model.parser.MGetStartedSeqListParser;
import cn.kol.pes.model.parser.MeCodeMsgParser;
import cn.kol.pes.model.parser.MeDateShiftParser;
import cn.kol.pes.model.parser.MeGetAssetSeqInfoByAssetIdParser;
import cn.kol.pes.model.parser.MeGetDescInfoWhenSeqSelectedParser;
import cn.kol.pes.model.parser.MeGetDescInfoWhenStartingSeqClickedParser;
import cn.kol.pes.model.parser.MeGetProduceListByProjectNumParser;
import cn.kol.pes.model.parser.MeGetSeqInfoBySeqIdParser;
import cn.kol.pes.model.parser.MeLoginParser;
import cn.kol.pes.model.parser.MeMainProcessListParser;
import cn.kol.pes.model.parser.MeMaterialsReportDisplayParser;
import cn.kol.pes.model.parser.MeMaterialsReportNumParser;
import cn.kol.pes.model.parser.MeOpDeleteOpParser;
import cn.kol.pes.model.parser.MePafteropWhenSeqSelectedParser;
import cn.kol.pes.model.parser.MeQaListNeedFillParser;
import cn.kol.pes.model.parser.MeReceiveOrderGetQtyByIdParser;
import cn.kol.pes.model.parser.MeSearchSeqListParser;
import cn.kol.pes.model.parser.MeTimeReportGetDescInfoParser;
import cn.kol.pes.model.parser.MeTimeReportSeqListParser;
import cn.kol.pes.model.parser.MmAttendanceFinishParser;
import cn.kol.pes.model.parser.MmDeclareJobParser;
import cn.kol.pes.model.parser.MmDeclareMtlFinishParser;
import cn.kol.pes.model.parser.MmDeclareTimeFinishParser;
import cn.kol.pes.model.parser.MmGetAttendanceListParser;
import cn.kol.pes.model.parser.MmGetDeclareMtlListParser;
import cn.kol.pes.model.parser.MmGetDeclareTimeListParser;
import cn.kol.pes.model.parser.MmGetGroupListParser;
import cn.kol.pes.model.parser.MmGetMachineReportTimeParser;
import cn.kol.pes.model.parser.MmGetMtlPlanListParser;
import cn.kol.pes.model.parser.MmGetNonDeclareTimeListParser;
import cn.kol.pes.model.parser.MmGetOrgListParser;
import cn.kol.pes.model.parser.MmGetReasonCodeListParser;
import cn.kol.pes.model.parser.MmGetRoutingListParser;
import cn.kol.pes.model.parser.MmGetWipListParser;
import cn.kol.pes.model.parser.MmNonDeclareTimeFinishParser;
import cn.kol.pes.model.parser.adapter.KoAssetCheckAddAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckAssetItemAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckCancelCheckAssetAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckChangedPartsListAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckCheckItemAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckRepairAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckSetAssetCheckAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckSubmitCheckAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckUpdateSetAssetCheckAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetGetCheckErrorListAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetInsertPicPathAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetMachineFailListAdapter;
import cn.kol.pes.model.parser.adapter.KoDataEnableAdapter;
import cn.kol.pes.model.parser.adapter.KoGetJobListAdapter;
import cn.kol.pes.model.parser.adapter.KoGetOpListAdapter;
import cn.kol.pes.model.parser.adapter.KoGetOpStartedListAdapter;
import cn.kol.pes.model.parser.adapter.KoOpCheckBeforeStartAdapter;
import cn.kol.pes.model.parser.adapter.KoOpEndAdapter;
import cn.kol.pes.model.parser.adapter.KoOpMaxQuanAdapter;
import cn.kol.pes.model.parser.adapter.KoPushMsgAdapter;
import cn.kol.pes.model.parser.adapter.KoQaInsertDataAdapter;
import cn.kol.pes.model.parser.adapter.KoSetPasswordAdapter;
import cn.kol.pes.model.parser.adapter.KoUpdateApkAdapter;
import cn.kol.pes.model.parser.adapter.MeCodeMsgAdapter;
import cn.kol.pes.model.parser.adapter.MeGetAssetListAdapter;
import cn.kol.pes.model.parser.adapter.MeGetAssetSeqInfoByAssetIdAdapter;
import cn.kol.pes.model.parser.adapter.MeGetDateShiftAdapter;
import cn.kol.pes.model.parser.adapter.MeGetDescInfoWhenSeqSelectedAdapter;
import cn.kol.pes.model.parser.adapter.MeGetDescInfoWhenStartingSeqClickedAdapter;
import cn.kol.pes.model.parser.adapter.MeGetPafteropWhenSeqSelectedAdapter;
import cn.kol.pes.model.parser.adapter.MeGetProduceListByProjectNumAdapter;
import cn.kol.pes.model.parser.adapter.MeGetSeqInfoBySeqIdAdapter;
import cn.kol.pes.model.parser.adapter.MeGetStartedSeqListAdapter;
import cn.kol.pes.model.parser.adapter.MeLoginAdapter;
import cn.kol.pes.model.parser.adapter.MeMainProcessListAdapter;
import cn.kol.pes.model.parser.adapter.MeMaterialsReportDisplayAdapter;
import cn.kol.pes.model.parser.adapter.MeMaterialsReportNumAdapter;
import cn.kol.pes.model.parser.adapter.MeOpDeleteOpAdapter;
import cn.kol.pes.model.parser.adapter.MeQaListNeedFillAdapter;
import cn.kol.pes.model.parser.adapter.MeReceiveOrderGetQtyByIdAdapter;
import cn.kol.pes.model.parser.adapter.MeSearchSeqListAdapter;
import cn.kol.pes.model.parser.adapter.MeTimeReportGetDescInfoAdapter;
import cn.kol.pes.model.parser.adapter.MeTimeReportSeqListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmAttendanceFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareJobAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareMtlFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareTimeFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetAttendanceListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetDeclareMtlListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetDeclareTimeListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetGroupListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetMachineReportTimeAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetMtlPlanListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetNonDeclareTimeListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetOrgListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetReasonCodeListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetRoutingListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetWipListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmNonDeclareTimeFinishAdapter;
import cn.kol.pes.service.BasicSimpleService;
import cn.kol.pes.service.IService;
import cn.kol.pes.service.ServiceFactory;


public class KolPesNetReqControl extends BaseControl implements OnHttpDownloadListener, OnDataParseListener {

    private static final String tag = "KolPesNetReqControl";
    
    private static final String SUC_CODE = "0";
    
    private IKolPesControlBack mIKolPesNetReqControlBack;//是具体网络请求的回调
    
    private BasicSimpleService mBaseService;
    
    private int mDataEnableServiceId = -1;
    private int mLoginServiceId = -1;
    private int mSetPwdServiceId = -1;
    private int mGetAssetInfoServiceId = -1;
    private int mGetAssetListServiceId = -1;
    private int mAttendanceFinishServiceId = -1;
    private int mDeclareTimeFinishServiceId = -1;
    private int mDeclareMtlFinishServiceId = -1;
    private int mNonDeclareTimeFinishServiceId = -1;
    private int mGetGroupListServiceId = -1;
    private int mGetOrgListServiceId = -1;
    private int mGetWipListServiceId = -1;
    private int mGetRoutingListServiceId = -1;
    private int mGetMtlPlanListServiceId = -1;
    private int mGetMtlNewListServiceId = -1;
    private int mGetReasonCodeListServiceId = -1;
    private int mGetDeclareTimeListServiceId = -1;
    private int mGetDeclareTimeListToMtlServiceId = -1;
    private int mGetAttendanceListServiceId = -1;
    private int mGetDeclareMtlListServiceId = -1;
    private int mGetNonDeclareTimeListServiceId = -1;
    private int mGetMainProcessListServiceId = -1;
    private int mGetProduceListByProjectNumServiceId = -1;
    private int mGetAssetSeqInfoByIdServiceId = -1;
    private int mGetSeqInfoBySeqIdServiceId = -1;
    private int mGetDescInfoWhenSeqSelectedServiceId = -1;
    private int mGetPAfterOpWhenSeqSelectedServiceId = -1;
    private int mGetDescInfoWhenStartingSeqClickedServiceId = -1;
    private int mStartSeqServiceId = -1;
    private int mGetStartedSeqListServiceId = -1;
    private int mGetQaListServiceId = -1;
    private int mEndSeqServiceId = -1;
    
    private int mReceiveOrderGetQtyByIdServiceId = -1;
    private int mReceiveOrderSureReceiveServiceId = -1;
    private int mReceiveOrderSureRejectServiceId = -1;
    
    private int mOpDeleteServiceId = -1;
    
    private int mSearchSeqListServiceId = -1;
    
    private int mTimeReportGetDescInfoWhenStartServiceId = -1;
    private int mTimeReportGetProduceInfoAndSeqListByProduceIdServiceId = -1;
    private int mTimeReportCompleteServiceId = -1;
    
    private int mMaterialsReportSeqListServiceId = -1;
    private int mMaterialsReportGetDescServiceId = -1;
    private int mMaterialsReportStandardNumServiceId = -1;
    private int mMaterialsReportNewNumServiceId = -1;
    private int mMaterialsReportGetMaterialDescServiceId = -1;
    private int mMaterialsReportCompleteServiceId = -1;
    
    private int mHolidayOnOffServiceId = -1;
    
    private int mGetDateShiftServiceId = -1;
    
    
    
    
    
    
    private int mGetOpListServiceId = -1;
    private int mAssetCheckAddServiceId = -1;
    private int mAssetInsertPicPathServiceId = -1;
    private int mGetAssetCheckListServiceId = -1;
    private int mAssetGetMachineFailListServiceId = -1;
    private int mAssetRepairServiceId = -1;
    private int mAssetGetAssetInfoServiceId = -1;
    private int mOpMaxQuanServiceId = -1;
    private int mOpStartServiceId = -1;
    private int mOpEndServiceId = -1;
    private int mGetOpStartedListServiceId = -1;
    private int mGetOpMoveAllListServiceId = -1;
    private int mQaListNeedFillListServiceId = -1;
    private int mQaListByPlanIdListServiceId = -1;
    private int mQaListManualAddListServiceId = -1;
    private int mQaInsertDataServiceId = -1;
    private int mGetMsgListServiceId = -1;
    private int mUpdateApkServiceId = -1;
    private int mGetMachineReportTimeServiceId = -1;
    private int mDeclareJobServiceId = -1;

    private int mOpCheckServiceId = -1;
    
    private int mAssetCheckGetAssetInfoServiceId = -1;
    private int mAssetCheckCancelCheckAssetServiceId = -1;
    private int mGetCheckItemListServiceId = -1;
    
    private int mAssetCheckSubmitCheckServiceId = -1;
    
    private int mAssetCheckGetAssetListServiceId = -1;
    
    private int mAssetCheckGetSetAssetListServiceId = -1;
    
    private int mAssetCheckUpdateSetAssetListServiceId = -1;
    
    private int mAssetCheckGetChangedPartsListServiceId = -1;
    
    
    
    
    private MeLoginAdapter mLoginAdapter = new MeLoginAdapter();
    private KoAssetGetAssetInfoAdapter mKoAssetGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
    private MeMainProcessListAdapter mKoMainProcessListAdapter = new MeMainProcessListAdapter();
    private MmGetMachineReportTimeAdapter mGetMachineReportTimeAdapter = new MmGetMachineReportTimeAdapter();
    private MmDeclareJobAdapter mDeclareJobAdapter = new MmDeclareJobAdapter();
    private KoSetPasswordAdapter mKoSetPasswordAdapter = new KoSetPasswordAdapter();
    private MeGetProduceListByProjectNumAdapter mMeGetProduceListByProjectNumAdapter = new MeGetProduceListByProjectNumAdapter();
    private MeGetAssetSeqInfoByAssetIdAdapter mMeGetAssetSeqInfoByAssetIdAdapter = new MeGetAssetSeqInfoByAssetIdAdapter();
    private MeGetSeqInfoBySeqIdAdapter mMeGetSeqInfoBySeqIdAdapter = new MeGetSeqInfoBySeqIdAdapter();
    private MeGetDescInfoWhenSeqSelectedAdapter mMeGetDescInfoWhenSeqSelectedAdapter = new MeGetDescInfoWhenSeqSelectedAdapter();
    private MeGetDescInfoWhenStartingSeqClickedAdapter mMeGetDescInfoWhenStartingSeqClickedAdapter = new MeGetDescInfoWhenStartingSeqClickedAdapter();
    private MeCodeMsgAdapter mMeStartSeqAdapter = new MeCodeMsgAdapter();
    private MeGetStartedSeqListAdapter mMeGetStartedSeqListAdapter = new MeGetStartedSeqListAdapter();
    private MeQaListNeedFillAdapter mKoQaListNeedFillAdapter = new MeQaListNeedFillAdapter();
    private MeCodeMsgAdapter mMeEndAdapter = new MeCodeMsgAdapter();
    
    private MeReceiveOrderGetQtyByIdAdapter mMeReceiveOrderGetQtyByIdAdapter = new MeReceiveOrderGetQtyByIdAdapter();
    private MeCodeMsgAdapter mMeReceiveOrderSureReceiveAdapter = new MeCodeMsgAdapter();
    private MeCodeMsgAdapter mMeReceiveOrderSureRejectAdapter = new MeCodeMsgAdapter();
    
    private MeSearchSeqListAdapter mMeSearchSeqListAdapter = new MeSearchSeqListAdapter();
    
    private MeTimeReportGetDescInfoAdapter mMeTimeReportGetDescInfoAdapter = new MeTimeReportGetDescInfoAdapter();
    private MeTimeReportSeqListAdapter mMeTimeReportProduceInfoAndSeqListAdapter = new MeTimeReportSeqListAdapter();
    private MeCodeMsgAdapter mMeTimeCompleteAdapter = new MeCodeMsgAdapter();
    
    private MeTimeReportSeqListAdapter mMeMaterialReportSeqListAdapter = new MeTimeReportSeqListAdapter();
    private MeMaterialsReportDisplayAdapter mMeMaterialsReportDisplayAdapter = new MeMaterialsReportDisplayAdapter();
    private MeMaterialsReportNumAdapter mMeMaterialsReportStandardNumAdapter = new MeMaterialsReportNumAdapter();
    private MeMaterialsReportNumAdapter mMeMaterialsReportNewNumAdapter = new MeMaterialsReportNumAdapter();
    private MeMaterialsReportDisplayAdapter mMeMaterialsReportMaterialDisplayAdapter = new MeMaterialsReportDisplayAdapter();
    private MeCodeMsgAdapter mMeMaterialsReportCompleteAdapter = new MeCodeMsgAdapter();
    private MeCodeMsgAdapter mMeHolidayOnOffeAdapter = new MeCodeMsgAdapter();
    private MeGetAssetListAdapter mMeAssetListAdapter = new MeGetAssetListAdapter();
    private MmGetGroupListAdapter mMmGroupListAdapter = new MmGetGroupListAdapter();
    private MmGetOrgListAdapter mMmOrgListAdapter = new MmGetOrgListAdapter();
    private MmGetWipListAdapter mMmWipListAdapter = new MmGetWipListAdapter();
    private MmGetRoutingListAdapter mMmGetRoutingListAdapter = new MmGetRoutingListAdapter();
    private MmGetMtlPlanListAdapter mMmGetMtlPlanListAdapter = new MmGetMtlPlanListAdapter();
    private MmGetReasonCodeListAdapter mMmGetReasonCodeListAdapter = new MmGetReasonCodeListAdapter();
    private MmGetDeclareTimeListAdapter mMmGetDeclareTimeListAdapter = new MmGetDeclareTimeListAdapter();
    private MmGetAttendanceListAdapter mMmGetAttendanceListAdapter = new MmGetAttendanceListAdapter();
    private MmGetDeclareMtlListAdapter mMmGetDeclareMtlListAdapter = new MmGetDeclareMtlListAdapter();
    private MmGetNonDeclareTimeListAdapter mMmGetNonDeclareTimeListAdapter = new MmGetNonDeclareTimeListAdapter();
    private MeGetDateShiftAdapter mMeDateShiftAdapter = new MeGetDateShiftAdapter();
    private MmAttendanceFinishAdapter mMmAttendanceFinishAdapter = new MmAttendanceFinishAdapter();
    private MmDeclareTimeFinishAdapter mMmDeclareTimeFinishAdapter = new MmDeclareTimeFinishAdapter();
    private MmDeclareMtlFinishAdapter mMmDeclareMtlFinishAdapter = new MmDeclareMtlFinishAdapter();
    private MmNonDeclareTimeFinishAdapter mMmNonDeclareTimeFinishAdapter = new MmNonDeclareTimeFinishAdapter();
    private MeGetPafteropWhenSeqSelectedAdapter mMeGetPafteropWhenSeqSelectedAdapter = new MeGetPafteropWhenSeqSelectedAdapter();
    
    
    
    
    
    private KoDataEnableAdapter mKoDataEnableAdapter = new KoDataEnableAdapter();
    private KoGetJobListAdapter mUpdateJobAdapter = new KoGetJobListAdapter();
    private KoGetOpListAdapter mKoGetOpListAdapter = new KoGetOpListAdapter();
    private KoAssetCheckAddAdapter mKoAssetCheckAddAdapter = new KoAssetCheckAddAdapter();
    private KoAssetInsertPicPathAdapter mKoAssetInsertPicPathAdapter = new KoAssetInsertPicPathAdapter();
    private KoAssetGetCheckErrorListAdapter mKoAssetGetCheckErrorListAdapter = new KoAssetGetCheckErrorListAdapter();
    private KoAssetMachineFailListAdapter mKoAssetMachineFailListAdapter = new KoAssetMachineFailListAdapter();
    private KoAssetCheckRepairAdapter mKoAssetCheckRepairAdapter = new KoAssetCheckRepairAdapter();
    private KoOpMaxQuanAdapter mKoOpMaxQuanAdapter = new KoOpMaxQuanAdapter();
    private MeCodeMsgAdapter mKoOpStartAdapter = new MeCodeMsgAdapter();
    private KoOpEndAdapter mKoOpEndAdapter = new KoOpEndAdapter();
    private KoGetOpStartedListAdapter mKoGetOpStartedListAdapter = new KoGetOpStartedListAdapter();
    private KoGetOpStartedListAdapter mKoGetOpMoveAllListAdapter = new KoGetOpStartedListAdapter();
    
    private MeQaListNeedFillAdapter mKoQaListManualAdapter = new MeQaListNeedFillAdapter();
    private MeQaListNeedFillAdapter mKoQaListByPlanIdAdapter = new MeQaListNeedFillAdapter();
    private KoQaInsertDataAdapter mKoQaInsertDataAdapter = new KoQaInsertDataAdapter();
    private KoPushMsgAdapter mKoPushMsgListAdapter = new KoPushMsgAdapter();
    private KoUpdateApkAdapter mKoUpdateApkAdapter = new KoUpdateApkAdapter();
    
    private KoOpCheckBeforeStartAdapter mKoOpCheckBeforeStartAdapter = new KoOpCheckBeforeStartAdapter();
    private MeOpDeleteOpAdapter mKoOpDeleteOpAdapter = new MeOpDeleteOpAdapter();
    
    private KoAssetCheckCheckItemAdapter mKoAssetCheckCheckItemAdapter = new KoAssetCheckCheckItemAdapter();
    private KoAssetGetAssetInfoAdapter mKoAssetCheckGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
    private KoAssetCheckSubmitCheckAdapter mKoAssetCheckSubmitCheckAdapter = new KoAssetCheckSubmitCheckAdapter();
    
    private KoAssetCheckAssetItemAdapter mKoAssetCheckAssetItemAdapter = new KoAssetCheckAssetItemAdapter();
    private KoAssetCheckSetAssetCheckAdapter mKoAssetCheckSetAssetCheckAdapter = new KoAssetCheckSetAssetCheckAdapter();
    private KoAssetCheckUpdateSetAssetCheckAdapter mKoAssetCheckUpdateSetAssetCheckAdapter = new KoAssetCheckUpdateSetAssetCheckAdapter();
    private KoAssetCheckChangedPartsListAdapter mKoAssetCheckChangedPartsListAdapter = new KoAssetCheckChangedPartsListAdapter();
    
    private KoAssetCheckCancelCheckAssetAdapter mKoAssetCheckCancelCheckAssetAdapter = new KoAssetCheckCancelCheckAssetAdapter();
    
 
    
    //任何Activity调用这个帮助类的同时，必须传入回调类的实例
    public KolPesNetReqControl(IKolPesControlBack iKolPesNetReqControlBack) {
    	NetworkManager.instance().stopRunningTask();
    	mBaseService = (BasicSimpleService) ServiceFactory.instant().createService(IService.SIMPLE);
        this.mIKolPesNetReqControlBack = iKolPesNetReqControlBack;
    }
    
    //设置回调的接口实现
    public void setBack(IKolPesControlBack iKolPesNetReqControlBack) {
    	this.mIKolPesNetReqControlBack = iKolPesNetReqControlBack;
    }
    
    //判断服务器数据库是否在更新
    public void isDataEnable() {
    	mDataEnableServiceId = mBaseService.getDataFromService(new KoDataEnableParams(), this, this);
    }
    
    //登陆请求2017
    public void login(String userId, String pwd) {
    	mLoginServiceId = mBaseService.getDataFromService(new KoLoginParams(userId, pwd), this, this);
    }
    
    //获取设备信息2017
    public void getAssetInfo(String staffNo, String resCode) {
    	mGetAssetInfoServiceId = mBaseService.getDataFromService(new MeGetAssetInfoParams(staffNo, resCode), this, this);
    }
    //获取排产计划列表2017
    public void getProcessList(String resCode, String scheduledate, String shift) {
        mGetMainProcessListServiceId = mBaseService.getDataFromService(new KoGetMainProcessListParams(resCode, scheduledate, shift), this, this);
    }
    //获取设备信息2017
    public void getAssetList(String staffNo) {
    	mGetAssetListServiceId = mBaseService.getDataFromService(new MeGetAssetListParams(staffNo), this, this);
    }

    //出勤状况完成
    public void attendanceFinish4F(MmAttendanceItem item) {
        mAttendanceFinishServiceId = mBaseService.getDataFromService(new MmAttendanceFinishParams(item), this, this);
    }
    //女工非生产状况完成
    public void nonDeclareTimeFinish4F(MmNonDeclareTimeItem item) {
        mNonDeclareTimeFinishServiceId = mBaseService.getDataFromService(new MmNonDeclareTimeFinishParams(item), this, this);
    }

    //女工生产状况完成
    public void declareTimeFinish4F(MmDeclareTimeItem item) {
        mDeclareTimeFinishServiceId = mBaseService.getDataFromService(new MmDeclareTimeFinishParams(item), this, this);
    }
    public void declareMtlFinish4F(MmDeclareMtlItem item) {
        mDeclareMtlFinishServiceId = mBaseService.getDataFromService(new MmDeclareMtlFinishParams(item), this, this);
    }
    //获取设备信息2018女工
    public void getAssetList4F(String staffNo) {
       mGetAssetListServiceId = mBaseService.getDataFromService(new MmGetAssetListParams(staffNo), this, this);
    }
    //获取组织列表
    public void getOrgList() {
        mGetOrgListServiceId = mBaseService.getDataFromService(new MmGetOrgListParams(), this, this);
    }
    //获取通过工程单号获取生产单号等信息
    public void getWipListByJobNo(String jobNo,String organizationId) {
        mGetWipListServiceId = mBaseService.getDataFromService(new MmGetWipListParams(jobNo,organizationId), this, this);
    }
    //获取组别信息2018女工
    public void getGroupList4F(String assetNumber){
        mGetGroupListServiceId = mBaseService.getDataFromService(new MmGetGroupListParams(assetNumber), this, this);
    }
    //获取女工细工序
    public void getRoutingList4F() {
        mGetRoutingListServiceId = mBaseService.getDataFromService(new MmGetRoutingListParams(), this, this);
    }
    //得到计划物料列表
    public void getMtlPlanList4F(String wipEntityId,String operationSeqNum,String organizationId) {
        mGetMtlPlanListServiceId = mBaseService.getDataFromService(new MmGetMtlPlanListParams( wipEntityId, operationSeqNum, organizationId), this, this);
    }
    //得到新增物料列表
    public void getMtlNewList4F(String wipEntityId, String mtlNo, String organizationId) {
        mGetMtlNewListServiceId = mBaseService.getDataFromService(new MmGetMtlNewListParams( wipEntityId, mtlNo, organizationId), this, this);
    }
    //获取生产状况坏货原因
    public void getReasonCodeList4F() {
        mGetReasonCodeListServiceId = mBaseService.getDataFromService(new MmGetReasonCodeListParams(), this, this);
    }
    //获取生产状况列表
    public void getDeclareTimeList4F(MmGetDeclareTimesItem declareTimesItem) {
        mGetDeclareTimeListServiceId = mBaseService.getDataFromService(new MmGetDeclareTimeListParams(declareTimesItem), this, this);
    }

    //获取生产状况列表4跳转物料报数
    public void getDeclareTimeListToMtl(MmGetDeclareTimesItem declareTimesItem) {
        mGetDeclareTimeListToMtlServiceId = mBaseService.getDataFromService(new MmGetDeclareTimeListParams(declareTimesItem), this, this);
    }
    //获取考勤状况列表
    public void getAttendanceList4F(MmGetAttendancesItem mmGetAttendancesItem) {
        mGetAttendanceListServiceId = mBaseService.getDataFromService(new MmGetAttendanceListParams(mmGetAttendancesItem), this, this);
    }
    //获取女工物料报数列表
    public void getDeclareMtlList4F(MmGetDeclareMtlsItem declareMtlsItem) {
        mGetDeclareMtlListServiceId = mBaseService.getDataFromService(new MmGetDeclareMtlListParams(declareMtlsItem), this, this);
    }
    //获取非生产状况列表
    public void getNonDeclareTimeList4F(MmGetNonDeclareTimesItem nonDeclareTimesItem) {
        mGetNonDeclareTimeListServiceId = mBaseService.getDataFromService(new MmGetNonDeclareTimeListParams(nonDeclareTimesItem), this, this);
    }

    //获取非生产状况坏货原因
    public void getNonReasonCodeList4F() {
        mGetReasonCodeListServiceId = mBaseService.getDataFromService(new MmGetNonReasonCodeListParams(), this, this);
    }
    //获取排产计划列表2018女工
    public void getProcessList4F(MmMainListItem mmMainProcessItem) {
        mGetMainProcessListServiceId = mBaseService.getDataFromService(new MmGetMainProcessListParams(mmMainProcessItem), this, this);
    }
    //获取女工班次时间段列表2018女工
    public void getMachineReportTime4F(String resCode, String scheduledate, String shift){
        mGetMachineReportTimeServiceId = mBaseService.getDataFromService(new MmGetMachineReportTimeParams(resCode, scheduledate, shift), this, this);
    }
    //女工建单2018女工
    public void declareJob4F(String organizationId,String jobId,String wipEntityId,String inventoryItemId,String scheduleDate,String workGroup,String workMonitor,String workClassCode){
        mDeclareJobServiceId = mBaseService.getDataFromService(new MmDeclareJobParams(organizationId,jobId,wipEntityId,inventoryItemId,scheduleDate,workGroup,workMonitor,workClassCode), this, this);
    }

    //2017根据工程单号获取排产计划列表
    public void getProduceListByProjectNum(String projectNum) {
    	mGetProduceListByProjectNumServiceId = mBaseService.getDataFromService(new MeGetProduceListByProjectNumParams(projectNum), this, this);
    }
    
    //2017根据设备id获取设备信息，
    public void getAssetSeqInfoByAssetId(String assetId) {
    	mGetAssetSeqInfoByIdServiceId = mBaseService.getDataFromService(new MeGetAssetSeqInfoByAssetIdParams(assetId), this, this);
    }
    
    //2017根据工序号获取工序描述
    public void getSeqInfoBySeqId(String assetCode, String wipId) {
    	mGetSeqInfoBySeqIdServiceId = mBaseService.getDataFromService(new MeGetSeqInfoBySeqIdParams(assetCode, wipId), this, this);
    }
    
    //2017
    public void getPAfterOpWhenSeqSelected(String wipId) {
    	
    	mGetPAfterOpWhenSeqSelectedServiceId = mBaseService.getDataFromService(new MeGetPAfterOpWhenSeqSelectedParams(wipId), this, this);
    }

    //2017
    public void getDescInfoWhenSeqSelected(String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String staffNo, String seqNum) {
    	
    	mGetDescInfoWhenSeqSelectedServiceId = mBaseService.getDataFromService(new MeGetDescInfoWhenSeqSelectedParams(wipId,opCode,assetId,schedDate, pAfterOp, staffNo, seqNum), this, this);
    }
    
    //2017
    public void getDescInfoWhenStartingSeqClicked(String wipId, String opCode, String assetId, String schedDate, String seqNum) {
    	
    	mGetDescInfoWhenStartingSeqClickedServiceId = mBaseService.getDataFromService(new MeGetDescInfoWhenStartingSeqClickedParams(wipId,opCode,assetId,schedDate, seqNum), this, this);
    }

    //2017
    public void startSeq(String staffNo, String inputQty, String startOpTime, String wipId, String opCode, String assetId, String schedDate, String pafterop, String seqNum, String pafteropSeqNum, String workClassCode) {
    	mStartSeqServiceId = mBaseService.getDataFromService(new MeStartSeqParams(staffNo, inputQty, startOpTime, wipId,opCode,assetId,schedDate,pafterop, seqNum, pafteropSeqNum, workClassCode), this, this);
    }
    
    //2017
    public void getStartedSeqList(String assetId) {
    	mGetStartedSeqListServiceId = mBaseService.getDataFromService(new MeGetStartedSeqListParams(assetId), this, this);
    }
    
    //2017//获取质量管理计划要填写的数据项信息  同时 检查是否可以完成工序
    public void getQaList(String opCode, String organizationId) {
    	mGetQaListServiceId = mBaseService.getDataFromService(new MeQaListNeedFillParams(opCode, organizationId), this, this);
    }
    
    //2017
    public void endSeq(String trxId, String wipId, String opCode, String planId, String staffNo, String inputQty, String scrapQty, String endTime, String qaListString, String workClassCode, String seqNum, String schedDate) {
    	mEndSeqServiceId = mBaseService.getDataFromService(new MeEndSeqParams(trxId, wipId, opCode, planId, staffNo, inputQty, scrapQty, endTime, qaListString, workClassCode, seqNum, schedDate), this, this);
    }
    
    //2017
    public void receiveOrderGetQtyById(String id, String staffNum) {
    	mReceiveOrderGetQtyByIdServiceId = mBaseService.getDataFromService(new MeReceiveOrderGetQtyByIdParams(id, staffNum), this, this);
    }
    
    //2017
    public void receiveOrderSureToReceive(String id, String qty, String staffNo) {
    	mReceiveOrderSureReceiveServiceId = mBaseService.getDataFromService(new MeReceiveOrderReceiveParams(id, qty, staffNo), this, this);
    }
    
    //2017
    public void receiveOrderSureReject(String id, String staffNo) {
    	mReceiveOrderSureRejectServiceId = mBaseService.getDataFromService(new MeReceiveOrderRejectParams(id, staffNo), this, this);
    }
    
    //2017删除一个工序
    public void deleteStartedSeq(String transactionId) {
    	mOpDeleteServiceId = mBaseService.getDataFromService(new MeDeleteStartedSeqParams(transactionId), this, this);
    }
    
    //2017
    public void searchSeqList(String wipId) {
    	mSearchSeqListServiceId = mBaseService.getDataFromService(new MeSearchSeqListParams(wipId), this, this);
    }
    
    //2017
    public void timeReportGetDescInfoWhenStart(String assetCode, String schedDate, String reportType, String wipId, String staffNo, String workClassCode, String opCode) {
    	mTimeReportGetDescInfoWhenStartServiceId = mBaseService.getDataFromService(new MeTimeReportGetDescInfoWhenStartParams(assetCode, schedDate, reportType, wipId, staffNo, workClassCode, opCode), this, this);
    }
    
    //2017
    public void timeReportGetSeqListByProduceId(String wipId, String assetId) {
    	mTimeReportGetProduceInfoAndSeqListByProduceIdServiceId = mBaseService.getDataFromService(new MeTimeReportGetProduceInfoAndSeqListParams(wipId, assetId), this, this);
    }
    
    //2017
    public void timeReportComplete(String p_sched_date, String assetCode, String staffNo, 
						    		String reportType, String wipId, String opCode, 
						    		String seqNum, String activityName, 
						    		String completeNum, String scrapNum, 
						    		String addTime, String addTimeReason, String workClassCode) {
    	
    	mTimeReportCompleteServiceId = mBaseService.getDataFromService(new MeTimeReportCompleteParams(p_sched_date, assetCode, staffNo, 
															    		 reportType, wipId, opCode, seqNum, activityName, 
															    		 completeNum, scrapNum, addTime, addTimeReason, workClassCode), this, this);
    }

    //2017
    public void materialsReportSeqList(String wipId) {
    	mMaterialsReportSeqListServiceId = mBaseService.getDataFromService(new MeMaterialsReportSeqListParams(wipId), this, this);
    }
    
    public void materialsReportGetDesc(String wipId, String type, String seqNum) {
    	mMaterialsReportGetDescServiceId = mBaseService.getDataFromService(new MeMaterialsReportGetDescParams(wipId, type, seqNum), this, this);
    }
    
    public void materialsReportGetStandardNum(String wipId, String seqNum) {
    	mMaterialsReportStandardNumServiceId = mBaseService.getDataFromService(new MeMaterialsReportGetStandardNumParams(wipId, seqNum), this, this);
    }
    
    public void materialsReportGetNewNum(String wipId, String keyWords) {
    	mMaterialsReportNewNumServiceId = mBaseService.getDataFromService(new MeMaterialsReportGetNewNumParams(wipId, keyWords), this, this);
    }
    
    public void materialsReportGetNewMaterialDesc(String wipId, String itemId) {
    	mMaterialsReportGetMaterialDescServiceId = mBaseService.getDataFromService(new MeMaterialsReportGetNewMaterialDescParams(wipId, itemId), this, this);
    }
    
    public void materialsReportComplete(String assetCode, String staffNo, String reportType, String wipId, String opCode, String seqNum, String itemId, String trxQty, String remark, String schedDate) {
    	mMaterialsReportCompleteServiceId = mBaseService.getDataFromService(new MeMaterialsReportCompleteParams(assetCode, staffNo, reportType, wipId, opCode, seqNum, itemId, trxQty, remark, schedDate), this, this);
    }
    
    public void holidayOnOff(String staffNo) {
    	mHolidayOnOffServiceId = mBaseService.getDataFromService(new MeHolidayOnOffParams(staffNo), this, this);
    }
    
    public void getDateShift() {
    	mGetDateShiftServiceId = mBaseService.getDataFromService(new MegetDateShiftParams(), this, this);
    }
    
    //获取apk升级信息 2017
    public void getUpdateApk(String apkPadVersion) {
    	mUpdateApkServiceId = mBaseService.getDataFromService(new KoUpdateApkParams(apkPadVersion), this, this);
    }
    
    
    
    
    
    
    
    public void setPassword(String userId, String pwd) {
    	mSetPwdServiceId = mBaseService.getDataFromService(new KoSetPasswordParams(userId, pwd), this, this);
    }
    
    //获取工序
    public void getOp(String seqId) {
    	mGetOpListServiceId = mBaseService.getDataFromService(new KoGetOpParams(seqId), this, this);
    }
    
    //添加点检设备记录
    public void assetCheckAdd(String CREATION_DATE, String CREATED_BY, 
							 String LAST_UPDATE_DATE, String LAST_UPDATED_BY,
							 String ASSET_ID, String CHECK_TIME, String CHECK_RESULT, 
							 String EST_REPAIR_START, String EST_REPAIR_END, 
							 String CHECK_REMARKS) {
    	
    	mAssetCheckAddServiceId = mBaseService.getDataFromService(new KoAssetCheckAddParams(CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, ASSET_ID, CHECK_TIME, CHECK_RESULT, EST_REPAIR_START, EST_REPAIR_END, CHECK_REMARKS), this, this);
    }
    
    //上传图片信息
    public void assetInserPicPath(String checkId, String picPathDescList, String isEnd, String isSeq) {
    	mAssetInsertPicPathServiceId = mBaseService.getDataFromService(new KoAssetInsertPicPathParams(checkId, picPathDescList, isEnd, isSeq), this, this);
    }
    
    //获取存在故障的设备的点检列表
    public void getAssetCheckErrorList() {
    	mGetAssetCheckListServiceId = mBaseService.getDataFromService(new KoAssetGetCheckErrorListParams(), this, this);
    }
    
    //获取相应设备的故障类型
    public void getAssetMachineFailList(String tagNum) {
    	mAssetGetMachineFailListServiceId = mBaseService.getDataFromService(new KoAssetMachineFailListParams(tagNum), this, this);
    }
    
    //更新设备维修信息
    public void assetRepair(String CHECK_ID, String LAST_UPDATE_DATE, String LAST_UPDATED_BY,
    					    String ACT_REPAIR_START, String ACT_REPAIR_END, String DOWN_TIME, 
							String REPAIR_REMARKS, String FAILURE_CODE, String changedParts, String opCode) {
    	
    	mAssetRepairServiceId = mBaseService.getDataFromService(
											    			new KoAssetRepairParams(CHECK_ID, LAST_UPDATE_DATE, LAST_UPDATED_BY, 
																	    			ACT_REPAIR_START, ACT_REPAIR_END, DOWN_TIME, 
																	    			REPAIR_REMARKS, FAILURE_CODE, changedParts, opCode), 
														    this, this);
    }
    
    //根据设备id获取设备相关信息
    public void assetGetAssetInfo(String id) {
    	mAssetGetAssetInfoServiceId = mBaseService.getDataFromService(new KoAssetGetAssetInfoParams(id), this, this);
    }
    
    //开始工序前，获取最大可投入数
    public void opGetMaxQuan(String wipEntityName, String wipEntityId, String seqId, String fmOperationCode, String curOperationId, String canJump) {
    	mOpMaxQuanServiceId = mBaseService.getDataFromService(new KoOpMaxQuanParams(wipEntityName, wipEntityId, seqId, fmOperationCode, curOperationId, canJump), this, this);
    }
    
    //开始工序
    public void opStart(String createdBy, String lastUpdatedBy, 
						String wipEntityId, String wipEntityName, String fmOperationCode, String curOperationId,
						String trxQuantity,
						List<String> assetIdList, String opStart, String seqId, String canJump) {
    	
    	mOpStartServiceId = mBaseService.getDataFromService(new KoOpStartParams(createdBy, lastUpdatedBy, 
																    			wipEntityId, wipEntityName, fmOperationCode, curOperationId,
																    			trxQuantity,
																    			assetIdList, opStart, seqId, canJump), 
														    this, this);
    }
    
    //完成工序
    public void opEnd(String transactionId, String scrapQuantity, String opEnd, String lastUpdatedBy, String organizationId, String wipId, String opCode) {
    	
    	mOpEndServiceId = mBaseService.getDataFromService(new KoOpEndParams(transactionId, scrapQuantity, opEnd, lastUpdatedBy, organizationId, wipId, opCode), 
														    this, this);
    }
    
    //获取已开启工序的列表
    public void getStartedOpList(String lastUpdatedByOrAssetId) {
    	
    	mGetOpStartedListServiceId = mBaseService.getDataFromService(new KoGetStartedOpListParams(lastUpdatedByOrAssetId), this, this);
    }
    
    //获取特定工序所有的工序列表
    public void getOpMoveAllList(String wipId) {
    	
    	mGetOpMoveAllListServiceId = mBaseService.getDataFromService(new KoGetOpMoveAllListParams(wipId), this, this);
    }
    

    
    //获取质量管理计划要填写的数据项信息
    public void getQaListByPlanId(String planId, String wipId, String opCode) {
    	mQaListByPlanIdListServiceId = mBaseService.getDataFromService(new KoQaListByPlanIdParams(planId, wipId, opCode), this, this);
    }
    
    //获取人工添加的质量收集计划
    public void getQaListManualAdd(String wipId, String opCode) {
    	mQaListManualAddListServiceId = mBaseService.getDataFromService(new KoQaListManualAddParams(wipId, opCode), this, this);
    }
    
    //提交质量管理计划数据
    public void submitQaData(String createStaffNo, String wipId, String opCode, String transactionId, String qaNvList, String qaChildNvList, boolean isManualAddedQa, String childPlanId) {
    	mQaInsertDataServiceId = mBaseService.getDataFromService(new KoQaInsertDataParams(createStaffNo, wipId, opCode, transactionId, qaNvList, qaChildNvList, isManualAddedQa, childPlanId), this, this);
    }
    
    //获取坏品推送消息
    public void getPushMsgList(String staffNo, String transId, boolean isNotice) {
    	mGetMsgListServiceId = mBaseService.getDataFromService(new KoPushMsgParams(staffNo, transId, isNotice), this, this);
    }
    

    
    //开始工序前检查条件
    public void opCheckBeforeStart(String wipEntityId, String wipEntityName, String fmOperationCode, String curOperationId, String seqId) {
    	mOpCheckServiceId = mBaseService.getDataFromService(new KoOpCheckBeforeStartParams(wipEntityId, wipEntityName, fmOperationCode, curOperationId, seqId), this, this);
    }
    
    
    
    //根据设备id获取设备相关信息
    public void assetCheckGetAssetInfo(String id) {
    	mAssetCheckGetAssetInfoServiceId = mBaseService.getDataFromService(new KoAssetCheckGetAssetInfoParams(id), this, this);
    }
    
    //取消某台设备的点检设置
    public void cancelCheckAsset(String id) {
    	mAssetCheckCancelCheckAssetServiceId = mBaseService.getDataFromService(new KoAssetCheckCancelCheckAssetParams(id), this, this);
    }
    
    //根据设备id获取设备的点检信息列表
    public void getCheckItemList(String assetId) {
    	mGetCheckItemListServiceId = mBaseService.getDataFromService(new KoAssetCheckCheckItemParams(assetId), this, this);
    }
    
    //提交点检的结果
    public void assetCheckSubmitCheck(String assetId, String userId, List<KoAssetCheckCheckItem> checkValueList) {
    	mAssetCheckSubmitCheckServiceId = mBaseService.getDataFromService(new KoAssetCheckSubmitCheckParams(userId, assetId, userId, checkValueList), this, this);
    }
    
    //获取“选择需要点检的机器”界面的数据
    public void assetCheckGetAssetItemList(String startDate, String endDate) {
    	mAssetCheckGetAssetListServiceId = mBaseService.getDataFromService(new KoAssetCheckAssetListParams(startDate, endDate), this, this);
    }
    
    //获取设置点检机器界面的数据
    public void assetCheckGetSetAssetCheckList(String date, String shift, String staffNo) {
    	mAssetCheckGetSetAssetListServiceId = mBaseService.getDataFromService(new KoAssetCheckGetSetAssetListParams(date, shift, staffNo), this, this);	
    }
    
    //更新待点检设备列表
    public void assetCheckUpdateSetAssetCheck(String checkDate, String shift, List<KoAssetCheckSetAssetCheckItem> assetList) {
    	mAssetCheckUpdateSetAssetListServiceId = mBaseService.getDataFromService(new KoAssetCheckUpdateSetAssetListParams(checkDate, shift, CacheUtils.getLoginUserInfo().staffNo, assetList), this, this);	
    }
    
    //获取零配件列表
    public void assetCheckGetChangedPartsList(String opCode) {
    	mAssetCheckGetChangedPartsListServiceId = mBaseService.getDataFromService(new KoAssetCheckGetChangedPartsParams(opCode), this, this);	
    }

    //这是网络请求的返回数据被解析完成后的回调，每个taskId对应一个请求线程.此处就可以将解析后的数据对应到相应的业务回调中了
    @Override
    public void onHttpDownLoadDone(int taskId, String result) {
    	
    	LogUtils.e(tag, "onHttpDownLoadDone():result="+result);
    	
    	if(!OnHttpDownloadListener.SUCCESS.equals(result)) {
    		return;
    	}
    	
        if(taskId == mLoginServiceId) {
        	mIKolPesNetReqControlBack.loginBack(mLoginAdapter.isSuccess() && OnHttpDownloadListener.SUCCESS.equals(result), mLoginAdapter, mLoginAdapter.getMessage());
        }
        else if(taskId == mGetAssetInfoServiceId) {
        	mIKolPesNetReqControlBack.getAssetInfoBack(SUC_CODE.equals(mKoAssetGetAssetInfoAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
									        			mKoAssetGetAssetInfoAdapter, 
									        			mKoAssetGetAssetInfoAdapter.getMessage());
        }
        else if(taskId == mGetMainProcessListServiceId) {
        	mIKolPesNetReqControlBack.getMainProcessListBack(SUC_CODE.equals(mKoMainProcessListAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
										        			mKoMainProcessListAdapter, 
										        			mKoMainProcessListAdapter.getMessage());
        }
        else if(taskId == mGetMachineReportTimeServiceId) {
            mIKolPesNetReqControlBack.getMachineReportTimeBack(SUC_CODE.equals(mGetMachineReportTimeAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result),
                    mGetMachineReportTimeAdapter,
                    mGetMachineReportTimeAdapter.getMessage());
        }
        else if(taskId == mDeclareJobServiceId) {
            mIKolPesNetReqControlBack.declareJobBack(SUC_CODE.equals(mDeclareJobAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result),
                    mDeclareJobAdapter,
                    mDeclareJobAdapter.getMessage());
        }

        else if(taskId == mSetPwdServiceId) {
        	mIKolPesNetReqControlBack.setPasswordBack(SUC_CODE.equals(mKoSetPasswordAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
        											  mKoSetPasswordAdapter.getMessage());
        }
        else if(taskId == mGetProduceListByProjectNumServiceId) {
        	mIKolPesNetReqControlBack.getProduceListByProjectNumBack(SUC_CODE.equals(mMeGetProduceListByProjectNumAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
												        			mMeGetProduceListByProjectNumAdapter.getList(),
												        			mMeGetProduceListByProjectNumAdapter.getMessage());
        }
        else if(taskId == mGetAssetSeqInfoByIdServiceId) {
        	mIKolPesNetReqControlBack.getAssetSeqInfoByAssetIdBack(SUC_CODE.equals(mMeGetAssetSeqInfoByAssetIdAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
												        			mMeGetAssetSeqInfoByAssetIdAdapter,
												        			mMeGetAssetSeqInfoByAssetIdAdapter.getMessage());
        }
        else if(taskId == mGetSeqInfoBySeqIdServiceId) {
        	mIKolPesNetReqControlBack.getSeqInfoBySeqIdBack(SUC_CODE.equals(mMeGetSeqInfoBySeqIdAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
										        			mMeGetSeqInfoBySeqIdAdapter,
										        			mMeGetSeqInfoBySeqIdAdapter.getMessage());
        }
		else if(taskId == mGetDescInfoWhenSeqSelectedServiceId) {
			mIKolPesNetReqControlBack.getDescInfoWhenSeqSelectedBack(SUC_CODE.equals(mMeGetDescInfoWhenSeqSelectedAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
																	mMeGetDescInfoWhenSeqSelectedAdapter,
																	mMeGetDescInfoWhenSeqSelectedAdapter.getMessage());
		}
		else if(taskId == mGetDescInfoWhenStartingSeqClickedServiceId) {
			mIKolPesNetReqControlBack.getDescInfoWhenStartingSeqClicked(SUC_CODE.equals(mMeGetDescInfoWhenStartingSeqClickedAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
																		mMeGetDescInfoWhenStartingSeqClickedAdapter,
																		mMeGetDescInfoWhenStartingSeqClickedAdapter.getMessage());
		}
		else if(taskId == mGetPAfterOpWhenSeqSelectedServiceId) {
			mIKolPesNetReqControlBack.getPafteropWhenStartingSeqClicked(SUC_CODE.equals(mMeGetPafteropWhenSeqSelectedAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
																		mMeGetPafteropWhenSeqSelectedAdapter,
																		mMeGetPafteropWhenSeqSelectedAdapter.getMessage());
		}
        else if(taskId == mStartSeqServiceId) {
        	mIKolPesNetReqControlBack.startSeqBack(SUC_CODE.equals(mMeStartSeqAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
        										   mMeStartSeqAdapter.getMessage());
        }
		else if(taskId == mGetStartedSeqListServiceId) {
			mIKolPesNetReqControlBack.getStartedSeqListBack(SUC_CODE.equals(mMeGetStartedSeqListAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
															mMeGetStartedSeqListAdapter.getList(), mMeGetStartedSeqListAdapter.getMessage());
		}
		else if(taskId == mGetQaListServiceId) {
        	mIKolPesNetReqControlBack.getQaListBack(SUC_CODE.equals(mKoQaListNeedFillAdapter.getCode()), 
													mKoQaListNeedFillAdapter.getList(),
													mKoQaListNeedFillAdapter.getMessage());
        }
		else if(taskId == mEndSeqServiceId) {
			mIKolPesNetReqControlBack.endSeqBack(SUC_CODE.equals(mMeEndAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
												 mMeEndAdapter.getMessage());
		}
		else if(taskId == mReceiveOrderGetQtyByIdServiceId) {
			mIKolPesNetReqControlBack.receiveOrderGetQtyByIdBack(SUC_CODE.equals(mMeReceiveOrderGetQtyByIdAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
																mMeReceiveOrderGetQtyByIdAdapter.qty, 
																mMeReceiveOrderGetQtyByIdAdapter.getMessage());
		}
		else if(taskId == mReceiveOrderSureReceiveServiceId) {
			mIKolPesNetReqControlBack.receiveOrderReceiveBack(SUC_CODE.equals(mMeReceiveOrderSureReceiveAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
															  mMeReceiveOrderSureReceiveAdapter.getMessage());	
		}
		else if(taskId == mReceiveOrderSureRejectServiceId) {
			mIKolPesNetReqControlBack.receiveOrderRejectBack(SUC_CODE.equals(mMeReceiveOrderSureRejectAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
															 mMeReceiveOrderSureRejectAdapter.getMessage());
		}
		else if(taskId == mOpDeleteServiceId) {
        	mIKolPesNetReqControlBack.deleteStartedSeqBack(SUC_CODE.equals(mKoOpDeleteOpAdapter.getCode()), 
        											  mKoOpDeleteOpAdapter.getMessage());
        }
		else if(taskId == mSearchSeqListServiceId) {
        	mIKolPesNetReqControlBack.searchSeqListBack(SUC_CODE.equals(mMeSearchSeqListAdapter.getCode()), 
									        			mMeSearchSeqListAdapter.getList(),
									        			mMeSearchSeqListAdapter.getMessage());
        }
        else if(taskId == mTimeReportGetDescInfoWhenStartServiceId) {
        	mIKolPesNetReqControlBack.timeReportDescInfoAndActiveListBack(SUC_CODE.equals(mMeTimeReportGetDescInfoAdapter.getCode()), 
													        			mMeTimeReportGetDescInfoAdapter,
													        			mMeTimeReportGetDescInfoAdapter.getMessage());
        }
        else if(taskId == mTimeReportGetProduceInfoAndSeqListByProduceIdServiceId) {
        	mIKolPesNetReqControlBack.timeReportGetSeqListBack(SUC_CODE.equals(mMeTimeReportProduceInfoAndSeqListAdapter.getCode()), 
												        		mMeTimeReportProduceInfoAndSeqListAdapter,
												        		mMeTimeReportProduceInfoAndSeqListAdapter.getMessage());
        }
        else if(taskId == mTimeReportCompleteServiceId) {
        	mIKolPesNetReqControlBack.timeReportCompleteBack(SUC_CODE.equals(mMeTimeCompleteAdapter.getCode()),
									        				 mMeTimeCompleteAdapter.getMessage());
        }
        else if(taskId == mMaterialsReportSeqListServiceId) {
        	mIKolPesNetReqControlBack.materialsReportSeqListBack(SUC_CODE.equals(mMeMaterialReportSeqListAdapter.getCode()),
											        			mMeMaterialReportSeqListAdapter.getList(),
											        			mMeMaterialReportSeqListAdapter.getMessage());
        }
        else if(taskId == mMaterialsReportGetDescServiceId) {
        	mIKolPesNetReqControlBack.materialsReportGetDescBack(SUC_CODE.equals(mMeMaterialsReportDisplayAdapter.getCode()),
											        			mMeMaterialsReportDisplayAdapter.display,
											        			mMeMaterialsReportDisplayAdapter.getMessage());
        }
        else if(taskId == mMaterialsReportStandardNumServiceId) {
        	mIKolPesNetReqControlBack.materialsReportGetStandardNumBack(SUC_CODE.equals(mMeMaterialsReportStandardNumAdapter.getCode()),
													        			mMeMaterialsReportStandardNumAdapter.getList(),
													        			mMeMaterialsReportStandardNumAdapter.getMessage());
        }
        else if(taskId == mMaterialsReportNewNumServiceId) {
        	mIKolPesNetReqControlBack.materialsReportGetNewNumBack(SUC_CODE.equals(mMeMaterialsReportNewNumAdapter.getCode()),
												        			mMeMaterialsReportNewNumAdapter.getList(),
												        			mMeMaterialsReportNewNumAdapter.getMessage());
        }
        else if(taskId == mMaterialsReportGetMaterialDescServiceId) {
        	mIKolPesNetReqControlBack.materialsReportGetMaterialDescBack(SUC_CODE.equals(mMeMaterialsReportMaterialDisplayAdapter.getCode()),
													        			mMeMaterialsReportMaterialDisplayAdapter.display,
													        			mMeMaterialsReportMaterialDisplayAdapter.getMessage());
        }
        else if(taskId == mMaterialsReportCompleteServiceId) {
        	mIKolPesNetReqControlBack.materialsReportCompleteBack(SUC_CODE.equals(mMeMaterialsReportCompleteAdapter.getCode()),
        														  mMeMaterialsReportCompleteAdapter.getMessage());
        }
        else if(taskId == mHolidayOnOffServiceId) {
        	mIKolPesNetReqControlBack.holidayOnOffBack(SUC_CODE.equals(mMeHolidayOnOffeAdapter.getCode()),
        												mMeHolidayOnOffeAdapter.getMessage());

        	
        }
        else if(taskId == mGetAssetListServiceId) {
        	mIKolPesNetReqControlBack.getAssetListBack(SUC_CODE.equals(mMeAssetListAdapter.getCode()),
									        		   mMeAssetListAdapter.getList(),
									        		   mMeAssetListAdapter.getMessage());
        }
        else if(taskId == mAttendanceFinishServiceId) {
            mIKolPesNetReqControlBack.getAttendanceFinishBack(SUC_CODE.equals(mMmAttendanceFinishAdapter.getCode()),
                    mMmAttendanceFinishAdapter,
                    mMmAttendanceFinishAdapter.getMessage());
        }
        else if(taskId == mDeclareTimeFinishServiceId) {
            mIKolPesNetReqControlBack.getDeclareTimeFinishBack(SUC_CODE.equals(mMmDeclareTimeFinishAdapter.getCode()),
                    mMmDeclareTimeFinishAdapter,
                    mMmDeclareTimeFinishAdapter.getMessage());
        }
        else if(taskId == mDeclareMtlFinishServiceId) {
            mIKolPesNetReqControlBack.getDeclareMtlFinishBack(SUC_CODE.equals(mMmDeclareMtlFinishAdapter.getCode()),
                    mMmDeclareMtlFinishAdapter,
                    mMmDeclareMtlFinishAdapter.getMessage());
        }

        else if(taskId == mNonDeclareTimeFinishServiceId) {
            mIKolPesNetReqControlBack.getNonDeclareTimeFinishBack(SUC_CODE.equals(mMmNonDeclareTimeFinishAdapter.getCode()),
                    mMmNonDeclareTimeFinishAdapter,
                    mMmNonDeclareTimeFinishAdapter.getMessage());
        }
        else if(taskId == mGetGroupListServiceId){
            mIKolPesNetReqControlBack.getGroupListBack(SUC_CODE.equals(mMmGroupListAdapter.getCode()),
                    mMmGroupListAdapter.getList(),
                    mMmGroupListAdapter.getMessage());
        }
        else if(taskId == mGetOrgListServiceId){
            mIKolPesNetReqControlBack.getOrgListBack(SUC_CODE.equals(mMmOrgListAdapter.getCode()),
                    mMmOrgListAdapter.getList(),
                    mMmOrgListAdapter.getMessage());
        }
        else if(taskId == mGetWipListServiceId){
            mIKolPesNetReqControlBack.getWipListBack(SUC_CODE.equals(mMmWipListAdapter.getCode()),
                    mMmWipListAdapter.getList(),
                    mMmWipListAdapter.getMessage());
        }
        else if(taskId == mGetRoutingListServiceId){
            mIKolPesNetReqControlBack.getRoutingListBack(SUC_CODE.equals(mMmGetRoutingListAdapter.getCode()),
                    mMmGetRoutingListAdapter.getList(),
                    mMmGetRoutingListAdapter.getMessage());
        }
        else if(taskId == mGetMtlPlanListServiceId || taskId == mGetMtlNewListServiceId){
            mIKolPesNetReqControlBack.getMtlPlanListBack(SUC_CODE.equals(mMmGetMtlPlanListAdapter.getCode()),
                    mMmGetMtlPlanListAdapter.getList(),
                    mMmGetMtlPlanListAdapter.getMessage());
        }
        else if(taskId == mGetReasonCodeListServiceId){
            mIKolPesNetReqControlBack.getReasonCodeListBack(SUC_CODE.equals(mMmGetReasonCodeListAdapter.getCode()),
                    mMmGetReasonCodeListAdapter.getList(),
                    mMmGetReasonCodeListAdapter.getMessage());
        }
        else if(taskId == mGetDeclareTimeListServiceId){
            mIKolPesNetReqControlBack.getDeclareTimeListBack(SUC_CODE.equals(mMmGetDeclareTimeListAdapter.getCode()),
                    mMmGetDeclareTimeListAdapter.getList(),
                    mMmGetDeclareTimeListAdapter.getMessage());
        }
        else if(taskId == mGetDeclareTimeListToMtlServiceId){
            mIKolPesNetReqControlBack.getDeclareTimeListToMtlBack(SUC_CODE.equals(mMmGetDeclareTimeListAdapter.getCode()),
                    mMmGetDeclareTimeListAdapter.getList(),
                    mMmGetDeclareTimeListAdapter.getMessage());
        }
        else if(taskId == mGetAttendanceListServiceId){
            mIKolPesNetReqControlBack.getAttendanceListBack(SUC_CODE.equals(mMmGetAttendanceListAdapter.getCode()),
                    mMmGetAttendanceListAdapter.getList(),
                    mMmGetAttendanceListAdapter.getMessage());
        }
        else if(taskId == mGetDeclareMtlListServiceId){
            mIKolPesNetReqControlBack.getDeclareMtlListBack(SUC_CODE.equals(mMmGetDeclareMtlListAdapter.getCode()),
                    mMmGetDeclareMtlListAdapter.getList(),
                    mMmGetDeclareMtlListAdapter.getMessage());
        }
        else if(taskId == mGetNonDeclareTimeListServiceId){
            mIKolPesNetReqControlBack.getNonDeclareTimeListBack(SUC_CODE.equals(mMmGetNonDeclareTimeListAdapter.getCode()),
                    mMmGetNonDeclareTimeListAdapter.getList(),
                    mMmGetNonDeclareTimeListAdapter.getMessage());
        }
        else if(taskId == mGetDateShiftServiceId) {
        	mIKolPesNetReqControlBack.getDateShiftBack(SUC_CODE.equals(mMeDateShiftAdapter.getCode()),
        												mMeDateShiftAdapter.date, mMeDateShiftAdapter.shift,
        												mMeDateShiftAdapter.getMessage());
        }
        else if(taskId == mGetOpListServiceId) {
        	mIKolPesNetReqControlBack.getOpBack(SUC_CODE.equals(mKoGetOpListAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
								        		mKoGetOpListAdapter.getList(), 
								        		mKoGetOpListAdapter.getMessage());
        }
        else if(taskId == mAssetCheckAddServiceId) {
        	
        	mIKolPesNetReqControlBack.assetCheckAddBack(SUC_CODE.equals(mKoAssetCheckAddAdapter.getCode()), mKoAssetCheckAddAdapter.checkId, mKoAssetCheckAddAdapter.getMessage());
        }
        else if(taskId == mAssetInsertPicPathServiceId) {
        	
        	mIKolPesNetReqControlBack.assetInsertPicPathBack(SUC_CODE.equals(mKoAssetInsertPicPathAdapter.getCode()), mKoAssetInsertPicPathAdapter.getMessage());
        }
        else if(taskId == mGetAssetCheckListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckGetErrorListBack(
        												SUC_CODE.equals(mKoAssetGetCheckErrorListAdapter.getCode()), 
									        			mKoAssetGetCheckErrorListAdapter.getMessage(), 
									        			mKoAssetGetCheckErrorListAdapter.getList());
        }
        else if(taskId == mAssetGetMachineFailListServiceId) {
        	mIKolPesNetReqControlBack.assetGetMachineFailListBack(
											        			SUC_CODE.equals(mKoAssetMachineFailListAdapter.getCode()), 
											        			mKoAssetMachineFailListAdapter.getMessage(), 
											        			mKoAssetMachineFailListAdapter.getList());
        }
        else if(taskId == mAssetRepairServiceId) {
        	mIKolPesNetReqControlBack.assetCheckRepairBack(SUC_CODE.equals(mKoAssetCheckRepairAdapter.getCode()),
        												   mKoAssetCheckRepairAdapter.getMessage());
        }
        else if(taskId == mAssetGetAssetInfoServiceId) {
        	mIKolPesNetReqControlBack.assetGetAssetInfoBack(SUC_CODE.equals(mKoAssetGetAssetInfoAdapter.getCode()), 
										        			mKoAssetGetAssetInfoAdapter.getMessage(), 
										        			mKoAssetGetAssetInfoAdapter);
        }
        else if(taskId == mDataEnableServiceId) {
        	mIKolPesNetReqControlBack.dateEnableBack(SUC_CODE.equals(mKoDataEnableAdapter.getCode()), 
        											 mKoDataEnableAdapter.getWeekMap(),
        											 mKoDataEnableAdapter.getMessage());
        }
        else if(taskId == mOpMaxQuanServiceId) {
        	mIKolPesNetReqControlBack.opGetMaxQuanBack(SUC_CODE.equals(mKoOpMaxQuanAdapter.getCode()), 
        											   mKoOpMaxQuanAdapter.maxQuan,
        											   mKoOpMaxQuanAdapter.getOpAssetList(),
        											   mKoOpMaxQuanAdapter.getMessage());
        }
        else if(taskId == mOpEndServiceId) {
        	mIKolPesNetReqControlBack.opEndBack(SUC_CODE.equals(mKoOpEndAdapter.getCode()), 
								        			mKoOpEndAdapter.transactionId,
								        			mKoOpEndAdapter.getMessage());
        }
        else if(taskId == mGetOpStartedListServiceId) {
        	mIKolPesNetReqControlBack.opStartedListBack(SUC_CODE.equals(mKoGetOpStartedListAdapter.getCode()), 
									        			mKoGetOpStartedListAdapter.getList(),
									        			mKoGetOpStartedListAdapter.isOpCompleted,
									        			mKoGetOpStartedListAdapter.curWorkingOpCode,
									        			mKoGetOpStartedListAdapter.getMessage());
        }
        else if(taskId == mGetOpMoveAllListServiceId) {
        	mIKolPesNetReqControlBack.opMoveAllListBack(SUC_CODE.equals(mKoGetOpMoveAllListAdapter.getCode()), 
									        			mKoGetOpMoveAllListAdapter.getList(),
									        			mKoGetOpMoveAllListAdapter.getMessage());
        }
        
        else if(taskId == mQaListByPlanIdListServiceId) {
        	mIKolPesNetReqControlBack.qaListByPlanIdBack(SUC_CODE.equals(mKoQaListByPlanIdAdapter.getCode()), 
									        			 mKoQaListByPlanIdAdapter.incompleteQuan,
									        			 mKoQaListByPlanIdAdapter.minStartTime,
														 mKoQaListByPlanIdAdapter.maxEndTime,
        												 mKoQaListByPlanIdAdapter.getList(),
        												 mKoQaListByPlanIdAdapter.getMessage());
        }
        else if(taskId == mQaListManualAddListServiceId) {
        	mIKolPesNetReqControlBack.qaListManualAddBack(SUC_CODE.equals(mKoQaListManualAdapter.getCode()), 
										        			mKoQaListManualAdapter.incompleteQuan,
										        			mKoQaListManualAdapter.minStartTime,
															mKoQaListManualAdapter.maxEndTime,
										        			mKoQaListManualAdapter.getList(),
										        			mKoQaListManualAdapter.getMessage());
        }
        else if(taskId == mQaInsertDataServiceId) {
        	mIKolPesNetReqControlBack.submitQaDataBack(SUC_CODE.equals(mKoQaInsertDataAdapter.getCode()), 
        											   mKoQaInsertDataAdapter.getMessage());
        }
        else if(taskId == mGetMsgListServiceId) {
			mIKolPesNetReqControlBack.getPushMsgListBack(SUC_CODE.equals(mKoPushMsgListAdapter.getCode()), 
														mKoPushMsgListAdapter.getList(),
														mKoPushMsgListAdapter.getMessage());
		}
        else if(taskId == mUpdateApkServiceId) {
        	mIKolPesNetReqControlBack.getUpdateApkBack(SUC_CODE.equals(mKoUpdateApkAdapter.getCode()), 
									        			mKoUpdateApkAdapter,
									        			mKoUpdateApkAdapter.getMessage());
        }
        else if(taskId == mOpCheckServiceId) {
        	mIKolPesNetReqControlBack.getOpCheckBeforeStartBack(SUC_CODE.equals(mKoOpCheckBeforeStartAdapter.getCode()), 
        														mKoOpCheckBeforeStartAdapter.timeBufferOpStart,
        														mKoOpCheckBeforeStartAdapter.getMessage());
        }
        
        else if(taskId == mGetCheckItemListServiceId) {
        	mIKolPesNetReqControlBack.getCheckItemListDataBack(SUC_CODE.equals(mKoAssetCheckCheckItemAdapter.getCode()), 
        													   mKoAssetCheckCheckItemAdapter.getList(),
        	SUC_CODE.equals(mKoAssetCheckCheckItemAdapter.getCode())?mKoAssetCheckCheckItemAdapter.assetLastCheckTime:mKoAssetCheckCheckItemAdapter.getMessage());
        }
        else if(taskId == mAssetCheckGetAssetInfoServiceId) {
        	mIKolPesNetReqControlBack.assetCheckGetAssetInfoBack(SUC_CODE.equals(mKoAssetCheckGetAssetInfoAdapter.getCode()), 
											    			mKoAssetCheckGetAssetInfoAdapter.getMessage(), 
											    			mKoAssetCheckGetAssetInfoAdapter);
        }
        else if(taskId == mAssetCheckSubmitCheckServiceId) {
        	mIKolPesNetReqControlBack.assetCheckSubmitCheckBack(SUC_CODE.equals(mKoAssetCheckSubmitCheckAdapter.getCode()), 
        														mKoAssetCheckSubmitCheckAdapter.getMessage());
        }
        else if(taskId == mAssetCheckGetAssetListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckAssetListBack(SUC_CODE.equals(mKoAssetCheckAssetItemAdapter.getCode()), 
											        			mKoAssetCheckAssetItemAdapter.getList(), 
											        			mKoAssetCheckAssetItemAdapter.getMessage());
        }
        else if(taskId == mAssetCheckGetSetAssetListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckGetSetAssetCheckListBack(SUC_CODE.equals(mKoAssetCheckSetAssetCheckAdapter.getCode()), 
        																mKoAssetCheckSetAssetCheckAdapter.getList(), 
        																mKoAssetCheckSetAssetCheckAdapter.getMessage());
        }
        else if(taskId == mAssetCheckUpdateSetAssetListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckUpdateSetAssetCheckListBack(SUC_CODE.equals(mKoAssetCheckUpdateSetAssetCheckAdapter.getCode()), 
        																	mKoAssetCheckUpdateSetAssetCheckAdapter.getMessage());
        }
        else if(taskId == mAssetCheckGetChangedPartsListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckGetChangedPartsBack(SUC_CODE.equals(mKoAssetCheckChangedPartsListAdapter.getCode()), 
												        			mKoAssetCheckChangedPartsListAdapter.getList(), 
												        			mKoAssetCheckChangedPartsListAdapter.getMessage());
        }
        else if(taskId == mAssetCheckCancelCheckAssetServiceId) {
        	mIKolPesNetReqControlBack.assetCheckCancelCheckAssetBack(SUC_CODE.equals(mKoAssetCheckCancelCheckAssetAdapter.getCode()), 
        															 mKoAssetCheckCancelCheckAssetAdapter.getMessage());
        }
    }
    
    //网络请求的回调，此处的数据还未被解析。需在此处根据不同的id进行区分解析。
	@Override
	public String onDataParse(int id, InputStream is) {
		
		LogUtils.e(tag, "onDataParse()");
		
		if(mGetAssetInfoServiceId == id) {
			try {
				mKoAssetGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetGetAssetInfoParser(mKoAssetGetAssetInfoAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mLoginServiceId == id) {
			try {
				mLoginAdapter = new MeLoginAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeLoginParser(mLoginAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mGetMainProcessListServiceId == id) {
			try {
				mKoMainProcessListAdapter = new MeMainProcessListAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeMainProcessListParser(mKoMainProcessListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
        else if(mGetMachineReportTimeServiceId == id) {
            try {
                mGetMachineReportTimeAdapter = new MmGetMachineReportTimeAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetMachineReportTimeParser(mGetMachineReportTimeAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(mDeclareJobServiceId == id) {
            try {
                mDeclareJobAdapter = new MmDeclareJobAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmDeclareJobParser(mDeclareJobAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
		else if(mSetPwdServiceId == id) {
			try {
				mKoSetPasswordAdapter = new KoSetPasswordAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoSetPasswordParser(mKoSetPasswordAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mGetProduceListByProjectNumServiceId == id) {
			try {
				mMeGetProduceListByProjectNumAdapter = new MeGetProduceListByProjectNumAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeGetProduceListByProjectNumParser(mMeGetProduceListByProjectNumAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mGetAssetSeqInfoByIdServiceId == id) {
			try {
				mMeGetAssetSeqInfoByAssetIdAdapter = new MeGetAssetSeqInfoByAssetIdAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeGetAssetSeqInfoByAssetIdParser(mMeGetAssetSeqInfoByAssetIdAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mGetSeqInfoBySeqIdServiceId == id) {
			try {
				mMeGetSeqInfoBySeqIdAdapter = new MeGetSeqInfoBySeqIdAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeGetSeqInfoBySeqIdParser(mMeGetSeqInfoBySeqIdAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mGetDescInfoWhenSeqSelectedServiceId == id) {
			try {
				mMeGetDescInfoWhenSeqSelectedAdapter = new MeGetDescInfoWhenSeqSelectedAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeGetDescInfoWhenSeqSelectedParser(mMeGetDescInfoWhenSeqSelectedAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mGetDescInfoWhenStartingSeqClickedServiceId == id) {
			try {
				mMeGetDescInfoWhenStartingSeqClickedAdapter = new MeGetDescInfoWhenStartingSeqClickedAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeGetDescInfoWhenStartingSeqClickedParser(mMeGetDescInfoWhenStartingSeqClickedAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mStartSeqServiceId == id) {
			try {
				mMeStartSeqAdapter = new MeCodeMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeCodeMsgParser(mMeStartSeqAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mGetStartedSeqListServiceId == id) {
			try {
				mMeGetStartedSeqListAdapter = new MeGetStartedSeqListAdapter();
				Xml.parse(is, Encoding.UTF_8, new MGetStartedSeqListParser(mMeGetStartedSeqListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mGetQaListServiceId) {
			try {
				mKoQaListNeedFillAdapter = new MeQaListNeedFillAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeQaListNeedFillParser(mKoQaListNeedFillAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mEndSeqServiceId == id) {
			try {
				mMeEndAdapter = new MeCodeMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeCodeMsgParser(mMeEndAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mReceiveOrderGetQtyByIdServiceId == id) {
			try {
				mMeReceiveOrderGetQtyByIdAdapter = new MeReceiveOrderGetQtyByIdAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeReceiveOrderGetQtyByIdParser(mMeReceiveOrderGetQtyByIdAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mReceiveOrderSureReceiveServiceId == id) {
			try {
				mMeReceiveOrderSureReceiveAdapter = new MeCodeMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeCodeMsgParser(mMeReceiveOrderSureReceiveAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mReceiveOrderSureRejectServiceId == id) {
			try {
				mMeReceiveOrderSureRejectAdapter = new MeCodeMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeCodeMsgParser(mMeReceiveOrderSureRejectAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpDeleteServiceId) {
        	try {
				mKoOpDeleteOpAdapter = new MeOpDeleteOpAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeOpDeleteOpParser(mKoOpDeleteOpAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mSearchSeqListServiceId) {
        	try {
				mMeSearchSeqListAdapter = new MeSearchSeqListAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeSearchSeqListParser(mMeSearchSeqListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mTimeReportGetDescInfoWhenStartServiceId) {
        	try {
        		mMeTimeReportGetDescInfoAdapter = new MeTimeReportGetDescInfoAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeTimeReportGetDescInfoParser(mMeTimeReportGetDescInfoAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mTimeReportGetProduceInfoAndSeqListByProduceIdServiceId) {
        	try {
        		mMeTimeReportProduceInfoAndSeqListAdapter = new MeTimeReportSeqListAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeTimeReportSeqListParser(mMeTimeReportProduceInfoAndSeqListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mTimeReportCompleteServiceId) {
        	try {
        		mMeTimeCompleteAdapter = new MeCodeMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeCodeMsgParser(mMeTimeCompleteAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mMaterialsReportSeqListServiceId) {
        	try {
        		mMeMaterialReportSeqListAdapter = new MeTimeReportSeqListAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeTimeReportSeqListParser(mMeMaterialReportSeqListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mMaterialsReportGetDescServiceId) {
        	try {
        		mMeMaterialsReportDisplayAdapter = new MeMaterialsReportDisplayAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeMaterialsReportDisplayParser(mMeMaterialsReportDisplayAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mMaterialsReportStandardNumServiceId) {
        	try {
        		mMeMaterialsReportStandardNumAdapter = new MeMaterialsReportNumAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeMaterialsReportNumParser(mMeMaterialsReportStandardNumAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mMaterialsReportNewNumServiceId) {
        	try {
        		mMeMaterialsReportNewNumAdapter = new MeMaterialsReportNumAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeMaterialsReportNumParser(mMeMaterialsReportNewNumAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mMaterialsReportGetMaterialDescServiceId) {
        	try {
        		mMeMaterialsReportMaterialDisplayAdapter = new MeMaterialsReportDisplayAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeMaterialsReportDisplayParser(mMeMaterialsReportMaterialDisplayAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mMaterialsReportCompleteServiceId) {
        	try {
        		mMeMaterialsReportCompleteAdapter = new MeCodeMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeCodeMsgParser(mMeMaterialsReportCompleteAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mHolidayOnOffServiceId) {
        	try {
        		mMeHolidayOnOffeAdapter = new MeCodeMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeCodeMsgParser(mMeHolidayOnOffeAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mGetAssetListServiceId) {
			try {
                mMeAssetListAdapter = new MeGetAssetListAdapter();
				Xml.parse(is, Encoding.UTF_8, new MGetAssetListParser(mMeAssetListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
        else if(id == mAttendanceFinishServiceId) {
            try {
                mMmAttendanceFinishAdapter = new MmAttendanceFinishAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmAttendanceFinishParser(mMmAttendanceFinishAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(id == mDeclareTimeFinishServiceId) {
            try {
                mMmDeclareTimeFinishAdapter = new MmDeclareTimeFinishAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmDeclareTimeFinishParser(mMmDeclareTimeFinishAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(id == mDeclareMtlFinishServiceId) {
            try {
                mMmDeclareMtlFinishAdapter = new MmDeclareMtlFinishAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmDeclareMtlFinishParser(mMmDeclareMtlFinishAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(id == mNonDeclareTimeFinishServiceId) {
            try {
                mMmNonDeclareTimeFinishAdapter = new MmNonDeclareTimeFinishAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmNonDeclareTimeFinishParser(mMmNonDeclareTimeFinishAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
		else if (id == mGetGroupListServiceId){
            try {
                mMmGroupListAdapter = new MmGetGroupListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetGroupListParser(mMmGroupListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == mGetOrgListServiceId){
            try {
                mMmOrgListAdapter = new MmGetOrgListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetOrgListParser(mMmOrgListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == mGetWipListServiceId){
            try {
                mMmWipListAdapter = new MmGetWipListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetWipListParser(mMmWipListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == mGetRoutingListServiceId){
            try {
                mMmGetRoutingListAdapter = new MmGetRoutingListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetRoutingListParser(mMmGetRoutingListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == mGetMtlPlanListServiceId || id == mGetMtlNewListServiceId){
            try {
                mMmGetMtlPlanListAdapter = new MmGetMtlPlanListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetMtlPlanListParser(mMmGetMtlPlanListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == mGetReasonCodeListServiceId){
            try {
                mMmGetReasonCodeListAdapter = new MmGetReasonCodeListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetReasonCodeListParser(mMmGetReasonCodeListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == mGetDeclareTimeListServiceId || id == mGetDeclareTimeListToMtlServiceId){
            try {
                mMmGetDeclareTimeListAdapter = new MmGetDeclareTimeListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetDeclareTimeListParser(mMmGetDeclareTimeListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (id == mGetAttendanceListServiceId){
            try {
                mMmGetAttendanceListAdapter = new MmGetAttendanceListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetAttendanceListParser(mMmGetAttendanceListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == mGetDeclareMtlListServiceId){
            try {
                mMmGetDeclareMtlListAdapter = new MmGetDeclareMtlListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetDeclareMtlListParser(mMmGetDeclareMtlListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == mGetNonDeclareTimeListServiceId){
            try {
                mMmGetNonDeclareTimeListAdapter = new MmGetNonDeclareTimeListAdapter();
                Xml.parse(is, Encoding.UTF_8, new MmGetNonDeclareTimeListParser(mMmGetNonDeclareTimeListAdapter));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

		else if(id == mGetDateShiftServiceId) {
			try {
				mMeDateShiftAdapter = new MeGetDateShiftAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeDateShiftParser(mMeDateShiftAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mGetPAfterOpWhenSeqSelectedServiceId) {
			try {
				mMeGetPafteropWhenSeqSelectedAdapter = new MeGetPafteropWhenSeqSelectedAdapter();
				Xml.parse(is, Encoding.UTF_8, new MePafteropWhenSeqSelectedParser(mMeGetPafteropWhenSeqSelectedAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		


		
		
		
		else if(mGetOpListServiceId == id) {
			try {
				mKoGetOpListAdapter.clear();
				Xml.parse(is, Encoding.UTF_8, new KoGetOpListParser(mKoGetOpListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mAssetCheckAddServiceId == id) {
			try {
				mKoAssetCheckAddAdapter = new KoAssetCheckAddAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckAddParser(mKoAssetCheckAddAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(mAssetInsertPicPathServiceId == id) {
			try {
				mKoAssetInsertPicPathAdapter = new KoAssetInsertPicPathAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetInsertPicPathParser(mKoAssetInsertPicPathAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mGetAssetCheckListServiceId) {
			try {
				mKoAssetGetCheckErrorListAdapter.clear();
				Xml.parse(is, Encoding.UTF_8, new KoAssetGetCheckErrorListParser(mKoAssetGetCheckErrorListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mAssetGetMachineFailListServiceId) {
			try {
				mKoAssetMachineFailListAdapter = new KoAssetMachineFailListAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetMachineFailListParser(mKoAssetMachineFailListAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mAssetRepairServiceId) {
			try {
				mKoAssetCheckRepairAdapter = new KoAssetCheckRepairAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckRepairParser(mKoAssetCheckRepairAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mAssetGetAssetInfoServiceId) {
			try {
				mKoAssetGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetGetAssetInfoParser(mKoAssetGetAssetInfoAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mDataEnableServiceId) {
			try {
				mKoDataEnableAdapter = new KoDataEnableAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoDataEnableParser(mKoDataEnableAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpMaxQuanServiceId) {
			try {
				mKoOpMaxQuanAdapter = new KoOpMaxQuanAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoOpMaxQuanParser(mKoOpMaxQuanAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpStartServiceId) {
			try {
				mKoOpStartAdapter = new MeCodeMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeCodeMsgParser(mKoOpStartAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpEndServiceId) {
			try {
				mKoOpEndAdapter = new KoOpEndAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoOpEndParser(mKoOpEndAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mGetOpStartedListServiceId) {
			try {
				mKoGetOpStartedListAdapter = new KoGetOpStartedListAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoGetOpStartedListParser(mKoGetOpStartedListAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mGetOpMoveAllListServiceId) {
			try {
				mKoGetOpMoveAllListAdapter = new KoGetOpStartedListAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoGetOpStartedListParser(mKoGetOpMoveAllListAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if(id == mQaListByPlanIdListServiceId) {
			try {
				mKoQaListByPlanIdAdapter = new MeQaListNeedFillAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeQaListNeedFillParser(mKoQaListByPlanIdAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mQaListManualAddListServiceId) {
			try {
				mKoQaListManualAdapter = new MeQaListNeedFillAdapter();
				Xml.parse(is, Encoding.UTF_8, new MeQaListNeedFillParser(mKoQaListManualAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mQaInsertDataServiceId) {
			try {
				mKoQaInsertDataAdapter = new KoQaInsertDataAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoQaInsertDataParser(mKoQaInsertDataAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mGetMsgListServiceId) {
			try {
				mKoPushMsgListAdapter = new KoPushMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoPushMsgListParser(mKoPushMsgListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mUpdateApkServiceId) {
			try {
				mKoUpdateApkAdapter = new KoUpdateApkAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoUpdateApkParser(mKoUpdateApkAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpCheckServiceId) {
			try {
				mKoOpCheckBeforeStartAdapter = new KoOpCheckBeforeStartAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoOpCheckBeforeStartParser(mKoOpCheckBeforeStartAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        else if(id == mGetCheckItemListServiceId) {
        	try {
				mKoAssetCheckCheckItemAdapter = new KoAssetCheckCheckItemAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckCheckItemParser(mKoAssetCheckCheckItemAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckGetAssetInfoServiceId) {
        	try {
        		mKoAssetCheckGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetGetAssetInfoParser(mKoAssetCheckGetAssetInfoAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckSubmitCheckServiceId) {
        	try {
        		mKoAssetCheckSubmitCheckAdapter = new KoAssetCheckSubmitCheckAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckSubmitCheckParser(mKoAssetCheckSubmitCheckAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckGetAssetListServiceId) {
        	try {
        		mKoAssetCheckAssetItemAdapter = new KoAssetCheckAssetItemAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckAssetItemParser(mKoAssetCheckAssetItemAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckGetSetAssetListServiceId) {
        	try {
        		mKoAssetCheckSetAssetCheckAdapter = new KoAssetCheckSetAssetCheckAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckSetAssetCheckItemParser(mKoAssetCheckSetAssetCheckAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckUpdateSetAssetListServiceId) {
        	try {
        		mKoAssetCheckUpdateSetAssetCheckAdapter = new KoAssetCheckUpdateSetAssetCheckAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckUpdateSetAssetCheckParser(mKoAssetCheckUpdateSetAssetCheckAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckGetChangedPartsListServiceId) {
        	try {
        		mKoAssetCheckChangedPartsListAdapter = new KoAssetCheckChangedPartsListAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckChangedPartsListParser(mKoAssetCheckChangedPartsListAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckCancelCheckAssetServiceId) {
        	try {
        		mKoAssetCheckCancelCheckAssetAdapter = new KoAssetCheckCancelCheckAssetAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoCheckAssetCancelCheckAssetParser(mKoAssetCheckCancelCheckAssetAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		
		return OnDataParseListener.SUCCESS;
	}



}
