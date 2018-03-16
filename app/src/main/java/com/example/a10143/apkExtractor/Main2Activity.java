package com.example.a10143.apkExtractor;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        PackageManager packageManager=getPackageManager();
        for(ApplicationInfo applicationInfo:packageManager.getInstalledApplications(0)){
            Log.d(TAG, "onCreate: "+applicationInfo.sourceDir);
        }
    }
}
