package com.example.chinarecorder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.bean.Recording;
import com.example.util.FileDataUtil;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordDetailActivity extends ActionBarActivity {
	
	
    private final int TYPE_DETAIL   = 0;
    private final int TYPE_BUTTON   = 1;
//	private final int TYPE_SWITCH   = 2;
//	private final int TYPE_SEEKBAR  = 3;
	Recording recording;
	List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	MyAdapter listAdapter ;
	public FileDataUtil fileDataUtil ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fileDataUtil = new FileDataUtil(this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
	    int	i = bundle.getInt(RecordListFragment.ARG_POSITION);
	    recording = RecordListFragment.recordings.get(i);
		setContentView(R.layout.activity_record_detail);
		
		ListView lv = (ListView) this.findViewById(R.id.listview_home);       
		initDetail();
		
		listAdapter = new MyAdapter(this);
		lv.setAdapter(listAdapter);
		
		
	}
	/**
	 * 初始化属性栏
	 * */
	private void initDetail() {
		 Map<String, Object> map;
			map = new HashMap<String, Object>();
			map.put("title", "名称");
			map.put("detail", recording.getRname());
			mData.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "时间");
			map.put("detail", recording.getRdate().toString());
			mData.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "时长");
			map.put("detail", String.valueOf(recording.getRduration()));
			mData.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "大小");
			map.put("detail",String.valueOf(recording.getRsize())+"KB");
			mData.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "格式");
			map.put("detail", recording.getRformat());
			mData.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "路径");
			map.put("detail", recording.getRurl());
			mData.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "图片路径");
			map.put("detail", recording.getRpic());
			mData.add(map);
	}
	
	/**
	 * 修改“详细信息”内容按钮事件
	 * */
	private void change(int position) {
		Map<String, Object> map = mData.get(position);
		Iterator<String> iter = map.keySet().iterator();
		String[] key = new String[2] ;
		String[] value = new String[2];
		int i=0;
		while (iter.hasNext()) {
		    key[i] = iter.next();
		    value[i] = (String) map.get(key[i]);
//		    System.out.println(key[i]+"——"+value[i]);
		    i++; 
		}
		if(position == 0){
			inputTitleDialog(value[0]);
		}else if(position == 6){
			changePic();
		}
	}
	/**
	 * 弹出修改对话框
	 * */
	private void inputTitleDialog(String str) {

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);
//        System.out.println("++++++++++++"+str);
        inputServer.setText(str.toCharArray(), 0, str.length());

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.fix)).setIcon(
                R.drawable.ic_launcher).setView(inputServer).setNegativeButton(
                getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.notarize),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	String tempFile = fileDataUtil.basePath+"/"+fileDataUtil.recordDir;
                        String inputName = inputServer.getText().toString();
                        fileDataUtil.deleteFileInData(recording.getRid());
                        System.out.println("tempFile:"+tempFile);
                        fileDataUtil.Rename_file(tempFile+recording.getRname(), tempFile+inputName);
                        flushDataBase();
                        Intent intent = new Intent();
                		
                		intent.setClass(builder.getContext(), RecordDetailActivity.class);
                		
                		startActivity(intent);
//                        System.out.println(inputName);
                      /*************************************/  
                        
                    }
                });
        builder.show();
    }
	private void flushDataBase() {
		fileDataUtil.scanDirAsync3(this, fileDataUtil.basePath+"/"+fileDataUtil.recordDir);
	}
	private void flushListView() {
		
	}
	/**
	 * 修改图片
	 * */
	private void changePic() {
		Intent intent = new Intent();  
        /* 开启Pictures画面Type设定为image */  
        intent.setType("image/*");  
        /* 使用Intent.ACTION_GET_CONTENT这个Action */  
        intent.setAction(Intent.ACTION_GET_CONTENT);   
        /* 取得相片后返回本画面 */  
        startActivityForResult(intent, 1); 
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {  
            Uri uri = data.getData(); 
            Toast.makeText(this, "uri: "+uri, Toast.LENGTH_SHORT)
			.show();
            System.out.println("++++++++uri:"+uri);
            Log.e("uri", uri.toString());  
            ContentResolver cr = this.getContentResolver();  
            try {  
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
//                ImageView imageView = (ImageView) findViewById(R.id.iv01);  
                /* 将Bitmap设定到ImageView */  
//                imageView.setImageBitmap(bitmap);  
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }  
        }  
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.record_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 自定义BaseAdapter
	 * */

	/**
		 * 自定义一个Adapter(实现了ListAdapter接口)
		 * 
		 * @author Administrator
		 * 
		 */	
		class MyAdapter extends BaseAdapter{

			LayoutInflater inflater = null;

	        public MyAdapter(Context context) {

			    this.inflater = LayoutInflater.from(context);
			    //inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}

		     @Override
	         public int getCount() {
	         // TODO Auto-generated method stub
		    	 return mData.size();}

	         //每个convert view都会调用此方法，获得当前所需要的view样式
	         @Override
		     public int getItemViewType(int position) {
			     // TODO Auto-generated method stub        	 
			     
			     if(position == 0||position == 6)
			         return TYPE_BUTTON;
			     else
			    	 return TYPE_DETAIL;
		     }

	         @Override
	         public int getViewTypeCount() {
	        	 // TODO Auto-generated method stub
	        	 return 2;
	         }

	         @Override
	         public Object getItem(int position) {
	        	 // TODO Auto-generated method stub
	        	 //return mData.get(arg0);
	        	 return position;
	         }

	         @Override
	         public long getItemId(int position) {
	        	 // TODO Auto-generated method stub
	        	 return position;
	         }

	         @Override
	         public View getView(final int position, View convertView, ViewGroup parent) {
	        	 // TODO Auto-generated method stub    	 
	     	 
//	        	 viewHolder_switch holder_switch = null;
//	        	 viewHolder_seekbar holder_seekbar = null;
	        	 viewHolder_button holder_button = null;
	        	 viewHolder_detail holder_detail = null;
	        	 
	        	 int type = getItemViewType(position);
	        	 
	        	 if (convertView == null)
	        	 {
	        		Log.e("convertView = ", " NULL");

	              switch(type)
	              {
	              case TYPE_DETAIL:
	            	  
	            	  
	            	  holder_detail = new viewHolder_detail();
	            	  convertView = inflater.inflate(R.layout.listview_detail_row, parent, false);
	            	 // convertView = inflater.inflate(R.layout.listview_switch_row,null);
	            	  //
	            	 
	            	  holder_detail.title = (TextView)convertView.findViewById(R.id.title_sw);
	            	  holder_detail.detail = (TextView)convertView.findViewById(R.id.detail_sw);
	            	  Log.e("convertView = ", "NULL TYPE_DETAIL");  
	            	  
	            	  convertView.setTag(holder_detail);           	  
	            	  
	            	  
	            	  break;
	                     	  
	              case TYPE_BUTTON:
	            	  
	            	  convertView = inflater.inflate(R.layout.listview_button_row, parent, false);
	            	  holder_button = new viewHolder_button();
	     
	            	  holder_button.title = (TextView)convertView.findViewById(R.id.title_btn);
	            	  holder_button.detail = (TextView)convertView.findViewById(R.id.detail_btn);
	            	  holder_button.button = (Button)convertView.findViewById(R.id.btn_btn);
	            	  holder_button.button.setTag(position);
	            	  holder_button.button.setOnClickListener(new View.OnClickListener() {  
	                      
	                      @Override  
	                      public void onClick(View v) {
//	                    	  Log.e("convertView = ", "ButtonClick"+position);
	                          change(position);                   
	                      }
  
	                  });  
	            	  Log.e("convertView = ", "NULL TYPE_BUTTON");
						
	            	  convertView.setTag(holder_button);
					break;
	              }
	        	 }else{
					//有convertView，按样式，取得不用的布局
					switch(type)
					{
					case TYPE_DETAIL:
						holder_detail = (viewHolder_detail) convertView.getTag();
						Log.e("convertView !!!!!!= ", "NULL TYPE_DETAIL");
						break;
					
					case TYPE_BUTTON:
						holder_button = (viewHolder_button) convertView.getTag();
						Log.e("convertView !!!!!!= ", "NULL TYPE_BUTTON");
						break;
					}
				 }

	        	 //设置资源
	        	 switch(type)
		         {
					case TYPE_DETAIL:
						holder_detail.title.setText((String)mData.get(position).get("title"));
						holder_detail.detail.setText((String)mData.get(position).get("detail"));
						  //holder_switch.Switch.setChecked(true);  
					break;
				 
		          case TYPE_BUTTON:
					  holder_button.title.setText((String)mData.get(position).get("title"));
					  holder_button.detail.setText((String)mData.get(position).get("detail"));
					break;
				  }
	        	 
				return convertView;
			   }
		 }

		//各个布局的控件资源
		class viewHolder_detail{			
			/** 布局ID */
			public int layoutID;

			/** 标题 */
			public TextView title;

			/** 内容 */
			public TextView detail;
			/* switch*/
			
	
			 
		}

		
		class viewHolder_button
		{

			private int layoutID;

			/** 标题 */
			private TextView title;

			/** 内容 */
			private TextView detail;

			/** 按钮名称 */
			private Button button;		
	
			
		}	 	
}


	
	

