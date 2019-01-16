package cn.kol.pes.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.pes.R;

public class MeReceiveOrderDialog extends KoCommomDialogFatherClass implements android.view.View.OnClickListener {
	
	public interface IKoReceiveOrderDialog {
		
		public void receivceDlgScanId();
		public void receivceDlgGetQtyById(String id);
		public void receivceDlgSureReceive(String id, String qty);
		public void receivceDlgSureReject(String id);
	}
	
	private EditText mReceiveIdEdit;
	private EditText mReceiveQtyEdit;
	
	private IKoReceiveOrderDialog mCallback;

	public MeReceiveOrderDialog(Activity context, IKoReceiveOrderDialog callback) {
		super(context);
		
		mCallback = callback;
		
		LayoutInflater li = LayoutInflater.from(context);
        ViewGroup parentView = (ViewGroup) li.inflate(R.layout.me_receive_order_dialog_layout, null);
        
        mReceiveIdEdit = (EditText)parentView.findViewById(R.id.receive_order_id_edit);
        mReceiveQtyEdit = (EditText)parentView.findViewById(R.id.receive_order_qty_edit);
        
        
        parentView.findViewById(R.id.receive_order_id_search_qty_btn).setOnClickListener(this);
        parentView.findViewById(R.id.receive_order_id_scan_btn).setOnClickListener(this);
        
        parentView.findViewById(R.id.receive_order_sure_to_receive_btn).setOnClickListener(this);
        parentView.findViewById(R.id.receive_order_sure_to_reject_btn).setOnClickListener(this);
        parentView.findViewById(R.id.receive_order_sure_to_back_btn).setOnClickListener(this);

        this.setContentView(parentView);
	}
	
	public void setValueId(String id) {
		mReceiveIdEdit.setText(id);
	}
	
	public void setValueQty(String qty) {
		if(qty!=null && qty.length()>0 && KoDataUtil.isValidNumber(qty)) {
			mReceiveQtyEdit.setText(qty);
		}
		else {
			DialogUtils.showToast(getContext(), "请输入数字");
		}
	}

	@Override
	public void onClick(View v) {
		String id = mReceiveIdEdit.getText().toString();
		String qty = mReceiveQtyEdit.getText().toString();
		
		switch(v.getId()) {
		
		case R.id.receive_order_id_scan_btn:
			mCallback.receivceDlgScanId();
			break;
			
		case R.id.receive_order_id_search_qty_btn:
			if(!KoDataUtil.isStringNotNull(id)) {
				DialogUtils.showToast(getContext(), "请输入ID");
			}
			else {
				mCallback.receivceDlgGetQtyById(id);
			}
			break;
		
		case R.id.receive_order_sure_to_receive_btn:
			if(!KoDataUtil.isStringNotNull(id)) {
				DialogUtils.showToast(getContext(), "请输入ID");
			}
			else if(!KoDataUtil.isStringNotNull(qty)) {
				DialogUtils.showToast(getContext(), "请输入数量");
			}
			else {
				mCallback.receivceDlgSureReceive(id, qty);
			}
			break;
			case R.id.receive_order_sure_to_back_btn:
				if(!KoDataUtil.isStringNotNull(id)) {
					DialogUtils.showToast(getContext(), "请输入ID");
				}
				else if(!KoDataUtil.isStringNotNull(qty)) {
					DialogUtils.showToast(getContext(), "请输入数量");
				}
				else {
					String qtyStr = String.valueOf(-1*Long.valueOf(qty));
					mCallback.receivceDlgSureReceive(id, qtyStr);
				}
				break;

		case R.id.receive_order_sure_to_reject_btn:
			if(!KoDataUtil.isStringNotNull(id)) {
				DialogUtils.showToast(getContext(), "请输入ID");
			}
			else {
				mCallback.receivceDlgSureReject(id);
			}
			break;
		}
	}

}
