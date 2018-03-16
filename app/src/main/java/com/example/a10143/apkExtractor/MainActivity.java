package com.example.a10143.apkExtractor;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    boolean doubleClick=false;
    DrawerLayout drawerLayout;
    List<ApplicationInfo> rightApplicationInfos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        toolbar.setTitle("亲我亲我！");
        toolbar.setAlpha(Float.parseFloat("0.5"));
        setSupportActionBar(toolbar);
       // android.support.v7.app.ActionBar actionBar=getSupportActionBar();
      //  actionBar.setDisplayHomeAsUpEnabled(true);
        PackageManager packageManager=getPackageManager();
        List<ApplicationInfo> applicationInfos=packageManager.getInstalledApplications(0);

        for(ApplicationInfo applicationInfo:applicationInfos){
           // Log.d(TAG, "onCreate: "+applicationInfo.name+"  "+packageManager.getApplicationLabel(applicationInfo)+"  "+applicationInfo.flags+"  "+applicationInfo.sourceDir);
            if(applicationInfo.sourceDir.startsWith("/data/app/")){
                Log.d(TAG, "onCreate: "+"  "+packageManager.getApplicationLabel(applicationInfo)+"  "+applicationInfo.packageName+"  "+applicationInfo.sourceDir);
                rightApplicationInfos.add(applicationInfo);
            }
        }
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(rightApplicationInfos,MainActivity.this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager=new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);


    }

/*PackageManager packageManager=getPackageManager();
                List<ApplicationInfo> applicationInfos=packageManager.getInstalledApplications(0);
                Log.d(TAG, "onClick: "+applicationInfos.size());
                for(ApplicationInfo applicationInfo:applicationInfos){
                    if(applicationInfo.name!=null){
                        appLabel.add(packageManager.getApplicationLabel(applicationInfo).toString());
                        appPackageName.add(applicationInfo.name);
                        appLogo.add(packageManager.getApplicationIcon(applicationInfo));
                        appSoureceDir.add(applicationInfo.sourceDir);
                    }
                }

 */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_mainactivity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);break;
                default:break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(doubleClick==true){
            super.onBackPressed();
            return;
        }
        doubleClick=true;
        Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleClick=false;

            }
        },2000);
    }
}
