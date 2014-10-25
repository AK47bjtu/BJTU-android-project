package com.example.chinarecorder;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.example.bean.Recording;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RecordListEditFragment extends ListFragment{
	
	final static String ARG_EDIT = "listedit";
	 public static List<Recording> editRecordings = new ArrayList<Recording>();
	 Context mContext;
	 private ListView lv ;
	 Button editSure,editCancel;
	 RecoderEditAdapter adapter;
	 List<Integer> listItemID = new ArrayList<Integer>();
	 
	 int position = -1;
	 
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		position = getArguments().getInt(ARG_EDIT);//获得显示状态（分享、删除）
//		System.out.println("ARG_EDIT"+ position);
		mContext = getActivity().getApplicationContext();
		View view = inflater.inflate(
				R.layout.fragment_record_list_edit, container, false);
//		 lv = (ListView) view.findViewById(R.id.lvrecord);
		editSure = (Button) view.findViewById(R.id.delete_list_edit);
		if (position == 1) {
			editSure.setText(R.string.share);
		}
		editSure.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击button事件：获取选中checkbooksID并响应
             * */
            @Override
            public void onClick(View v) {
            	clickCheckBoxList();
            }
            
		});
		
		editCancel = (Button) view.findViewById(R.id.cancel_list_edit);
		editCancel.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击button事件：退回前一Fregment
             * */
            @Override
            public void onClick(View v) {
            	getFragmentManager().popBackStack();
//            	((RecordListActivity) activity).restoreActionBar();
            }
            
		});
		
		return view;
	}
	
	public  void clickCheckBoxList() {
		 listItemID.clear();
         for(int i=0;i<adapter.mChecked.size();i++){
             if(adapter.mChecked.get(i)){
                 listItemID.add(i);
             }
//             System.out.println("显示"+i+"|||"+adapter.mChecked.get(i));
         }
          
         if(listItemID.size()==0){
             AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
             builder1.setMessage("没有选中任何要记录");
             builder1.show();
         }else{
             StringBuilder sb = new StringBuilder();
              
             for(int i=0;i<listItemID.size();i++){
                 sb.append("ItemID="+listItemID.get(i)+" . ");
             }
             AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
             builder2.setMessage(sb.toString());
             builder2.show();
         }
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 ListView lv;
		 lv =(ListView) this.getView().findViewById(android.R.id.list);
		 initRecording();
	     adapter = new RecoderEditAdapter(editRecordings);
		 lv.setAdapter(adapter);
		
		
	}
	

  


	/**
	 *当调用popBackStack()方法返回时，先调用onPause()方法，再调用onDestroyView()方法
	 * */
	@Override
	public void onPause() {
		System.out.println("pause");
		
		super.onPause();
	}

	/**
	 *当调用popBackStack()方法返回时，先调用onPause()方法，再调用onDestroyView()方法
	 * */
	@Override
	public void onDestroyView() {
		System.out.println("onDestroyView");
		Activity act = getActivity();
		((RecordListActivity)act).onSectionAttached(RecordListActivity.BACKTOLIST);
		super.onDestroyView();
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((RecordListActivity) activity).onSectionAttached(position);
		
//		((RecordListActivity) activity).myGetItem2(getArguments().getInt(
//				"aa"));
	}



	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
//		myGetItem(position);
		
		super.onListItemClick(l, v, position, id);
	}



	/**
	 * 初始化列表数据
	 * */
	private void initRecording() {
		Recording tempRecording ;
		editRecordings.clear();
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
			editRecordings.add(tempRecording);
		}
	}
//	/**
//	 * 列表跳转按钮事件方法
//	 * */
//	private void showDetail(int id) {
//		
////		Toast.makeText(mContext, "imgBtn"+id, Toast.LENGTH_SHORT)
////		.show();
//		Bundle args = new Bundle();
//       args.putInt(RecordListFragment.ARG_POSITION, id);
//		Intent intent = new Intent();
//		
//		intent.setClass(RecordListEditFragment.this.getActivity(), RecordDetailActivity.class);
//		intent.putExtras(args);
//		startActivity(intent);
//		
//		
//	}
	
//	private void myGetItem(int id) {
////		Toast.makeText(mContext, "myitem"+id, Toast.LENGTH_SHORT)
////		.show();
//		Bundle args = new Bundle();
//       args.putInt(RecordListFragment.ARG_POSITION, id);
//		Intent intent = new Intent();
//		
//		intent.setClass(RecordListFragment.this.getActivity(), PlayActivity.class);
//		intent.putExtras(args);
//		startActivity(intent);
//		
//	}
	
	/**
	 * 列表持久化
	 * */
	public final class ViewHolder{
		public ImageView pic;
		public TextView name;
		public TextView time;
		public CheckBox selected;
	}
	
	/**
	 * 自定义ListView   Item 控件
	 * */
	 class RecoderEditAdapter extends BaseAdapter{
		 List<Boolean> mChecked;
		 List<Recording> listRecording;
		 HashMap<Integer,View> map = new HashMap<Integer,View>(); 
         
		
		
		
		public RecoderEditAdapter(List<Recording> list){
			listRecording = new ArrayList<Recording>();
			listRecording = list;
            
           mChecked = new ArrayList<Boolean>();
           for(int i=0;i<list.size();i++){
               mChecked.add(false);
           }
       }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listRecording.size();
		}

		@Override
		public Object getItem(int arg0) {
			
			return listRecording.get(arg0);
		}

		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder = null;
			if (map.get(position) == null) {
				
				holder=new ViewHolder();  
				LayoutInflater  mInflater = (LayoutInflater) mContext
	                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    view = mInflater.inflate(R.layout.listitem_edit, null);
				holder.pic = (ImageView)view.findViewById(R.id.list_pic_edit);
				holder.name = (TextView)view.findViewById(R.id.list_name_edit);
				holder.time = (TextView)view.findViewById(R.id.list_time_edit);
				holder.selected = (CheckBox)view.findViewById(R.id.list_select_edit);
				final int p = position;
                map.put(position, view);
                holder.selected.setOnClickListener(new View.OnClickListener() {

//					@Override
//					public void onCheckedChanged(CompoundButton buttonView,
//							boolean isChecked) {
//						 CheckBox cb = (CheckBox)buttonView;
//                         mChecked.set(p, cb.isChecked());
//					}
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox)v;
                        mChecked.set(p, cb.isChecked());
//                        System.out.println("++++++++++++:"+p+"+++"+cb.isChecked());
                    }

                });
                
				view.setTag(holder);
			}else {
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			
			
//			holder.pic.setBackgroundResource((Integer)recordings.get(position).get("img"));
			holder.pic.setBackgroundResource(R.drawable.app_pic_net_people);//自定义图片
			holder.name.setText(editRecordings.get(position).getRname());
			holder.time.setText(editRecordings.get(position).getRdate().toString());
			holder.selected.setChecked(mChecked.get(position));
			
			
			return view;
		}
		
	}
	
}
