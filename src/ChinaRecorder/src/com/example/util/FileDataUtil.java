package com.example.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.example.bean.Recording;
import com.example.chinarecorder.RecordDetailFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

public class FileDataUtil {
	/** 设置 录音文件文件夹  **/
	public String recordDir = "chinarecord/";
	/** SD卡根目录  **/
	public static String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();//得到SD卡根目录
	public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
	public static Activity activity;
	
	public FileDataUtil(Activity activity) {
		this.activity = activity;
	}
	
	
	/**
     * 文件删除
     * */
    public void Delete_file(final List<Recording> recordings ,final Fragment fragment){
    	
       File file = new File(recordings.get(0).getRurl());   
    	if (file.exists()) { 
    	
    		new AlertDialog.Builder(  
                    activity)  
                    .setTitle("警告！")  
                    .setMessage("文件确定要删除吗？")  
                    .setPositiveButton(  
	                        "确定",  
	                        new DialogInterface.OnClickListener() {  
	                            @Override  
	                            public void onClick(  
	                                    DialogInterface dialog,  
	                                    int which) {
	                            	// 删除选中全部文件
	                            	for (int i = 0; i < recordings.size(); i++) {
	                            		//删除文件	
	                            		new File(recordings.get(i).getRurl()).delete();
	                            		deleteFileInData(recordings.get(i).getRid());
//	                            		deleteFileInData(25482);
	                            		scanDirAsync3(activity,basePath+"/"+recordDir);
	                         		}
	                            	fragment.getFragmentManager().popBackStack();
	                            	 
	                            }  
	                        })  
                    .setNegativeButton(  
		                    "取消",  
		                    new DialogInterface.OnClickListener() {  
		
		                        @Override  
		                        public void onClick(  
		                                DialogInterface dialog,  
		                                int which) {
		                        	fragment.getFragmentManager().popBackStack();
		                        }  
		                    }).show(); 
    	 
          }  
    }
   
    public void deleteFileInData(int id) {
		ContentResolver resolver = activity.getContentResolver();

//		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//		Uri uri = Uri.fromFile(new File("/sdcard/chinarecord/20141029_001.amr"));
		Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);
		ContentValues values = new ContentValues();

//		values.put(MediaStore.Audio.Media.ARTIST, "44444dfdkk");

		int delete = resolver.delete(uri, null, null);
										// MediaStore.Audio.Media.DATA+"=?", new String[] { "/storage/emulated/0/chinarecord/20141029_001.amr" }
	}
    
    
    /**
     * 文件重命名
     * */
    public void Rename_file(String oldPath ,final String newPath,final Fragment fragment){
		final File file = new File(oldPath);
		final File newFile = new File(newPath);
        // 判断文件是否存在   
        if ((new File(newPath)).exists()) {  
            // 排除修改文件时没修改直接送出的情况

            if (!file.getName().equals(newFile.getName())) {  
                // 跳出警告 文件名重复，并确认时候修改   
                new AlertDialog.Builder(  
                        activity)  
                        .setTitle("警告！")  
                        .setMessage("文件已存在，是否要覆盖？")  
                        .setPositiveButton(  
                                "确定",  
                                new DialogInterface.OnClickListener() {  

                                    @Override  
                                    public void onClick(  
                                            DialogInterface dialog,  
                                            int which) {  
                                         
                                        // 单机确定，覆盖原来的文件   
                                        file.renameTo(newFile);  
                                        // 重新生成文件列表   
                                        fragment.getView().postInvalidate();
//                                         System.out.println("rename");
                                    }  
                                })  
                        .setNegativeButton(  
                                "取消",  
                                new DialogInterface.OnClickListener() {  

                                    @Override  
                                    public void onClick(  
                                            DialogInterface dialog,  
                                            int which) {  
//                                        System.out.println("qx rename");

                                    }  
                                }).show();  
            }  
        } else {  
            // 文件名不存在直接做修改动作   
            file.renameTo(new File(newPath));
            fragment.getView().postInvalidate();
//            System.out.println("rename0 ");
        } 
    	
    	
    }
    
    
    /**
     * 插入图片路径（更新时会丢失）
     * */
    public void updateArtist2Pic(String artist2Pic, String path) {
		ContentResolver resolver = activity.getContentResolver();

//		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//		Uri uri = Uri.fromFile(new File("/sdcard/chinarecord/20141029_001.amr"));
		Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,23514);
		ContentValues values = new ContentValues();

		values.put(MediaStore.Audio.Media.ARTIST, "44444dfdkk");

		int update = resolver.update(uri,values,null,null);
										// MediaStore.Audio.Media.DATA+"=?", new String[] { "/storage/emulated/0/chinarecord/20141029_001.amr" }
	}
    
    
    
    /*
     * getTypeName 取出文件中的 音频格式类型
     * http://blog.bccn.net/kingyor/1919
     * */
    public String getTypeName(String str){
    	  String temp=str.substring(str.indexOf(".")+1,str.length());
    	  return temp;
    }
    public String getName(String str){
    	String temp = str.substring(0, str.indexOf("."));
    	return temp;
    }
    
    public void test() {
    	ContentResolver mResolver = activity.getContentResolver();   
        Cursor cursor = mResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {}
        cursor.close();
    }
    
    /**
     * 获取文件信息Model列表
     * */
    public List<Recording> mediaList(String src){
//    public String mediaList(String src){
	    ContentResolver mResolver = activity.getContentResolver();   
        Cursor cursor = mResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        String url,list = "";
        int i = 0;
        int cursorCount = cursor.getCount();
        List<Recording> recordings = new ArrayList<Recording>();
        if (cursorCount >0 )
        {
        	cursor.moveToFirst();
            while (i < cursorCount)
            {
            	//歌曲文件的路径 ：MediaStore.Audio.Media.DATA 
                url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int t=url.indexOf(src);
               SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               SimpleDateFormat timeFormat2 = new SimpleDateFormat("mm:ss");
               DecimalFormat df = new DecimalFormat(".00");
//                System.out.println();
                if( t> 0)
                {
//                	list += 
//                			"("+cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))+")"
//                			+"("+cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))+")"
//                			
//                			+"("+timeFormat2.format((cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))))+")"
//                			+"("+timeFormat1.format(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))*1000)+")"
////                			+"("+new Date().getTime()+")"
// //               			+"("+cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.CONTENT_TYPE))+")"
//                			+"("+cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))+")"
//                			+"("+getTypeName(url)+")"
//         			          
//                			 +")"+"\n"+url+"\n"+"\n";
                	
                	Recording recording = new Recording();
                	recording.setRid(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                	recording.setRname(getName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))));
                	
                	recording.setRduration(timeFormat2.format(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                	Date date;
					
						try {
							date = timeFormat1.parse(timeFormat1.format(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))*1000));
//							System.out.println("date:"+timeFormat1.format(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))*1000));
							recording.setRdate(date);
						} catch (java.text.ParseException e) {
							e.printStackTrace();
						}
						
					
                	recording.setRsize(Float.valueOf(df.format(cursor.getFloat(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))/1024)));
                	recording.setRurl(url);
                	recording.setRformat(getTypeName(url));
                	
                	recordings.add(recording);
                }            
                i++;
                cursor.moveToNext();                
            }
            cursor.close();
        }
        return recordings;
//        return list;
	}
	/**
	 * 通知MediaStore系统服务扫描指定文件
	 * */
	public void scanFileAsync(Context ctx, String filePath) {
	Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	scanIntent.setData(Uri.fromFile(new File(filePath)));
	ctx.sendBroadcast(scanIntent);
	}
	
	/**
	 * 通知MediaStore系统服务扫描指定文件夹(利用循环扫描指定文件)
	 * */
	public void scanDirAsync3(Context ctx, String dir) {
		File file = new File(dir);
//		System.out.println("file0");
        if(file.exists() && file.isDirectory()){
            File[] array = file.listFiles();
//            System.out.println("file");
            for(int i=0;i<array.length;i++){
                File f = array[i];
                
//                if(f.isFile()){//FILE TYPE
                    String name = f.getName();
//                    System.out.println("++++++name:"+name);
                    if(name.endsWith(".amr") || name.endsWith(".mp3") || name.endsWith(".3gpp") ){
//                    	System.out.println("+++++++音频文件++++++");
                    	scanFileAsync(activity,f.getAbsolutePath());
                    }
//                }
//                else {//FOLDER TYPE
//                	scanFileAsync(this,f.getAbsolutePath());
//                }
            }
        }
	} 
	
	
//	/**
//	 * 通知MediaStore系统服务扫描指定文件夹(不起作用)
//	 * */
//	public void scanDirAsync(Context ctx, String dir) {
//	Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
//	scanIntent.setData(Uri.fromFile(new File(dir)));
//	ctx.sendBroadcast(scanIntent);
//	} 
	
//	/**
//	 * 通过MediaScanner扫描指定文件夹(MediaScanner未公开于AndroidAPI)
//	 * */
	
//	public void scanDirAsync2(Context ctx,String dir) {
//	String[] driectories = new String[]{dir};
//	MediaScanner scanner= new MediaScanner(ctx);
//	
//	}
}
