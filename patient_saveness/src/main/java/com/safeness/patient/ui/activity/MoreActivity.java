package com.safeness.patient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.safeness.patient.R;

/**
 * Created by Lionnd on 2015/2/6.
 */
public class MoreActivity extends Activity  {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_more);

        findViewById(R.id.more_btn_user_infor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(MoreActivity.this, UserInforActivity.class);
                MoreActivity.this.startActivity(it1);
            }
        });
    }
    public void back(View view){
        this.finish();
    }

}
