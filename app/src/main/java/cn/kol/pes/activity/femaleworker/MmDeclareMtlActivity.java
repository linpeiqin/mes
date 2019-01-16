package cn.kol.pes.activity.femaleworker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.activity.BaseActivity;
import cn.kol.pes.activity.KoCaptureActivity;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.femaleworker.MmDeclareMtlItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareMtlsBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareMtlsItem;
import cn.kol.pes.model.item.femaleworker.MmMtlPlanItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareMtlFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetMtlPlanInfoAdapter;
import cn.kol.pes.widget.KoListDialg;


public class MmDeclareMtlActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener {

	private TextView jobNoText;
	private TextView wipEntityNoText;
	private TextView itemNameText;
	private TextView handworkSeqText;

	private EditText mtlNoText;
	private TextView mtlNameText;
	private TextView planQuantityText;
	private TextView alreadyQuantityText;
	private TextView canQuantityText;
	private TextView uomText;
	private WebView declareMtlListText;
	private ImageView mtlNoSearchBtn;


	private EditText actualQuantityEdit;
	private EditText remarkEdit;

	private MmGetMtlPlanInfoAdapter mMtlPlanInfoData;
	private RadioButton radioPlan;
	private RadioButton radioNew;
	private Button mtlNoScanBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.mm_handwork_declare_mtl_activity);
		super.onCreate(savedInstanceState);
		mTitleView.setTitle("物料报数");

		jobNoText = (TextView) findViewById(R.id.text_job_no);
		wipEntityNoText = (TextView) findViewById(R.id.text_wip_entity_no);
		itemNameText = (TextView) findViewById(R.id.text_item_name);
		handworkSeqText = (TextView) findViewById(R.id.text_handwork_seq);
		declareMtlListText = (WebView) findViewById(R.id.text_declare_mtl_list);

		mtlNoText = (EditText) findViewById(R.id.edit_mtl_no);
		mtlNameText = (TextView) findViewById(R.id.edit_mtl_name);
		planQuantityText = (TextView) findViewById(R.id.edit_plan_quantity);
		alreadyQuantityText = (TextView) findViewById(R.id.edit_already_quantity);
		canQuantityText = (TextView) findViewById(R.id.edit_can_quantity);
		uomText = (TextView) findViewById(R.id.edit_uom);

		actualQuantityEdit = (EditText) findViewById(R.id.edit_actual_quantity);
		remarkEdit = (EditText) findViewById(R.id.edit_remark);

		mtlNoSearchBtn = (ImageView)findViewById(R.id.btn_mtl_no_search);
		mtlNoSearchBtn.setOnClickListener(this);
		mtlNoScanBtn = (Button) findViewById(R.id.btn_mtl_no_scan);
		mtlNoScanBtn.setOnClickListener(this);
		radioPlan = (RadioButton) findViewById(R.id.radio_mtl_plan);
		radioNew = (RadioButton) findViewById(R.id.radio_mtl_new);
		radioPlan.setOnCheckedChangeListener(this);
		radioNew.setOnCheckedChangeListener(this);
		radioPlan.setChecked(true);

		findViewById(R.id.declare_mtl_next_btn).setOnClickListener(this);
		findViewById(R.id.declare_mtl_back_btn).setOnClickListener(this);



		jobNoText.setText(getIntent().getStringExtra("jobNo"));
		wipEntityNoText.setText(getIntent().getStringExtra("wipEntityNo"));
		itemNameText.setText(getIntent().getStringExtra("itemName"));
		handworkSeqText.setText(getIntent().getStringExtra("operationSeqDesc")+"-"+getIntent().getStringExtra("handworkSeqCode"));
		initMtlList();
	}
	private void initMtlList(){
		MmGetDeclareMtlsItem mmGetDeclareMtlsItem = new MmGetDeclareMtlsItem();
		mmGetDeclareMtlsItem.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
		mmGetDeclareMtlsItem.jobTransactionId = getIntent().getStringExtra("jobTransactionId");
		mmGetDeclareMtlsItem.moveTransactionId = getIntent().getStringExtra("moveTransactionId");
		mmGetDeclareMtlsItem.transactionType = "DECLARE";
		mKoControl.getDeclareMtlList4F(mmGetDeclareMtlsItem);
	}
	public static void startAct(Context context, String jobTransactionId, String moveTransactionId, String jobNo, String wipEntityNo,String wipEntityId, String itemName,String operationSeqNum,String operationSeqDesc, String handworkSeqCode) {
		Intent i = new Intent(context, MmDeclareMtlActivity.class);
		i.putExtra("jobTransactionId", jobTransactionId);
		i.putExtra("moveTransactionId", moveTransactionId);
		i.putExtra("jobNo", jobNo);
		i.putExtra("wipEntityNo", wipEntityNo);
		i.putExtra("wipEntityId", wipEntityId);
		i.putExtra("itemName", itemName);
		i.putExtra("operationSeqNum", operationSeqNum);
		i.putExtra("operationSeqDesc", operationSeqDesc);
		i.putExtra("handworkSeqCode", handworkSeqCode);
		context.startActivity(i);
	}
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (radioNew.isChecked() && !radioPlan.isChecked()){
			mtlNoText.setFocusable(true);
			mtlNoText.setFocusableInTouchMode(true);
			mtlNoText.setOnClickListener(null);
			mtlNoSearchBtn.setVisibility(View.VISIBLE);
			mtlNoScanBtn.setVisibility(View.VISIBLE);
			clearTextData();
        }
        if (radioPlan.isChecked() && !radioNew.isChecked()){
			mtlNoText.setFocusable(false);
			mtlNoText.setFocusableInTouchMode(false);
            mtlNoText.setOnClickListener(this);
			mtlNoSearchBtn.setVisibility(View.GONE);
			mtlNoScanBtn.setVisibility(View.GONE);
			clearTextData();
        }
    }
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.edit_mtl_no:
				showLoadingDlg();
				mKoControl.getMtlPlanList4F(
						getIntent().getStringExtra("wipEntityId"),
						getIntent().getStringExtra("operationSeqNum"),
						CacheUtils.getMmGetOrgInfoAdapter().organizationId
				);
				break;
			case R.id.btn_mtl_no_search:
				String mtlNo = mtlNoText.getText().toString().trim();
				if(mtlNo!=null && mtlNo.length()>=10) {
					showLoadingDlg();
					mKoControl.getMtlNewList4F(getIntent().getStringExtra("wipEntityId"), mtlNo.toUpperCase(),CacheUtils.getMmGetOrgInfoAdapter().organizationId);
				}
				else {
					DialogUtils.showToast(this, "请至少填写10个字符作为关键字");
				}
				break;
			case R.id.btn_mtl_no_scan:
				KoCaptureActivity.startActForRes(this);
				break;
			case R.id.declare_mtl_back_btn:
				MmDeclareMtlItem declareMtlItem = new MmDeclareMtlItem();
				if (mtlNoText.getText().toString().equals("")){
					DialogUtils.showToast(MmDeclareMtlActivity.this, "请输入物料");
					break;
				}
				if (actualQuantityEdit.getText().toString().equals("")){
					DialogUtils.showToast(MmDeclareMtlActivity.this, "请输入实际用量");
					break;
				}
				this.setTextToItem(declareMtlItem);
				declareMtlItem.transactionType = "WITHDRAWAL";
				showLoadingDlg();
				mKoControl.declareMtlFinish4F(declareMtlItem);
				break;
			case R.id.declare_mtl_next_btn:
				MmDeclareMtlItem declareMtlItem2 = new MmDeclareMtlItem();
				if (mtlNoText.getText().toString().equals("")){
					DialogUtils.showToast(MmDeclareMtlActivity.this, "请输入物料");
					break;
				}
				if (actualQuantityEdit.getText().toString().equals("")){
					DialogUtils.showToast(MmDeclareMtlActivity.this, "请输入实际用量");
					break;
				}
				Double canQD = Double.valueOf(canQuantityText.getText().toString());
				Double actualQD = Double.valueOf(actualQuantityEdit.getText().toString());
				if(actualQD>canQD){
					DialogUtils.showToast(MmDeclareMtlActivity.this, "实际用量不能大于可报数量");
					break;
				}
				this.setTextToItem(declareMtlItem2);
				declareMtlItem2.transactionType = "DECLARE";
				showLoadingDlg();
				mKoControl.declareMtlFinish4F(declareMtlItem2);
				break;
			default:
				break;
		}

	}
	private void setTextToItem(MmDeclareMtlItem item) {
		item.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
		item.jobTransactionId = getIntent().getStringExtra("jobTransactionId");
		item.moveTransactionId = getIntent().getStringExtra("moveTransactionId");
		if (mMtlPlanInfoData == null)
			return ;
		item.inventoryItemId = mMtlPlanInfoData.inventoryItemId;
		item.transactionUom = uomText.getText().toString();
		item.transactionQuantity = actualQuantityEdit.getText().toString();
		item.remark = remarkEdit.getText().toString();
		if (radioPlan.isChecked()){
			item.transactionItemType = "BOM";
		} else {
			item.transactionItemType = "SPEC";
		}

	}
	public void clearTextData(){
		mtlNoText.setText("");
		mtlNameText.setText("");
		planQuantityText.setText("");
		alreadyQuantityText.setText("");
		canQuantityText.setText("");
		uomText.setText("");
		actualQuantityEdit.setText("");
		remarkEdit.setText("");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode==KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {//扫码结果的回调

			String res = data.getStringExtra("res");
			LogUtils.e(tag, "onActivityResult()"+res);
			if(res!=null && res.length()>=10) {
				showLoadingDlg();
				mKoControl.getMtlNewList4F(getIntent().getStringExtra("wipEntityId"), res.toUpperCase(),CacheUtils.getMmGetOrgInfoAdapter().organizationId);
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
			public void getMtlPlanListBack(boolean isSuc, List<MmMtlPlanItem> list, String msg) {
				dismissLoadingDlg();
				if (isSuc) {
					showMtlPlanList(list);
				} else {
					DialogUtils.showToast(MmDeclareMtlActivity.this, msg);
				}
			}

			@Override
			public void getDeclareMtlFinishBack(boolean isSuc, MmDeclareMtlFinishAdapter mMmDeclareMtlFinishAdapter, String msg) {
				dismissLoadingDlg();
				if (isSuc) {
					DialogUtils.showToast(MmDeclareMtlActivity.this, "物料报数成功");
					initMtlList();
					clearTextData();
				} else {
					DialogUtils.showToast(MmDeclareMtlActivity.this, msg);
				}
			}
			@Override
			public void getDeclareMtlListBack(boolean isSuc, List<MmGetDeclareMtlsBackItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {//如果请求成功，则显示相关数据
					showDeclareMtlList(list);
				}
				else {//如果失败，清除相关的显示和数据
					DialogUtils.showToast(MmDeclareMtlActivity.this, msg);
				}
			}
		};
	}

	private void showDeclareMtlList(List<MmGetDeclareMtlsBackItem> list) {
		String msgList = "<table width='100%'>";
		msgList += initTableHead();
		for (MmGetDeclareMtlsBackItem m : list){
			msgList += "<tr>";
			msgList +=  m.showMsg();
			msgList += "</tr>";
		}
		msgList+="</table>";
		declareMtlListText.loadData(msgList,"text/html; charset=UTF-8","UTF-8");
	}

	private String initTableHead(){
		return "<tr>" +
				"        <th align='left'>物料编号</th>" +
				"        <th align='center'>实际用量</th>" +
				"        <th align='right'>报数类型</th>" +
				"    </tr>";
	}

	private void showMtlPlanList(List<MmMtlPlanItem> list) {
		MtlPlanListDlg dlg = new MtlPlanListDlg(this, null, list);
		dlg.show();
	}

	//2018获取计划物料
	public class MtlPlanListDlg extends KoListDialg<MmMtlPlanItem> {

		public MtlPlanListDlg(Activity context, MmMtlPlanItem selectedData, List<MmMtlPlanItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MmMtlPlanItem selectedItem) {
			return selectedItem.concatenatedSegments + " " + selectedItem.itemDescription + " " + selectedItem.requiredQuantity;
		}

		@Override
		public boolean isSelectedObjEquals(MmMtlPlanItem selectedData, MmMtlPlanItem item) {
			if (selectedData != null && item != null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MmMtlPlanItem selData) {
			mMtlPlanInfoData = new MmGetMtlPlanInfoAdapter();
			mMtlPlanInfoData.wipEntityId = selData.wipEntityId;
			mMtlPlanInfoData.operationSeqNum = selData.operationSeqNum;
			mMtlPlanInfoData.organizationId = selData.organizationId;
			mMtlPlanInfoData.inventoryItemId = selData.inventoryItemId;
			mMtlPlanInfoData.concatenatedSegments = selData.concatenatedSegments;
			mMtlPlanInfoData.itemDescription = selData.itemDescription;
			if (selData.requiredQuantity.equals("-1")){
				mMtlPlanInfoData.requiredQuantity = "不适用";
			} else {
				mMtlPlanInfoData.requiredQuantity = selData.requiredQuantity;
			}
			mMtlPlanInfoData.itemPrimaryUomCode = selData.itemPrimaryUomCode;
			mMtlPlanInfoData.issued = selData.issued;
			mMtlPlanInfoData.available = selData.available;
			mtlNameText.setText(mMtlPlanInfoData.itemDescription);
			mtlNoText.setText(mMtlPlanInfoData.concatenatedSegments);
			planQuantityText.setText(mMtlPlanInfoData.requiredQuantity);
			alreadyQuantityText.setText(mMtlPlanInfoData.issued);
			canQuantityText.setText(mMtlPlanInfoData.available);
			uomText.setText(mMtlPlanInfoData.itemPrimaryUomCode);
		}
	}
}
