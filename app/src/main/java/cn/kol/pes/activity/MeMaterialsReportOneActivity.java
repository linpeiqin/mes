package cn.kol.pes.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.MeProduceItem;
import cn.kol.pes.model.item.MeTimeReportSeqListItem;
import cn.kol.pes.widget.KoListDialg;


public class MeMaterialsReportOneActivity extends BaseActivity implements OnClickListener {
	
	private static final String KEY_PRODUCE_NUM = "key_produce_num";
	private static final String KEY_WIP_ID = "key_wip_id";
	private static final String KEY_OP_CODE = "key_op_code";
	private static final String KEY_OP_DESC = "key_op_desc";
	private static final String KEY_SEQ_NUM = "key_seq_num";
	private static final String KEY_ASSET_CODE = "key_asset_code";
	private static final String KEY_ASSET_ID = "key_asset_id";
	private static final String KEY_ASSET_NAME = "key_asset_name";
	
	public static void startAct(Context context, String produceNum, String wipId, String opCode, String seqNum, String assetCode, String assetId, String assetName, String opDesc) {
		Intent i = new Intent(context, MeMaterialsReportOneActivity.class);
		i.putExtra(KEY_PRODUCE_NUM, produceNum);
		i.putExtra(KEY_WIP_ID, wipId);
		i.putExtra(KEY_OP_CODE, opCode);
		i.putExtra(KEY_SEQ_NUM, seqNum);
		i.putExtra(KEY_ASSET_CODE, assetCode);
		i.putExtra(KEY_ASSET_ID, assetId);
		i.putExtra(KEY_ASSET_NAME, assetName);
		i.putExtra(KEY_OP_DESC, opDesc);
		context.startActivity(i);
	} 
	
	private enum ScanType {
		Produce,
		Asset,
		Seq
	}
	
	private ScanType mScanType = ScanType.Produce;

	private EditText mProduceNumEdit;
	private EditText mAssetEdit;
	private EditText mSeqEdit;
	
	private ImageView mSearchProduceBtn;
	private Button mScanProduceBtn;
	private Button mScanAssetBtn;
	
	private TextView mDescInfo;
	
	private MeProduceItem mSelectedProduceItemData;
	private List<MeTimeReportSeqListItem> mSeqList;

	private MeTimeReportSeqListItem mSelectedSeqData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_materials_report_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle("物料报数");

		mProduceNumEdit = (EditText) findViewById(R.id.materials_report_produce_edit);
		mAssetEdit = (EditText) findViewById(R.id.materials_report_asset_edit);
		mSeqEdit = (EditText) findViewById(R.id.materials_report_seq_edit);
		
		mSearchProduceBtn = (ImageView) findViewById(R.id.materials_report_produce_search_btn);
		mSearchProduceBtn.setOnClickListener(this);
		
		mScanProduceBtn = (Button) findViewById(R.id.materials_report_produce_btn);
		mScanProduceBtn.setOnClickListener(this);
		
		mScanAssetBtn = (Button) findViewById(R.id.materials_report_asset_btn);
		mScanAssetBtn.setOnClickListener(this);
		
		mSeqEdit.setOnClickListener(this);
		
		mDescInfo = (TextView) findViewById(R.id.materials_report_desc);
		
		findViewById(R.id.materials_report_next_step_btn).setOnClickListener(this);

		String produceNum = getIntent().getStringExtra(KEY_PRODUCE_NUM);
		String assetNum = getIntent().getStringExtra(KEY_ASSET_CODE);
		String assetName = getIntent().getStringExtra(KEY_ASSET_NAME);
		
		mAssetEdit.setEnabled(false);
		mScanAssetBtn.setVisibility(View.GONE);
		
		mAssetEdit.setText(assetNum+" "+assetName);
		
		if(KoDataUtil.isStringNotNull(produceNum)) {
			mProduceNumEdit.setText(produceNum);
			mProduceNumEdit.setEnabled(false);
			mSearchProduceBtn.setVisibility(View.GONE);
			mScanProduceBtn.setVisibility(View.GONE);
			
			mSelectedProduceItemData = new MeProduceItem();
			mSelectedProduceItemData.wipEntityId = getIntent().getStringExtra(KEY_WIP_ID);
			
			mSeqEdit.setText(getIntent().getStringExtra(KEY_SEQ_NUM)+"-"+getIntent().getStringExtra(KEY_OP_DESC));
			mSeqEdit.setEnabled(false);
			
			showLoadingDlg();
			mKoControl.materialsReportGetDesc(getIntent().getStringExtra(KEY_WIP_ID), "1", getIntent().getStringExtra(KEY_SEQ_NUM));
		}
		else {
			mProduceNumEdit.setEnabled(true);
			mSearchProduceBtn.setVisibility(View.VISIBLE);
			mScanProduceBtn.setVisibility(View.VISIBLE);
		}
	}
	
	private void getSeqList(String wipId) {
		showLoadingDlg();
		mKoControl.materialsReportSeqList(wipId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.materials_report_produce_btn:
			mScanType = ScanType.Produce;
			KoCaptureActivity.startActForRes(this);
			break;
		
		case R.id.materials_report_asset_btn:
			mScanType = ScanType.Asset;
			break;
			
		case R.id.materials_report_seq_btn:
			mScanType = ScanType.Seq;
			KoCaptureActivity.startActForRes(this);
			break;
			
		case R.id.materials_report_seq_edit:
			if(mSelectedProduceItemData!=null && mSelectedProduceItemData.wipEntityId!=null) {
				showSeqList();
			}
			else {
				DialogUtils.showToast(this, "请先确定生产单");
			}
			break;
			
		case R.id.materials_report_produce_search_btn://搜索工单
			String projectNum = mProduceNumEdit.getText().toString();
			if(KoDataUtil.isStringNotNull(projectNum)) {
				showLoadingDlg();
				mKoControl.getProduceListByProjectNum(projectNum.toUpperCase());
			}
			else {
				DialogUtils.showToast(this, "请输入工程单号");
			}
			break;
			
		case R.id.materials_report_next_step_btn:
			Intent i = getIntent();
			
			if(mSelectedProduceItemData == null) {
				DialogUtils.showToast(this, "请先选择生产单");
			}
			else if(!KoDataUtil.isStringNotNull(i.getStringExtra(KEY_PRODUCE_NUM)) && mSelectedSeqData == null) {
				DialogUtils.showToast(this, "请选择工序");
			}
			else {
				if(KoDataUtil.isStringNotNull(i.getStringExtra(KEY_PRODUCE_NUM))) {
					
					MeMaterialsReportTwoActivity.startAct(this, 
														i.getStringExtra(KEY_PRODUCE_NUM),
														i.getStringExtra(KEY_WIP_ID), 
														i.getStringExtra(KEY_OP_CODE), 
														i.getStringExtra(KEY_SEQ_NUM), 
														i.getStringExtra(KEY_ASSET_CODE), 
														i.getStringExtra(KEY_ASSET_ID));
				}
				else {
					MeMaterialsReportTwoActivity.startAct(this, 
														mSelectedProduceItemData.jobDesc,
														mSelectedProduceItemData.wipEntityId, 
														mSelectedSeqData.opCode, 
														mSelectedSeqData.seqNum, 
														i.getStringExtra(KEY_ASSET_CODE), 
														i.getStringExtra(KEY_ASSET_ID));
				}
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode==KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {//扫码结果的回调
			
			String res = data.getStringExtra("res");
			LogUtils.e(tag, "onActivityResult()"+res);
			
//			if(mScanType == ScanType.Produce) {
//				showLoadingDlg();
//				mKoControl.getProduceListByProjectNum(res);
//			}
//			else if(mScanType == ScanType.Asset) {
//				
//			}
//			else if(mScanType == ScanType.Seq) {
//				showLoadingDlg();
//				mKoControl.getSeqInfoBySeqId(res);
//			}
//			else {
//				
//			}
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			@Override
			public void getProduceListByProjectNumBack(boolean isSuc, List<MeProduceItem> produceList, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					showProcessOperateList(produceList);
				}
				else {
					DialogUtils.showToast(MeMaterialsReportOneActivity.this, msg);
				}
			}
			
//			@Override
//			public void getAssetSeqInfoByAssetIdBack(boolean isSuc, MeGetAssetSeqInfoByAssetIdAdapter assetInfo, String msg) {
//				if(isSuc) {
//					mAssetInfo = assetInfo;
//				}
//				else {
//					DialogUtils.showToast(MeMaterialsReportOneActivity.this, msg);
//				}
//				
//				if(KoDataUtil.isStringNotNull(getIntent().getStringExtra(KEY_OP_CODE))) {
//					mKoControl.getSeqInfoBySeqId(getIntent().getStringExtra(KEY_OP_CODE));
//				}
//				else {
//					dismissLoadingDlg();
//				}
//			}

			@Override
			public void materialsReportSeqListBack(boolean isSuc, List<MeTimeReportSeqListItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mSeqList = list;
				}
				else {
					DialogUtils.showToast(MeMaterialsReportOneActivity.this, msg);
				}
				
				showLoadingDlg();
				mKoControl.materialsReportGetDesc(getIntent().getStringExtra(KEY_WIP_ID), "1", getIntent().getStringExtra(KEY_SEQ_NUM));
				
			}

			@Override
			public void materialsReportGetDescBack(boolean isSuc, String display, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mDescInfo.setText(Html.fromHtml(display));
				}
				else {
					DialogUtils.showToast(MeMaterialsReportOneActivity.this, msg);
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
	
	private void showSeqList() {
		if(mSeqList!=null && mSeqList.size()>0) {
			KoSeqListDlg dlg = new KoSeqListDlg(this, mSeqList.get(0), mSeqList);
			dlg.show();
		}
		else {
			DialogUtils.showToast(this, "暂无工序信息");
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
			mProduceNumEdit.setText(selData.jobDesc);
			
			getSeqList(selData.wipEntityId);
		}
	}
	
	//2017
	class KoSeqListDlg extends KoListDialg<MeTimeReportSeqListItem> {

		public KoSeqListDlg(Activity context, MeTimeReportSeqListItem selectedData, List<MeTimeReportSeqListItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MeTimeReportSeqListItem selectedItem) {
			return selectedItem.opCode;
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
			mSeqEdit.setText(selData.opCode+"-"+selData.seqNum);
		}
	}
	
}
