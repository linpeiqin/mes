package cn.kol.pes.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import cn.kol.common.util.DialogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.MeSeqListAdapter;
import cn.kol.pes.model.item.MeSearchSeqListItem;

public class MeSeqListActivity extends BaseActivity implements OnClickListener {


	private static final String KEY_WIP_ID = "key_wip_id";
	
	private ListView mSeqListView;
	private MeSeqListAdapter mListAdapter;
	
	public static void startAct(Context context, String wipId) {
		
		Intent i = new Intent(context, MeSeqListActivity.class);
		i.putExtra(KEY_WIP_ID, wipId);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.me_seq_list_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle("工序列表");
		
		mSeqListView = (ListView) findViewById(R.id.seq_list_list_view);
		mListAdapter = new MeSeqListAdapter(this);
		mSeqListView.setAdapter(mListAdapter);

		showLoadingDlg();
		mKoControl.searchSeqList(getIntent().getStringExtra(KEY_WIP_ID));
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		
			
		default:
			break;
		}
	}
	
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {

			public void searchSeqListBack(boolean isSuc, List<MeSearchSeqListItem> seqList, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mListAdapter.setData(seqList);
				}
				else {
					DialogUtils.showToast(MeSeqListActivity.this, msg);
				}
			}
		};
	}

}
