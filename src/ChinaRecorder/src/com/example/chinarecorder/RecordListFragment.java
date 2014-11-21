package com.example.chinarecorder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.bean.Recording;
import com.example.util.FileDataUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordListFragment extends ListFragment{
	 final static String ARG_POSITION = "position";
	 public static List<Recording> recordings = new ArrayList<Recording>() ;
	 public static List<Recording> onlineRecordings = new ArrayList<Recording>() ;
	 Context mContext;
	 ImageButton editShare,editDelete;
	 private ListView lv ;
	 RecoderAdapter adapter;
	 FileDataUtil fileDataUtil ;
	 
	 OnRecordListEditListener recordEditJump;
	 /**
	  * 定义选择跳转的编辑Fragment接口
	  * */
	 public interface OnRecordListEditListener{
		 /**
		  * 选择跳转的编辑Fragment接口方法
		  * */
		 public void onEditSelected(int position);
	 }
	 
	 OnRecordDitalListener recordDetailJump;
	 /**
	  * 定义选择跳转的详细信息Fragment接口
	  * */
	 public interface OnRecordDitalListener{
		 /**
		  * 选择跳转的详细信息Fragment接口方法
		  * */
		 public void onDetail(int position);
	 }
	 
	 
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mContext = getActivity().getApplicationContext();
		View view = inflater.inflate(
				R.layout.fragment_record_list, container, false);
//		 lv = (ListView) view.findViewById(R.id.lvrecord);
		
		fileDataUtil = new FileDataUtil(getActivity());
		System.out.println("path:"+fileDataUtil.basePath+"/"+fileDataUtil.recordDir);
		fileDataUtil.scanDirAsync3(getActivity(), fileDataUtil.basePath+"/"+fileDataUtil.recordDir);
		recordings = fileDataUtil.mediaList(fileDataUtil.basePath.substring(3)+"/"+fileDataUtil.recordDir);
		                                //fileDataUtil.basePath.substring(3)+"/"+fileDataUtil.recordDir    "/chinarecord/"
		
//		List<Recording>test = fileDataUtil.mediaList("/chinarecord/");
		editShare = (ImageButton) view.findViewById(R.id.share);
		editShare.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击button事件：跳转到“分享”模式的fragment
             * */
            @Override
            public void onClick(View v) {
//            	Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT)
//    			.show();
            	changeListEditFragment(RecordListActivity.GOTOSHARE);
            }
            
		});
		
		editDelete = (ImageButton) view.findViewById(R.id.delete_list);
		editDelete.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击button事件：跳转到“删除”模式的fragment
             * */
            @Override
            public void onClick(View v) {
            	changeListEditFragment(RecordListActivity.GOTODELETE);
            }
            
		});
 		return view;
	}
	public void changeListEditFragment(int state){
		recordEditJump.onEditSelected(state);
	}
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initListView();
		 
	}
	
	private void initListView(){
		ListView lv;
		 lv =(ListView) this.getView().findViewById(android.R.id.list);
//		 initRecording();
	     adapter = new RecoderAdapter(recordings);
		 lv.setAdapter(adapter);
	}
	
	
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {  
			recordEditJump = (OnRecordListEditListener) activity;
        } catch (ClassCastException e) {  
            throw new ClassCastException(activity.toString()  
                    + " must implement OnRecordListEditListener");  
        }
		
		try {  
			recordDetailJump = (OnRecordDitalListener) activity;
        } catch (ClassCastException e) {  
            throw new ClassCastException(activity.toString()  
                    + " must implement OnRecordDitalListener");  
        }
		
		
//		((RecordListActivity) activity).myGetItem2(getArguments().getInt(
//				"aa"));
	}



	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		myGetItem(position);
		
		super.onListItemClick(l, v, position, id);
	}



	/**
	 * 初始化列表数据
	 * */
	private void initRecording() {
		Recording tempRecording ;
		recordings.clear();
//		int a = fileDataUtil.mediaList("/chinarecord/").size();
//		for (int i = 0; i < fileDataUtil.mediaList("/chinarecord/").size(); i++) {
//			System.out.println(fileDataUtil.mediaList("/chinarecord/").get(i).getRurl());
//			recordings.add( fileDataUtil.mediaList("/chinarecord/").get(i));
//		}
//		System.out.println(recordings.size());
//		for (int i = 0; i < 20; i++) {
//			tempRecording = new Recording();
//			tempRecording.setRname("ABC:"+i+"              ");
//			tempRecording.setRdate(new Date());
//			tempRecording.setRid(i);
//			tempRecording.setRsize(i);
//			tempRecording.setRformat("amr");
//			tempRecording.setRpic("C:/"+i+".jpg");
//			tempRecording.setRduration(5+i);
//			tempRecording.setRurl("D:/"+i+".amr");
//			recordings.add(tempRecording);
//		}
	}
	/**
	 * 列表跳转按钮事件方法
	 * */
	private void showDetail(int id) {
		
//		Toast.makeText(mContext, "imgBtn"+id, Toast.LENGTH_SHORT)
//		.show();
//		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//		Bundle args = new Bundle();
//        args.putInt(RecordListFragment.ARG_POSITION, id);
//		Intent intent = new Intent();
//		
//		intent.setClass(RecordListFragment.this.getActivity(), RecordDetailFragment.class);
//		intent.putExtras(args);
//		startActivity(intent);
		jumpToDetail(id);
		
		
	}
	private void jumpToDetail(int id){
		recordDetailJump.onDetail(id);
	}
	
	private void myGetItem(int id) {
//		Toast.makeText(mContext, "myitem"+id, Toast.LENGTH_SHORT)
//		.show();
		Bundle args = new Bundle();
        args.putInt(RecordListFragment.ARG_POSITION, id);
        args.putString("RECORD_FILE", recordings.get(id).getRurl());
        args.putString("RECORD_NAME", recordings.get(id).getRname()+"."+recordings.get(id).getRformat());
		Intent intent = new Intent();
		
		intent.setClass(RecordListFragment.this.getActivity(), PlayActivity.class);
		intent.putExtras(args);
		startActivity(intent);
		
	}
	
	
	

	/**
	 * 
	 * */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		return super.onContextItemSelected(item);
	}
	/**
	 * 
	 * */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.file_synch);
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}




	/**
	 * 列表持久化
	 * */
	public final class ViewHolder{
		public ImageView pic;
		public TextView name;
		public TextView time;
		public ImageButton viewBtn;
	}
	
	/**
	 * 自定义ListView   Item 控件
	 * */
	 class RecoderAdapter extends BaseAdapter{
		 List<Recording> listRecording;
		private LayoutInflater mInflater;
		
		
		public RecoderAdapter(List<Recording> list){
			listRecording = new ArrayList<Recording>();
			listRecording = list;
             
//            mChecked = new ArrayList<Boolean>();
//            for(int i=0;i<list.size();i++){
//                mChecked.add(false);
//            }
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return recordings.size();
		}

		@Override
		public Object getItem(int arg0) {
			
			return recordings.get(arg0);
		}

		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
			    mInflater = (LayoutInflater) mContext
	                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.listitem, null);
				holder.pic = (ImageView)convertView.findViewById(R.id.list_pic);
				holder.name = (TextView)convertView.findViewById(R.id.list_name);
				holder.time = (TextView)convertView.findViewById(R.id.list_time);
				holder.viewBtn = (ImageButton)convertView.findViewById(R.id.list_viewbtn);
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			 SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//			holder.pic.setBackgroundResource((Integer)recordings.get(position).get("img"));
			holder.pic.setBackgroundResource(R.drawable.ic_tab_songs_unselected);
			holder.name.setText(limitName(recordings.get(position).getRname())+"    ");
			holder.name.setTextColor(Color.GREEN);
			holder.time.setText(timeFormat1.format(recordings.get(position).getRdate())+"  "
						+recordings.get(position).getRsize()+"KB");//+" "+recordings.get(position).getRsize()+"KB"
			holder.viewBtn.setBackgroundResource(R.drawable.button_show_record_detail);
			holder.viewBtn.setTag(position);  
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {
				/**
				 * 自定义按钮事件
				 * */
				
				@Override
				public void onClick(View v) {
//					showInfo();	
//					System.out.println(position);
					showDetail(position);
					
				}
			});
			
			
			return convertView;
		}
		
		private String limitName(String str){
			if(str.length()>16){
				str = str.substring(0, 15)+"...";
			}
			return str;
		}
		
		
		
		
		
	}
	
	
}
