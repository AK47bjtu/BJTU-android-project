package com.example.chinarecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.util.PreferenceNameHelp;





import android.R.bool;
import android.app.Activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
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
	//定义SharedPreferences对象  
    private static SharedPreferences sp;
    private String  userName;
    private boolean loginStatus;
	
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
        userName = sp.getString(PreferenceNameHelp.USER_NAME, null);
		loginStatus = sp.getBoolean(PreferenceNameHelp.LOGIN_STATUS, true);
		
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
//		String mtitle[] = new String[3];
//		mtitle[0] = getString(R.string.title_section1);
//		mtitle[1] = getString(R.string.title_section2);
//		mtitle[2] = getString(R.string.title_section3);
//		
//		Toast.makeText(this, mtitle[number - 1], Toast.LENGTH_SHORT)
//		.show();
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
		if (loginStatus) {
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(userName);
		}else {
			actionBar.setDisplayShowTitleEnabled(false);
		}
		
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
//			intent.putExtra(PreferenceNameHelp.LOGIN_STATUS, loginStatus);
//			intent.putExtra(PreferenceNameHelp.USER_NAME, userName);
			intent.setClass(MainActivity.this, RecordListActivity.class);
			startActivity(intent);
//			overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
//			ActivityOptions opts = ActivityOptions.makeCustomAnimation(MainActivity.this, R.anim.fade, R.anim.hold);
//			startActivity(intent, opts.toBundle());
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
		/**是否暂停标志位**/  
	    private static boolean isPause;  
	      
	    /**在暂停状态中**/  
	    private static boolean inThePause;
	    /**是否停止录音**/  
	    private static boolean isStopRecord;
	    /**是否开始**/
	    private static boolean isStarted;
	    
	    /**记录需要合成的几段amr语音文件**/  
	    private static ArrayList<String> list; 
	    
	    /**临时文件计数**/
		private static  int tempNO;
		private static final String ARG_SECTION_NUMBER = "section_number";
		 
	    //定义Preferences 文件中的键  
	    public final String LATEST_DATE_KEY = "LATEST_DATE";
	    public final String LATEST_NO_KEY = "LATEST_DATE_KEY";
	    
		
		
//		private String fileName;
//		private Button button_start;
		private Button button_stop;
		private MediaRecorder recorder;
		private Chronometer  chronometer;
		private Button buttonpause;
		
		/**记录暂停时的时刻**/ 
		private static long tempClock = 0;
		private static long tempStartClock = 0;
		private static long tempRestartClock = 0;
						
		private static File myRecAudioFile;
		private boolean xx=true;  
		private boolean sigle = false;  
	    //定义Preferences 文件中的键  
	
//		private ListPreference listPreferenceFormat;
		private String forMat;
		
		private static String recordFileName;
		private static String tempFileName;
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
//			getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			// 重新设置界面大小
			init(rootView);
			button_stop.setEnabled(false);
			return rootView;
		}
		
		private void init(View v) {
			
			 //暂停标志位 为false  
	        isPause=false;  
	        //暂停状态标志位  
	        inThePause=false; 
			//初始化list  
	        list=new ArrayList<String>(); 
	        //
	        isStarted = false;
	        
	        
			chronometer =(Chronometer)v.findViewById(R.id.chronometer);
//			button_start = (Button)v.findViewById(R.id.start);
			button_stop = (Button)v.findViewById(R.id.stop);
			button_stop.setOnClickListener(new AudioListerner());
//			button_start.setOnClickListener(new AudioListerner());
			buttonpause = (Button)v.findViewById(R.id.buttonpause);
			buttonpause.setOnClickListener(new  AudioListerner()); 
		}

		class AudioListerner implements OnClickListener {
			@Override
			public void onClick(View v) {
//				if (v == button_start) {
//					tempNO = 0;
//					tempClock = 0;
//	                tempStartClock = 0;
//	                tempRestartClock = 0;
//					initializeAudio();
//					chronometer.setBase(SystemClock.elapsedRealtime());
//					chronometer.start();
//					tempStartClock = chronometer.getBase();
////					System.out.println("StartClock:"+tempStartClock/1000);
//					buttonpause.setEnabled(true);
//					button_stop.setEnabled(true);
//					button_start.setEnabled(false);
//					isStopRecord = false;
//					
//				}
				if (v == buttonpause) {
	            	if(!isStarted){
	            		tempNO = 0;
						tempClock = 0;
		                tempStartClock = 0;
		                tempRestartClock = 0;
						initializeAudio();
						chronometer.setBase(SystemClock.elapsedRealtime());
						chronometer.start();
						tempStartClock = chronometer.getBase();
//						System.out.println("StartClock:"+tempStartClock/1000);
						buttonpause.setEnabled(true);
						buttonpause.setText("暂停录音"); 
						button_stop.setEnabled(true);
//						button_start.setEnabled(false);
						isStopRecord = false;
						isStarted = true;
	            	}else{
	            		//已经暂停过了，再次点击按钮 开始录音，录音状态在录音中  
			            if(inThePause){ 
			            	chronometer.setBase(SystemClock.elapsedRealtime()-tempClock);
			            	tempRestartClock = SystemClock.elapsedRealtime()-tempStartClock;
//			            	System.out.println("SystemClockjx:"+SystemClock.elapsedRealtime()/1000);
			            	chronometer.start();
			                buttonpause.setText("暂停录音");
			                button_stop.setEnabled(true);
			                initializeAudio(); 
			                inThePause=false;  
			                  
			                  
			            }  
			            //正在录音，点击暂停,现在录音状态为暂停  
			            else{
			            	tempClock = SystemClock.elapsedRealtime()-tempStartClock-(tempRestartClock-tempClock);
//			            	System.out.println("SystemClockzt:"+SystemClock.elapsedRealtime()/1000);
//			            	System.out.println("tempClock:"+tempClock/1000);
			            	chronometer.stop();
			                //当前正在录音的文件名，全程  
			                list.add(myRecAudioFile.getPath());
			                recodeStop(); 
			                inThePause=true; 
			                //start();  
			                buttonpause.setText("继续录音");
			                button_stop.setEnabled(true);
			                  
			                //计时停止  
			//                timer.cancel();  
			            }
			            isPause=true;
	            	} 
	            	
				}else if (v == button_stop) {
					
					setFileName();
					xx=false;  
	                sigle = true;  
	                
	                // TODO Auto-generated method stub  
	                chronometer.setBase(SystemClock.elapsedRealtime());
	    			chronometer.stop();
	    			
//	    			button_start.setEnabled(true);
	    			button_stop.setEnabled(false);
//	    			buttonpause.setEnabled(); 
	                  
	                //这里写暂停处理的 文件！加上list里面 语音合成起来  
	                if(isPause){  
	                      
	                    //在暂停状态按下结束键,处理list就可以了  
	                    if(inThePause){  
	                        getInputCollection(list, false);  
	                    }  
	                    //在正在录音时，处理list里面的和正在录音的语音  
	                    else{  
	                        list.add(myRecAudioFile.getPath());  
	                        recodeStop();  
	                        getInputCollection(list, true);  
	                    }  
	                      
	                    //还原标志位  
	                    isPause=false;  
	                    inThePause=false;  
	                    buttonpause.setText("开始录音");  
	                      
	                  
	                      
	                      
	                //  adapter.add(myRecAudioFile.getName());  
	                      
	                }else{  //若录音没有经过任何暂停 
	                      
	                  
	                    if (myRecAudioFile != null) {  
		                    // 停止录音  
		                    	recorder.stop();  
		                    	recorder.release();  
		                    	recorder = null;  
		                    // 将录音频文件给Adapter  
//		                    adapter.add(myRecAudioFile.getName());  
//		                    DecimalFormat df = new DecimalFormat("#.000");  
//		                    if (myRecAudioFile.length() <= 1024*1024) {  
//		                        //length1 = (myRecAudioFile.length() / 1024.0)+"";  
//		                          
//		                          length1=df.format(myRecAudioFile.length() / 1024.0)+"K";  
//		                    } else {  
//		                        //length1 = (myRecAudioFile.length() / 1024.0 / 1024)+"";  
//		                        //DecimalFormat df = new DecimalFormat("#.000");  
//		                          length1=df.format(myRecAudioFile.length() / 1024.0 / 1024)+"M";  
//		                    }  
//	                        System.out.println(length1);  
//	                        
//	                        myTextView1.setText("停  止" + myRecAudioFile.getName()  
//	                            + "文件大小为：" + length1); 
	                        
	                        list.add(myRecAudioFile.getPath());
	                        getInputCollection(list, true);
	                        
	                        
	                    }  
	                  
	                }  
	 
	                //停止录音了  
	                isStopRecord = true;
	                tempNO  = 0;
	                list=new ArrayList<String>();
	                saveLatestData(recordFileName);
	                recordFileName = "";
	                tempFileName = "";
	                buttonpause.setText("开始录音");
	                isStarted = false;
	                
	            }
			}
			
			 protected void recodeStop(){  
			        if (recorder != null && !isStopRecord) {  
			            // 停止录音  
			        	recorder.stop();  
			        	recorder.release();  
			        	recorder = null;  
			        }  
			}  
			private void initializeAudio() {
				
				sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
	            forMat = sp.getString(PreferenceNameHelp.FOMAT_SETTING_KEY, null);
				recorder = new MediaRecorder();// new出MediaRecorder对象
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置MediaRecorder的音频源为麦克风
				if(forMat.equals("amr")){
					recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
					// 设置MediaRecorder录制的音频格式
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					// 设置MediaRecorder录制音频的编码为amr.
				}else if(forMat.equals("3gpp")){
					recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					// 设置MediaRecorder录制的音频格式
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
					// 设置MediaRecorder录制音频的编码为amr.
				}
				
//				recorder.setOutputFile("/sdcard/peipei.amr");
				isExist(basePath);
				setTempFileName();
				myRecAudioFile=new File(basePath+"/"+tempFileName+"."+forMat); 
				recorder.setOutputFile(myRecAudioFile  
	                    .getAbsolutePath());
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
			/**
			 * 合并录音文件
			 * */
			public  void getInputCollection(List list,boolean isAddLastRecord){  
			    
			    
			    String  mMinute1=getTime();  
			    Toast.makeText(getActivity(), "当前时间是:"+mMinute1,Toast.LENGTH_LONG).show();  
			    int head = 0;
			    // 创建音频文件,合并的文件放这里  
			    File file1=new File(basePath+"/"+recordFileName+"."+forMat);  
			    FileOutputStream fileOutputStream = null; 
			    if(forMat.equals("amr")){
			    	head = 6;
			    }else if (forMat.equals("3gpp")) {
			    	head = 135;
				}
			       
			    if(!file1.exists()){  
			        try {  
			            file1.createNewFile();  
			        } catch (IOException e) {  
			            // TODO Auto-generated catch block  
			            e.printStackTrace();  
			        }  
			    }  
			    try {  
			        fileOutputStream=new FileOutputStream(file1);  

			    } catch (IOException e) {  
			        // TODO Auto-generated catch block  
			        e.printStackTrace();  
			    }  
			    //list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件  
			      
			      
			      
			  
			    for(int i=0;i<list.size();i++){  
			        File file=new File((String) list.get(i));  
			    Log.d("list的长度", list.size()+"");  
			        try {  
			            FileInputStream fileInputStream=new FileInputStream(file);  
			            byte  []myByte=new byte[fileInputStream.available()];  
			            //文件长度  
			            int length = myByte.length;  
			      
			            //头文件  
			            if(i==0){  
			                    while(fileInputStream.read(myByte)!=-1){  
			                            fileOutputStream.write(myByte, 0,length);  
			                        }  
			                    }  
			                  
			            //之后的文件，去掉头文件就可以了  
			            else{  
			                while(fileInputStream.read(myByte)!=-1){  
			                      
			                    fileOutputStream.write(myByte, head, length-head);  
			                }  
			            }  
			              
			            fileOutputStream.flush();  
			            fileInputStream.close();  
			            System.out.println("合成文件长度："+file1.length());  
			          
			        } catch (Exception e) {  
			            // TODO Auto-generated catch block  
			            e.printStackTrace();  
			        }  
			          
			          
			          
			        }  
			    //结束后关闭流  
			    try {  
			        fileOutputStream.close();  
			    } catch (IOException e) {  
			        // TODO Auto-generated catch block  
			        e.printStackTrace();  
			    }  

		        //合成一个文件后，删除之前暂停录音所保存的零碎合成文件  
		        deleteListRecord(isAddLastRecord);  
		        //  
//			        adapter.add(file1.getName());  
			  
			}
			private void deleteListRecord(boolean isAddLastRecord){  
			    for(int i=0;i<list.size();i++){  
			        File file=new File((String) list.get(i));  
			        if(file.exists()){  
			            file.delete();  
			        }
			    }
			}  
			/**
			 * 设置TEMP文件名（按Temp_序号  ）
			 * */
			private void setTempFileName() {
				String newName;
					
					 newName = "Temp_"+String.format("%1$,03d", tempNO+=1);
//					System.out.println("name:"+newName);
				
				tempFileName = newName;
			}
			
			private String getTime(){  
			    SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH：mm：ss");        
			    Date  curDate=new  Date(System.currentTimeMillis());//获取当前时间        
			    String   time   =   formatter.format(curDate);    
			    System.out.println("当前时间");  
			    return time;  
			    }  
			/**
			 * 设置新的文件名（按日期_序号  ）
			 * */
			private void setFileName() {
				String date = getDateYYMMDD();
				String newName;
				if(isOldDate(date)){

					 String old = getLatestData();
					 newName = date+"_"+String.format("%1$,03d", Integer.valueOf(old.substring(9))+1);
//					System.out.println("name:"+newName);
				}else {
					newName = date+"_001";
				}
				recordFileName = newName;
			}
			/**
			 * 保存最新的日期数据
			 * */
			private void saveLatestData(String filename){
				String date = filename.substring(0,8);
				int no = Integer.valueOf(filename.substring(9));
				SharedPreferences.Editor editor = sp.edit();  
		        //修改数据  
		        editor.putString(LATEST_DATE_KEY, date);
		        editor.putInt(LATEST_NO_KEY, no);
		        editor.commit();
			}
			/**
			 * 是否是旧日期
			 * */
			private Boolean isOldDate(String date){
				sp = getActivity().getPreferences(MODE_PRIVATE);  
		        String oldDate = sp.getString(LATEST_DATE_KEY, null);
//		        System.out.println("+++++++++++++++date:"+oldDate);
		        if(date.equals(oldDate)){
		        	return true;
		        }else {
		        	return false;
				}
			}
			/**
			 * 得到最近保存的文件名
			 * */
			private String getLatestData(){
				sp = getActivity().getPreferences(MODE_PRIVATE);  
		        String date = sp.getString(LATEST_DATE_KEY, null);
		        String no = String.format("%1$,03d",sp.getInt(LATEST_NO_KEY, 0));
		        return date+"_"+no;
			}
			
		}
		
//		private String[] findFile (File file, String keyword)   
//	    {   
//	        String res = "";   
//	        if (!file.isDirectory())   
//	        {   
//	            res = "不是目录";   
//	            return res;    
//	        }   
//	        File[] files = new File(file.getPath()).listFiles();   
//	           
//	        for (File f : files)   
//	        {   
//	            if (f.getName().indexOf(keyword) >= 0)   
//	            {   
//	                res += f.getPath() + "\n";   
//	            }   
//	        }       
//	  
//	          if (res.equals(""))   
//	        {   
//	            res = "没有找到相关文件";   
//	        }   
//	             
//	        return res;   
//	               
//	    }   
		/**
		 * 判断文件夹是否存在
		 * */
		public static void isExist(String path) {
			File file = new File(path);
			//判断文件夹是否存在,如果不存在则创建文件夹
			if (!file.exists()) {
			file.mkdir();
			}
		}
		/**
		 * 得到格式化日期
		 * */
		public String getDateYYMMDD() {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");  
			String date=sdf.format(new Date());
			return date;
		}
		
		
		
		@Override
		public void onStop() {
			if (recorder != null && !isStopRecord) {  
	            // 停止录音  
				recorder.stop();  
				recorder.release();  
				recorder = null;  
	        }  
			super.onStop();
		}

//		@Override
//		public void onAttach(Activity activity) {
//			super.onAttach(activity);
//			((MainActivity) activity).onSectionAttached(getArguments().getInt(
//					ARG_SECTION_NUMBER));
//		}
		
	}

}
