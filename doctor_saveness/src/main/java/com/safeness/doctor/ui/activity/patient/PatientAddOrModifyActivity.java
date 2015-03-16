package com.safeness.doctor.ui.activity.patient;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.safeness.doctor.R;
import com.safeness.doctor.model.Patient;
import com.safeness.e_saveness_common.base.AppBaseActivity;

/**
 * Created by EISAVISA on 2015/3/16.
 */
public class PatientAddOrModifyActivity extends AppBaseActivity {



    private EditText et_name;
    private EditText et_patientNum;
    private EditText et_contact;
    private EditText et_address;
    private EditText et_qq;
    private EditText et_weixin;
    private EditText et_email;

    private RadioGroup group_sex;

    private TextView tv_birthday;
    @Override
    protected int getLayoutId() {
        return R.layout.patient_add_modify;
    }

    @Override
    protected void setupView() {
        initialView();
    }

    private  void initialView(){
        et_name = (EditText)this.findViewById(R.id.tx1);
        et_patientNum = (EditText)this.findViewById(R.id.tx4);
        et_contact = (EditText)this.findViewById(R.id.tx6);
        et_address = (EditText)this.findViewById(R.id.tx7);
        et_qq = (EditText)this.findViewById(R.id.tx8);
        et_weixin = (EditText)this.findViewById(R.id.tx9);
        et_email = (EditText)this.findViewById(R.id.tx10);

        group_sex = (RadioGroup)this.findViewById(R.id.rg_sex);
        tv_birthday = (TextView)this.findViewById(R.id.tx3);

        View btn2_tv  = this.findViewById(R.id.btn2_tv);
        btn2_tv.setVisibility(View.VISIBLE);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn2_iv:
                savePatient();
                break;
            default:
                break;
        }
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(et_name.getText())){

            Toast.makeText(this,"请输入患者的用户名",Toast.LENGTH_LONG).show();
            return false;
        }else  if(TextUtils.isEmpty(et_patientNum.getText())){
            Toast.makeText(this,"请输入患者的编号",Toast.LENGTH_LONG).show();
            return false;
        }
        return  true;
    }

    private  void savePatient(){
        if(checkInput()){

            //TODO：这个要调用后台服务，现在只临时存入手机数据库
            Patient p = new Patient();
            p.setPatient_code(et_patientNum.getText().toString());
            p.setName(et_name.getText().toString());
            p.setAddress(et_address.getText().toString());
            p.setQQ(et_qq.getText().toString());
            p.setWeixin(et_weixin.getText().toString());
            p.setBirthday(tv_birthday.getText().toString());
            p.setTel(et_contact.getText().toString());
        }
    }

    @Override
    protected void initializedData() {

    }
}
