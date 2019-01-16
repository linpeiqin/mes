/*-----------------------------------------------------------
-- PURPOSE
--    开始工序 1st Page
-- History
--	  25-Feb-17  LiZheng  Created.

------------------------------------------------------------*/
package cn.kol.pes.activity.femaleworker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.activity.BaseActivity;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.femaleworker.MmDeclareTimeItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesItem;
import cn.kol.pes.model.item.femaleworker.MmReasonCodeItem;
import cn.kol.pes.model.item.femaleworker.MmRoutingItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmDeclareTimeFinishAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetReasonCodeInfoAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetRoutingInfoAdapter;
import cn.kol.pes.widget.KoListDialg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;


public class MmDeclareTimeActivity extends BaseActivity implements OnClickListener {

    public static void startAct(Context context, String jobTransactionId, String jobNo, String wipEntityNo, String itemNo,String itemName,String primaryUomCode, String operationSeqNum, String operationSeqDesc,String groupCode,String initDate,MmGetDeclareTimesItem declareTimesItem,String assetCode) {
        Intent i = new Intent(context, MmDeclareTimeActivity.class);
        i.putExtra("jobTransactionId", jobTransactionId);
        i.putExtra("jobNo", jobNo);
        i.putExtra("wipEntityNo", wipEntityNo);
        i.putExtra("itemNo", itemNo);
        i.putExtra("itemName", itemName);
        i.putExtra("primaryUomCode", primaryUomCode);
        i.putExtra("operationSeqNum", operationSeqNum);
        i.putExtra("operationSeqDesc", operationSeqDesc);
        i.putExtra("declareTimesItem", declareTimesItem);
        i.putExtra("groupCode", groupCode);
        i.putExtra("initDate", initDate);
        i.putExtra("assetCode", assetCode);

        context.startActivity(i);
    }


    private TextView mRoutingEdit;
    private TextView jobNoText;
    private TextView wipEntityNoText;
    private TextView itemNoText;
    private TextView operationSeqDescText;
    private TextView subgoodsQuantityNameText;
    private TextView goodsQuantityNameText;
    private TextView reasonCodeEdit;
    private TextView startPullTimeEdit;
    private TextView endPullTimeEdit;
    private WebView declareTimeListText;
    private TextView transactionUomText;

    private MmGetRoutingInfoAdapter mRoutingData;
    private MmGetReasonCodeInfoAdapter mReasonCodeData;


    private EditText workTimeEdit;
    private EditText perQuantityEdit;
    private EditText subgoodsQuantityEdit;
    private EditText goodsWasteQuantityEdit;
    private EditText quantityEdit;
    private EditText niOperationDescEdit;


    private EditText goodsQuantityEdit;
    private EditText reasonRemarkEdit;


    private Calendar startCal = Calendar.getInstance();//开拉时间
    private Calendar endCal = Calendar.getInstance();//收拉时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.mm_handwork_declare_time_activity);
        super.onCreate(savedInstanceState);

        mTitleView.setTitle("生产状况");

        jobNoText = (TextView) findViewById(R.id.text_job_no);
        wipEntityNoText = (TextView) findViewById(R.id.text_wip_entity_no);
        itemNoText = (TextView) findViewById(R.id.text_item_no);
        operationSeqDescText = (TextView) findViewById(R.id.text_operation_seq_desc);
        declareTimeListText = (WebView) findViewById(R.id.text_declare_time_list);
        transactionUomText = (TextView) findViewById(R.id.edit_transaction_uom);
        jobNoText.setText(getIntent().getStringExtra("jobNo"));
        wipEntityNoText.setText(getIntent().getStringExtra("wipEntityNo"));
        itemNoText.setText(getIntent().getStringExtra("itemNo"));

        operationSeqDescText.setText(getIntent().getStringExtra("operationSeqNum") + " " + getIntent().getStringExtra("operationSeqDesc"));
        transactionUomText.setText(getIntent().getStringExtra("primaryUomCode"));



        mRoutingEdit = (TextView) findViewById(R.id.produce_asset_seq_female_edit);//女工工序编号
        mRoutingEdit.setOnClickListener(this);

        niOperationDescEdit = (EditText) findViewById(R.id.produce_asset_seq_desc_edit);//女工工序描述

        reasonCodeEdit = (TextView) findViewById(R.id.edit_reason_code);//坏货原因代码
        reasonCodeEdit.setOnClickListener(this);
        startPullTimeEdit = (TextView) findViewById(R.id.edit_start_pull_time);
        startPullTimeEdit.setOnClickListener(this);
        Integer[] beginCalDate = initBeginCalDate();
        startCal.set(beginCalDate[0],beginCalDate[1],beginCalDate[2],beginCalDate[3],beginCalDate[4]);
        startPullTimeEdit.setText(StringUtils.formatDateTime(startCal));

        endPullTimeEdit = (TextView) findViewById(R.id.edit_end_pull_time);
        endPullTimeEdit.setOnClickListener(this);
        Integer[] endCalDate = initEndCalDate();
        endCal.set(endCalDate[0],endCalDate[1],endCalDate[2],endCalDate[3],endCalDate[4]);
        endPullTimeEdit.setText(StringUtils.formatDateTime(endCal));

        findViewById(R.id.declare_time_finish_btn).setOnClickListener(this);
        findViewById(R.id.declare_mtl_begin_btn).setOnClickListener(this);


        workTimeEdit = (EditText) findViewById(R.id.edit_work_time);
        perQuantityEdit = (EditText) findViewById(R.id.edit_per_quantity);
        subgoodsQuantityEdit = (EditText) findViewById(R.id.edit_subgoods_quantity);



        subgoodsQuantityNameText = (TextView) findViewById(R.id.text_subgoods_quantity_name);
        goodsQuantityNameText = (TextView) findViewById(R.id.text_goods_quantity_name);
        goodsWasteQuantityEdit = (EditText) findViewById(R.id.edit_goods_waste_quantity);
        quantityEdit = (EditText) findViewById(R.id.edit_quantity);

        goodsQuantityEdit = (EditText) findViewById(R.id.edit_goods_quantity);
        reasonRemarkEdit = (EditText) findViewById(R.id.edit_reason_remark);
        initTimeList();
    }

    public void clearTextData(){
        mRoutingEdit.setText("");
        niOperationDescEdit.setText("");
        reasonCodeEdit.setText("");
        reasonRemarkEdit.setText("");
        goodsQuantityEdit.setText("");
        quantityEdit.setText("");
        goodsWasteQuantityEdit.setText("");

        workTimeEdit.setText("");
        perQuantityEdit.setText("");
        subgoodsQuantityEdit.setText("");
    }

    private void initTimeList(){
        mKoControl.getDeclareTimeList4F((MmGetDeclareTimesItem) getIntent().getSerializableExtra("declareTimesItem"));
    }
    private Integer[] initBeginCalDate(){
        String initDate = getIntent().getStringExtra("initDate");
        String groupCode = getIntent().getStringExtra("groupCode");
        Integer[] initBeginCalDatesI = new Integer[5];
        Integer[] initTemp = calDate(initDate);
        initBeginCalDatesI[0] = initTemp[0];
        initBeginCalDatesI[1] = initTemp[1];
        initBeginCalDatesI[2] = initTemp[2];
        initBeginCalDatesI[3] = 8;
        if (groupCode.substring(0,1).equals("D")){
            initBeginCalDatesI[4] = 0;
            return initBeginCalDatesI;
        }
        initBeginCalDatesI[4] = 30;
        return initBeginCalDatesI;
    }
    private Integer[] initEndCalDate(){
        String initDate = getIntent().getStringExtra("initDate");
        String groupCode = getIntent().getStringExtra("groupCode");
        Integer[] initEndCalDatesI = new Integer[5];
        Integer[] initTemp = calDate(initDate);
        initEndCalDatesI[0] = initTemp[0];
        initEndCalDatesI[1] = initTemp[1];
        initEndCalDatesI[2] = initTemp[2];
        initEndCalDatesI[3] = 20;
        if (groupCode.substring(0,1).equals("D")){
            initEndCalDatesI[4] = 0;
            return initEndCalDatesI;
        }
        initEndCalDatesI[4] = 30;
        return initEndCalDatesI;
    }
    private Integer[] calDate(String initDate){
        String[] initDates = initDate.substring(0,10).split("-");
        Integer[] initDatesI = new Integer[3];
        initDatesI[0] = Integer.valueOf(initDates[0]);
        initDatesI[1] = Integer.valueOf(initDates[1])-1;
        initDatesI[2] = Integer.valueOf(initDates[2]);
        return initDatesI;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_start_pull_time:
                new StartTimeDlg(this, this, startCal, true);
                break;
            case R.id.edit_end_pull_time:
                 new EndTimeDlg(this, this, endCal, true);
                break;
            case R.id.produce_asset_seq_female_edit://选择女工工序
                showLoadingDlg();
                //修改加载女工的组别列表
                mKoControl.getRoutingList4F();
                break;
            case R.id.edit_reason_code://选择坏货原因
                showLoadingDlg();
                mKoControl.getReasonCodeList4F();
                break;
            case R.id.declare_mtl_begin_btn://物料报数
                showLoadingDlg();
                mKoControl.getDeclareTimeListToMtl((MmGetDeclareTimesItem) getIntent().getSerializableExtra("declareTimesItem"));
                break;
            case R.id.declare_time_finish_btn://完成
                if (mRoutingData == null){
                    DialogUtils.showToast(MmDeclareTimeActivity.this, "请填写女工工序");
                    break;
                }
                if (quantityEdit.getText().toString() == null || quantityEdit.getText().toString().equals("")){
                    DialogUtils.showToast(MmDeclareTimeActivity.this, "请填写人数");
                    break;
                }
                if (perQuantityEdit.getText().toString() == null || perQuantityEdit.getText().toString().equals("")){
                    DialogUtils.showToast(MmDeclareTimeActivity.this, "请填写每小时产量");
                    break;
                }
                if (workTimeEdit.getText().toString() == null || workTimeEdit.getText().toString().equals("")){
                    DialogUtils.showToast(MmDeclareTimeActivity.this, "请填写总工时");
                    break;
                }

                if (niOperationDescEdit.getText().toString() == null || niOperationDescEdit.getText().toString().equals("")){
                    DialogUtils.showToast(MmDeclareTimeActivity.this, "请填写女工工序描述");
                    break;
                }
                if (mRoutingData.routingCode.equals("G3")){
                    if (subgoodsQuantityEdit == null || subgoodsQuantityEdit.getText().toString().equals("")) {
                        DialogUtils.showToast(MmDeclareTimeActivity.this, "G3请填写半制品");
                        break;
                    }
                }
                if (mRoutingData.routingCode.equals("G4")){
                    if (goodsQuantityEdit == null || goodsQuantityEdit.getText().toString().equals("")) {
                        DialogUtils.showToast(MmDeclareTimeActivity.this, "G4请填写成品数");
                        break;
                    }
                }
                Integer quantity = Integer.valueOf(quantityEdit.getText().toString());
                Double workTime  = Double.valueOf(workTimeEdit.getText().toString());
                Integer restTime = calRestTime(startCal,endCal);//计算休息时间
                Double workTimeMax = Double.valueOf ((endCal.get(Calendar.HOUR_OF_DAY)*60+endCal.get(Calendar.MINUTE)) - (startCal.get(Calendar.HOUR_OF_DAY)*60+startCal.get(Calendar.MINUTE)) - restTime)/60*quantity;
                if (workTime>workTimeMax){
                    DialogUtils.showToast(MmDeclareTimeActivity.this, "总工时不能大于：（收拉时间-开拉时间-休息时间）*人数");
                    break;
                }
                MmDeclareTimeItem item = new MmDeclareTimeItem();
                this.setTextToItem(item);
                showLoadingDlg();
                mKoControl.declareTimeFinish4F(item);
                break;
            default:
                break;
        }
    }
    private Integer calRestTime(Calendar startCal, Calendar endCal){
        Calendar midTimeCal = Calendar.getInstance();
        Calendar afterTimeCal = Calendar.getInstance();
        Integer restTime = 120;
        midTimeCal.set(startCal.get(Calendar.YEAR),startCal.get(Calendar.MONTH),startCal.get(Calendar.DAY_OF_MONTH),13,30);
        afterTimeCal.set(startCal.get(Calendar.YEAR),startCal.get(Calendar.MONTH),startCal.get(Calendar.DAY_OF_MONTH),18,30);
        if (startCal.before(midTimeCal) && endCal.before(afterTimeCal) && endCal.after(midTimeCal)){//开始时间小于中饭，结束时间小于晚饭，结束时间大于中饭
            restTime = 60;
        } else if (startCal.after(midTimeCal) && startCal.before(afterTimeCal) && endCal.after(afterTimeCal)){//开始时间大于中饭，开始时间小于晚饭，结束时间大于晚饭
            restTime = 60;
        } else if (startCal.before(midTimeCal) && endCal.before(midTimeCal)){//开始时间小于中饭，结束时间小于中饭
            restTime = 0;
        } else if (startCal.after(midTimeCal) && startCal.before(afterTimeCal) && endCal.before(afterTimeCal)){//开始时间大于中饭，早于晚饭，结束时间早于晚饭
            restTime = 0;
        } else if (startCal.after(afterTimeCal) && endCal.after(afterTimeCal)){//开始时间和结束时间都小于晚饭
            restTime = 0;
        }
        return restTime;
    }
    private void setTextToItem(MmDeclareTimeItem item) {
        item.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
        item.jobTransactionId = getIntent().getStringExtra("jobTransactionId");
        item.operationSeqNum = getIntent().getStringExtra("operationSeqNum");
        item.niOperationCode = mRoutingData.routingCode;
        item.niOperationDesc = niOperationDescEdit.getText().toString();
        item.assetCode = getIntent().getStringExtra("assetCode");
        item.quantity = quantityEdit.getText().toString();
        item.startPullTime = startPullTimeEdit.getText().toString().replace("-","/");
        item.endPullTime = endPullTimeEdit.getText().toString().replace("-","/");
        item.workTime = workTimeEdit.getText().toString();
        item.transactionUom = transactionUomText.getText().toString();
        item.perQuantity = perQuantityEdit.getText().toString();
        item.subgoodsQuantity = subgoodsQuantityEdit.getText().toString();
        item.goodsQuantity = goodsQuantityEdit.getText().toString();
        item.goodsWasteQuantity = goodsWasteQuantityEdit.getText().toString();
        item.returnWasteQuantity = "0";
        item.inputQuantity = "0";
        item.wasteInputQuantity = "0";
        if (mReasonCodeData == null) {
            item.reasonCode = "";
            item.reasonRemark = "";
        } else {
            item.reasonCode = mReasonCodeData.reasonCode;
            item.reasonRemark = reasonRemarkEdit.getText().toString();
        }

    }


    @Override
    protected KolPesControlBack initControlBack() {
        return new KolPesControlBack() {

            @Override
            public void getRoutingListBack(boolean isSuc, List<MmRoutingItem> list, String msg) {
                dismissLoadingDlg();
                if (isSuc) {
                    showRoutingList(list);
                } else {
                    DialogUtils.showToast(MmDeclareTimeActivity.this, msg);
                }
            }

            @Override
            public void getReasonCodeListBack(boolean isSuc, List<MmReasonCodeItem> list, String msg) {
                dismissLoadingDlg();
                if (isSuc) {
                    showReasonCodeList(list);
                } else {
                    DialogUtils.showToast(MmDeclareTimeActivity.this, msg+"您是对这个工序第一个报数的人");
                }
            }
            @Override
            public void getDeclareTimeListBack(boolean isSuc, List<MmGetDeclareTimesBackItem> list, String msg) {
                dismissLoadingDlg();
                if(isSuc) {//如果请求成功，则显示相关数据
                   showDeclareTimeList(list);
                }
                else {//如果失败，清除相关的显示和数据
                    DialogUtils.showToast(MmDeclareTimeActivity.this, msg);
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
                        showDeclareTimeListToMtl(list);
                    }
                }
                else {//如果失败，清除相关的显示和数据
                    DialogUtils.showToast(MmDeclareTimeActivity.this, msg);
                }
            }
            @Override
            public void getDeclareTimeFinishBack(boolean isSuc, MmDeclareTimeFinishAdapter mMmDeclareTimeFinishAdapter, String msg) {
                dismissLoadingDlg();
                if (isSuc) {
                    DialogUtils.showToast(MmDeclareTimeActivity.this, "提交成功");
                    initTimeList();
                    clearTextData();
                } else {
                    DialogUtils.showToast(MmDeclareTimeActivity.this, "提交失败:" + msg);
                }
            }
        };
    }

    //2018
    private void showDeclareTimeListToMtl(List<MmGetDeclareTimesBackItem> declareTimeList) {
        DeclareTimeListDlg dlg = new DeclareTimeListDlg(this, null, declareTimeList);
        dlg.show();
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

    //启动物料报数界面
    private void startDeclareMtl(MmGetDeclareTimesBackItem declareTimeItem){
        MmDeclareMtlActivity.startAct(MmDeclareTimeActivity.this,
                declareTimeItem.jobTransactionId,
                declareTimeItem.moveTransactionId,
                getIntent().getStringExtra("jobNo"),
                getIntent().getStringExtra("wipEntityNo"),
                ((MmGetDeclareTimesItem) getIntent().getSerializableExtra("declareTimesItem")).wipEntityId,
                getIntent().getStringExtra("itemName"),
                declareTimeItem.operationSeqNum,
                declareTimeItem.operationDesc,
                declareTimeItem.niOperationCode
        );
    }
    public void showDeclareTimeList(List<MmGetDeclareTimesBackItem> list){
        String msgList = "<table width='100%'>";
        msgList +=initTableHead();
        for (MmGetDeclareTimesBackItem m : list){
            msgList += "<tr>";
            msgList +=  m.showMsg();
            msgList += "</tr>";
        }
        msgList+="</table>";
        declareTimeListText.loadData(msgList,"text/html; charset=UTF-8","UTF-8");
    }
    private String initTableHead(){
        return "<tr>" +
                "        <th align='center'width='12%'>工序编号</th>" +
                "        <th align='center' width='16%'>工序描述</th>" +
                "        <th align='center'width='12%'>人数</th>" +
                "        <th align='center' width='12%' >总工时</th>" +
                "        <th align='center' width='12%'>每小时产量</th>" +
                "        <th align='center' width='12%'>半制品数</th>" +
                "        <th align='center' width='12%'>成品数</th>" +
                "        <th align='center'width='12%'>生产坏货数</th>" +
                "    </tr>";
    }
    private void showRoutingList(List<MmRoutingItem> list) {
        RoutingListDlg dlg = new RoutingListDlg(this, null, list);
        dlg.show();
    }

    private void showReasonCodeList(List<MmReasonCodeItem> list) {
        ReasonCodeListDlg dlg = new ReasonCodeListDlg(this, null, list);
        dlg.show();
    }


    //2018女工细工序显示
    public class RoutingListDlg extends KoListDialg<MmRoutingItem> {

        public RoutingListDlg(Activity context, MmRoutingItem selectedData, List<MmRoutingItem> listData) {
            super(context, selectedData, listData);
        }

        @Override
        public String getStringToShowFromObj(MmRoutingItem selectedItem) {
            return selectedItem.routingCode;
        }

        @Override
        public boolean isSelectedObjEquals(MmRoutingItem selectedData, MmRoutingItem item) {
            if (selectedData != null && item != null && selectedData.equals(item)) {
                return true;
            }
            return false;
        }

        @Override
        public void selectedItemData(MmRoutingItem selData) {
            mRoutingData = new MmGetRoutingInfoAdapter();
            mRoutingData.routingCode = selData.routingCode;
            mRoutingEdit.setText(mRoutingData.routingCode);
            if (mRoutingData.routingCode.equals("G3")){
                subgoodsQuantityNameText.setVisibility(View.VISIBLE);
                subgoodsQuantityEdit.setVisibility(View.VISIBLE);
                goodsQuantityEdit.setVisibility(View.GONE);
                goodsQuantityNameText.setVisibility(View.GONE);
                goodsQuantityEdit.setText("");
            }
            if (mRoutingData.routingCode.equals("G4")){
                subgoodsQuantityNameText.setVisibility(View.GONE);
                subgoodsQuantityEdit.setVisibility(View.GONE);
                goodsQuantityEdit.setVisibility(View.VISIBLE);
                goodsQuantityNameText.setVisibility(View.VISIBLE);
                subgoodsQuantityEdit.setText("");
            }
        }
    }

    //2018展示错误原因代码列表
    public class ReasonCodeListDlg extends KoListDialg<MmReasonCodeItem> {

        public ReasonCodeListDlg(Activity context, MmReasonCodeItem selectedData, List<MmReasonCodeItem> listData) {
            super(context, selectedData, listData);
        }

        @Override
        public String getStringToShowFromObj(MmReasonCodeItem selectedItem) {
            return selectedItem.reasonCode + " " + selectedItem.reasonName + selectedItem.reasonDesc;
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
            reasonRemarkEdit.setText(selData.reasonDesc);
        }
    }

    public class EndTimeDlg implements IKoPickTimeDlgBack{

        public EndTimeDlg(MmDeclareTimeActivity mmDeclareTimeActivity, MmDeclareTimeActivity mmDeclareTimeActivity1, Calendar cal, boolean b) {
            KoPickTimeDlg k =  new  KoPickTimeDlg(mmDeclareTimeActivity, this, cal, true);
            k.show();
        }

        @Override
        public void pickTime(Calendar cal) {
            if (startCal!=null && cal.before(startCal)){
                DialogUtils.showToast(MmDeclareTimeActivity.this, "开拉时间不能晚于收拉时间");
            } else {
                endCal = Calendar.getInstance();
                endCal.setTimeInMillis(cal.getTimeInMillis());
                endPullTimeEdit.setText(StringUtils.formatDateTime(endCal));
            }
        }
    }

    public class StartTimeDlg implements IKoPickTimeDlgBack{

        public StartTimeDlg(MmDeclareTimeActivity mmDeclareTimeActivity, MmDeclareTimeActivity mmDeclareTimeActivity1, Calendar cal, boolean b) {
            KoPickTimeDlg k =  new  KoPickTimeDlg(mmDeclareTimeActivity, this, cal, true);
            k.show();
        }

        @Override
        public void pickTime(Calendar cal) {
            if (endCal!=null && cal.after(endCal)){
                DialogUtils.showToast(MmDeclareTimeActivity.this, "收拉时间不能早于开拉时间");
            } else {
                startCal = Calendar.getInstance();
                startCal.setTimeInMillis(cal.getTimeInMillis());
                startPullTimeEdit.setText(StringUtils.formatDateTime(startCal));
            }
        }
    }
}
