package com.safeness.patient.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.e_saveness_common.dao.QueryResult;
import com.safeness.patient.R;
import com.safeness.patient.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Lionnd on 2015/2/6.
 */
public class UserInforActivity extends AppBaseActivity {
    /** Called when the activity is first created. */
    private TextView txv_userinfor_title;

    private EditText txb_userinfor_name;
    private EditText txb_userinfor_age;
    private EditText txb_userinfor_height;
    private EditText txb_userinfor_weight;
    private EditText txb_userinfor_mail;
    private EditText txb_userinfor_tel;
    private TextView btn_gender_f;
    private TextView btn_gender_m;

    private String gender = "男";

    PatientApplication app;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_infor;
    }

    public void back(View view){

        User user = new User();
        //user.setUsername(txb_userinfor_name.getText());

        this.finish();
    }

    @Override
    protected void setupView() {
        getViews();
    }

    @Override
    protected void initializedData() {
        app = (PatientApplication) this.getApplication();
        IBaseDao<User> userDao = DaoFactory.createGenericDao(this, User.class);
        List<QueryResult> userinfor = userDao.execQuerySQL("select * from user where server_id=?", app.getUserID());

        if (userinfor.size() > 0) {

            CharSequence aaa= userinfor.get(0).getStringProperty("username");
            txv_userinfor_title.setText(aaa);
            txb_userinfor_name.setText(userinfor.get(0).getStringProperty("username"));

            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                long days = (now.getTime() - sdf.parse(userinfor.get(0).getStringProperty("birthday")).getTime()) / (1000 * 60 * 60 * 24);//得到总天数 years=days/365;
                txb_userinfor_age.setText(String.valueOf(days/365));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            txb_userinfor_height.setText(userinfor.get(0).getStringProperty("height"));
            txb_userinfor_weight.setText(userinfor.get(0).getStringProperty("weight"));
            txb_userinfor_mail.setText(userinfor.get(0).getStringProperty("mail").equals("null") ? null : userinfor.get(0).getStringProperty("mail"));
            txb_userinfor_tel.setText(userinfor.get(0).getStringProperty("tel").equals("null") ? null : userinfor.get(0).getStringProperty("tel"));

            if (userinfor.get(0).getStringProperty("gender").equals("女")){
                btn_gender_f.setBackgroundColor(Color.parseColor("#ff4a4a"));
                btn_gender_m.setBackgroundColor(Color.parseColor("#e2e2e2"));
                btn_gender_f.setTextColor(Color.parseColor("#ffffff"));
                gender = "女";
            }
            else{
                btn_gender_f.setBackgroundColor(Color.parseColor("#e2e2e2"));
                btn_gender_m.setBackgroundColor(Color.parseColor("#60b3ed"));
                btn_gender_m.setTextColor(Color.parseColor("#ffffff"));
                gender = "男";
            }
        }

    }

    //初始化下层切换
    private void getViews() {
        txv_userinfor_title = (TextView)this.findViewById(R.id.txv_userinfor_title);
        txb_userinfor_name = (EditText)this.findViewById(R.id.txb_userinfor_name);
        txb_userinfor_age = (EditText)this.findViewById(R.id.txb_userinfor_age);
        txb_userinfor_height = (EditText)this.findViewById(R.id.txb_userinfor_height);
        txb_userinfor_weight = (EditText)this.findViewById(R.id.txb_userinfor_weight);
        txb_userinfor_mail = (EditText)this.findViewById(R.id.txb_userinfor_mail);
        txb_userinfor_tel = (EditText)this.findViewById(R.id.txb_userinfor_tel);

        btn_gender_f = (TextView)this.findViewById(R.id.btn_userinfor_gender_f);
        btn_gender_m = (TextView)this.findViewById(R.id.btn_userinfor_gender_m);

        btn_gender_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_gender_f.setBackgroundColor(Color.parseColor("#ff4a4a"));
                btn_gender_m.setBackgroundColor(Color.parseColor("#e2e2e2"));
                btn_gender_f.setTextColor(Color.parseColor("#ffffff"));
                gender = "女";
            }
        });
        btn_gender_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_gender_f.setBackgroundColor(Color.parseColor("#e2e2e2"));
                btn_gender_m.setBackgroundColor(Color.parseColor("#60b3ed"));
                btn_gender_m.setTextColor(Color.parseColor("#ffffff"));
                gender = "男";
            }
        });
    }
}
