package com.example.chinarecorder;

import com.example.util.FileDataUtil;

import android.speech.RecognitionListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class RecordListActivity extends ActionBarActivity 
	implements RecordListFragment.OnRecordListEditListener
	,RecordListFragment.OnRecordDitalListener{

	public static int BACKTOLIST = 0;
	public static int GOTOSHARE = 1;
	public static int GOTODELETE = 2;
	
	private CharSequence rTitle;
	FileDataUtil fileDataUtil;
	
	@Override
	public void onEditSelected(int position) {
		onSectionAttached(position);
		RecordListEditFragment recordListEditFragment = new RecordListEditFragment();
		Bundle args = new Bundle();
		args.putInt(RecordListEditFragment.ARG_EDIT, position);
		recordListEditFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out);
		transaction.replace(R.id.listcontainer, recordListEditFragment);
	    transaction.addToBackStack(null); 
		transaction.commit();
	}
	
	@Override
	public void onDetail(int position) {
		rTitle = getString(R.string.detail);
		restoreActionBar();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out,R.anim.push_right_in,R.anim.push_right_out);
		RecordDetailFragment recordDetailFragment = new RecordDetailFragment();
		Bundle args = new Bundle();
		System.out.println("id:"+position);
        args.putInt(RecordListFragment.ARG_POSITION, position);
		recordDetailFragment.setArguments(args);
		transaction.replace(R.id.listcontainer, recordDetailFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
		fileDataUtil = new FileDataUtil(this);
		fileDataUtil.scanDirAsync3(this, fileDataUtil.basePath+"/"+fileDataUtil.recordDir);
		fileDataUtil.test();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.listcontainer, new RecordListFragment()).commit();
		}
		rTitle = getTitle();
		System.out.println(rTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.record_list, menu);
//		restoreActionBar();
		return true;
	}
	private void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);//是否显示标题（是/否）
		actionBar.setTitle(rTitle);
	}
	
	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			rTitle = getString(R.string.share);
			break;
		case 2:
			rTitle = getString(R.string.delete);
			break;
		case 0:
			rTitle = getString(R.string.title_record);
		break;
		}
		restoreActionBar();
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		int id = item.getItemId();
		
		if (id == android.R.id.home) {
			if(rTitle.equals(getString(R.string.detail))||
					rTitle.equals(getString(R.string.delete))||
					rTitle.equals(getString(R.string.share))){
				getSupportFragmentManager().popBackStack();
				rTitle = getString(R.string.title_record);
				restoreActionBar();
				return true;
			}else if(rTitle.equals(getString(R.string.title_record))){
				 this.finish();  //finish当前activity  
		         overridePendingTransition(R.anim.push_right_in,  
		                    R.anim.push_right_out);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	

	
	
//	public void myGetItem2(int id) {
//		Toast.makeText(this, "myitem", Toast.LENGTH_SHORT)
//		.show();
//	}

//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_record_list,
//					container, false);
//			return rootView;
//		}
//	}
}
