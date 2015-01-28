package com.safeness.patient.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.patient.R;
import com.safeness.patient.ui.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashActivity extends AppBaseActivity {


    private ProgressDialog pd;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mContext = this;
        processThread();


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void initializedData() {

    }


    private Handler handler =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            pd.dismiss();

            Intent it = new Intent();
            it.setClass(mContext,LoginActivity.class);
            SplashActivity.this.startActivity(it);
            finish();

        }
    };


    private void processThread(){
        //构建一个下载进度条
        pd= ProgressDialog.show(mContext, "同步", "正在同步…");
        new Thread(){
            public void run(){
                //在这里执行长耗时方法
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //执行完毕后给handler发送一个消息
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

}
