/*-----------------------------------------------------------

-- PURPOSE

--    登陆界面，也是该应用启动的第一个界面，只提供了一个按钮点击功能

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;

public class KoSetPasswordActivity extends BaseActivity implements OnClickListener {
	
	
	private EditText mUserPasswordRepeat;
	private EditText mUserPassword;
	
	private static final String KEY_USER_ID = "key_user_id";
	
	public static void startAct(Context context, String userId) {
		Intent i = new Intent(context, KoSetPasswordActivity.class);
		i.putExtra(KEY_USER_ID, userId);
		context.startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_set_password_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_set_password_title);

		mUserPasswordRepeat = (EditText) findViewById(R.id.set_user_repeat_password_edit);
		
		mUserPassword = (EditText) findViewById(R.id.set_user_password_edit);
		
		findViewById(R.id.set_user_password_btn).setOnClickListener(this);

	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_user_password_btn:
			String pwd = mUserPassword.getText().toString();
			String pwdRepeat = mUserPasswordRepeat.getText().toString();
			if(pwd.length() == 0) {
				DialogUtils.showToast(this, "请输入密码");
			}
			else if(pwdRepeat.length() == 0) {
				DialogUtils.showToast(this, "请输入确认密码");
			}
			else if(!pwd.equals(pwdRepeat)) {
				DialogUtils.showToast(this, "两次输入的密码不一致");
			}
			else {
				showLoadingDlg();
				mKoControl.setPassword(getIntent().getStringExtra(KEY_USER_ID), pwd);
			}
			break;

		default:
			break;
		}
	}
	

	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			@Override
			public void setPasswordBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					DialogUtils.showToast(KoSetPasswordActivity.this, "设置密码成功");
					KoSetPasswordActivity.this.finish();
				}
				else {
					DialogUtils.showToast(KoSetPasswordActivity.this, msg);
				}
			}
		};
	}

	@Override
	public void onBackPressed() {
		CacheUtils.clearAllCache();
		super.onBackPressed();
	}

}
