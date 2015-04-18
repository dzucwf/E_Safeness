package com.safeness.patient.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.safeness.patient.R;
import com.safeness.patient.ui.util.UpdateManager;

/**
 * Created by Lionnd on 2015/2/2.
 */
public class LauncherActivity extends Activity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_launcher);
        //升级助手
        UpdateManager updateTester = new UpdateManager(this);

        updateTester.checkUpdate();

        //这里可以加入相关的启动代码
       // this.finish();
    }

}