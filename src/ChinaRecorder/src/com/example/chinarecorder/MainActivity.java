package com.example.chinarecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	public static String basePath = "/sdcard/chinarecord";
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

    /**
     * 侧边栏列表选择事件
     * */
	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			Toast.makeText(this, mTitle, Toast.LENGTH_SHORT)
			.show();
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			Toast.makeText(this, mTitle, Toast.LENGTH_SHORT)
			.show();
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			Toast.makeText(this, mTitle, Toast.LENGTH_SHORT)
			.show();
			break;
		}
	}
	/**
	 * 主界面标题显示改变
	 * */
	public void restoreActionBar() {//主界面标题显示改变
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
    /**
     * 跳转录音文件列表
     * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//侧边栏action bar 事件（主要（当控件id相同时以此为主））
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_fileList) {//跳转录音文件列表按钮事件2
//			Toast.makeText(this, "file List2.", Toast.LENGTH_SHORT)
//			.show();
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, RecordListActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		private String fileName;
		private Button button_start;
		private Button button_stop;
		private MediaRecorder recorder;
		private Chronometer  chronometer;
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
//			getWindow().setFormat(PixelFormat.TRANSLUCENT);// 让界面横屏
//			requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉界面标题
			getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			// 重新设置界面大小
			init(rootView);
			
			return rootView;
		}
		
		private void init(View v) {
			
			chronometer =(Chronometer)v.findViewById(R.id.chronometer);
			button_start = (Button)v.findViewById(R.id.start);
			button_stop = (Button)v.findViewById(R.id.stop);
			button_stop.setOnClickListener(new AudioListerner());
			button_start.setOnClickListener(new AudioListerner());
		}

		class AudioListerner implements OnClickListener {
			@Override
			public void onClick(View v) {
				if (v == button_start) {
					initializeAudio();
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometer.start();
				}
				if (v == button_stop) {
//					System.out.println("date:"+getDateYYMMDD());
					
					recorder.stop();// 停止刻录
//					chronometer.stop();
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometer.stop();
					
					// recorder.reset(); // 重新启动MediaRecorder.
					recorder.release(); // 刻录完成一定要释放资源
					// recorder = null;
				}
			}

			private void initializeAudio() {
				recorder = new MediaRecorder();// new出MediaRecorder对象
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置MediaRecorder的音频源为麦克风
				recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
				// 设置MediaRecorder录制的音频格式
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				// 设置MediaRecorder录制音频的编码为amr.
//				recorder.setOutputFile("/sdcard/peipei.amr");
				isExist(basePath);
				setFileName();
				recorder.setOutputFile(basePath+"/"+fileName+".amr");
				// 设置录制好的音频文件保存路径
				try {
					recorder.prepare();// 准备录制
					recorder.start();// 开始录制
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			private void setFileName() {
				String date = getDateYYMMDD();
				String newName;
				if(true){
					String old = "20141025_011";
					 newName = date+"_"+String.format("%1$,03d", Integer.valueOf(old.substring(9))+1);
//					System.out.println("name:"+newName);
				}else {
					newName = date+"_001";
				}
				fileName = newName;
			}
		}
		
		public static void isExist(String path) {
			File file = new File(path);
			//判断文件夹是否存在,如果不存在则创建文件夹
			if (!file.exists()) {
			file.mkdir();
			}
		}
		
		public String getDateYYMMDD() {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");  
			String date=sdf.format(new Date());
			return date;
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
