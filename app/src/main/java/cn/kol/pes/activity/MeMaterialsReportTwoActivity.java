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
import android.widget.EditText;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.MeMaterialsReportNumItem;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.KoListDialg;


public class MeMaterialsReportTwoActivity extends BaseActivity implements OnClickListener {

	private TextView mProduceNumView;
	private TextView mSeqNumView;

	private TextView mDisplayInfoView;

	private TextView mInputStandardMaterialEdit;
	private EditText mInputNewMaterialEdit;

	private TextView mDisplayNewMaterialsInfoView;

	
	private EditText mInputRealQtyEdit;
	private EditText mInputRemarkEdit;
	
	private MeMaterialsReportNumItem mSelectedMaterialNumData;

	private static final String KEY_PRODUCE_NUM = "key_produce_num";
	private static final String KEY_WIP_ID = "key_wip_id";
	private static final String KEY_OP_CODE = "key_op_code";
	private static final String KEY_ASSET_CODE = "key_asset_code";
	private static final String KEY_ASSET_ID = "key_asset_id";
	private static final String KEY_SEQ_NUM = "key_seq_num";
	
	public static void startAct(Context context, String produceNum, String wipId, String opCode, String seqNum, String assetCode, String assetId) {
		
		Intent i = new Intent(context, MeMaterialsReportTwoActivity.class);
		i.putExtra(KEY_PRODUCE_NUM, produceNum);
		i.putExtra(KEY_WIP_ID, wipId);
		i.putExtra(KEY_SEQ_NUM, seqNum);
		i.putExtra(KEY_OP_CODE, opCode);
		i.putExtra(KEY_ASSET_CODE, assetCode);
		i.putExtra(KEY_ASSET_ID, assetId);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_materials_report_two_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle("物料报数");

		mProduceNumView = (TextView) findViewById(R.id.materials_report_2_produce_num);
		mSeqNumView = (TextView) findViewById(R.id.materials_report_2_seq_code);
		mDisplayInfoView = (TextView) findViewById(R.id.materials_report_2_info_display);
		
		mInputStandardMaterialEdit = (TextView) findViewById(R.id.materials_report_2_standard_material_num_edit);
		mInputStandardMaterialEdit.setOnClickListener(this);
		
		mInputNewMaterialEdit = (EditText) findViewById(R.id.materials_report_2_new_material_num_edit);
		findViewById(R.id.materials_report_2_search_new_material_num).setOnClickListener(this);
		
		mDisplayNewMaterialsInfoView = (TextView) findViewById(R.id.materials_report_2_new_material_info);
		
		mInputRealQtyEdit = (EditText) findViewById(R.id.materials_report_2_real_qty);
		mInputRemarkEdit = (EditText) findViewById(R.id.materials_report_2_remark_edit);
		
		findViewById(R.id.materials_report_2_use_btn).setOnClickListener(this);
		findViewById(R.id.materials_report_2_return_btn).setOnClickListener(this);
		findViewById(R.id.materials_report_2_search_new_material_scan_btn).setOnClickListener(this);
		
		Intent i = getIntent();
		
		mProduceNumView.setText(i.getStringExtra(KEY_PRODUCE_NUM));
		mSeqNumView.setText(i.getStringExtra(KEY_OP_CODE)+" - "+i.getStringExtra(KEY_SEQ_NUM));
		
		showLoadingDlg();
		mKoControl.materialsReportGetDesc(i.getStringExtra(KEY_WIP_ID), "2", i.getStringExtra(KEY_SEQ_NUM));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.materials_report_2_standard_material_num_edit:
			showLoadingDlg();
			mKoControl.materialsReportGetStandardNum(getIntent().getStringExtra(KEY_WIP_ID),getIntent().getStringExtra(KEY_SEQ_NUM));
			break;
			
		case R.id.materials_report_2_search_new_material_num:
			String newMaterialNum = mInputNewMaterialEdit.getText().toString().trim();
			if(newMaterialNum!=null && newMaterialNum.length()>=10) {
				showLoadingDlg();
				mKoControl.materialsReportGetNewNum(getIntent().getStringExtra(KEY_WIP_ID), newMaterialNum.toUpperCase());
			}
			else {
				DialogUtils.showToast(this, "请至少填写10个字符作为关键字");
			}
			break;
			
		case R.id.materials_report_2_search_new_material_scan_btn:
			KoCaptureActivity.startActForRes(this);
			break;

		case R.id.materials_report_2_use_btn:
			String realQty = mInputRealQtyEdit.getText().toString();
			String remark = mInputRemarkEdit.getText().toString();
			
			if(mSelectedMaterialNumData == null) {
				DialogUtils.showToast(this, "请输入标准物料编号或新增物料编号");
			}
			else if(realQty.length()==0) {
				DialogUtils.showToast(this, "请输入实际用量");
			}
			else if(!KoDataUtil.isValidFloatNumber(realQty)) {
				DialogUtils.showToast(this, "实际用量必须为数字");
			}
			else {
				showLoadingDlg();
				Intent i= getIntent();
				mKoControl.materialsReportComplete(i.getStringExtra(KEY_ASSET_CODE), 
													CacheUtils.getLoginUserInfo().staffNo, 
													"1", 
													i.getStringExtra(KEY_WIP_ID),
													i.getStringExtra(KEY_OP_CODE), 
													i.getStringExtra(KEY_SEQ_NUM), 
													mSelectedMaterialNumData.itemId, 
													realQty, remark, KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()));
			}
			break;
			
		case R.id.materials_report_2_return_btn:
			String realQty2 = mInputRealQtyEdit.getText().toString();
			String remark2 = mInputRemarkEdit.getText().toString();
			
			if(mSelectedMaterialNumData == null) {
				DialogUtils.showToast(this, "请输入标准物料编号或新增物料编号");
			}
			else if(realQty2.length()==0) {
				DialogUtils.showToast(this, "请输入实际用量");
			}
			else if(!KoDataUtil.isValidFloatNumber(realQty2)) {
				DialogUtils.showToast(this, "实际用量必须为数字");
			}
			else {
				showLoadingDlg();
				Intent i= getIntent();
				mKoControl.materialsReportComplete(i.getStringExtra(KEY_ASSET_CODE), 
													CacheUtils.getLoginUserInfo().staffNo, 
													"2", 
													i.getStringExtra(KEY_WIP_ID),
													i.getStringExtra(KEY_OP_CODE), 
													i.getStringExtra(KEY_SEQ_NUM), 
													mSelectedMaterialNumData.itemId, 
													realQty2, remark2, KoDataUtil.getFormatDataForProcess(CacheUtils.getSelectedDate()));
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
			
			if(res!=null && res.length()>=10) {
				showLoadingDlg();
				mKoControl.materialsReportGetNewNum(getIntent().getStringExtra(KEY_WIP_ID), res.toUpperCase());
			}
			else {
				DialogUtils.showToast(this, "请至少填写10个字符作为新增物料号");
			}
		}
	}
	
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			
			@Override
			public void materialsReportGetDescBack(boolean isSuc, String display, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mDisplayInfoView.setText(Html.fromHtml(display));
				}
				else {
					DialogUtils.showToast(MeMaterialsReportTwoActivity.this, msg);
				}
			}

			@Override
			public void materialsReportGetStandardNumBack(boolean isSuc, List<MeMaterialsReportNumItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					if(list!=null && list.size()>0) {
						KoNumListDlg dlg = new KoNumListDlg(MeMaterialsReportTwoActivity.this, list.get(0), list, true);
						dlg.show();
					}
				}
				else {
					DialogUtils.showToast(MeMaterialsReportTwoActivity.this, msg);
				}
			}

			@Override
			public void materialsReportGetNewNumBack(boolean isSuc, List<MeMaterialsReportNumItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					if(list!=null && list.size()>0) {
						KoNumListDlg dlg = new KoNumListDlg(MeMaterialsReportTwoActivity.this, list.get(0), list, false);
						dlg.show();
					}
				}
				else {
					DialogUtils.showToast(MeMaterialsReportTwoActivity.this, msg);
				}
			}

			@Override
			public void materialsReportGetMaterialDescBack(boolean isSuc, String display, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mDisplayNewMaterialsInfoView.setText(Html.fromHtml(display));
				}
				else {
					DialogUtils.showToast(MeMaterialsReportTwoActivity.this, msg);
				}
			}

			@Override
			public void materialsReportCompleteBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MeMaterialsReportTwoActivity.this, msg);
				if(isSuc) {
					mSelectedMaterialNumData = null;
					mInputStandardMaterialEdit.setText(null);
					mInputNewMaterialEdit.setText(null);
					mInputRealQtyEdit.setText(null);
					mInputRemarkEdit.setText(null);
					mDisplayNewMaterialsInfoView.setText(null);
					//MeMaterialsReportTwoActivity.this.finish();
					
					showLoadingDlg();
					Intent i = getIntent();
					mKoControl.materialsReportGetDesc(i.getStringExtra(KEY_WIP_ID), "2", i.getStringExtra(KEY_SEQ_NUM));
				}
			}
		};
	}
	
	class KoNumListDlg extends KoListDialg<MeMaterialsReportNumItem> {
		
		private boolean mIsStandard = true;

		public KoNumListDlg(Activity context, MeMaterialsReportNumItem selectedData, List<MeMaterialsReportNumItem> listData, boolean isStandard) {
			super(context, selectedData, listData);
			mIsStandard = isStandard;
		}

		@Override
		public String getStringToShowFromObj(MeMaterialsReportNumItem selectedItem) {
			return selectedItem.item+"-"+selectedItem.desc;
		}

		@Override
		public boolean isSelectedObjEquals(MeMaterialsReportNumItem selectedData, MeMaterialsReportNumItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MeMaterialsReportNumItem selData) {
			mSelectedMaterialNumData = selData;
			if(mIsStandard) {
				mInputStandardMaterialEdit.setText(selData.item+"-"+selData.desc);
				mInputNewMaterialEdit.setText(null);
				mDisplayNewMaterialsInfoView.setText(null);
			}
			else {
				mInputStandardMaterialEdit.setText(null);
				mInputNewMaterialEdit.setText(selData.item+"-"+selData.desc);
			}
			showLoadingDlg();
			mKoControl.materialsReportGetNewMaterialDesc(getIntent().getStringExtra(KEY_WIP_ID), mSelectedMaterialNumData.itemId);
		}
	}
	
	@Override
	public void onBackPressed() {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			@Override
			public void onOkBack() {
				MeMaterialsReportTwoActivity.this.finish();
			}
			
			@Override
			public void onCancelBack() {
				
			}
		}, getString(R.string.ko_tips_sure_to_quit_material_report));
		
		dlg.setOkCancelBtn(true, true);
		dlg.show();
	}

}
