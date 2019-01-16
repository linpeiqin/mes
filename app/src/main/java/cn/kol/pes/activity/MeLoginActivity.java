/*-----------------------------------------------------------

-- PURPOSE

--    登陆界面，也是该应用启动的第一个界面，只提供了一个按钮点击功能

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.Constants;
import cn.kol.common.util.ContextSaveUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.DownLoadNewApkUtils;
import cn.kol.common.util.KoCommonUtil;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.KolApplication;
import cn.kol.pes.R;
import cn.kol.pes.activity.femaleworker.MmMainActivity;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.femaleworker.MmGroupItem;
import cn.kol.pes.model.item.femaleworker.MmOrgItem;
import cn.kol.pes.model.parser.adapter.KoUpdateApkAdapter;
import cn.kol.pes.model.parser.adapter.MeLoginAdapter;
import cn.kol.pes.model.parser.adapter.femaleworker.MmGetOrgInfoAdapter;
import cn.kol.pes.widget.KoListDialg;

public class MeLoginActivity extends BaseActivity implements OnClickListener,OnLongClickListener {
	
	private View mLoginBtn;//登录按钮
	private TextView mVersion;//显示版本号的控件
	private TextView mChoiceOrgText;//选择组织

	private EditText mUserName;
	private EditText mUserPassword;

    private RadioButton mEditionListRadio;
    private RadioButton mFinishedProductRadio;

	private MmGetOrgInfoAdapter mOrgData;
	public static void startAct(Context context) {
		Intent i = new Intent(context, MeLoginActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_login_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_app_name);
		
		findViewById(R.id.splash_settings_btn).setOnLongClickListener(this);
		
		mLoginBtn = findViewById(R.id.splash_login_btn);
		mLoginBtn.setOnClickListener(this);

		mVersion = (TextView) findViewById(R.id.splash_version_text);
		
		mUserName = (EditText) findViewById(R.id.login_user_name_edit);
		
		mUserPassword = (EditText) findViewById(R.id.login_user_password_edit);

		mChoiceOrgText = (TextView) findViewById(R.id.choice_org_text);
		if (CacheUtils.getMmGetOrgInfoAdapter() == null){
			mOrgData = new MmGetOrgInfoAdapter();
			mOrgData.organizationCode = "D01";
			mOrgData.organizationId = "85";
			mOrgData.organizationName = "NIPRC Dongguan COMMON";
			CacheUtils.setMmGetOrgInfoAdapter(mOrgData);
		}
		mChoiceOrgText.setText(CacheUtils.getMmGetOrgInfoAdapter().organizationName);
		mChoiceOrgText.setOnClickListener(this);

		findViewById(R.id.login_user_name_scan_view).setOnClickListener(this);
		
		LogUtils.e(tag, KoCommonUtil.getLanguage());
		
		mUserName.setText(ContextSaveUtils.getSharePreString("user_info", "staff_num"));
		if(Constants.IS_USE_TEST) {
			mUserPassword.setText(ContextSaveUtils.getSharePreString("user_info", "pwd"));
		}
		mEditionListRadio = (RadioButton) findViewById(R.id.main_edition_list_radio);
		mEditionListRadio.setChecked(true);
		mKoControl.getUpdateApk(String.valueOf(KolApplication.VERSION_CODE));//判断是否需要升级apk
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		String versionS = KoDataUtil.isUseTestUrl() ? "test-"+getText(R.string.ko_title_version_code)+KolApplication.VERSION : getText(R.string.ko_title_version_code)+KolApplication.VERSION;
		mVersion.setText(versionS);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_user_name_scan_view:
			KoCaptureActivity.startActForRes(this);
			break;
		
		case R.id.splash_login_btn:
			String userName = mUserName.getText().toString();
			String userPwd = mUserPassword.getText().toString();
			if(!KoDataUtil.isStringNotNull(userName)) {
				DialogUtils.showToast(this, "请输入工号");
			}
//			else if(!KoDataUtil.isStringNotNull(userPwd)) {
//				DialogUtils.showToast(this, "请输入密码");
//			}
			else {
				showLoadingDlg(R.string.ko_title_is_login);
				mKoControl.login(userName, userPwd);
			}
			break;
			
		case R.id.splash_settings_btn:
			SettingsActivity.startAct(this, SettingsActivity.class);
			break;
        case R.id.choice_org_text:
			showLoadingDlg();
            mKoControl.getOrgList();
            break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.splash_settings_btn:
			SettingsActivity.startAct(this, SettingsActivity.class);
			break;
		default:
			break;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {//扫码结果的回调
			String res = data.getStringExtra("res");
			LogUtils.e(tag, "onActivityResult()"+res);
			mUserName.setText(res);
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			@Override
			public void loginBack(boolean isSuc, MeLoginAdapter userData, String msg) {//网络请求登录的回调
				if(isSuc) {
					CacheUtils.setLoginUserInfo(userData);
					ContextSaveUtils.saveSharePre("user_info", "staff_num", mUserName.getText().toString());
					ContextSaveUtils.saveSharePre("user_info", "pwd", mUserPassword.getText().toString());
					if (!mEditionListRadio.isChecked()){
						MmMainActivity.startAct(MeLoginActivity.this);
					} else {
						MeMainActivity.startAct(MeLoginActivity.this);
					}
					MeLoginActivity.this.finish();
				}
				else if("2".equals(userData.getCode())) {
					KoSetPasswordActivity.startAct(MeLoginActivity.this, mUserName.getText().toString().trim());
				}
				else {
					DialogUtils.showToast(MeLoginActivity.this, msg);
				}
				dismissLoadingDlg();
			}
			@Override
			public void getOrgListBack(boolean isSuc, List<MmOrgItem> list, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					showOrgList(list);
				}
				else {
					DialogUtils.showToast(MeLoginActivity.this, msg);
				}
			}
			@Override
			public void getUpdateApkBack(boolean isSuc, KoUpdateApkAdapter updateData, String msg) {//apk升级的回调
				
				LogUtils.e(tag, "getUpdateApkBack:isSuc="+isSuc+updateData.isNeedUpdate());
				
				if(isSuc && updateData.isNeedUpdate()) {
					DownLoadNewApkUtils.getInstance(MeLoginActivity.this).showVersionUpdateUI(updateData);
				}
			}
		};
	}
	//2018 修改选择组织
	private void showOrgList(List<MmOrgItem> groupList) {
		OrgListDlg dlg = new OrgListDlg(this, null, groupList);
		dlg.show();
	}
	//2018修改女工显示组别
	public class OrgListDlg extends KoListDialg<MmOrgItem> {

		public OrgListDlg(Activity context, MmOrgItem selectedData, List<MmOrgItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(MmOrgItem selectedItem) {
			return selectedItem.organizationName;
		}

		@Override
		public boolean isSelectedObjEquals(MmOrgItem selectedData, MmOrgItem item) {
			if(selectedData!=null && item!=null && selectedData.equals(item)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(MmOrgItem selData) {
			mOrgData = new MmGetOrgInfoAdapter();
			mOrgData.organizationName = selData.organizationName;
			mOrgData.organizationCode = selData.organizationCode;
			mOrgData.organizationId = selData.organizationId;
			CacheUtils.setMmGetOrgInfoAdapter(mOrgData);
			mChoiceOrgText.setText(mOrgData.organizationName);
		}
	}
	@Override
	public void onBackPressed() {
		CacheUtils.clearAllCache();
		super.onBackPressed();
	}

}
