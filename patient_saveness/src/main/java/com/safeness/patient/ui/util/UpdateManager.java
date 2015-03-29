package com.safeness.patient.ui.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.safeness.e_saveness_common.util.Constant;
import com.safeness.patient.R;
import com.safeness.patient.ui.activity.LoginActivity;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// 检查软件更新  zhang.yl 20140523

public class UpdateManager {
    //下载中
    private static final int DOWNLOAD = 1;
    //下载结束
    private static final int DOWNLOAD_FINISH = 2;

    private static final int IS_NEED_UPDATE = 3;

    private static final String TAG = "UPDATEMANAGER";
    //下载保存路径
    private String mSavePath;
    //记录进度条数量
    private int progress;
    //是否取消更新
    private boolean cancelUpdate = false;

    private Context mContext;
    //更新进度条
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    //是否强制更新
    private boolean mustDownLoad = false;

    private String  versionName;

    private String versionInfos;



    private  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                case IS_NEED_UPDATE:

                    boolean isNeedUpdate = msg.getData().getBoolean("isNeedUpdate");
                    if(isNeedUpdate){

                        if(mustDownLoad){

                            showDownloadDialog();
                        }else{
                            // 显示提示对话框
                            showNoticeDialog(true);
                        }
                    }else{

                        goToNext();
                       // CheckAccount.getSipProfile(mContext);
                    }
//				Activity activty = (Activity)mContext;
//				activty.finish();
                    break;
                default:
                    break;
            }
        };
    };


    private void goToNext(){

        Intent mainIntent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(mainIntent);


    }


    public UpdateManager(Context context) {
        this.mContext = context;

        mSavePath = "data/data/" + packageName + "/";

    }


    /**
     * 直接更新
     */
    public void update(){
        showNoticeDialog(false);
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {

        new Thread(){

            @Override
            public void run(){
                boolean isNeedUpdate = isNeedUpdate();

                Message msg = new Message();
                msg.what = IS_NEED_UPDATE;
                Bundle b = new Bundle();

                b.putBoolean("isNeedUpdate", isNeedUpdate);
                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        }.start();

    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    public boolean isNeedUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);

        versionInfos = getUpdateInfo();
        int serviceCode = -1;// Integer.valueOf(mHashMap.get("version"));

        serviceCode = getVersionFile();

        // 版本判断
        if (serviceCode > versionCode) {
            return true;
        }else{
            return false;
        }


    }

    /**
     * 获取软件版本号
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public String getVersionName(Context context){
        String versionName = "";
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionName = context.getPackageManager().getPackageInfo(
                    packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * @param isCloseActivity 是否关闭打开更新的Activity
     */
    private void showNoticeDialog(final boolean isCloseActivity) {
        // 构造对话框
        // 构造软件下载对话框


        AlertDialog.Builder builder = new Builder(mContext);
        String title = mContext.getString(R.string.soft_update_title) ;
        title +=":"+versionName;
        builder.setTitle(title);
        builder.setMessage(versionInfos);

        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //登陆账号
                        if(isCloseActivity){
                            goToNext();
                        }


                    }
                });
        Dialog noticeDialog = builder.create();
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {


        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);

        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;
                    }
                });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    private String getUpdateInfo(){
        String infos = "";

        try {

            URL url = new URL(Constant.getUpdateUrl() + "/updateinfo.txt");// mHashMap.get("url"));
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.connect();

            // 创建输入流
            InputStream is = conn.getInputStream();

            int buffersize = is.available();// 取得输入流的字节长度

            byte buffer[] = new byte[buffersize];

            is.read(buffer);// 将数据读入数组
            is.close();// 读取完毕后要关闭流。

            String result = EncodingUtils.getString(buffer, "UTF-8");
            infos = result;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {


                URL url = new URL(Constant.getUpdateUrl() + strApk);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();

                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(mSavePath, strFile);
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 计算进度条位置
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD);
                    if (numread <= 0) {
                        System.out.println("下载完成!");
                        // 下载完成
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                } while (!cancelUpdate);// 点击取消就停止下载.
                fos.close();
                is.close();

            } catch (Exception e) {
                System.out.println("IOException:" + e.getMessage());
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    };

    /**
     * 安装APK文件
     */
    private void installApk() {
        // 加读写权限
        String[] command = { "chmod", "777", mSavePath + strFile };
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 升级
        File apkfile = new File(mSavePath, strFile);// mHashMap.get("name"));
        if (!apkfile.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkfile),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // 服务器地址

    private String strApk = "/patient_saveness-release.apk";

    String strFile = "tmp.apk";
    String packageName = "com.safeness.patient";

    // -----------读取版本文件-----------
    private int getVersionFile() {



        int serviceCode = 0;
        try {

            URL url = new URL(Constant.getUpdateUrl() + "/versions.json");// mHashMap.get("url"));
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.connect();

            // 创建输入流
            InputStream is = conn.getInputStream();

            int buffersize = is.available();// 取得输入流的字节长度

            byte buffer[] = new byte[buffersize];

            is.read(buffer);// 将数据读入数组
            is.close();// 读取完毕后要关闭流。

            String result = EncodingUtils.getString(buffer, "UTF-8");
            JSONObject object = new JSONObject(result);
            serviceCode = object.getInt("verCode");

            versionName = object.getString("verName");
            mustDownLoad = object.getBoolean("mustDownload");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return serviceCode;

    }

    String strVerFile = "ver";

    // --------end-----下载版本文件----------------------------------

}
