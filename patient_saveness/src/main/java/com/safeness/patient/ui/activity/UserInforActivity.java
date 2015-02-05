package com.safeness.patient.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.safeness.patient.R;

/**
 * Created by Lionnd on 2015/2/6.
 */
public class UserInforActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_user_infor);
    }
    public void back(View view){
        this.finish();
    }
}
