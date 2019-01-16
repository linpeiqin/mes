package cn.kol.pes.activity.femaleworker;

import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.pes.R;
import cn.kol.pes.activity.BaseActivity;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.femaleworker.MmAttendanceItem;
import cn.kol.pes.model.item.femaleworker.MmGetAttendancesBackItem;
import cn.kol.pes.model.item.femaleworker.MmGetAttendancesItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareMtlsBackItem;
import cn.kol.pes.model.parser.adapter.femaleworker.MmAttendanceFinishAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MmAttendanceActivity extends BaseActivity implements OnClickListener{


    private static final String KEY_DATE = "key_date";
    private static final String KEY_SHIFT = "key_shift";
    private static final String KEY_LOGIN_NAME = "key_loginName";
    private static final String KEY_GROUP = "key_group";

    private TextView attendanceDateText;
    private TextView attendanceGroupText;
    private TextView dayOrNightText;
    private TextView groupMasterText;
    private TextView attendanceListText;

    private EditText forecastWorkManEdit;
    private EditText factWorkManEdit;
    private EditText leaveQuantityEdit;
    private EditText absentQuantityEdit;
    private EditText yearOfRestDayEdit;
    private EditText turnOfRestDayEdit;
    private EditText autoAemissionQuantityEdit;
    private EditText fireQuantityEdit;
    private EditText outQuantityEdit;
    private EditText newInQuantityEdit;
    private EditText jobTimeEdit;
    private EditText jobOvertimeEdit;
    private EditText nonjobTimeEdit;
    private EditText nonjobOvertimeEdit;
    private EditText remarkEdit;

    public static void startAct(Context context, String date, String shift, String loginName, String group) {

        Intent i = new Intent(context, MmAttendanceActivity.class);
        i.putExtra(KEY_DATE, date);
        i.putExtra(KEY_SHIFT, shift);
        i.putExtra(KEY_LOGIN_NAME, loginName);
        i.putExtra(KEY_GROUP, group);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.mm_handwork_attendance);//此行代码必须写在super.onCreate(savedInstanceState)前面，
        super.onCreate(savedInstanceState);
        mTitleView.setTitle("出勤状况");

        attendanceDateText = (TextView) findViewById(R.id.text_attendance_date);
        attendanceGroupText = (TextView) findViewById(R.id.text_attendance_group);
        dayOrNightText = (TextView) findViewById(R.id.text_day_or_night);
        groupMasterText = (TextView) findViewById(R.id.text_group_master);

        attendanceListText = (TextView) findViewById(R.id.text_attendance_list);

        //初始化数据
        attendanceDateText.setText(getIntent().getStringExtra(KEY_DATE));
        attendanceGroupText.setText(getIntent().getStringExtra(KEY_GROUP));
        dayOrNightText.setText(getIntent().getStringExtra(KEY_SHIFT).equals("DAY")?"白班":"晚班");
        groupMasterText.setText(getIntent().getStringExtra(KEY_LOGIN_NAME));
        //文本数据
        forecastWorkManEdit = (EditText) findViewById(R.id.edit_forecast_work_man);
        factWorkManEdit = (EditText) findViewById(R.id.edit_fact_work_man);
        leaveQuantityEdit = (EditText) findViewById(R.id.edit_leave_quantity);
        absentQuantityEdit = (EditText) findViewById(R.id.edit_absent_quantity);
        yearOfRestDayEdit = (EditText) findViewById(R.id.edit_year_of_rest_day);
        turnOfRestDayEdit = (EditText) findViewById(R.id.edit_turn_of_rest_day);
        autoAemissionQuantityEdit = (EditText) findViewById(R.id.edit_auto_demission_quantity);
        fireQuantityEdit = (EditText) findViewById(R.id.edit_fire_quantity);
        outQuantityEdit = (EditText) findViewById(R.id.edit_out_quantity);
        newInQuantityEdit = (EditText) findViewById(R.id.edit_new_in_quantity);
        jobTimeEdit = (EditText) findViewById(R.id.edit_job_time);
        jobOvertimeEdit = (EditText) findViewById(R.id.edit_job_overtime);
        nonjobTimeEdit = (EditText) findViewById(R.id.edit_nonjob_time);
        nonjobOvertimeEdit = (EditText) findViewById(R.id.edit_nonjob_overtime);
        remarkEdit = (EditText) findViewById(R.id.edit_remark);
       findViewById(R.id.attendance_finish_btn).setOnClickListener(this);

        MmGetAttendancesItem mmGetAttendancesItem = new MmGetAttendancesItem();
        mmGetAttendancesItem.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
        mmGetAttendancesItem.scheduleDate = getIntent().getStringExtra(KEY_DATE);
        mmGetAttendancesItem.workClassCode = getIntent().getStringExtra(KEY_SHIFT);
        mmGetAttendancesItem.workGroup = getIntent().getStringExtra(KEY_GROUP);
        mmGetAttendancesItem.workMonitor = getIntent().getStringExtra(KEY_LOGIN_NAME);
        mKoControl.getAttendanceList4F(mmGetAttendancesItem);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.attendance_finish_btn:
                MmAttendanceItem item = new MmAttendanceItem();
                this.setTextToItem(item);
                showLoadingDlg();
                mKoControl.attendanceFinish4F(item);
                break;
            default:
                break;
        }
    }

    private void setTextToItem(MmAttendanceItem item){
        item.organizationId = CacheUtils.getMmGetOrgInfoAdapter().organizationId;
        item.scheduleDate = attendanceDateText.getText().toString();
        item.workGroup =attendanceGroupText.getText().toString();
        item.workClassCode = getIntent().getStringExtra(KEY_SHIFT);
        item.workMonitor = groupMasterText.getText().toString();
        item.forecastWorkMan = forecastWorkManEdit.getText().toString();
        item.factWorkMan = factWorkManEdit.getText().toString();
        item.leaveQuantity = leaveQuantityEdit.getText().toString();
        item.absentQuantity = absentQuantityEdit.getText().toString();
        item.yearOfRestDay = yearOfRestDayEdit.getText().toString();
        item.turnOfRestDay = turnOfRestDayEdit.getText().toString();
        item.autoAemissionQuantity = autoAemissionQuantityEdit.getText().toString();
        item.fireQuantity = fireQuantityEdit.getText().toString();
        item.outQuantity = outQuantityEdit.getText().toString();
        item.newInQuantity = newInQuantityEdit.getText().toString();
        item.jobTime = jobTimeEdit.getText().toString();
        item.jobOvertime = jobOvertimeEdit.getText().toString();
        item.nonjobTime =nonjobTimeEdit.getText().toString();
        item.nonjobOvertime = nonjobOvertimeEdit.getText().toString();
        item.remark = remarkEdit.getText().toString();
    }
    @Override
    protected KolPesControlBack initControlBack() {
        return new KolPesControlBack() {
            @Override
            public void getAttendanceFinishBack(boolean isSuc, MmAttendanceFinishAdapter data, String msg) {
                if (isSuc) {
                    DialogUtils.showToast(MmAttendanceActivity.this, "提交成功");
                    MmAttendanceActivity.this.finish();
                } else {
                    DialogUtils.showToast(MmAttendanceActivity.this, "提交失败:"+msg);
                }
            }
            @Override
            public void getAttendanceListBack(boolean isSuc, List<MmGetAttendancesBackItem> list, String msg) {
                dismissLoadingDlg();
                if(isSuc) {//如果请求成功，则显示相关数据
                    showAttendanceList(list);
                }
                else {//如果失败，清除相关的显示和数据
                    DialogUtils.showToast(MmAttendanceActivity.this, msg);
                }
            }
        };
    }
    private void showAttendanceList(List<MmGetAttendancesBackItem> list) {
        String msgList = "";
        int index = 1;
        for (MmGetAttendancesBackItem m : list){
            msgList += index+ "." + m.showMsg();
            index ++;
        }
        attendanceListText.setText(Html.fromHtml(msgList));
    }

}
