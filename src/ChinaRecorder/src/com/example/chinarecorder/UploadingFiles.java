package com.example.chinarecorder;

import android.app.Application;

import com.kii.cloud.storage.Kii;


public class UploadingFiles extends Application {
	
    @Override
    public void onCreate() {
        super.onCreate();
                
        // initialize the Kii SDK!
        Kii.initialize("b1568cd5", "074aa5c66529a78d930fa1b7f152780a", Kii.Site.CN);
    }
	
}
