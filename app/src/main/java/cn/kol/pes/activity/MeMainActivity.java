/*-----------------------------------------------------------

-- PURPOSE

--    登陆后的界面，也是主界面。
--	  

-- History

--	  10-Sep-17  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.MeProcessListAdapter;
import cn.kol.pes.model.item.MeAssetItem;
import cn.kol.pes.model.item.MeMainProcessItem;
import cn.kol.pes.model.item.MeStartedSeqItem;
import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;
import cn.kol.pes.model.parser.adapter.MeLoginAdapter;
import cn.kol.pes.model.parser.adapter.MeMainProcessListAdapter;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.KoListDialg;
import cn.kol.pes.widget.MeReceiveOrderDialog;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class MeMainActivity extends BaseActivity implements MeReceiveOrderDialog.IKoReceiveOrderDialog, 
															OnClickListener, OnItemClickListener, 
															OnItemLongClickListener, OnLongClickListener,
															OnCheckedChangeListener {

	
	private MeReceiveOrderDialog mReceiveDlg;
	
	
	private TextView mAssetEdit;//显示工单名称的view
	private ListView mProcessListView;//展示未完成工序列表的view
	
	private MeProcessListAdapter mListAdapter;//未完成工序列表的数据适配器
	private ViewGroup mListParentLayout;//列表的父容器

	private TextView mProcessDateView;
	
	private Calendar mSelectedDate = Calendar.getInstance();
	private KoAssetGetAssetInfoAdapter mAssetData;
	
	private MeMainProcessItem mSelectedMainProcessItem;
	
	private View mStartSeqParent;
	private TextView mStartedSeqProduceNum;
	private TextView mStartedSeqStartTime;
	private TextView mStartedSeqInputQty;
	
	private RadioButton mRadioDay;
	private RadioButton mRadioNight;
	
	private MeStartedSeqItem mStartedSeqData;
	
	private TextView mMachineReportTime;
	
	private enum ScanType {
		Asset,
		TrxId
	}
	
	private ScanType mScanType = ScanType.Asset;

	public static void startAct(Context context) {
		Intent i = new Intent(context, MeMainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_main_activity);//此行代码必须写在super.onCreate(savedInstanceState)前面，
												  //因为在父类的onCreate(savedInstanceState)中，实例化了mTitleView，
												  //这需要首先设置setContentView
		super.onCreate(savedInstanceState);
		
		mReceiveDlg = new MeReceiveOrderDialog(this, this);
		
		mTitleView.setTitle(R.string.ko_title_app_name);

		mAssetEdit = (TextView) findViewById(R.id.main_asset_id_edit);
		mAssetEdit.setOnClickListener(this);
		
		findViewById(R.id.main_camera_btn_asset).setOnClickListener(this);
		
		TextView staffName = (TextView) findViewById(R.id.main_staff_name);
		staffName.setOnClickListener(this);
		
		mProcessDateView = (TextView) findViewById(R.id.main_process_date);
		findViewById(R.id.main_process_reset_date_btn).setOnClickListener(this);
		
		mStartSeqParent = findViewById(R.id.main_started_seq_parent_view);
		mStartSeqParent.setOnClickListener(this);
		mStartSeqParent.setOnLongClickListener(this);
		mStartedSeqProduceNum = (TextView) findViewById(R.id.main_started_seq_process_num);
		mStartedSeqStartTime = (TextView) findViewById(R.id.main_started_seq_start_time);
		mStartedSeqInputQty = (TextView) findViewById(R.id.main_started_seq_input_qty);
		mStartedSeqInputQty.setVisibility(View.GONE);
		
		mProcessDateView.setText(KoDataUtil.getFormatDataForProcess(mSelectedDate));
		
		MeLoginAdapter staff = CacheUtils.getLoginUserInfo();//从缓存中读取登录员工信息,这也是本应用中各个界面交换信息的一种方式
		if(staff!=null && staff.staffNo!=null && staff.staffName!=null) {//判断信息是否为空，如果可用，则显示操作员工信息
			staffName.setText(staff.staffNo.trim()+"-"+staff.staffName.trim());
		}
		
		findViewById(R.id.main_process_asset_search_btn).setOnClickListener(this);
		
		findViewById(R.id.main_no_plan_produce_reply_btn).setOnClickListener(this);//
		findViewById(R.id.main_time_reply_btn).setOnClickListener(this);//
		findViewById(R.id.main_no_plan_materials_reply_btn).setOnClickListener(this);//
		findViewById(R.id.main_check_receive_btn).setOnClickListener(this);//

		
		mProcessListView = (ListView) findViewById(R.id.main_process_list_list_view);
		mListAdapter = new MeProcessListAdapter(this);
		mProcessListView.setAdapter(mListAdapter);
		mProcessListView.setOnItemClickListener(this);
		mProcessListView.setOnItemLongClickListener(this);
		
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
		
		mKoControl.getDateShift();

		getStartedSeq();
	}
	
	private void getStartedSeq() {
		mStartSeqParent.setVisibility(View.GONE);
		if(mAssetData!=null && mAssetData.resourceId!=null) {
			mKoControl.getStartedSeqList(mAssetData.resourceId);
		}
	}

	private IKoPickTimeDlgBack mTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mSelectedDate.setTimeInMillis(cal.getTimeInMillis());
			mProcessDateView.setText(KoDataUtil.getFormatDataForProcess(mSelectedDate));
			
			CacheUtils.setSelectedDate(mSelectedDate);
			
			if(mAssetData != null) {
				getProcessList(mAssetData.assetNumber, KoDataUtil.getFormatDataForProcess(mSelectedDate), CacheUtils.getShift());//assetData.createdDate
			}
		}
	};
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		CacheUtils.setShift(mRadioDay.isChecked()?"DAY":"NIGHT");
		
		if(mAssetData!=null && mAssetData.assetNumber!=null) {
			getProcessList(mAssetData.assetNumber, KoDataUtil.getFormatDataForProcess(mSelectedDate), CacheUtils.getShift());
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.main_asset_id_edit:
			showLoadingDlg();
			mKoControl.getAssetList(CacheUtils.getLoginUserInfo().staffNo);
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
			
		case R.id.main_camera_btn_asset:
			mScanType = ScanType.Asset;
			KoCaptureActivity.startActForRes(this);
			break;
			
		case R.id.main_process_reset_date_btn:
			KoPickTimeDlg dlgT = new KoPickTimeDlg(this, mTimeBack, mSelectedDate, false);
			dlgT.show();
			break;
			
		case R.id.main_started_seq_parent_view:
			MeEndProduceSeqActivity.startAct(this, mStartedSeqData.transactionId, mStartedSeqData.wipId, mStartedSeqData.fmOperationCode, mAssetData.assetNumber, KoDataUtil.getFormatDataForProcess(mSelectedDate), mStartedSeqData.trxQuantity, mStartedSeqData.seqNum);
			break;
			
		case R.id.main_no_plan_produce_reply_btn://打开“非计划生产报数”界面
			
			if(mAssetData!=null && mAssetEdit.getText().toString().equals(mAssetData.assetNumber+" "+mAssetData.description)) {
				CacheUtils.setSelectedDate(mSelectedDate);
				MeProduceIdAssetSeqActivity.startAct(this, null, null, null, null, mAssetData.assetNumber, mAssetData.resourceId, mAssetData.description, null);
			}
			else {
				DialogUtils.showToast(this, "请先扫入设备或输入设备号查询设备");
			}
			break;
			
		case R.id.main_time_reply_btn://开启“时间报数”界面
			
			if(mAssetData!=null && mAssetEdit.getText().toString().equals(mAssetData.assetNumber+" "+mAssetData.description)) {
				CacheUtils.setSelectedDate(mSelectedDate);
				MeStartTimeReportActivity.startAct(MeMainActivity.this, 
												null, 
												null, 
												null,
												null, 
												mAssetData.assetNumber, mAssetData.resourceId, 
												KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()),
												null);
			}
			else {
				DialogUtils.showToast(this, "请先扫入设备或输入设备号查询设备");
			}
			break;
			
		case R.id.main_no_plan_materials_reply_btn://打开“非计划物料报数”界面
			
			if(mAssetData!=null && mAssetEdit.getText().toString().equals(mAssetData.assetNumber+" "+mAssetData.description)) {
				CacheUtils.setSelectedDate(mSelectedDate);
				CacheUtils.setShift(mRadioDay.isChecked()?"DAY":"NIGHT");
				
				MeMaterialsReportOneActivity.startAct(MeMainActivity.this, 
														null, 
														null, 
														null, 
														null,
														mAssetData.assetNumber, mAssetData.resourceId,  
														mAssetData.description, null);
			}
			else {
				DialogUtils.showToast(this, "请先扫入设备或输入设备号查询设备");
			}
			break;
			
		case R.id.main_check_receive_btn:
				mReceiveDlg.show();
				break;
			
		case R.id.main_staff_name:
			showLoadingDlg();
			mKoControl.holidayOnOff(CacheUtils.getLoginUserInfo().staffNo);
			break;
		}
	}
	
	@Override
	public void receivceDlgScanId() {
		mScanType = ScanType.TrxId;
		KoCaptureActivity.startActForRes(this);
	}
	
	@Override
	public void receivceDlgGetQtyById(String id) {
		showLoadingDlg();
		mKoControl.receiveOrderGetQtyById(id,  CacheUtils.getLoginUserInfo().staffNo);
	}

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
	
	private void getProcessList(String resCode, String scheduleDate, String shift) {
		showLoadingDlg();
		mKoControl.getProcessList(resCode, scheduleDate, shift);
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

	//网络请求的回调，需要哪个回调，就复写哪个函数
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			
			@Override
			public void getAssetInfoBack(boolean isSuc, KoAssetGetAssetInfoAdapter assetData, String msg) {
				if(isSuc) {//如果请求成功，则显示相关数据
					mAssetData = assetData;
					mAssetEdit.setText(assetData.assetNumber+" "+assetData.description);
					getProcessList(assetData.assetNumber, KoDataUtil.getFormatDataForProcess(mSelectedDate), CacheUtils.getShift());//assetData.createdDate
					getStartedSeq();
				}
				else {//如果失败，清除相关的显示和数据
					dismissLoadingDlg();
					DialogUtils.showToast(MeMainActivity.this, msg);
				}
			}

			@Override
			public void getAssetListBack(boolean isSuc, List<MeAssetItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					showAssetList(list);
				}
				else {
					DialogUtils.showToast(MeMainActivity.this, msg);
				}
			}

			@Override
			public void getMainProcessListBack(boolean isSuc, MeMainProcessListAdapter processData, String msg) {
				dismissLoadingDlg();
				mMachineReportTime.setText(Html.fromHtml(processData.machineReportTime));
				
				if(isSuc) {
					mListParentLayout.setVisibility(View.VISIBLE);
					mListAdapter.setData(processData.getList());
					}
				else {
					mListParentLayout.setVisibility(View.GONE);
					DialogUtils.showToast(MeMainActivity.this, msg);
				}
				
			}

			@Override
			public void getStartedSeqListBack(boolean isSuc, List<MeStartedSeqItem> startedSeqList, String msg) {
				if(isSuc) {
					if(startedSeqList!=null && startedSeqList.size()>0) {
						mStartSeqParent.setVisibility(View.VISIBLE);
						MeStartedSeqItem seqItem = startedSeqList.get(0);
						mStartedSeqData = seqItem;
						mStartedSeqProduceNum.setText(seqItem.job);
						mStartedSeqStartTime.setText(seqItem.opStart);
						//mStartedSeqInputQty.setText(seqItem.trxQuantity);
					}
				}
				else {
					mStartSeqParent.setVisibility(View.GONE);
					DialogUtils.showToast(MeMainActivity.this, msg);
				}
			}

			@Override
			public void receiveOrderGetQtyByIdBack(boolean isSuc, String qty, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mReceiveDlg.setValueQty(qty);
				}
				else {
					DialogUtils.showToast(MeMainActivity.this, msg);
				}
			}

			@Override
			public void receiveOrderReceiveBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MeMainActivity.this, msg);
				if(isSuc) {
					mReceiveDlg.dismiss();
				}
			}

			@Override
			public void receiveOrderRejectBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MeMainActivity.this, msg);
				if(isSuc) {
					mReceiveDlg.dismiss();
				}
			}

			@Override
			public void deleteStartedSeqBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MeMainActivity.this, msg);
				if(isSuc) {
					getStartedSeq();
				}
			}

			@Override
			public void holidayOnOffBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MeMainActivity.this, msg);
			}

			@Override
			public void getDateShiftBack(boolean isSuc, String date, String shift, String msg) {
				if(isSuc) {
					mSelectedDate.setTimeInMillis(KoDataUtil.convertDateStringToCal(date).getTimeInMillis());
					mProcessDateView.setText(KoDataUtil.getFormatDataForProcess(mSelectedDate));
					
					if("DAY".equals(shift)) {
						mRadioDay.setChecked(true);
						mRadioNight.setChecked(false);
					}
					else {
						mRadioDay.setChecked(false);
						mRadioNight.setChecked(true);
					}
					
					CacheUtils.setSelectedDate(mSelectedDate);
					CacheUtils.setShift(shift);
					
					
					if(mAssetData!=null && mAssetData.assetNumber!=null) {
						getProcessList(mAssetData.assetNumber, KoDataUtil.getFormatDataForProcess(mSelectedDate), CacheUtils.getShift());
					}
				}
			}
		};
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int arg2, long arg3) {
		mSelectedMainProcessItem = (MeMainProcessItem) adapter.getAdapter().getItem(arg2);
		showProcessOperateList();
	}
	
	@Override
	public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int position, long arg3) {//长按未完成工序，删除
		
		return true;
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
	
	//2017
	private void showProcessOperateList() {
		List<String> jobList = new ArrayList<String>();
		jobList.add("生产报数");
		jobList.add("时间报数");
		jobList.add("物料报数");
		
		
		KoProcessListOperateDlg dlg = new KoProcessListOperateDlg(this, null, jobList);
		dlg.show();
	}
	
	//2017
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
			if("生产报数".equals(selData)) {
				
				MeProduceIdAssetSeqActivity.startAct(MeMainActivity.this, 
													mSelectedMainProcessItem.processNum, 
													mSelectedMainProcessItem.wipId, 
													mSelectedMainProcessItem.opCode,
													mSelectedMainProcessItem.seqNum,
													mAssetData.assetNumber, mAssetData.resourceId, 
													mAssetData.description,
													mSelectedMainProcessItem.seqDesc);
			}
			else if("时间报数".equals(selData)) {
				
				MeStartTimeReportActivity.startAct(MeMainActivity.this, 
													mSelectedMainProcessItem.processNum, 
													mSelectedMainProcessItem.wipId, 
													mSelectedMainProcessItem.opCode, 
													mSelectedMainProcessItem.seqNum,
													mAssetData.assetNumber, mAssetData.resourceId, 
													KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()),
													mSelectedMainProcessItem.seqDesc);
			}
			else if("物料报数".equals(selData)) {
				
				MeMaterialsReportOneActivity.startAct(MeMainActivity.this, 
													mSelectedMainProcessItem.processNum, 
													mSelectedMainProcessItem.wipId, 
													mSelectedMainProcessItem.opCode, 
													mSelectedMainProcessItem.seqNum,
													mAssetData.assetNumber, mAssetData.resourceId,  
													mAssetData.description,
													mSelectedMainProcessItem.seqDesc);
			}
		}
	}
	
	//2017
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
			getProcessList(mAssetData.assetNumber, KoDataUtil.getFormatDataForProcess(mSelectedDate), CacheUtils.getShift());//assetData.createdDate
			getStartedSeq();
		}
	}

	//按了back健的回调，提示用户是否退出应用
	@Override
	public void onBackPressed() {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			
			@Override
			public void onOkBack() {
				CacheUtils.clearAllCache();
				MeMainActivity.this.finish();
			}
			
			@Override
			public void onCancelBack() {
				
			}
		}, getString(R.string.ko_title_sure_exit_app));
		
		dlg.setOkCancelBtn(true, true);
		dlg.show();
	}

	@Override
	public boolean onLongClick(View v) {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			@Override
			public void onOkBack() {
				if(mStartedSeqData != null) {
					showLoadingDlg(R.string.ko_title_is_deleting_op);
					mKoControl.deleteStartedSeq(mStartedSeqData.transactionId);
				}
			}
			
			@Override
			public void onCancelBack() {
				
			}
		}, R.string.ko_title_sure_delete_op);
		dlg.setOkCancelBtn(true, true);
		return false;
	}


}
