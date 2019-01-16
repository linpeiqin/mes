/*-----------------------------------------------------------

-- PURPOSE

--    生产报数开始的第二个界面

------------------------------------------------------------*/

package cn.kol.pes.activity.femaleworker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.pes.R;
import cn.kol.pes.activity.BaseActivity;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.femaleworker.MmDeclareTimeItem;
import cn.kol.pes.model.item.femaleworker.MmGetNonDeclareTimesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetNonDeclareTimesItem;
import cn.kol.pes.model.item.femaleworker.MmNonDeclareTimeItem;
import cn.kol.pes.model.item.femaleworker.MmReasonCodeItem;
import cn.kol.pes.model.item.femaleworker.MmWipItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareTimeFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetReasonCodeInfoAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmNonDeclareTimeFinishAdapter;
import cn.kol.pes.widget.KoListDialg;

public class MmNonDeclareTimeActivity extends BaseActivity implements OnClickListener {

    private EditText jobNoEdit;
    private TextView reasonCodeEdit;
    private WebView nonDeclareTimeListText;
    private LinearLayout jobNoLinear;
    private TextView jobNameText;

    private EditText quantityEdit;
    private EditText goodsQuantityEdit;
    private EditText workTimeEdit;
    private EditText reasonRemarkEdit;
    private EditText goodsWasteQuantityEdit;
    private ImageView jobNoSearchBtn;
    private MmGetReasonCodeInfoAdapter mReasonCodeData;
    private static final String SCHEDULE_DATE = "scheduleDate";
    private static final String WORK_GROUP = "workGroup";
    private static final String DAY_OR_NIGHT = "dayOrNight";
    private static final String GROUP_MASTER = "groupMaster";
    private MmWipItem mWipItemData;
    public static void startAct(Context context, String scheduleDate, String workGroup, String dayOrNight, String groupMaster) {

        Intent i = new Intent(context, MmNonDeclareTimeActivity.class);
        i.putExtra(SCHEDULE_DATE, scheduleDate);
        i.putExtra(WORK_GROUP, workGroup);
        i.putExtra(DAY_OR_NIGHT, dayOrNight);
        i.putExtra(GROUP_MASTER, groupMaster);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.mm_handwork_non_declare_time_activity);
        super.onCreate(savedInstanceState);
        mTitleView.setTitle("非生产状况");
        jobNameText = (TextView) findViewById(R.id.text_job_name);
        nonDeclareTimeListText = (WebView) findViewById(R.id.text_non_declare_time_list);

        jobNoEdit = (EditText) findViewById(R.id.eidt_job_no);
        jobNoSearchBtn = (ImageView) findViewById(R.id.job_no_search_btn);
        jobNoSearchBtn.setOnClickListener(this);
        reasonCodeEdit = (TextView) findViewById(R.id.edit_reason_code);
        reasonCodeEdit.setOnClickListener(this);

        jobNoLinear = (LinearLayout)findViewById(R.id.linear_job_no);
        quantityEdit = (EditText) findViewById(R.id.edit_quantity);
        goodsQuantityEdit = (EditText) findViewById(R.id.edit_goods_quantity);
        workTimeEdit = (EditText) findViewById(R.id.edit_work_time);
        reasonRemarkEdit = (EditText) findViewById(R.id.edit_reason_remark);
        goodsWasteQuantityEdit = (EditText) findViewById(R.id.edit_goods_waste_quantity);

        findViewById(R.id.non_declare_time_finish_btn).setOnClickListener(this);
        initNonDeclareTimeList();

    }

    private void initNonDeclareTimeList(){
        MmGetNonDeclareTimesItem nonDeclareTimesItem = new MmGetNonDeclareTimesItem();
        nonDeclareTimesItem.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
        nonDeclareTimesItem.scheduleDate = getIntent().getStringExtra(SCHEDULE_DATE);
        nonDeclareTimesItem.workGroup = getIntent().getStringExtra(WORK_GROUP);
        nonDeclareTimesItem.workMonitor = getIntent().getStringExtra(GROUP_MASTER);
        nonDeclareTimesItem.workClassCode = getIntent().getStringExtra(DAY_OR_NIGHT);
        mKoControl.getNonDeclareTimeList4F(nonDeclareTimesItem);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.non_declare_time_finish_btn:
                MmNonDeclareTimeItem item = new MmNonDeclareTimeItem();
                item.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
                item.scheduleDate = getIntent().getStringExtra(SCHEDULE_DATE);
                item.workGroup = getIntent().getStringExtra(WORK_GROUP);
                item.dayOrNight = getIntent().getStringExtra(DAY_OR_NIGHT);
                item.groupMaster = getIntent().getStringExtra(GROUP_MASTER);
                if (mWipItemData!=null){
                    item.jobId = mWipItemData.jobId;
                    item.wipEntityId = mWipItemData.wipEntityId;
                    item.inventoryItemId = mWipItemData.inventoryItemId;
                }
                if (quantityEdit.getText().toString() == null || quantityEdit.getText().toString().equals("")){
                    DialogUtils.showToast(this, "请输入人数");
                    break;
                }
                if (workTimeEdit.getText().toString() == null || workTimeEdit.getText().toString().equals("")){
                    DialogUtils.showToast(this, "请输入耗时");
                    break;
                }
                if (mReasonCodeData.reasonCode == null || mReasonCodeData.reasonCode.equals("")){
                    DialogUtils.showToast(this, "请输入原因分类");
                    break;
                }
                if (reasonRemarkEdit.getText().toString() == null || reasonRemarkEdit.getText().toString().equals("")){
                    DialogUtils.showToast(this, "请输入详细说明");
                    break;
                }
                if (goodsQuantityEdit.getText().toString() == null || goodsQuantityEdit.getText().toString().equals("")){
                    DialogUtils.showToast(this, "请输入非生产总数");
                    break;
                }
                item.quantity = quantityEdit.getText().toString();
                item.workTime = workTimeEdit.getText().toString();
                item.reasonCode = mReasonCodeData.reasonCode;
                item.reasonRemark = reasonRemarkEdit.getText().toString();
                item.goodsQuantity = goodsQuantityEdit.getText().toString();
                item.goodsWasteQuantity = goodsWasteQuantityEdit.getText().toString();
                mKoControl.nonDeclareTimeFinish4F(item);
                break;
            case R.id.edit_reason_code://选择坏货原因
                showLoadingDlg();
                mKoControl.getNonReasonCodeList4F();
                break;
            case R.id.job_no_search_btn://搜索工单
                String jobNo = jobNoEdit.getText().toString();
                if(KoDataUtil.isStringNotNull(jobNo)) {
                    showLoadingDlg();
                    mKoControl.getWipListByJobNo(jobNo.toUpperCase(),CacheUtils.getMmGetOrgInfoAdapter().organizationId);
                }
                else {
                    DialogUtils.showToast(this, "请输入工程单号");
                }
                break;
            default:
                break;
        }
    }
    public void clearTextData(){
        reasonCodeEdit.setText("");
        reasonRemarkEdit.setText("");
        goodsQuantityEdit.setText("");
        quantityEdit.setText("");
        goodsWasteQuantityEdit.setText("");
        workTimeEdit.setText("");
        jobNoEdit.setText("");
        jobNameText.setText("");
    }

    @Override
    protected KolPesControlBack initControlBack() {
        return new KolPesControlBack() {
            @Override
            public void getNonDeclareTimeFinishBack(boolean isSuc, MmNonDeclareTimeFinishAdapter mMmNonDeclareTimeFinishAdapter, String msg) {
                dismissLoadingDlg();
                if (isSuc) {
                    DialogUtils.showToast(MmNonDeclareTimeActivity.this, "提交成功");
                    initNonDeclareTimeList();
                    clearTextData();
                } else {
                    DialogUtils.showToast(MmNonDeclareTimeActivity.this, "提交失败:" + msg);
                }
            }

            @Override
            public void getReasonCodeListBack(boolean isSuc, List<MmReasonCodeItem> list, String msg) {
                dismissLoadingDlg();
                if (isSuc) {
                    showReasonCodeList(list);
                } else {
                    DialogUtils.showToast(MmNonDeclareTimeActivity.this, msg);
                }
            }
            @Override
            public void getWipListBack(boolean isSuc, List<MmWipItem> list, String msg) {
                dismissLoadingDlg();
                if(isSuc) {
                    showProcessOperateList(list);
                }
                else {
                    DialogUtils.showToast(MmNonDeclareTimeActivity.this, msg);
                }
            }
            @Override
            public void getNonDeclareTimeListBack(boolean isSuc, List<MmGetNonDeclareTimesBackItem> list, String msg) {
                dismissLoadingDlg();
                if (isSuc) {
                    showNonDeclareTimeList(list);
                } else {
                    DialogUtils.showToast(MmNonDeclareTimeActivity.this, msg);
                }

            }
        };
    }
    private void showProcessOperateList(List<MmWipItem> listData) {
        if(listData!=null && listData.size()>0) {
            KoProduceListDlg dlg = new KoProduceListDlg(this, listData.get(0), listData);
            dlg.show();
        }
    }
    public void showNonDeclareTimeList(List<MmGetNonDeclareTimesBackItem> list){
        String msgList = "<table width='100%'>";
        msgList +=initTableHead();
        for (MmGetNonDeclareTimesBackItem m : list){
            msgList += "<tr>";
            msgList +=  m.showMsg();
            msgList += "</tr>";
        }
        msgList+="</table>";
        nonDeclareTimeListText.loadData(msgList,"text/html; charset=UTF-8","UTF-8");
    }
    private String initTableHead(){
        return "<tr>" +
                "        <th align='center'width='12%'>原因代码</th>" +
                "        <th align='center'width='16%'>详细说明</th>" +
                "        <th align='center' width='12%'>人数</th>" +
                "        <th align='center'width='12%'>总工时</th>" +
                "        <th align='center' width='12%' >非生产总数</th>" +
                "        <th align='center' width='12%'>非生产坏货数</th>" +
                "        <th align='center' width='12%'>工程单</th>" +
                "        <th align='center'width='12%'>生产单</th>" +
                "    </tr>";
    }
    private void showReasonCodeList(List<MmReasonCodeItem> list) {
       ReasonCodeListDlg dlg = new ReasonCodeListDlg(this, null, list);
        dlg.show();
    }
    //2018展示生产单号信息
    class KoProduceListDlg extends KoListDialg<MmWipItem> {

        public KoProduceListDlg(Activity context, MmWipItem selectedData, List<MmWipItem> listData) {
            super(context, selectedData, listData);
        }

        @Override
        public String getStringToShowFromObj(MmWipItem selectedItem) {
            return selectedItem.jobDesc;
        }

        @Override
        public boolean isSelectedObjEquals(MmWipItem selectedData, MmWipItem item) {
            if(selectedData!=null && item!=null && selectedData.equals(item)) {
                return true;
            }
            return false;
        }

        @Override
        public void selectedItemData(MmWipItem selData) {
            mWipItemData = selData;
            jobNoEdit.setText(selData.jobDesc);
            jobNameText.setText(selData.jobName);
        }
    }
    //2018展示错误原因代码列表
    public class ReasonCodeListDlg extends KoListDialg<MmReasonCodeItem> {

        public ReasonCodeListDlg(Activity context, MmReasonCodeItem selectedData, List<MmReasonCodeItem> listData) {
            super(context, selectedData, listData);
        }

        @Override
        public String getStringToShowFromObj(MmReasonCodeItem selectedItem) {
            return selectedItem.reasonCode + " " + selectedItem.reasonName;
        }

        @Override
        public boolean isSelectedObjEquals(MmReasonCodeItem selectedData, MmReasonCodeItem item) {
            if (selectedData != null && item != null && selectedData.equals(item)) {
                return true;
            }
            return false;
        }

        @Override
        public void selectedItemData(MmReasonCodeItem selData) {
            mReasonCodeData = new MmGetReasonCodeInfoAdapter();
            mReasonCodeData.reasonCode = selData.reasonCode;
            mReasonCodeData.reasonName = selData.reasonName;
            reasonCodeEdit.setText(mReasonCodeData.reasonCode + "  " + mReasonCodeData.reasonName);
            if (isNeedSetPJ(mReasonCodeData.reasonCode)){
                jobNoLinear.setVisibility(View.VISIBLE);
            } else {
                jobNoLinear.setVisibility(View.GONE);
                if (mWipItemData!=null) {
                    mWipItemData.inventoryItemId = null;
                    mWipItemData.wipEntityId = null;
                    mWipItemData.jobId = null;
                }
                jobNoEdit.setText("");
                jobNameText.setText("");
            }
        }
        private Boolean isNeedSetPJ(String reasonCode){
            if (reasonCode.equals("A") || reasonCode.equals("B") || reasonCode.equals("F"))
                return true;
            return false;
        }
    }
}
