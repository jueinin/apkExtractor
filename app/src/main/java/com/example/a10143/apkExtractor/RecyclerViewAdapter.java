package com.example.a10143.apkExtractor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by 10143 on 2018/2/5.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public RecyclerViewAdapter(List<ApplicationInfo> applicationInfos,Context context) {
        applicationInfoList=applicationInfos;
        mContext=context;
    }
    Context mContext;
    List<ApplicationInfo> applicationInfoList;
    private static final String TAG = "RecyclerViewAdapter";
    PackageManager packageManager;
    View viewForSnackBar;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ApplicationInfo applicationInfo=applicationInfoList.get(position);
        packageManager=mContext.getPackageManager();
        holder.label.setText(packageManager.getApplicationLabel(applicationInfo));
        holder.name.setText(applicationInfo.packageName);
        holder.size.setText(""+getFileSize(applicationInfo)+"MB");
        holder.imageView.setImageDrawable(packageManager.getApplicationIcon(applicationInfo));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewForSnackBar=view;
                String[] choices={"提取APK","发送APK","APP信息","打开APP"};
                new AlertDialog.Builder(mContext).setTitle("请选择").setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:extractApp(applicationInfo);break;
                            case 1:extractApp(applicationInfo);sendApp(applicationInfo);break;
                            case 2:showAppInfo(applicationInfo);break;
                            case 3:openApp(applicationInfo);break;
                        }
                    }
                }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return applicationInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView label,name,size;
        View linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageviewIcon);
            label=itemView.findViewById(R.id.textviewLabel);
            name=itemView.findViewById(R.id.textviewName);
            size=itemView.findViewById(R.id.textviewSize);
            linearLayout=itemView.findViewById(R.id.linearLayoutAll);
        }
    }
    public double getFileSize(ApplicationInfo applicationInfo){
        File file=new File(applicationInfo.sourceDir);
        double Bytes=file.length();
        double MB=Double.parseDouble(String.format("%.3f",(Bytes/1024)/1024));
        return MB;
    }
    public void extractApp(final ApplicationInfo applicationInfo){
        String fileName="apk提取目录";
        final File file=new File(Environment.getExternalStorageDirectory().getPath()+"/"+fileName);
        if(!file.exists()){
            file.mkdirs();
        }
        try{
            FileInputStream fileInputStream=new FileInputStream(new File(applicationInfo.sourceDir));
            final File outputFile=new File(file.getPath()+"/"+packageManager.getApplicationLabel(applicationInfo)+".apk");
            FileOutputStream fileOutputStream=new FileOutputStream(outputFile);
            byte[] bytes=new byte[1024];
            int length;
            while ((length=fileInputStream.read(bytes))>0){
                fileOutputStream.write(bytes,0,length);
            }

           Toast.makeText(mContext,"文件在根目录的apk提取目录下",Toast.LENGTH_SHORT).show();

        }catch (FileNotFoundException e){
            e.printStackTrace();Toast.makeText(mContext,"file not exist",Toast.LENGTH_SHORT).show();
        }catch (IOException e){e.printStackTrace();
    }
}

public void sendApp(ApplicationInfo applicationInfo){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("application/vnd.android.package-archive");
        intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(mContext,"fileproviderForA12121",
                new File(Environment.getExternalStorageDirectory().getPath()+"/apk提取目录/"+packageManager.getApplicationLabel(applicationInfo)+".apk")));
        mContext.startActivity(Intent.createChooser(intent,"share to ..."));
}
public void showAppInfo(ApplicationInfo applicationInfo){
    Intent intent =new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",applicationInfo.packageName,null));
    mContext.startActivity(intent);
}
public void openApp(ApplicationInfo applicationInfo){
Intent intent=packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
mContext.startActivity(intent);
}

}
