package cn.kol.pes.model.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.item.MeMainProcessItem;

public class MeProcessListAdapter extends BaseAdapter {
	
	private LayoutInflater mLi;
	private List<MeMainProcessItem> mProcessListData;
	
	public MeProcessListAdapter(Context context) {
		mLi = LayoutInflater.from(context);
	}
	
	public void setData(List<MeMainProcessItem> assetList) {
		mProcessListData = assetList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mProcessListData != null) {
			return mProcessListData.size();
		}
		return 0;
	}

	@Override
	public MeMainProcessItem getItem(int position) {
		if(mProcessListData != null) {
			return mProcessListData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mLi.inflate(R.layout.me_process_list_item_layout, null);
		}
		
		MeMainProcessItem processData = getItem(position);
		
		if(processData != null) {
			TextView jobIdView = (TextView) convertView.findViewById(R.id.process_item_job_id);
			TextView productIdView = (TextView) convertView.findViewById(R.id.process_item_product_id);
			TextView scheduleDateView = (TextView) convertView.findViewById(R.id.process_item_schedule_date);
			TextView jobDescView = (TextView) convertView.findViewById(R.id.process_item_job_desc);
			TextView primaryUomCodeView = (TextView) convertView.findViewById(R.id.process_item_primary_uom_code);


			TextView projectNumView = (TextView) convertView.findViewById(R.id.process_item_project_num);
			TextView processNumView = (TextView) convertView.findViewById(R.id.process_item_process_num);
			TextView publishCodeView = (TextView) convertView.findViewById(R.id.process_item_publish_code);
			TextView seqNumView = (TextView) convertView.findViewById(R.id.process_item_view_seq_num);
			TextView productCodeView = (TextView) convertView.findViewById(R.id.process_item_product_code);
			TextView productDescView = (TextView) convertView.findViewById(R.id.process_item_product_desc);
			TextView seqDescView = (TextView) convertView.findViewById(R.id.process_item_seq_desc);
			TextView planNumView = (TextView) convertView.findViewById(R.id.process_item_plan_num);
			TextView needTimeView = (TextView) convertView.findViewById(R.id.process_item_need_time);
			TextView reportedQty = (TextView) convertView.findViewById(R.id.process_item_reported_qty);

			jobIdView.setText(processData.jobId);
			productIdView.setText(processData.productId);
			scheduleDateView.setText(processData.scheduleDate);
			jobDescView.setText(processData.jobDesc);
			primaryUomCodeView.setText(processData.primaryUomCode);

			projectNumView.setText(processData.projectNum);
			processNumView.setText(processData.processNum);
			publishCodeView.setText(processData.publishCode);
			seqNumView.setText(processData.seqNum);
			productCodeView.setText(processData.productCode);
			productDescView.setText(processData.productDesc);
			seqDescView.setText(processData.seqDesc);
			planNumView.setText(processData.planNum);
			needTimeView.setText(processData.timeNeeded);
			reportedQty.setText(processData.reportedQty);
		}
		
		return convertView;
	}

}
