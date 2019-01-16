/*-----------------------------------------------------------

-- PURPOSE

--    生产报数开始的第二个界面

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.MeProduceItem;
import cn.kol.pes.model.item.MeTimeReportActiveItem;
import cn.kol.pes.model.item.MeTimeReportSeqListItem;
import cn.kol.pes.model.parser.adapter.MeTimeReportGetDescInfoAdapter;
import cn.kol.pes.model.parser.adapter.MeTimeReportSeqListAdapter;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.KoListDialg;
import cn.kol.pes.widget.MeAddNewReasonDialog;

public class MeStartTimeReportActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	private TextView mAssetView;
	private TextView mTimeReportDateView;
	private TextView mShiftView;
	private TextView mDisplayView;

	private EditText mInputProduceNumEdit;
	private View mProduceNumSearch;
	
	private ViewGroup mCheckParent;
	private CheckBox mCheckProduceTime;
	private CheckBox mCheckNoneProduceTime;
	
	private TextView mInputSeqNumEdit;
	private TextView mInputActiveCodeEdit;
	private EditText mInputAddTimeEdit;
	private TextView mInputAddTimeReasonEdit;
	private EditText mInputCompleteNumEdit;
	private EditText mInputScrapNumEdit;
	
	private View mProduceNumParent;
	private View mSeqNumParent;
	private View mCompleteQtyParent;
	private View mScrapQtyParent;
	
	private Button mEndBtn;

	private MeProduceItem mSelectedProduceItemData;
	private MeTimeReportSeqListItem mSelectedSeqData;
	private MeTimeReportActiveItem mSelectedActiveData;
	
	private List<MeTimeReportSeqListItem> mSeqListData;
	private MeTimeReportGetDescInfoAdapter mDescAndActiveListInfo;
	private List<MeTimeReportActiveItem> mActivieListData;
	
	private List<String> mReasonListData;

	private static final String KEY_PRODUCE_NUM = "key_produce_num";
	private static final String KEY_WIP_ID = "key_wip_id";
	private static final String KEY_OP_CODE = "key_op_code";
	private static final String KEY_SEQ_NUM = "key_seq_num";
	private static final String KEY_ASSET_CODE = "key_asset_code";
	private static final String KEY_ASSET_ID = "key_asset_id";
	private static final String KEY_SCHED_DATE = "key_sched_date";
	private static final String KEY_OP_DESC = "key_op_desc";
	
	public static void startAct(Context context, String produceNum, String wipId, String opCode, String seqNum, String assetCode, String assetId, String schedDate, String opDesc) {
		
		Intent i = new Intent(context, MeStartTimeReportActivity.class);
		i.putExtra(KEY_PRODUCE_NUM, produceNum);
		i.putExtra(KEY_WIP_ID, wipId);
		i.putExtra(KEY_OP_CODE, opCode);
		i.putExtra(KEY_SEQ_NUM, seqNum);
		i.putExtra(KEY_ASSET_CODE, assetCode);
		i.putExtra(KEY_ASSET_ID, assetId);
		i.putExtra(KEY_SCHED_DATE, schedDate);
		i.putExtra(KEY_OP_DESC, opDesc);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_start_time_report_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle("时间报数");

		mAssetView = (TextView) findViewById(R.id.start_time_report_asset_num);
		mTimeReportDateView = (TextView) findViewById(R.id.start_time_report_date_time);
		
		mShiftView = (TextView) findViewById(R.id.start_time_report_date_shift);
		mShiftView.setText("DAY".equals(CacheUtils.getShift())?"白班":"晚班");
		
		mAssetView.setText(getIntent().getStringExtra(KEY_ASSET_CODE));
		mTimeReportDateView.setText(getIntent().getStringExtra(KEY_SCHED_DATE));

		mDisplayView = (TextView) findViewById(R.id.start_time_report_info_display);
		
		mProduceNumParent = findViewById(R.id.start_time_report_produce_num_parent);
		mSeqNumParent = findViewById(R.id.start_time_report_seq_num_parent);
		mCompleteQtyParent = findViewById(R.id.start_time_report_complate_qty_parent);
		mScrapQtyParent = findViewById(R.id.start_time_report_scrap_qty_parent);
		
		mCheckParent = (ViewGroup) findViewById(R.id.start_time_report_check_parent);
		mCheckProduceTime = (CheckBox) findViewById(R.id.start_time_report_check_produce_time);
		mCheckNoneProduceTime = (CheckBox) findViewById(R.id.start_time_report_check_none_produce_time);
		
		mInputProduceNumEdit = (EditText) findViewById(R.id.start_time_report_produce_num_edit);
		mProduceNumSearch = findViewById(R.id.start_time_report_produce_num_search);
		mProduceNumSearch.setOnClickListener(this);
		
		mInputSeqNumEdit = (TextView) findViewById(R.id.start_time_report_seq_num);
		mInputSeqNumEdit.setOnClickListener(this);
		
		mInputActiveCodeEdit = (TextView) findViewById(R.id.start_time_report_active_num);
		mInputActiveCodeEdit.setOnClickListener(this);
		
		mInputAddTimeEdit = (EditText) findViewById(R.id.start_time_report_add_time);
		mInputAddTimeReasonEdit = (TextView) findViewById(R.id.start_time_report_add_time_reason);
		mInputAddTimeReasonEdit.setOnClickListener(this);
		findViewById(R.id.start_time_report_add_time_reason_btn).setOnClickListener(this);
		
		mInputCompleteNumEdit = (EditText) findViewById(R.id.start_time_report_complete_num);
		mInputScrapNumEdit = (EditText) findViewById(R.id.start_time_report_scrap_num);

		mEndBtn = (Button) findViewById(R.id.start_time_report_end_btn);
		mEndBtn.setOnClickListener(this);
		
		mCheckProduceTime.setOnCheckedChangeListener(this);
		mCheckNoneProduceTime.setOnCheckedChangeListener(this);
		
		mCheckProduceTime.setChecked(false);
		mCheckNoneProduceTime.setChecked(true);

		showLoadingDlg();
		Intent i = getIntent();
		boolean isWipNotNull = KoDataUtil.isStringNotNull(i.getStringExtra(KEY_WIP_ID));
		
		if(isWipNotNull) {
			mInputProduceNumEdit.setText(getIntent().getStringExtra(KEY_PRODUCE_NUM));
			mInputSeqNumEdit.setText(getIntent().getStringExtra(KEY_SEQ_NUM)+"-"+getIntent().getStringExtra(KEY_OP_DESC));
			mInputProduceNumEdit.setEnabled(false);
			mInputSeqNumEdit.setEnabled(false);
			mProduceNumSearch.setVisibility(View.GONE);
			
			mCheckParent.setVisibility(View.GONE);
			mCheckProduceTime.setChecked(true);
			mCheckNoneProduceTime.setVisibility(View.GONE);
		}
		else {
			mInputProduceNumEdit.setEnabled(true);
			mInputSeqNumEdit.setEnabled(true);
			mProduceNumSearch.setVisibility(View.VISIBLE);
			
			mCheckParent.setVisibility(View.VISIBLE);
			mCheckNoneProduceTime.setChecked(true);
			mCheckProduceTime.setVisibility(View.GONE);
		}
		
		mKoControl.timeReportGetDescInfoWhenStart(i.getStringExtra(KEY_ASSET_CODE), 
												  i.getStringExtra(KEY_SCHED_DATE), 
												  isWipNotNull?"1":"2", 
												  i.getStringExtra(KEY_WIP_ID),
												  CacheUtils.getLoginUserInfo().staffNo,
												  CacheUtils.getShift(), i.getStringExtra(KEY_OP_CODE));
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked) {
			showLoadingDlg();
			buttonView.setEnabled(false);
			
			if(buttonView.getId() == R.id.start_time_report_check_produce_time) {
				
				mCheckNoneProduceTime.setChecked(false);
				mCheckNoneProduceTime.setEnabled(true);

				mProduceNumParent.setVisibility(View.VISIBLE);
				mSeqNumParent.setVisibility(View.VISIBLE);
				mCompleteQtyParent.setVisibility(View.VISIBLE);
				mScrapQtyParent.setVisibility(View.VISIBLE);
			}
			else {
				mCheckProduceTime.setChecked(false);
				mCheckProduceTime.setEnabled(true);
				
				mProduceNumParent.setVisibility(View.GONE);
				mSeqNumParent.setVisibility(View.GONE);
				mCompleteQtyParent.setVisibility(View.GONE);
				mScrapQtyParent.setVisibility(View.GONE);
			}
			
			mSelectedActiveData = null;
			mInputActiveCodeEdit.setText(null);
			
			Intent i = getIntent();
			String intWipId = getIntent().getStringExtra(KEY_WIP_ID);
			boolean isProduce = KoDataUtil.isStringNotNull(intWipId) || mCheckProduceTime.isChecked();
			
			mKoControl.timeReportGetDescInfoWhenStart(i.getStringExtra(KEY_ASSET_CODE), 
													  i.getStringExtra(KEY_SCHED_DATE), 
													  isProduce?"1":"2", 
													  i.getStringExtra(KEY_WIP_ID),
													  CacheUtils.getLoginUserInfo().staffNo,
													  CacheUtils.getShift(),
													  i.getStringExtra(KEY_OP_CODE));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_time_report_produce_num_search:
			String projectNum = mInputProduceNumEdit.getText().toString();
			if(KoDataUtil.isStringNotNull(projectNum)) {
				showLoadingDlg();
				mKoControl.getProduceListByProjectNum(projectNum.toUpperCase());
			}
			else {
				DialogUtils.showToast(this, "请输入工程单号");
			}
			break;

		case R.id.start_time_report_seq_num:
			LogUtils.e(tag, "R.id.start_time_report_seq_num");
			showSeqList(mSeqListData);
			break;
			
		case R.id.start_time_report_active_num:
			LogUtils.e(tag, "R.id.start_time_report_active_num");
			showActiveList(mActivieListData);
			break;
			
		case R.id.start_time_report_add_time_reason:
			showReasonList(mReasonListData);
			break;
			
		case R.id.start_time_report_add_time_reason_btn:
			MeAddNewReasonDialog dlg = new MeAddNewReasonDialog(MeStartTimeReportActivity.this,
			new MeAddNewReasonDialog.IMeAddNewReasonDialog() {
				public void addedReason(String reason) {
					mInputAddTimeReasonEdit.setText(reason);
				}
			});
			dlg.show();
			break;

		case R.id.start_time_report_end_btn:
			
			if(mDescAndActiveListInfo == null || mDescAndActiveListInfo.errorCode>1) {
				DialogUtils.showToast(this, 
				KoDataUtil.isStringNotNull(mDescAndActiveListInfo.errorMsg)?mDescAndActiveListInfo.errorMsg:"未能正确获取描述信息，不能进行时间报数");
			}
			else {
				String intWipId = getIntent().getStringExtra(KEY_WIP_ID);
				boolean isProduce = KoDataUtil.isStringNotNull(intWipId) || mCheckProduceTime.isChecked();
				
				String seqNum = mInputSeqNumEdit.getText().toString();
				String activeCode = mInputActiveCodeEdit.getText().toString();
				String addTime = mInputAddTimeEdit.getText().toString();
				String addTimeReason = mInputAddTimeReasonEdit.getText().toString();
				String goodQty = mInputCompleteNumEdit.getText().toString();
				String scrapQty = mInputScrapNumEdit.getText().toString();
	
				if(isProduce && mSelectedProduceItemData == null && !KoDataUtil.isStringNotNull(getIntent().getStringExtra(KEY_WIP_ID))) {
					DialogUtils.showToast(this, "请先确定生产单");
				}
				else if(isProduce && (!KoDataUtil.isStringNotNull(seqNum) && mSelectedSeqData==null)) {
					DialogUtils.showToast(this, "请确定工序");
				}
				else if(!KoDataUtil.isStringNotNull(activeCode) || mSelectedActiveData==null) {
					DialogUtils.showToast(this, "请选择活动代号");
				}
				else if(!KoDataUtil.isStringNotNull(addTime)) {
					DialogUtils.showToast(this, "请输入时间");
				}
				else if(!KoDataUtil.isValidNumber(addTime) && !KoDataUtil.isValidReal(addTime)) {
					DialogUtils.showToast(this, "时间必须为数字");
				}
				else if(isProduce && !KoDataUtil.isStringNotNull(goodQty)) {
					DialogUtils.showToast(this, "请输入好货数");
				}
				else if(isProduce && !KoDataUtil.isValidNumber(goodQty)) {
					DialogUtils.showToast(this, "好货数必须为数字");
				}
				else if(isProduce && !KoDataUtil.isStringNotNull(scrapQty)) {
					DialogUtils.showToast(this, "请输入损耗数");
				}
				else if(isProduce && !KoDataUtil.isValidNumber(scrapQty)) {
					DialogUtils.showToast(this, "损耗数必须为数字");
				}
				else if(mDescAndActiveListInfo!=null && mDescAndActiveListInfo.errorCode>1) {
					DialogUtils.showToast(this, mDescAndActiveListInfo.errorMsg);
				}
				else {
					showLoadingDlg();
					if(isProduce) {
						mKoControl.timeReportComplete(getIntent().getStringExtra(KEY_SCHED_DATE), 
									getIntent().getStringExtra(KEY_ASSET_CODE),
									CacheUtils.getLoginUserInfo().staffNo, 
									"1", 
									KoDataUtil.isStringNotNull(intWipId)?intWipId:mSelectedProduceItemData.wipEntityId, 
									KoDataUtil.isStringNotNull(getIntent().getStringExtra(KEY_OP_CODE))?getIntent().getStringExtra(KEY_OP_CODE):mSelectedSeqData.opCode,
									KoDataUtil.isStringNotNull(getIntent().getStringExtra(KEY_SEQ_NUM))?getIntent().getStringExtra(KEY_SEQ_NUM):mSelectedSeqData.seqNum,
									mSelectedActiveData.activity, 
									goodQty, scrapQty, addTime, addTimeReason, CacheUtils.getShift());
					}
					else {
						mKoControl.timeReportComplete(getIntent().getStringExtra(KEY_SCHED_DATE), 
													getIntent().getStringExtra(KEY_ASSET_CODE),
													CacheUtils.getLoginUserInfo().staffNo, 
													"2", 
													"0", 
													"0", "0", 
													mSelectedActiveData.activity, 
													goodQty, scrapQty, addTime, addTimeReason,
													CacheUtils.getShift());
					}
				}
			}
			break;
			
		default:
			break;
		}
	}
	
	
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			@Override
			public void timeReportDescInfoAndActiveListBack(boolean isSuc, MeTimeReportGetDescInfoAdapter info, String msg) {
				if(isSuc) {
					mDescAndActiveListInfo = info;
					mActivieListData = info.getList();
					mReasonListData = info.reasonList;
					
					mDisplayView.setText(Html.fromHtml(info.display));
					mInputCompleteNumEdit.setText(String.valueOf(info.completeQty));
					mInputScrapNumEdit.setText(String.valueOf(info.scrapQty));
				}
				else {
					mActivieListData = null;
					mDisplayView.setText(null);
					mInputCompleteNumEdit.setText(null);
					mInputScrapNumEdit.setText(null);
					DialogUtils.showToast(MeStartTimeReportActivity.this, msg);
				}

				dismissLoadingDlg();
			}

			@Override
			public void getProduceListByProjectNumBack(boolean isSuc, List<MeProduceItem> produceList, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					showProcessOperateList(produceList);
				}
				else {
					DialogUtils.showToast(MeStartTimeReportActivity.this, msg);
				}
			}

			@Override
			public void timeReportGetSeqListBack(boolean isSuc, MeTimeReportSeqListAdapter info, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mSeqListData = info.getList();
					mInputCompleteNumEdit.setText(String.valueOf(info.trxQty));
					mInputScrapNumEdit.setText(String.valueOf(info.scrapQty));
				}
				else {
					mSeqListData = null;
					mInputCompleteNumEdit.setText(null);
					mInputScrapNumEdit.setText(null);
					DialogUtils.showToast(MeStartTimeReportActivity.this, msg);
				}
			}

			@Override
			public void timeReportCompleteBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MeStartTimeReportActivity.this, msg);
				if(isSuc) {
					Intent i = getIntent();
					boolean isWipNotNull = KoDataUtil.isStringNotNull(i.getStringExtra(KEY_WIP_ID));
					
					mKoControl.timeReportGetDescInfoWhenStart(i.getStringExtra(KEY_ASSET_CODE), 
															  i.getStringExtra(KEY_SCHED_DATE), 
															  isWipNotNull?"1":"2", 
															  i.getStringExtra(KEY_WIP_ID),
															  CacheUtils.getLoginUserInfo().staffNo,
															  CacheUtils.getShift(),
															  i.getStringExtra(KEY_OP_CODE));
					mSelectedActiveData = null;
					mInputActiveCodeEdit.setText(null);
					mInputAddTimeEdit.setText(null);
					mInputAddTimeReasonEdit.setText(null);
				}
			}
		};
	}
	
	private void showProcessOperateList(List<MeProduceItem> listData) {
		if(listData!=null && listData.size()>0) {
			KoProduceListDlg dlg = new KoProduceListDlg(this, listData.get(0), listData);
			dlg.show();
		}
	}
	
	private void showSeqList(List<MeTimeReportSeqListItem> listData) {
		if(listData!=null && listData.size()>0) {
			KoSeqListDlg dlg = new KoSeqListDlg(this, listData.get(0), listData);
			dlg.show();
		}
	}
	
	private void showActiveList(List<MeTimeReportActiveItem> listData) {
		if(listData!=null && listData.size()>0) {
			KoActiveListDlg dlg = new KoActiveListDlg(this, listData.get(0), listData);
			dlg.show();
		}
	}
	
	private void showReasonList(List<String> listData) {
		if(listData!=null && listData.size()>0) {
			KoReasonListDlg dlg = new KoReasonListDlg(this, null, listData);
			dlg.show();
		}
	}
		
	//2017
	class KoProduceListDlg extends KoListDialg<MeProduceItem> {

		public KoProduceListDlg(Activity context, MeProduceItem selectedData, List<MeProduceItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MeProduceItem selectedItem) {
			return selectedItem.jobDesc;
		}

		@Override
		public boolean isSelectedObjEquals(MeProduceItem selectedData, MeProduceItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MeProduceItem selData) {
			mSelectedProduceItemData = selData;
			mInputProduceNumEdit.setText(selData.jobDesc);
			getSeqList(mSelectedProduceItemData.wipEntityId);
		}
	}
	
	private void getSeqList(String wipId) {
		showLoadingDlg();
		mKoControl.timeReportGetSeqListByProduceId(wipId, getIntent().getStringExtra(KEY_ASSET_ID));
	}
	
	//2017
	class KoSeqListDlg extends KoListDialg<MeTimeReportSeqListItem> {

		public KoSeqListDlg(Activity context, MeTimeReportSeqListItem selectedData, List<MeTimeReportSeqListItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MeTimeReportSeqListItem selectedItem) {
			return selectedItem.opCode+"-"+selectedItem.opDesc;
		}

		@Override
		public boolean isSelectedObjEquals(MeTimeReportSeqListItem selectedData, MeTimeReportSeqListItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MeTimeReportSeqListItem selData) {
			mSelectedSeqData = selData;
			mInputSeqNumEdit.setText(selData.opCode+"-"+selData.opDesc);
		}
	}
	
	
	//2017
	class KoActiveListDlg extends KoListDialg<MeTimeReportActiveItem> {

		public KoActiveListDlg(Activity context, MeTimeReportActiveItem selectedData, List<MeTimeReportActiveItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MeTimeReportActiveItem selectedItem) {
			return selectedItem.activity+"-"+selectedItem.desc;
		}

		@Override
		public boolean isSelectedObjEquals(MeTimeReportActiveItem selectedData, MeTimeReportActiveItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MeTimeReportActiveItem selData) {
			mSelectedActiveData = selData;
			mInputActiveCodeEdit.setText(selData.activity+"-"+selData.desc);
		}
	}
	
	//2017
	class KoReasonListDlg extends KoListDialg<String> {

		public KoReasonListDlg(Activity context, String selectedData, List<String> listData) {
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
			mInputAddTimeReasonEdit.setText(selData);
		}
	}
	
	@Override
	public void onBackPressed() {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			@Override
			public void onOkBack() {
				MeStartTimeReportActivity.this.finish();
			}
			
			@Override
			public void onCancelBack() {
				
			}
		}, getString(R.string.ko_tips_sure_to_quit_time_report));
		
		dlg.setOkCancelBtn(true, true);
		dlg.show();
	}



}
