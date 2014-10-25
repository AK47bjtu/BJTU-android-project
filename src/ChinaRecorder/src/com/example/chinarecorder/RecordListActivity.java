package com.example.chinarecorder;

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
	implements RecordListFragment.OnRecordListEditListener{

	public static int BACKTOLIST = 0;
	public static int GOTOSHARE = 1;
	public static int GOTODELETE = 2;
	
	private CharSequence rTitle;
	
	@Override
	public void onEditSelected(int position) {
		onSectionAttached(position);
		RecordListEditFragment recordListEditFragment = new RecordListEditFragment();
		Bundle args = new Bundle();
		args.putInt(RecordListEditFragment.ARG_EDIT, position);
		recordListEditFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, recordListEditFragment);
	    transaction.addToBackStack(null); 
		transaction.commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new RecordListFragment()).commit();
		}
		rTitle = getTitle();
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
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
