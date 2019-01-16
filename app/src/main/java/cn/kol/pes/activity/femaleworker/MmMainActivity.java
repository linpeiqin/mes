/*-----------------------------------------------------------

-- PURPOSE

--    登陆后的界面，也是主界面。
--	  

-- History

--	  10-Sep-17  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity.femaleworker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.activity.BaseActivity;
import cn.kol.pes.activity.KoCaptureActivity;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.MeProcessListAdapter;
import cn.kol.pes.model.item.MeAssetItem;
import cn.kol.pes.model.item.MeMainProcessItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesItem;
import cn.kol.pes.model.item.femaleworker.MmGroupItem;
import cn.kol.pes.model.item.femaleworker.MmMainListItem;
import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;
import cn.kol.pes.model.parser.adapter.MeLoginAdapter;
import cn.kol.pes.model.parser.adapter.MeMainProcessListAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareJobAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetGroupInfoAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetMachineReportTimeAdapter;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.KoListDialg;
import cn.kol.pes.widget.MeReceiveOrderDialog;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class MmMainActivity extends BaseActivity implements MeReceiveOrderDialog.IKoReceiveOrderDialog,OnClickListener, OnItemClickListener,
															OnCheckedChangeListener {


	private MeReceiveOrderDialog mReceiveDlg;
	private TextView mAssetEdit;//显示工单名称的view
	private TextView groupEdit;//显示组别view
	private ListView mProcessListView;//展示未完成工序列表的view
	
	private MeProcessListAdapter mListAdapter;//未完成工序列表的数据适配器
	private ViewGroup mListParentLayout;//列表的父容器

	private TextView mProcessDateView;
	
	private Calendar mSelectedDate = Calendar.getInstance();
	private KoAssetGetAssetInfoAdapter mAssetData;
	private MmGetGroupInfoAdapter mGroupData;

	private MeMainProcessItem mSelectedMainProcessItem;

	private RadioButton mRadioDay;
	private RadioButton mRadioNight;

	private TextView mMachineReportTime;
	private Button mAttendanceBtn;

	private enum ScanType {
		Asset,
		TrxId
	}

	private MmMainActivity.ScanType mScanType = MmMainActivity.ScanType.Asset;

	public static void startAct(Context context) {
		Intent i = new Intent(context, MmMainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.mm_handwork_main_activity);//此行代码必须写在super.onCreate(savedInstanceState)前面，
												  //因为在父类的onCreate(savedInstanceState)中，实例化了mTitleView，
												  //这需要首先设置setContentView
		super.onCreate(savedInstanceState);
		mReceiveDlg = new MeReceiveOrderDialog(this, this);
		mTitleView.setTitle(R.string.ko_title_app_name);
		mSelectedDate.set(2018,6,16);
		mAssetEdit = (TextView) findViewById(R.id.main_asset_id_edit);
		mAssetEdit.setOnClickListener(this);

		groupEdit = (TextView) findViewById(R.id.main_group_user);
		groupEdit.setOnClickListener(this);
		groupEdit.setText("A4");
		mGroupData = new MmGetGroupInfoAdapter();
		mGroupData.groupCode = "A4";
		findViewById(R.id.main_camera_btn_asset).setOnClickListener(this);
		
		TextView staffName = (TextView) findViewById(R.id.main_staff_name);
		staffName.setOnClickListener(this);
		
		mProcessDateView = (TextView) findViewById(R.id.main_process_date);
		findViewById(R.id.main_process_date).setOnClickListener(this);//时间点击

		findViewById(R.id.main_non_plan_time_btn).setOnClickListener(this);//非生产状况
		mAttendanceBtn = (Button) findViewById(R.id.main_attendance_btn);
		mAttendanceBtn.setOnClickListener(this);//出勤状况按钮
		findViewById(R.id.main_check_receive_btn).setOnClickListener(this);//接收按钮
		findViewById(R.id.main_non_declare_time_btn).setOnClickListener(this);//非生产状况按钮


		mProcessDateView.setText(KoDataUtil.getFormatDataForProcess(mSelectedDate));
		
		MeLoginAdapter staff = CacheUtils.getLoginUserInfo();//从缓存中读取登录员工信息,这也是本应用中各个界面交换信息的一种方式
		if(staff!=null && staff.staffNo!=null && staff.staffName!=null) {//判断信息是否为空，如果可用，则显示操作员工信息
			staffName.setText(staff.staffNo.trim()+"-"+staff.staffName.trim());
		}
		
		findViewById(R.id.main_process_asset_search_btn).setOnClickListener(this);
        findViewById(R.id.main_process_select_btn).setOnClickListener(this);

		
		mProcessListView = (ListView) findViewById(R.id.main_process_list_list_view);
		mListAdapter = new MeProcessListAdapter(this);
		mProcessListView.setAdapter(mListAdapter);
		mProcessListView.setOnItemClickListener(this);

		mListParentLayout = (ViewGroup) findViewById(R.id.main_op_started_list_parent_layout);
		mListParentLayout.setVisibility(View.GONE);
		
		mRadioDay = (RadioButton) findViewById(R.id.main_shift_day_radio);
		mRadioNight = (RadioButton) findViewById(R.id.main_shift_night_radio);
		
		mRadioDay.setOnCheckedChangeListener(this);
		mRadioNight.setOnCheckedChangeListener(this);
		
		mRadioDay.setChecked(true);
		mRadioNight.setChecked(false);
		
		mMachineReportTime = (TextView) findViewById(R.id.main_machine_report_time);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshMainInfo();
	}

	public void refreshMainInfo() {
		if (mAssetData == null || mAssetData.assetNumber==null){
			DialogUtils.showToast(MmMainActivity.this, "请选择设备");
			return;
		}
		if (mGroupData == null || mGroupData.groupCode==null){
			DialogUtils.showToast(MmMainActivity.this, "请选择组别");
			return;
		}

		if(CacheUtils.getShift() == null){
			DialogUtils.showToast(MmMainActivity.this, "请选择班次");
			return;
		}
		showLoadingDlg();
		getMachineReportTime(mAssetData.assetNumber,KoDataUtil.getFormatDataForProcess(mSelectedDate),CacheUtils.getShift());
		getProcessList();
	}
	private IKoPickTimeDlgBack mTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mSelectedDate.setTimeInMillis(cal.getTimeInMillis());
			mProcessDateView.setText(KoDataUtil.getFormatDataForProcess(mSelectedDate));
			CacheUtils.setSelectedDate(mSelectedDate);
			refreshMainInfo();
		}
	};
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (mRadioDay.isChecked()&&!mRadioNight.isChecked()){
			CacheUtils.setShift("DAY");
			refreshMainInfo();
		} else if (!mRadioDay.isChecked()&&mRadioNight.isChecked()){
			CacheUtils.setShift("NIGHT");
			refreshMainInfo();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.main_asset_id_edit:
			showLoadingDlg();
			//修改加载女工的设备列表
			mKoControl.getAssetList4F(CacheUtils.getLoginUserInfo().staffNo);
			break;
		
		case R.id.main_process_asset_search_btn:
			if(mAssetEdit.getText().length()>1) {
				showLoadingDlg();

				getAssetInfo(mAssetEdit.getText().toString().toUpperCase());
			}
			else {
				DialogUtils.showToast(this, R.string.ko_tips_input_job_num);
			}
			break;
		//查找排期
		case R.id.main_process_select_btn:
            getProcessList();
            break;
		case R.id.main_non_plan_time_btn:
			getProcessList();
			break;

		case R.id.main_group_user:
			//修改加载女工的组别列表
			if (mAssetData == null || mAssetData.assetNumber==null){
				DialogUtils.showToast(this, "请选择设备");
				break;
			}
			showLoadingDlg();
			mKoControl.getGroupList4F(mAssetData.assetNumber);
			break;

		case R.id.main_process_date:
			KoPickTimeDlg dlgT = new KoPickTimeDlg(this, mTimeBack, mSelectedDate, false);
			dlgT.show();
			break;

		case R.id.main_attendance_btn:
		    if (groupEdit.getText().toString().equals("")){
                DialogUtils.showToast(this, "请选择组别");
            } else if (mAssetEdit.getText().toString().equals("")){
                DialogUtils.showToast(this, "请选择设备");
            } else {
                MmAttendanceActivity.startAct(MmMainActivity.this,
						mProcessDateView.getText().toString(),
                        CacheUtils.getShift(),
                        CacheUtils.getLoginUserInfo().staffNo,
                        groupEdit.getText().toString()
                        );
            }
			break;
		case R.id.main_check_receive_btn:
			mReceiveDlg.show();
			break;
		case R.id.main_non_declare_time_btn:
			if (groupEdit.getText().toString().equals("")){
				DialogUtils.showToast(this, "请选择组别");
			} else if (mAssetEdit.getText().toString().equals("")){
				DialogUtils.showToast(this, "请选择设备");
			}else {
				MmNonDeclareTimeActivity.startAct(MmMainActivity.this,
						mProcessDateView.getText().toString(),
						groupEdit.getText().toString(),
						CacheUtils.getShift(),
						CacheUtils.getLoginUserInfo().staffNo
				);
			}
			break;
		case R.id.main_staff_name:
			showLoadingDlg();
			mKoControl.holidayOnOff(CacheUtils.getLoginUserInfo().staffNo);
			break;
		}
	}

  public void setDeclareTimesItem(MmGetDeclareTimesItem declareTimesItem){
	  declareTimesItem.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
	  declareTimesItem.inventoryItemId = mSelectedMainProcessItem.productId;
	  declareTimesItem.jobId = mSelectedMainProcessItem.jobId;
	  declareTimesItem.wipEntityId = mSelectedMainProcessItem.wipId;
	  declareTimesItem.scheduleDate = mSelectedMainProcessItem.scheduleDate.replace("-","/").substring(0,10);
	  declareTimesItem.workGroup = groupEdit.getText().toString();
	  declareTimesItem.workMonitor = CacheUtils.getLoginUserInfo().staffNo;
	  declareTimesItem.workClassCode = CacheUtils.getShift();
  }

	//网络请求的回调，需要哪个回调，就复写哪个函数
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			
			@Override
			public void getAssetInfoBack(boolean isSuc, KoAssetGetAssetInfoAdapter assetData, String msg) {
				if(isSuc) {//如果请求成功，则显示相关数据
					mAssetData = assetData;
					mAssetEdit.setText(assetData.assetNumber+" "+assetData.description);
					refreshMainInfo();
				}
				else {//如果失败，清除相关的显示和数据
					dismissLoadingDlg();
					DialogUtils.showToast(MmMainActivity.this, msg);
				}
			}
			@Override
			public void getDeclareTimeListToMtlBack(boolean isSuc, List<MmGetDeclareTimesBackItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {//如果请求成功，则显示相关数据
					if (list.size() == 1){
						MmGetDeclareTimesBackItem declareTimesBackItem =  list.get(0);
						startDeclareMtl(declareTimesBackItem);
					}
					else if (list.size()>1) {
						showDeclareTimeList(list);
					}
				}
				else {//如果失败，清除相关的显示和数据
					DialogUtils.showToast(MmMainActivity.this, msg);
				}
			}
			@Override
			public void declareJobBack(boolean isSuc, MmDeclareJobAdapter data, String msg) {
				if(isSuc) {
					String selData = CacheUtils.getSelData();
					if("生产状况".equals(selData)) {
						MmGetDeclareTimesItem declareTimesItem = new MmGetDeclareTimesItem();
						setDeclareTimesItem(declareTimesItem);
						MmDeclareTimeActivity.startAct(MmMainActivity.this,
								data.declareJobId,
								mSelectedMainProcessItem.projectNum,
								mSelectedMainProcessItem.processNum,
								mSelectedMainProcessItem.productCode,
								mSelectedMainProcessItem.productDesc,
								mSelectedMainProcessItem.primaryUomCode,
								mSelectedMainProcessItem.seqNum,
								mSelectedMainProcessItem.seqDesc,
                                groupEdit.getText().toString(),
								mSelectedMainProcessItem.scheduleDate,
								declareTimesItem,
								mAssetData.assetNumber);
					}
					else if("物料报数".equals(selData)) {
						MmGetDeclareTimesItem declareTimesItem = new MmGetDeclareTimesItem();
						setDeclareTimesItem(declareTimesItem);
						showLoadingDlg();
						mKoControl.getDeclareTimeListToMtl(declareTimesItem);
					}
				}
				else {
					DialogUtils.showToast(MmMainActivity.this, msg);
				}
			}

			@Override
			public void getAssetListBack(boolean isSuc, List<MeAssetItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					showAssetList(list);
				}
				else {
					DialogUtils.showToast(MmMainActivity.this, msg);
				}
			}
			@Override
			public void receiveOrderGetQtyByIdBack(boolean isSuc, String qty, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mReceiveDlg.setValueQty(qty);
				}
				else {
					DialogUtils.showToast(MmMainActivity.this, msg);
				}
			}

			@Override
			public void receiveOrderReceiveBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MmMainActivity.this, msg);
				if(isSuc) {
					mReceiveDlg.dismiss();
				}
			}

			@Override
			public void receiveOrderRejectBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MmMainActivity.this, msg);
				if(isSuc) {
					mReceiveDlg.dismiss();
				}
			}
			@Override
			public void getGroupListBack(boolean isSuc, List<MmGroupItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					showGroupList(list);
				}
				else {
					DialogUtils.showToast(MmMainActivity.this, msg);
				}
			}
			@Override
			public void getMachineReportTimeBack(boolean isSuc, MmGetMachineReportTimeAdapter processData, String msg) {
				if(isSuc) {
					mMachineReportTime.setText(Html.fromHtml(processData.machineReportTime));
				}
				else {
					DialogUtils.showToast(MmMainActivity.this, msg);
				}
			}

			@Override
			public void getMainProcessListBack(boolean isSuc, MeMainProcessListAdapter processData, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mListParentLayout.setVisibility(View.VISIBLE);
					mListAdapter.setData(processData.getList());
					}
				else {
					mListParentLayout.setVisibility(View.GONE);
					DialogUtils.showToast(MmMainActivity.this, msg);
				}
				
			}
			@Override
			public void holidayOnOffBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				if (msg.equals("")){
					mAttendanceBtn.setVisibility(View.GONE);
				} else {
					mAttendanceBtn.setVisibility(View.VISIBLE);
				}
				DialogUtils.showToast(MmMainActivity.this, msg);
			}

		};
	}
	//启动物料报数界面
	private void startDeclareMtl(MmGetDeclareTimesBackItem declareTimeItem){
		MmDeclareMtlActivity.startAct(MmMainActivity.this,
				declareTimeItem.jobTransactionId,
				declareTimeItem.moveTransactionId,
				mSelectedMainProcessItem.projectNum,
				mSelectedMainProcessItem.processNum,
				mSelectedMainProcessItem.wipId,
				mSelectedMainProcessItem.productDesc,
				mSelectedMainProcessItem.seqNum,
				mSelectedMainProcessItem.seqDesc,
				declareTimeItem.niOperationCode
				);
	}
	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int arg2, long arg3) {
		mSelectedMainProcessItem = (MeMainProcessItem) adapter.getAdapter().getItem(arg2);
		showProcessOperateList();
	}
	


	private void getProcessList() {
		MmMainListItem mmMainProcessItem = new MmMainListItem();
		mmMainProcessItem.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
		mmMainProcessItem.resCode = mAssetData.assetNumber;
		mmMainProcessItem.scheduleDate = KoDataUtil.getFormatDataForProcess(mSelectedDate);
		mmMainProcessItem.workClassCode =  CacheUtils.getShift();
		mmMainProcessItem.workMonitor = CacheUtils.getLoginUserInfo().staffNo;
		mmMainProcessItem.workGroup = groupEdit.getText().toString();
		mKoControl.getProcessList4F(mmMainProcessItem);
	}
	private void getMachineReportTime(String resCode, String scheduleDate, String shift) {
		mKoControl.getMachineReportTime4F(resCode, scheduleDate, shift);
	}
	//女工生产任务建单
	private void declareJob(String organizationId,String jobId,String wipEntityId,String inventoryItemId,String scheduleDate,String workGroup,String workMonitor,String workClassCode){
		mKoControl.declareJob4F(organizationId,jobId,wipEntityId,inventoryItemId,scheduleDate,workGroup,workMonitor,workClassCode);

	}
	//2017
	private void getAssetInfo(String resCode) {
		mKoControl.getAssetInfo(CacheUtils.getLoginUserInfo().staffNo, resCode.toUpperCase());
	}
	
	//2017
	private void showAssetList(List<MeAssetItem> assetList) {
		AssetListDlg dlg = new AssetListDlg(this, null, assetList);
		dlg.show();
	}

	//2018 女工修改
	private void showGroupList(List<MmGroupItem> groupList) {
		GroupListDlg dlg = new GroupListDlg(this, null, groupList);
		dlg.show();
	}
	//2018 女工修改
	private void showDeclareTimeList(List<MmGetDeclareTimesBackItem> declareTimeList) {
		DeclareTimeListDlg dlg = new DeclareTimeListDlg(this, null, declareTimeList);
		dlg.show();
	}
	//2017
	private void showProcessOperateList() {
		List<String> jobList = new ArrayList<String>();
		jobList.add("生产状况");
		jobList.add("物料报数");
		KoProcessListOperateDlg dlg = new KoProcessListOperateDlg(this, null, jobList);
		dlg.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {

			String dataS = data.getStringExtra(KoCaptureActivity.KEY_RES_INTENT);

			LogUtils.e(tag, "onActivityResult():dataS="+dataS);

			if(dataS!=null && dataS.length()>1) {
				showLoadingDlg();
				if(mScanType == ScanType.TrxId) {
					mReceiveDlg.setValueId(dataS);
					mKoControl.receiveOrderGetQtyById(dataS,CacheUtils.getLoginUserInfo().staffNo);
				}
				else {
					getAssetInfo(dataS);
				}
			}
			else {
				DialogUtils.showToast(this, "机器号输入错误");
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	//接受按钮操作
	//扫描id
	@Override
	public void receivceDlgScanId() {
		mScanType = MmMainActivity.ScanType.TrxId;
		KoCaptureActivity.startActForRes(this);
	}
	//得到数量通过扫描的id
	@Override
	public void receivceDlgGetQtyById(String id) {
		showLoadingDlg();
		mKoControl.receiveOrderGetQtyById(id,  CacheUtils.getLoginUserInfo().staffNo);
	}
	//提交接受
	@Override
	public void receivceDlgSureReceive(String id, String qty) {
		showLoadingDlg();
		mKoControl.receiveOrderSureToReceive(id, qty, CacheUtils.getLoginUserInfo().staffNo);
	}

	@Override
	public void receivceDlgSureReject(String id) {
		showLoadingDlg();
		mKoControl.receiveOrderSureReject(id, CacheUtils.getLoginUserInfo().staffNo);
	}

	//2017主列表
	public class KoProcessListOperateDlg extends KoListDialg<String> {

		public KoProcessListOperateDlg(Activity context, String selectedData, List<String> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(String selectedItem) {
			return selectedItem;
		}

		@Override
		public boolean isSelectedObjEquals(String selectedData, String item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(String selData) {
			CacheUtils.setSelectedDate(mSelectedDate);
			CacheUtils.setSelData(selData);
			if (groupEdit.getText().toString() == null || groupEdit.getText().toString().equals("")){
				DialogUtils.showToast(MmMainActivity.this, "请选择组别");
				return ;
			}
			declareJob(CacheUtils.getMmGetOrgInfoAdapter().organizationId,
					mSelectedMainProcessItem.jobId,
					mSelectedMainProcessItem.wipId,
					mSelectedMainProcessItem.productId,
					mSelectedMainProcessItem.scheduleDate.replace("-","/").substring(0,10),
					groupEdit.getText().toString(),
					CacheUtils.getLoginUserInfo().staffNo,
					CacheUtils.getShift()
					);
		}
	}
	
	//2017 设备列表
	public class AssetListDlg extends KoListDialg<MeAssetItem> {

		public AssetListDlg(Activity context, MeAssetItem selectedData, List<MeAssetItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MeAssetItem selectedItem) {
			return selectedItem.assetCode+" "+selectedItem.desc;
		}

		@Override
		public boolean isSelectedObjEquals(MeAssetItem selectedData, MeAssetItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MeAssetItem selData) {
			mAssetData = new KoAssetGetAssetInfoAdapter();
			mAssetData.assetNumber = selData.assetCode;
			mAssetData.description = selData.desc;
			mAssetData.resourceId = selData.assetId;
			mAssetEdit.setText(mAssetData.assetNumber+" "+mAssetData.description);
			refreshMainInfo();
		}
	}



	//2018修改女工显示组别
	public class GroupListDlg extends KoListDialg<MmGroupItem> {

		public GroupListDlg(Activity context, MmGroupItem selectedData, List<MmGroupItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MmGroupItem selectedItem) {
			return selectedItem.groupCode;
		}

		@Override
		public boolean isSelectedObjEquals(MmGroupItem selectedData, MmGroupItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MmGroupItem selData) {
			mGroupData = new MmGetGroupInfoAdapter();
			mGroupData.groupCode = selData.groupCode;
			groupEdit.setText(mGroupData.groupCode);
			refreshMainInfo();
		}
	}

	//2018显示生产状况列表
	public class DeclareTimeListDlg extends KoListDialg<MmGetDeclareTimesBackItem> {

		public DeclareTimeListDlg(Activity context, MmGetDeclareTimesBackItem selectedData, List<MmGetDeclareTimesBackItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MmGetDeclareTimesBackItem selectedItem) {
			return selectedItem.operationSeqNum+" " + selectedItem.operationDesc+" "+selectedItem.niOperationCode+" "+selectedItem.niOperationDesc;
		}

		@Override
		public boolean isSelectedObjEquals(MmGetDeclareTimesBackItem selectedData, MmGetDeclareTimesBackItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MmGetDeclareTimesBackItem selData) {
			startDeclareMtl(selData);
		}
	}
	//按了back健的回调，提示用户是否退出应用
	@Override
	public void onBackPressed() {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			
			@Override
			public void onOkBack() {
				CacheUtils.clearAllCache();
				//MeLoginActivity.startAct(MmMainActivity.this);
				MmMainActivity.this.finish();
			}
			
			@Override
			public void onCancelBack() {
				
			}
		}, getString(R.string.ko_title_sure_exit_app));
		
		dlg.setOkCancelBtn(true, true);
		dlg.show();
	}

}
