/*-----------------------------------------------------------
-- PURPOSE
--    开始工序 1st Page
-- History
--	  25-Feb-17  LiZheng  Created.

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.MePafteropItem;
import cn.kol.pes.model.item.MeProduceItem;
import cn.kol.pes.model.parser.adapter.MeGetAssetSeqInfoByAssetIdAdapter;
import cn.kol.pes.model.parser.adapter.MeGetDescInfoWhenSeqSelectedAdapter;
import cn.kol.pes.model.parser.adapter.MeGetPafteropWhenSeqSelectedAdapter;
import cn.kol.pes.model.parser.adapter.MeGetSeqInfoBySeqIdAdapter;
import cn.kol.pes.widget.KoListDialg;


public class MeProduceIdAssetSeqActivity extends BaseActivity implements OnClickListener {
	
	private static final String KEY_PRODUCE_NUM = "key_produce_num";
	private static final String KEY_WIP_ID = "key_wip_id";
	private static final String KEY_OP_CODE = "key_op_code";
	private static final String KEY_SEQ_NUM = "key_seq_num";
	private static final String KEY_ASSET_CODE = "key_asset_code";
	private static final String KEY_ASSET_ID = "key_asset_id";
	private static final String KEY_ASSET_NAME = "key_asset_name";
	private static final String KEY_OP_DESC = "key_op_desc";
	
	public static void startAct(Context context, String produceNum, String wipId, String opCode, String seqNum, String assetCode, String assetId, String assetName, String opDesc) {
		Intent i = new Intent(context, MeProduceIdAssetSeqActivity.class);
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
	private TextView mSeqEdit;
	private TextView mPafteropEdit;
	
	private ImageView mSearchProduceBtn;
	private Button mScanProduceBtn;
	private Button mScanAssetBtn;
	private Button mScanSeqBtn;
	
	private TextView mDescInfoView;
	
	private MeProduceItem mSelectedProduceItemData;
	private MeGetAssetSeqInfoByAssetIdAdapter mAssetInfo;
	private MePafteropItem mSelectedSeqInfo;
	private MeGetDescInfoWhenSeqSelectedAdapter mDescInfo;
	
	private MePafteropItem mSelectedPafteropItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_produce_id_asset_seq_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle("开始工序");

		mProduceNumEdit = (EditText) findViewById(R.id.produce_asset_seq_produce_edit);
		mAssetEdit = (EditText) findViewById(R.id.produce_asset_seq_asset_edit);
		mSeqEdit = (TextView) findViewById(R.id.produce_asset_seq_seq_edit);
		mSeqEdit.setOnClickListener(this);
		mPafteropEdit = (TextView) findViewById(R.id.produce_asset_seq_pafterop_edit);
		
		mSearchProduceBtn = (ImageView) findViewById(R.id.produce_asset_seq_produce_search_btn);
		mSearchProduceBtn.setOnClickListener(this);
		
		mScanProduceBtn = (Button) findViewById(R.id.produce_asset_seq_produce_btn);
		mScanProduceBtn.setOnClickListener(this);
		
		mScanAssetBtn = (Button) findViewById(R.id.produce_asset_seq_asset_btn);
		mScanAssetBtn.setOnClickListener(this);
		
		mScanSeqBtn = (Button) findViewById(R.id.produce_asset_seq_seq_btn);
		mScanSeqBtn.setOnClickListener(this);
		
		mDescInfoView = (TextView) findViewById(R.id.produce_id_asset_seq_desc_view);
		
		mPafteropEdit.setOnClickListener(this);
		
		findViewById(R.id.produce_asset_seq_search_btn).setOnClickListener(this);
		findViewById(R.id.produce_asset_seq_search_seq_btn).setOnClickListener(this);
		findViewById(R.id.produce_asset_seq_next_step_btn).setOnClickListener(this);

		String produceNum = getIntent().getStringExtra(KEY_PRODUCE_NUM);
		String assetName = getIntent().getStringExtra(KEY_ASSET_NAME);
		
		mAssetEdit.setEnabled(false);
		mScanAssetBtn.setVisibility(View.GONE);
		
		mAssetEdit.setText(assetName);
		showLoadingDlg();
		mKoControl.getAssetSeqInfoByAssetId(getIntent().getStringExtra(KEY_ASSET_CODE));
		
		if(KoDataUtil.isStringNotNull(produceNum)) {
			mProduceNumEdit.setText(produceNum);
			mProduceNumEdit.setEnabled(false);
			mSearchProduceBtn.setVisibility(View.GONE);
			mScanProduceBtn.setVisibility(View.GONE);
			
			mSelectedProduceItemData = new MeProduceItem();
			mSelectedProduceItemData.wipEntityId = getIntent().getStringExtra(KEY_WIP_ID);
			
			mSeqEdit.setEnabled(false);
			mSeqEdit.setText(getIntent().getStringExtra(KEY_SEQ_NUM)+"-"+getIntent().getStringExtra(KEY_OP_DESC));
			
			Intent i = getIntent();
			
			mKoControl.getDescInfoWhenSeqSelected(mSelectedProduceItemData.wipEntityId, i.getStringExtra(KEY_OP_CODE), i.getStringExtra(KEY_ASSET_CODE), KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()), null, CacheUtils.getLoginUserInfo().staffNo, i.getStringExtra(KEY_SEQ_NUM));
		}
		else {
			mProduceNumEdit.setEnabled(true);
			mSearchProduceBtn.setVisibility(View.VISIBLE);
			mScanProduceBtn.setVisibility(View.VISIBLE);
			
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.produce_asset_seq_produce_btn:
			mScanType = ScanType.Produce;
			KoCaptureActivity.startActForRes(this);
			break;
		
		case R.id.produce_asset_seq_asset_btn:
			mScanType = ScanType.Asset;
			break;
			
		case R.id.produce_asset_seq_seq_btn:
			mScanType = ScanType.Seq;
			KoCaptureActivity.startActForRes(this);
			break;
			
		case R.id.produce_asset_seq_search_btn:
			if(mSelectedProduceItemData!=null && mSelectedProduceItemData.wipEntityId!=null) {
				MeSeqListActivity.startAct(this, mSelectedProduceItemData.wipEntityId);
			}
			else {
				DialogUtils.showToast(this, "请先确定生产单");
			}
			break;
			
		case R.id.produce_asset_seq_produce_search_btn://搜索工单
			String projectNum = mProduceNumEdit.getText().toString();
			if(KoDataUtil.isStringNotNull(projectNum)) {
				showLoadingDlg();
				mKoControl.getProduceListByProjectNum(projectNum.toUpperCase());
			}
			else {
				DialogUtils.showToast(this, "请输入工程单号");
			}
			break;
			
		case R.id.produce_asset_seq_seq_edit://搜索工序
			showLoadingDlg();
			mKoControl.getSeqInfoBySeqId(getIntent().getStringExtra(KEY_ASSET_CODE), 
					                     KoDataUtil.isStringNotNull(getIntent().getStringExtra(KEY_WIP_ID))?getIntent().getStringExtra(KEY_WIP_ID):mSelectedProduceItemData.wipEntityId);
			break;
			
		case R.id.produce_asset_seq_next_step_btn:
			if(mSelectedProduceItemData == null) {
				DialogUtils.showToast(this, "请先选择生产单");
			}
			else if(mAssetInfo == null) {
				DialogUtils.showToast(this, "未能获取到设备信息");
			}
			else if(mSelectedSeqInfo==null && !KoDataUtil.isStringNotNull(getIntent().getStringExtra(KEY_OP_CODE))) {
				DialogUtils.showToast(this, "请输入工序号并请求工序信息");
			}
//			else if(!mAssetInfo.attribute4.contains(mSeqInfo.operationCode)) {
//				DialogUtils.showToast(this, mAssetInfo.description+"不支持操作工序"+mSeqInfo.operationDescription);
//			}
			else if(mDescInfo==null) {
				DialogUtils.showToast(this, "未能正确获取工序信息");
			}
			else if(mDescInfo.errorCode>1) {
				DialogUtils.showToast(this, mDescInfo.errorMsg);
			}
			else {
				String seqNum = KoDataUtil.isStringNotNull(getIntent().getStringExtra(KEY_SEQ_NUM))?getIntent().getStringExtra(KEY_SEQ_NUM):mSelectedSeqInfo.seqNum;
				String pafteropSeqNum = mSelectedPafteropItem!=null ? mSelectedPafteropItem.seqNum:"0";
				
				MeStartProduceSeqActivity.startAct(this, mSelectedProduceItemData.wipEntityId, 
					mSelectedSeqInfo!=null ? mSelectedSeqInfo.opCode : getIntent().getStringExtra(KEY_OP_CODE), 
					mAssetInfo.resourceCode, 
					mAssetInfo.resourceId, 
					KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()), 
					mSelectedPafteropItem!=null?mSelectedPafteropItem.opCode:null, seqNum, pafteropSeqNum);
				
				this.finish();
			}
			break;
			
		case R.id.produce_asset_seq_pafterop_edit:
			if(mSelectedProduceItemData == null) {
				DialogUtils.showToast(this, "请先选择生产单");
			}
			else {
				showLoadingDlg();
				mKoControl.getPAfterOpWhenSeqSelected(mSelectedProduceItemData.wipEntityId);
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
			
			if(mScanType == ScanType.Produce) {
				showLoadingDlg();
				mKoControl.getProduceListByProjectNum(res);
			}
			else if(mScanType == ScanType.Asset) {
				
			}
			else if(mScanType == ScanType.Seq) {
				showLoadingDlg();
				mKoControl.getSeqInfoBySeqId(res, KoDataUtil.isStringNotNull(getIntent().getStringExtra(KEY_WIP_ID))?getIntent().getStringExtra(KEY_WIP_ID):mSelectedProduceItemData.wipEntityId);
			}
			else {
				
			}
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
					DialogUtils.showToast(MeProduceIdAssetSeqActivity.this, msg);
				}
			}
			
			@Override
			public void getAssetSeqInfoByAssetIdBack(boolean isSuc, MeGetAssetSeqInfoByAssetIdAdapter assetInfo, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mAssetInfo = assetInfo;
				}
				else {
					DialogUtils.showToast(MeProduceIdAssetSeqActivity.this, msg);
				}
			}

			@Override
			public void getSeqInfoBySeqIdBack(boolean isSuc, MeGetSeqInfoBySeqIdAdapter seqInfo, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					KoSeqListDlg dlg = new KoSeqListDlg(MeProduceIdAssetSeqActivity.this, null, seqInfo.getList());
					dlg.show();
				}
				else {
					DialogUtils.showToast(MeProduceIdAssetSeqActivity.this, msg);
				}
			}

			@Override
			public void getDescInfoWhenSeqSelectedBack(boolean isSuc, MeGetDescInfoWhenSeqSelectedAdapter descInfo, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mDescInfo = descInfo;
					mDescInfoView.setText(Html.fromHtml(descInfo.display));
					if(descInfo.errorCode > 1) {
						DialogUtils.showToast(MeProduceIdAssetSeqActivity.this, descInfo.errorMsg);
					}
				}
				else {
					mDescInfo = null;
					DialogUtils.showToast(MeProduceIdAssetSeqActivity.this, msg);
				}
			}

			@Override
			public void getPafteropWhenStartingSeqClicked(boolean isSuc, MeGetPafteropWhenSeqSelectedAdapter descInfo, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					KoPafteropListDlg dlg = new KoPafteropListDlg(MeProduceIdAssetSeqActivity.this, descInfo.get(0), descInfo.getList());
					dlg.show();
				}
				else {
					DialogUtils.showToast(MeProduceIdAssetSeqActivity.this, msg);
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
		}
	}
	
	//2017
	class KoSeqListDlg extends KoListDialg<MePafteropItem> {

		public KoSeqListDlg(Activity context, MePafteropItem selectedData, List<MePafteropItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MePafteropItem selectedItem) {
			return selectedItem.seqNum+"-"+selectedItem.opCode+" "+selectedItem.desc;
		}

		@Override
		public boolean isSelectedObjEquals(MePafteropItem selectedData, MePafteropItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MePafteropItem selData) {
			mSelectedSeqInfo = selData;
			mSeqEdit.setText(selData.seqNum+"-"+selData.opCode+"-"+selData.desc);
			
			if(mSelectedProduceItemData!=null && getIntent().getStringExtra(KEY_ASSET_CODE)!=null && mSelectedSeqInfo!=null) {
				mKoControl.getDescInfoWhenSeqSelected(mSelectedProduceItemData.wipEntityId, 
													selData.opCode, 
													getIntent().getStringExtra(KEY_ASSET_CODE), 
													KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()), 
													null, 
													CacheUtils.getLoginUserInfo().staffNo, selData.seqNum);
			}
		}
	}
	
	//2017
	class KoPafteropListDlg extends KoListDialg<MePafteropItem> {

		public KoPafteropListDlg(Activity context, MePafteropItem selectedData, List<MePafteropItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MePafteropItem selectedItem) {
			return selectedItem.seqNum+"-"+selectedItem.opCode+"-"+selectedItem.desc;
		}

		@Override
		public boolean isSelectedObjEquals(MePafteropItem selectedData, MePafteropItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MePafteropItem selData) {
			mSelectedPafteropItem = selData;
			mPafteropEdit.setText(selData.seqNum+"-"+selData.opCode+"-"+selData.desc);
			
//			showLoadingDlg();
//			Intent i = getIntent();
//			String produceNum =i.getStringExtra(KEY_PRODUCE_NUM);
//			
//			if(KoDataUtil.isStringNotNull(produceNum)) {
//				
//				mKoControl.getDescInfoWhenSeqSelected(i.getStringExtra(KEY_WIP_ID), 
//							i.getStringExtra(KEY_OP_CODE), 
//							i.getStringExtra(KEY_ASSET_CODE), 
//							KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()), 
//							selData.opCode, 
//							CacheUtils.getLoginUserInfo().staffNo, i.getStringExtra(KEY_SEQ_NUM));
//			}
//			else {
//				mKoControl.getDescInfoWhenSeqSelected(mSelectedProduceItemData.wipEntityId, 
//							mSelectedSeqInfo.opCode, 
//							mAssetInfo.resourceCode, 
//							KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()), 
//							selData.opCode, 
//							CacheUtils.getLoginUserInfo().staffNo, i.getStringExtra(KEY_SEQ_NUM));
//			}
		}
	}
	

}
