package cn.kol.pes.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.pes.R;

public class MeAddNewReasonDialog extends KoCommomDialogFatherClass implements android.view.View.OnClickListener {
	
	public interface IMeAddNewReasonDialog {
		
		public void addedReason(String reason);
	}
	
	private EditText mReceiveIdEdit;
	
	private IMeAddNewReasonDialog mCallback;

	public MeAddNewReasonDialog(Activity context, IMeAddNewReasonDialog callback) {
		super(context);
		
		mCallback = callback;
		
		LayoutInflater li = LayoutInflater.from(context);
        ViewGroup parentView = (ViewGroup) li.inflate(R.layout.me_add_new_reason_dialog_layout, null);
        
        mReceiveIdEdit = (EditText)parentView.findViewById(R.id.added_new_reason_edit);
 
        parentView.findViewById(R.id.added_new_reason_sure_btn).setOnClickListener(this);
        parentView.findViewById(R.id.added_new_reason_cancel_btn).setOnClickListener(this);
        
        this.setContentView(parentView);
	}

	@Override
	public void onClick(View v) {
		String id = mReceiveIdEdit.getText().toString();
		
		switch(v.getId()) {

		case R.id.added_new_reason_sure_btn:
			if(!KoDataUtil.isStringNotNull(id)) {
				DialogUtils.showToast(getContext(), "请输入新增原因");
			}
			else {
				mCallback.addedReason(id);
				this.dismiss();
			}
			break;
			
		case R.id.added_new_reason_cancel_btn:
			this.dismiss();
			break;
		}
	}

}
