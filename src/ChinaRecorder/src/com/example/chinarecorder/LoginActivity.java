package com.example.chinarecorder;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	private static final String TAG = "LoginActivity";

    // define our UI elements
    private TextView mUsernameField;
    private TextView mPasswordField;
    private ProgressDialog mProgress;
    
    // called by the 'Log In' button on the UI
    public void handleLogin(View v) {

    	// show a loading progress dialog
    	mProgress = ProgressDialog.show(LoginActivity.this, "", "Signing in...", true);
    	
    	// get the username/password combination from the UI
    	final String username = mUsernameField.getText().toString();
    	String password = mPasswordField.getText().toString();
    	Log.v(TAG, "Logging in: " + username + ":" + password);
    	
    	// authenticate the user asynchronously
    	KiiUser.logIn(new KiiUserCallBack() {
    		
    		// catch the callback's "done" request
    		public void onLoginCompleted(int token, KiiUser user, Exception e) {

    			// hide our progress UI element
        		mProgress.cancel();

        		// check for an exception (successful request if e==null)
        		if(e == null) {

        			// tell the console and the user it was a success!
        			Log.v(TAG, "Logged in: " + user.toString());
        			Toast.makeText(LoginActivity.this, "User authenticated!", Toast.LENGTH_SHORT).show();

            		// go to the main screen
//            		Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
//            		LoginActivity.this.startActivity(myIntent);
        			Intent intent = new Intent();
            		intent.putExtra("UserName", username);
            		setResult(Activity.RESULT_OK, intent);
            		finish();  //finish当前activity  
       	         	overridePendingTransition(R.anim.push_right_in,  
       	                    R.anim.push_right_out);
            		
        		} 
        		
        		// otherwise, something bad happened in the request
        		else {
        			
        			// tell the console and the user there was a failure
        			Log.v(TAG, "Error registering: " + e.getLocalizedMessage());
        			Toast.makeText(LoginActivity.this, "Error registering: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        		}
        		        		
        	} 
        	
        }, username, password);
    }
    
    // called by the 'Sign Up' button on the UI
    public void handleSignUp(View v) {

    	// show a loading progress dialog
    	mProgress = ProgressDialog.show(LoginActivity.this, "", "Signing up...", true);

    	// get the username/password combination from the UI
    	final String username = mUsernameField.getText().toString();
    	String password = mPasswordField.getText().toString();
    	Log.v(TAG, "Registering: " + username + ":" + password);
    	
    	// create a KiiUser object
    	try {
        	KiiUser user = KiiUser.createWithUsername(username);
        	// register the user asynchronously
            user.register(new KiiUserCallBack() {
            	
        		// catch the callback's "done" request
            	public void onRegisterCompleted(int token, KiiUser user, Exception e) {

        			// hide our progress UI element
            		mProgress.cancel();

            		// check for an exception (successful request if e==null)
            		if(e == null) {

            			// tell the console and the user it was a success!
                		Log.v(TAG, "Registered: " + user.toString());
            			Toast.makeText(LoginActivity.this, "User registered!", Toast.LENGTH_SHORT).show();

                		// go to the next screen
//                		Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
//                		LoginActivity.this.startActivity(myIntent);
            			Intent intent = new Intent();
                		intent.putExtra("UserName", username);
            		} 
            		
            		// otherwise, something bad happened in the request
            		else {
            			
            			// tell the console and the user there was a failure
            			Log.v(TAG, "Error registering: " + e.getLocalizedMessage());
            			Toast.makeText(LoginActivity.this, "Error Registering: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            		}
            		        		
            	}
            	
            }, password);

    	} catch(Exception e) {
    		mProgress.cancel();
    		Toast.makeText(this, "Error signing up: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    	}
    	
    }

    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        
        // link our variables to UI elements
        mUsernameField = (TextView) findViewById(R.id.username_field);
        mPasswordField = (TextView) findViewById(R.id.password_field);
        
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();  //finish当前activity  
	         overridePendingTransition(R.anim.push_right_in,  
	                    R.anim.push_right_out);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
