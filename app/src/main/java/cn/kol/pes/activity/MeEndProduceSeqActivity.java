/*-----------------------------------------------------------
-- PURPOSE
	完成工序
-- History
--	  25-Feb-17  LiZheng  Created.
------------------------------------------------------------*/
package cn.kol.pes.activity;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.MeQaNeedFillItem;
import cn.kol.pes.model.parser.adapter.MeGetDescInfoWhenStartingSeqClickedAdapter;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.KoQaItemView;
import cn.kol.pes.widget.KoQaItemView.QaItemDerivedOneBack;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class MeEndProduceSeqActivity extends BaseActivity implements OnClickListener, IKoPickTimeDlgBack {

	private TextView mDisplayView;
	
	private ViewGroup mQaParentView;//质量管理计划的父容器

	private EditText mInputQuanEdit;
	private EditText mScrapQuanEdit;//输入坏品数量的文本框
	
	private TextView mEndTimeView;
	private Calendar mOpEndCal = Calendar.getInstance();//完成工序的时间

	
	private KoQaItemView mClickedScanQaItemView;//质量收集计划的某项能通过扫码输入时，要有一个引用，在回调时通过这个引用把扫描到的数据填写到这个item中
	
	
	private Button mEndBtn;//完成工序的按钮
	
	private static final String KEY_TRX_ID = "key_trx_id";
	private static final String KEY_WIP_ID = "key_wip_id";
	private static final String KEY_OP_CODE = "key_op_code";
	private static final String KEY_SEQ_NUM = "key_seq_num";
	private static final String KEY_ASSET_ID = "key_asset_id";
	private static final String KEY_SCHED_DATE = "key_sched_date";
	private static final String KEY_TRX_QTY = "key_trx_qty";
	
	public static void startAct(Context context, String trxId, String wipId, String opCode, String assetCode, String schedDate, String trxQty, String seqNum) {
		
		Intent i = new Intent(context, MeEndProduceSeqActivity.class);
		i.putExtra(KEY_TRX_ID, trxId);
		i.putExtra(KEY_WIP_ID, wipId);
		i.putExtra(KEY_OP_CODE, opCode);
		i.putExtra(KEY_SEQ_NUM, seqNum);
		i.putExtra(KEY_ASSET_ID, assetCode);
		i.putExtra(KEY_SCHED_DATE, schedDate);
		i.putExtra(KEY_TRX_QTY, trxQty);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_end_produce_seq_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle("完成工序");

		mDisplayView = (TextView) findViewById(R.id.end_produce_seq_info_display);
		mQaParentView = (ViewGroup) findViewById(R.id.end_produce_seq_qa_parent_layout);
		
		TextView staffName = (TextView) findViewById(R.id.end_produce_seq_staff_name);
		staffName.setText(CacheUtils.getLoginUserInfo().staffName);
		
		mInputQuanEdit = (EditText) findViewById(R.id.end_produce_seq_input_quantity);
		mScrapQuanEdit = (EditText) findViewById(R.id.end_produce_seq_scrap_quantity);
		mEndTimeView = (TextView) findViewById(R.id.end_produce_seq_end_date_time);
		
		//mInputQuanEdit.setText(getIntent().getStringExtra(KEY_TRX_QTY));
		mEndTimeView.setText(StringUtils.formatDateTime(mOpEndCal));
		
		findViewById(R.id.end_produce_seq_reset_date_time_btn).setOnClickListener(this);

		mEndBtn = (Button) findViewById(R.id.end_produce_seq_end_btn);

		mEndBtn.setOnClickListener(this);

		showLoadingDlg();
		Intent i = getIntent();
		mKoControl.getDescInfoWhenStartingSeqClicked(i.getStringExtra(KEY_WIP_ID), i.getStringExtra(KEY_OP_CODE), 
													 i.getStringExtra(KEY_ASSET_ID), i.getStringExtra(KEY_SCHED_DATE),
													 i.getStringExtra(KEY_SEQ_NUM));
	}
	
	@Override
	public void pickTime(Calendar cal) {//重设时间的回调
		
		Calendar nowCal = Calendar.getInstance();
		
		if(nowCal.after(cal)) {
			mOpEndCal.setTimeInMillis(cal.getTimeInMillis());
			mEndTimeView.setText(StringUtils.formatDateTime(mOpEndCal));
		}else {
			DialogUtils.showToast(this, R.string.ko_tips_end_time_cant_after_now);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.end_produce_seq_reset_date_time_btn:
			KoPickTimeDlg dlg = new KoPickTimeDlg(this, this, mOpEndCal, true);
			dlg.show();
			break;

		case R.id.end_produce_seq_end_btn://完成工序
			
			String inputQuan = mInputQuanEdit.getText().toString();
			
			String scrQuan = mScrapQuanEdit.getText().toString();

			if(inputQuan.length()==0) {
				DialogUtils.showToast(this, R.string.ko_tips_input_input_quan);
			}
			else if(!KoDataUtil.isValidNumber(inputQuan)) {
				DialogUtils.showToast(this, R.string.ko_tips_wrong_input_quan);
			}
			else if(scrQuan.length()==0 || !KoDataUtil.isValidNumber(scrQuan)) {
				DialogUtils.showToast(this, R.string.ko_tips_wrong_scrap_quan);
			}
			else if(!isQalistViewFillOk()) {
				
			}
			else {
				endSeq();
			}
			break;
			
		default:
			break;
		}
	}

	private void endSeq() {
		showLoadingDlg();
		Intent i = getIntent();
		mKoControl.endSeq(i.getStringExtra(KEY_TRX_ID), 
						  i.getStringExtra(KEY_WIP_ID), 
						  i.getStringExtra(KEY_OP_CODE),
						  getPlanId(), CacheUtils.getLoginUserInfo().staffNo, 
						  mInputQuanEdit.getText().toString().trim(),
						  mScrapQuanEdit.getText().toString().trim(),
						  mEndTimeView.getText().toString(), 
						  getQaListStringFromViews(), CacheUtils.getShift(),
						  i.getStringExtra(KEY_SEQ_NUM), i.getStringExtra(KEY_SCHED_DATE));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {
			
			String dataS = data.getStringExtra(KoCaptureActivity.KEY_RES_INTENT);
			
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			
			if(dataS!=null && dataS.length()>1) {
				showLoadingDlg();
				mClickedScanQaItemView.setQaValue(dataS);
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {

			@Override
			public void getDescInfoWhenStartingSeqClicked(boolean isSuc, MeGetDescInfoWhenStartingSeqClickedAdapter descInfo, String msg) {
				if(isSuc) {
					mDisplayView.setText(Html.fromHtml(descInfo.display));
					mKoControl.getQaList(getIntent().getStringExtra(KEY_OP_CODE), "85");

					mOpEndCal.setTimeInMillis(KoDataUtil.convertStringToCal(descInfo.databaseTime).getTimeInMillis());
					mEndTimeView.setText(StringUtils.formatDateTime(mOpEndCal));
				}
				else {
					DialogUtils.showToast(MeEndProduceSeqActivity.this, msg);
				}
			}
			
			@Override
			public void getQaListBack(boolean isSuc, List<MeQaNeedFillItem> qaList, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					showQaItemViews(qaList);
				}
				else {
					DialogUtils.showToast(MeEndProduceSeqActivity.this, msg);
				}
			}

			@Override
			public void endSeqBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MeEndProduceSeqActivity.this, msg);
				if(isSuc) {
					MeEndProduceSeqActivity.this.finish();
				}
			}
		};
	}
	
	@Override
	public void onBackPressed() {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			@Override
			public void onOkBack() {
				MeEndProduceSeqActivity.this.finish();
			}
			
			@Override
			public void onCancelBack() {
			}
		}, getString(R.string.ko_tips_sure_to_quit_end_seq));
		
		dlg.setOkCancelBtn(true, true);
		dlg.show();
	}
	
	//显示质量收集计划view
	private void showQaItemViews(List<MeQaNeedFillItem> qaList) {
		mQaParentView.removeAllViews();

		if(qaList != null) {
			final String wipId = getIntent().getStringExtra(KEY_WIP_ID);
			for(MeQaNeedFillItem qa : qaList) {
				if(qa != null) {
					KoQaItemView qaItemView = new KoQaItemView(this, qa, mQaItemListener, mQaItemDerivedOneBack, wipId);
					mQaParentView.addView(qaItemView);
				}
			}
		}

		if(isHaveDerivedOneQaItem()) {
			mScrapQuanEdit.setEnabled(false);
			mScrapQuanEdit.setText(String.valueOf(getDerivedOneQaItemTotalSum()));
		}
		
		updatePassQtyAndScrapQty();
	}
	
	//某个质量收集计划项是否设置了derivied_flag
	private boolean isHaveDerivedOneQaItem() {
		
		for(int i=0; i<mQaParentView.getChildCount(); i++) {
			KoQaItemView qaView = (KoQaItemView) mQaParentView.getChildAt(i);
			if(qaView!=null && qaView.isDerivedOne()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isHaveQaItem() {
		return mQaParentView.getChildCount()>0;
	}
	
	//将设置了flag的质量收集计划项的值全部加起来
	private int getDerivedOneQaItemTotalSum() {
		int total = 0;
		for(int i=0; i<mQaParentView.getChildCount(); i++) {
			KoQaItemView qaView = (KoQaItemView) mQaParentView.getChildAt(i);
			if(qaView!=null && qaView.isDerivedOne() && qaView.isValueValid()) {
				total = total+Integer.valueOf(qaView.getQaValue());
			}
		}
		return total;
	}
	
	//设置了derived_flag的质量计划项，里面的数据改变时，同样要回调出来，更新坏品数量
	private QaItemDerivedOneBack mQaItemDerivedOneBack = new QaItemDerivedOneBack() {
		@Override
		public void derivedOneValueback(int value) {
			updatePassQtyAndScrapQty();
		}
	};
	
	//更新坏品数量
	private void updatePassQtyAndScrapQty() {
		int scrapTotal = getDerivedOneQaItemTotalSum();
		mScrapQuanEdit.setText(String.valueOf(scrapTotal));
		//updatePassQtyInQaView(scrapTotal);
	}
	
	//质量收集计划项点了扫描按钮的回调
	private View.OnClickListener mQaItemListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			KoQaItemView qaView = (KoQaItemView) v.getTag();
			if(qaView != null) {
				mClickedScanQaItemView = qaView;
				KoCaptureActivity.startActForRes(MeEndProduceSeqActivity.this);
			}
		}
	};
	
	//更新质量管理计划中的合格数
	private void updatePassQtyInQaView(int scrapTotal) {
		
		for(int i=0; i<mQaParentView.getChildCount(); i++) {
			KoQaItemView view = (KoQaItemView) mQaParentView.getChildAt(i);
			view.updateOpPassQty(scrapTotal);
		}
	}
	
	private String getPlanId() {
		ViewGroup parentV = mQaParentView;
		if(parentV.getChildCount() > 0) {
			KoQaItemView qaV = (KoQaItemView) parentV.getChildAt(0);
			return qaV.getPlanId();
		}
		return "";
	}
	
	//将动态展示的质量收集栏位整理成一个字符串
	private String getQaListStringFromViews() {
		ViewGroup parentV = mQaParentView;
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<parentV.getChildCount(); i++) {
			
			KoQaItemView qaV = (KoQaItemView) parentV.getChildAt(i);
			
			if(qaV.isValueValid() && !qaV.isNullCharNotMandatory()) {//简单判断数据格式，如果错误，直接弹出提示并且返回null
				sb.append(qaV.getCharId()).append(",").append(qaV.getResultColumnName()).append(",").append(qaV.getQaValue());
				if(i<(parentV.getChildCount()-1)) {
					sb.append("@");
				}
			}
			else if(qaV.isNullCharNotMandatory()) {
				
			}
			else {
				return null;
			}
		}
		LogUtils.e(tag, "getQaListStringFromViews:"+sb);
		return sb.toString();
	}
	
	//判断质量收集计划数据项是否都填写正确
	private boolean isQalistViewFillOk() {
		if(mQaParentView.getChildCount()>0) {
			for(int i=0; i<mQaParentView.getChildCount(); i++) {
				KoQaItemView qaV = (KoQaItemView) mQaParentView.getChildAt(i);
				if(!qaV.isValueValid()) {
					return false;
				}
			}
		}
		return true;
	}

}
