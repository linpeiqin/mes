package cn.kol.pes.model.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.item.MeSearchSeqListItem;

public class MeSeqListAdapter extends BaseAdapter {
	
	private LayoutInflater mLi;
	private List<MeSearchSeqListItem> mProcessListData;
	
	public MeSeqListAdapter(Context context) {
		mLi = LayoutInflater.from(context);
	}
	
	public void setData(List<MeSearchSeqListItem> assetList) {
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
	public MeSearchSeqListItem getItem(int position) {
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
			convertView = mLi.inflate(R.layout.me_search_seq_list_item_layout, null);
		}
		
		MeSearchSeqListItem processData = getItem(position);
		
		if(processData != null) {
			TextView trxIdView = (TextView) convertView.findViewById(R.id.search_seq_item_trx_id);
			TextView opCodeView = (TextView) convertView.findViewById(R.id.search_seq_item_op_code);
			TextView trxQtyView = (TextView) convertView.findViewById(R.id.search_seq_item_trx_qty);
			TextView scrapQtyView = (TextView) convertView.findViewById(R.id.search_seq_item_scrap_qty);
			TextView updateDateView = (TextView) convertView.findViewById(R.id.search_seq_item_update_date);
			
			trxIdView.setText(processData.trxId);
			opCodeView.setText(processData.opCode);
			trxQtyView.setText(processData.trxQty);
			scrapQtyView.setText(processData.scrapQty);
			updateDateView.setText(processData.updateDate);
		}
		
		return convertView;
	}

}
