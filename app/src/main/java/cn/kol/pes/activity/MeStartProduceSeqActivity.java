/*-----------------------------------------------------------

-- PURPOSE

--    生产报数开始的第二个界面

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.parser.adapter.MeGetDescInfoWhenStartingSeqClickedAdapter;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class MeStartProduceSeqActivity extends BaseActivity implements OnClickListener, IKoPickTimeDlgBack {

	private TextView mDisplayView;
	
	private TextView mEndDateTime;//展示完成工序的时间
	
	private TextView mStaffView;//展示登录员工信息的view
	
	private EditText mInputQuanEdit;//输入坏品数量的文本框
	
	private Button mResetTimeBtn;//重设完成时间的按钮
	private Button mStartOpBtn;//完成工序的按钮
	
	private MeGetDescInfoWhenStartingSeqClickedAdapter mDescInfo;

	private Calendar mOpEndCal = Calendar.getInstance();//完成工序的时间

	private static final String KEY_WIP_ID = "key_wip_id";
	private static final String KEY_OP_CODE = "key_op_code";
	private static final String KEY_SEQ_NUM = "key_seq_num";
	private static final String KEY_ASSET_CODE = "key_asset_code";
	private static final String KEY_ASSET_ID = "key_asset_id";
	private static final String KEY_SCHED_DATE = "key_sched_date";
	private static final String KEY_PAFTEROP = "key_pafterop";
	private static final String KEY_PAFTEROP_SEQ_NUM = "key_pafterop_seq_num";
	
	public static void startAct(Context context, String wipId, String opCode, String assetCode, String assetId, String schedDate, String pAfterOp, String seqNum, String pAfterOpSeqNum) {
		
		Intent i = new Intent(context, MeStartProduceSeqActivity.class);
		i.putExtra(KEY_WIP_ID, wipId);
		i.putExtra(KEY_OP_CODE, opCode);
		i.putExtra(KEY_SEQ_NUM, seqNum);
		i.putExtra(KEY_ASSET_CODE, assetCode);
		i.putExtra(KEY_ASSET_ID, assetId);
		i.putExtra(KEY_SCHED_DATE, schedDate);
		i.putExtra(KEY_PAFTEROP, pAfterOp);
		i.putExtra(KEY_PAFTEROP_SEQ_NUM, pAfterOpSeqNum);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_start_produce_seq_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle("开始工序");

		mStaffView = (TextView) findViewById(R.id.start_produce_seq_staff_name);
		mStaffView.setText(CacheUtils.getLoginUserInfo().staffName);
		
		mDisplayView = (TextView) findViewById(R.id.start_produce_seq_info_display);
		
		mInputQuanEdit = (EditText) findViewById(R.id.start_produce_seq_trx_quantity);
		
		mEndDateTime = (TextView) findViewById(R.id.start_produce_seq_end_date_time);
		
		mResetTimeBtn = (Button) findViewById(R.id.start_produce_seq_reset_date_time_btn);
		
		mStartOpBtn = (Button) findViewById(R.id.start_produce_seq_start_btn);

		mResetTimeBtn.setOnClickListener(this);
		mStartOpBtn.setOnClickListener(this);

		mEndDateTime.setText(StringUtils.formatDateTime(mOpEndCal));
		
		showLoadingDlg();
		Intent i = getIntent();
		mKoControl.getDescInfoWhenStartingSeqClicked(i.getStringExtra(KEY_WIP_ID), i.getStringExtra(KEY_OP_CODE), 
													 i.getStringExtra(KEY_ASSET_CODE), i.getStringExtra(KEY_SCHED_DATE), i.getStringExtra(KEY_SEQ_NUM));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_produce_seq_reset_date_time_btn://重设开始工序时间
			KoPickTimeDlg dlg = new KoPickTimeDlg(this, this, mOpEndCal, true);
			dlg.show();
			break;

		case R.id.start_produce_seq_start_btn://

			if(mDescInfo == null) {
				DialogUtils.showToast(this, "获取工单信息失败");
			}
			else if(mDescInfo.errorCode>1) {
				DialogUtils.showToast(this, mDescInfo.errorMsg);
			}
			else {
				startSeq();
			}
			break;
			
		default:
			break;
		}
	}
	
	
	@Override
	public void pickTime(Calendar cal) {//重设时间的回调
		
		Calendar nowCal = Calendar.getInstance();
		
		if(nowCal.after(cal)) {
			mOpEndCal.setTimeInMillis(cal.getTimeInMillis());
			mEndDateTime.setText(StringUtils.formatDateTime(mOpEndCal));
		}else {
			DialogUtils.showToast(this, R.string.ko_tips_end_time_cant_after_now);
		}
	}

	private void startSeq() {
		showLoadingDlg();
		Intent i = getIntent();
		mKoControl.startSeq(CacheUtils.getLoginUserInfo().staffNo, "0",
							mEndDateTime.getText().toString(),
							i.getStringExtra(KEY_WIP_ID), i.getStringExtra(KEY_OP_CODE), 
							i.getStringExtra(KEY_ASSET_ID), i.getStringExtra(KEY_SCHED_DATE),
							i.getStringExtra(KEY_PAFTEROP), i.getStringExtra(KEY_SEQ_NUM), 
							i.getStringExtra(KEY_PAFTEROP_SEQ_NUM), CacheUtils.getShift());
	}
	
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			@Override
			public void getDescInfoWhenStartingSeqClicked(boolean isSuc, MeGetDescInfoWhenStartingSeqClickedAdapter descInfo, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mDescInfo = descInfo;
					mDisplayView.setText(Html.fromHtml(descInfo.display));
					mInputQuanEdit.setText(String.valueOf(descInfo.availQty));
					mOpEndCal.setTimeInMillis(KoDataUtil.convertStringToCal(descInfo.databaseTime).getTimeInMillis());
					mEndDateTime.setText(StringUtils.formatDateTime(mOpEndCal));
					
					if(descInfo.errorCode>0) {
						DialogUtils.showToast(MeStartProduceSeqActivity.this, descInfo.errorMsg);
					}
				}
				else {
					DialogUtils.showToast(MeStartProduceSeqActivity.this, msg);
				}
			}

			@Override
			public void startSeqBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				DialogUtils.showToast(MeStartProduceSeqActivity.this, msg);
				if(isSuc) {
					MeStartProduceSeqActivity.this.finish();
				}
			}
		};
	}
	
	@Override
	public void onBackPressed() {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			@Override
			public void onOkBack() {
				MeStartProduceSeqActivity.this.finish();
			}
			
			@Override
			public void onCancelBack() {
				
			}
		}, getString(R.string.ko_tips_sure_to_quit_start_seq));
		
		dlg.setOkCancelBtn(true, true);
		dlg.show();
	}

}
