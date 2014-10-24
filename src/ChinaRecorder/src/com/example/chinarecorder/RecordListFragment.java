package com.example.chinarecorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.bean.Recording;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
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
	 public static List<Recording> recordings = new ArrayList<Recording>();
	 Context mContext;
	 ImageButton editShare,editDelete;
	 private ListView lv ;
	 RecoderAdapter adapter;
	 
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
	 
	 
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		mContext = getActivity().getApplicationContext();
		View view = inflater.inflate(
				R.layout.fragment_record_list, container, false);
//		 lv = (ListView) view.findViewById(R.id.lvrecord);
		editShare = (ImageButton) view.findViewById(R.id.share);
		editShare.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击button事件
             * */
            @Override
            public void onClick(View v) {
            	Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT)
    			.show();
            }
            
		});
		
		editDelete = (ImageButton) view.findViewById(R.id.delete_list);
		editDelete.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击button事件：退回前一Fregment
             * */
            @Override
            public void onClick(View v) {
            	changeListEditFragment();
            }
            
		});
 		return view;
	}
	public void changeListEditFragment(){
		recordEditJump.onEditSelected(RecordListActivity.GOTODELETE);
		
	}
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 ListView lv;
		 lv =(ListView) this.getView().findViewById(android.R.id.list);
		 initRecording();
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
		for (int i = 0; i < 20; i++) {
			tempRecording = new Recording();
			tempRecording.setRname("ABC:"+i+"              ");
			tempRecording.setRdate(new Date());
			tempRecording.setRid(i);
			tempRecording.setRsize(i);
			tempRecording.setRformat("amr");
			tempRecording.setRpic("C:/"+i+".jpg");
			tempRecording.setRduration(5+i);
			tempRecording.setRurl("D:/"+i+".amr");
			recordings.add(tempRecording);
		}
	}
	/**
	 * 列表跳转按钮事件方法
	 * */
	private void showDetail(int id) {
		
//		Toast.makeText(mContext, "imgBtn"+id, Toast.LENGTH_SHORT)
//		.show();
		Bundle args = new Bundle();
        args.putInt(RecordListFragment.ARG_POSITION, id);
		Intent intent = new Intent();
		
		intent.setClass(RecordListFragment.this.getActivity(), RecordDetailActivity.class);
		intent.putExtras(args);
		startActivity(intent);
		
		
	}
	
	private void myGetItem(int id) {
//		Toast.makeText(mContext, "myitem"+id, Toast.LENGTH_SHORT)
//		.show();
		Bundle args = new Bundle();
        args.putInt(RecordListFragment.ARG_POSITION, id);
		Intent intent = new Intent();
		
		intent.setClass(RecordListFragment.this.getActivity(), PlayActivity.class);
		intent.putExtras(args);
		startActivity(intent);
		
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
			
			
//			holder.pic.setBackgroundResource((Integer)recordings.get(position).get("img"));
			holder.pic.setBackgroundResource(R.drawable.app_pic_net_people);
			holder.name.setText(recordings.get(position).getRname());
			holder.time.setText(recordings.get(position).getRdate().toString());
			holder.viewBtn.setBackgroundResource(R.drawable.button_show_record_detail);
			holder.viewBtn.setTag(position);  
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {
				/**
				 * 自定义按钮事件
				 * */
				
				@Override
				public void onClick(View v) {
//					showInfo();	
					
					showDetail(position);
					
				}
			});
			
			
			return convertView;
		}
		
	}
	
	
}
