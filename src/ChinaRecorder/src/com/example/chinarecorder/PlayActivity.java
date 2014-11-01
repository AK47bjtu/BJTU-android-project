package com.example.chinarecorder;

import java.io.File;

import com.example.chinarecorder.*;

import com.example.bean.Recording;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 播放界面
 * */
public class PlayActivity extends ActionBarActivity {
	
	Recording recording;
	private EditText edi;		
	private String path;		
	private MediaPlayer mediaPlayer;	
	private boolean ipause;	
	private int pos;	
	private SeekBar seekbar;	
	private TextView currentTime;
	private ImageView rotecd;
	private int duration;
	private String recordFile;
	private String recordName;
	private Button pause;
	private TextView rn;
	LinearInterpolator lin;
	Animation cd;
	Handler handler = new Handler();
	private Runnable thread = new Runnable()
	{
		public void run()
		{
			updateTextView();
			handler.postDelayed(thread, 1000);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
	    int	i = bundle.getInt(RecordListFragment.ARG_POSITION);
	    recordName = bundle.getString("RECORD_NAME");
	    recordFile = bundle.getString("RECORD_FILE");
	    recording = RecordListFragment.recordings.get(i);
		setContentView(R.layout.activity_play);	
//		System.out.println(recording.getRname());
		 mediaPlayer = new MediaPlayer();
		 	rn = (TextView)this.findViewById(R.id.tex);
		 	rn.setText(recordName);
	        edi = (EditText)this.findViewById(R.id.filename);
	        edi.setText(recordName);
	        seekbar = (SeekBar)this.findViewById(R.id.seekbar);
			currentTime = (TextView) findViewById(R.id.currtime);
			currentTime.setText("0:00");
			rotecd=(ImageView)this.findViewById(R.id.imageview);
			rotecd.setBackgroundColor(Color.TRANSPARENT);			
			pause = (Button)this.findViewById(R.id.pausebutton);						
			cd = AnimationUtils.loadAnimation(this, R.anim.rote); 
			lin = new LinearInterpolator(); 
			cd.setInterpolator(lin);
			new Thread(thread).start();
	        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					if(fromUser) {
						mediaPlayer.start();
						mediaPlayer.seekTo(progress);
						pause.setText(R.string.pausebutton);
					}
				}

				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
	        	
	        });
	    }
	    protected void onDestory() {
	    	mediaPlayer.release();
	    	mediaPlayer = null;
	    	super.onDestroy();
	    }
	    
	    @Override
		protected void onPause() {
	    	if(mediaPlayer.isPlaying()) {
		    	pos = mediaPlayer.getCurrentPosition();
		    	mediaPlayer.stop();
	    	}
			super.onPause();
		}	    
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub		
			if(pos>0&&path!=null) {
				play();			
				mediaPlayer.seekTo(pos);			
				pos=0;
			}
			super.onResume();
		}
		public void mediaplayer(View v){
	    	switch (v.getId()){
	    	 case R.id.playbutton:
//	    		 String filename = edi.getText().toString();
	    		 /** BUG document add**/
//	    		 File audio = new File(Environment.getExternalStorageDirectory()+"filerecord",filename);
	    		 File audio = new File(recordFile);
	    		 System.out.println("path:"+audio);
	    		 if(audio.exists()) {
	    			 path = audio.getAbsolutePath();
	    			 System.out.println("pipi");
	    			 play(); 
	    			 if(cd!=null) {
	    				 rotecd.startAnimation(cd);
	    			 }
	    			 pause.setText(R.string.pausebutton);
	    		 }
	    		 else {
	    			 path = null;
	    			 Toast.makeText(getApplicationContext(), R.string.filenotfind, 2).show();
	    			 pause.setText(R.string.pausebutton);
	    		 }
	    		 break;
	    	  case R.id.pausebutton:    		  
	    		  if(mediaPlayer.isPlaying()) {
	    			  mediaPlayer.pause();
	    			  rotecd.clearAnimation();
	    			  ipause=true;
	    			  ((Button)v).setText(R.string.continues);
	    		  }
	    		  else if(ipause){
	    			  mediaPlayer.start();
	    			  rotecd.startAnimation(cd);
	    			  ipause = false;
	    			  ((Button)v).setText(R.string.pausebutton);
	    		  }
	    		  break;
	    	  case R.id.resetbutton:
	    		  if(mediaPlayer.isPlaying()){
	    			  mediaPlayer.seekTo(0);	    			  
	    			  pause.setText(R.string.pausebutton);
	    		  }else{
	    			  play();
	    			  rotecd.startAnimation(cd);
	    			  pause.setText(R.string.pausebutton);
	    		  }
	    		  break;
	    	  case R.id.stopbutton:
	    		  if(mediaPlayer.isPlaying()){
	    			  mediaPlayer.seekTo(0);
	    			  mediaPlayer.stop();
	    			  currentTime.setText("0:00");    			  
	    			  seekbar.setProgress(0);    			  
	    			  rotecd.clearAnimation();
	    			  pause.setText(R.string.pausebutton);
	    		  }
	    		  break;
	    	}
	    }
	    private void play() {
	    	try {
	    		mediaPlayer.reset();
	    		mediaPlayer.setDataSource(path);
	    		mediaPlayer.prepare();
	    		mediaPlayer.setOnPreparedListener(new PrepareListener());
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    private final class PrepareListener implements OnPreparedListener {
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub			
				mediaPlayer.start();			
				duration = mediaPlayer.getDuration();			
				seekbar.setMax(duration);
			}    	
	    }
	    private void updateTextView()
		{
			if (mediaPlayer != null&&mediaPlayer.isPlaying())
			{
				int m = mediaPlayer.getCurrentPosition() / 1000;
				int s = m / 60;
				int add = m % 60;
				if (add < 10)
					currentTime.setText(s + ":0" + add);
				else
					currentTime.setText(s + ":" + add);	
				seekbar.setProgress( mediaPlayer.getCurrentPosition());
			}
			else if (mediaPlayer != null&&(!mediaPlayer.isPlaying())){	
				if(seekbar.getMax()!=0) {
				seekbar.setProgress( mediaPlayer.getCurrentPosition());
				}
				else {
					seekbar.setProgress(0);	
				}
			}
			else {
				System.out.println("hehe");				
				seekbar.setProgress(0);				
				rotecd.clearAnimation();
			}
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
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
}
