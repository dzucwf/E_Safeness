package com.safeness.patient.ui.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.e_saveness_common.net.SourceJsonHandler;
import com.safeness.e_saveness_common.util.Constant;
import com.safeness.e_saveness_common.util.DateTimeUtil;
import com.safeness.patient.R;
import com.safeness.patient.bussiness.WebServiceName;
import com.safeness.patient.model.BloodGlucose;
import com.safeness.patient.ui.util.GlucoseUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EISAVISA on 2015/1/24.
 */
public class GlucoseInputActivity extends AppBaseActivity {


    private  final static  String TAG = "GlucoseInputActivity";
    private static final int INPUT_GLUCOSE = 21 ;
    private static final int INPUT_GLUCOSE_ERROR = 22;
    Context mContext;

    private TextView glucose_time_dialog_tv;

    private EditText glucose_value_et;

    private EditText memo_et;

    private TextView input_time_et;

    int hour;

    int minute;

    boolean isInsert = true;

    private String server_id;

    private int afterOrBefore;

    private int takeTag;

    private  String inputTime;
    Calendar calendarInput;

    GlucoseUtil glucoseUtil;
    @Override
    protected int getLayoutId() {
        return R.layout.glucose_input_dialog;
    }



    @Override
    protected void setupView() {

        mContext = this;
        glucose_time_dialog_tv = (TextView)this.findViewById(R.id.glucose_time_dialog_tv);
        glucose_value_et= (EditText)this.findViewById(R.id.glucose_value_et);
        glucose_value_et.addTextChangedListener(glucoseValueListener);
        memo_et = (EditText)this.findViewById(R.id.memo_et);
        input_time_et = (TextView)this.findViewById(R.id.input_time_et);
        input_time_et.setOnClickListener(TimeListener);
        server_id = getIntent().getStringExtra("server_id");
        afterOrBefore = getIntent().getIntExtra("afterOrBefore",0);
        takeTag = getIntent().getIntExtra("takeTag",0);;
        inputTime = getIntent().getStringExtra("inputTime");

        try {
            calendarInput = DateTimeUtil.getSelectCalendar(inputTime,DateTimeUtil.NORMAL_PATTERN);
            hour = calendarInput.get(Calendar.HOUR);
            minute = calendarInput.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView btn_save = (TextView)this.findViewById(R.id.glucose_btn_save);
        TextView btn_clear = (TextView)this.findViewById(R.id.glucose_btn_clear);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearValue();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToServer();
            }
        });

        getGlucose_time_Text();
        clearValue();
        glucoseUtil = new GlucoseUtil(this);
    }


    /**
     * 当输入是，要根据数据的数据变色
     */
    TextWatcher glucoseValueListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {


            try {
                glucose_value_et.setTextColor(glucoseUtil.setTextColorByValue(Double.parseDouble(s.toString())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 弹出日期选择框
     */
    View.OnClickListener TimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if(calendarInput==null){
                        calendarInput = Calendar.getInstance();
                    }
                    calendarInput.set(Calendar.HOUR,hourOfDay);
                    calendarInput.set(Calendar.MINUTE,minute);
                    input_time_et.setText(DateTimeUtil.getSelectedDate(calendarInput,DateTimeUtil.NORMAL_PATTERN));
                }
            },hour,minute,true);

            timePickerDialog.show();
        }
    };
    @Override
    protected void initializedData() {

       IBaseDao<BloodGlucose>  daoFactory= DaoFactory.createGenericDao(mContext, BloodGlucose.class);

        try {
            BloodGlucose bloodGlucose =  daoFactory.queryUniqueRecord("server_id=?",server_id+"");
            if(bloodGlucose == null){
                isInsert = true;
                input_time_et.setText(DateTimeUtil.getSelectedDate(calendarInput,DateTimeUtil.NORMAL_PATTERN));
            }else{
                isInsert = false;

                getInitialValue(bloodGlucose);
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
            isInsert = false;
        }
    }

    /**如果已经存在数据则要填充数据
     * @param bloodGlucose
     */
    private  void getInitialValue(BloodGlucose bloodGlucose){

        String str = String.valueOf(bloodGlucose.getBloodGlucose());
        Log.d(TAG,"value is "+str);
        glucose_value_et.setText(str);
        glucose_value_et.setTextColor(glucoseUtil.setTextColorByValue(bloodGlucose.getBloodGlucose()));
        if(!TextUtils.isEmpty(bloodGlucose.getMemo())){
            memo_et.setText(bloodGlucose.getMemo());
        }
        try {
            calendarInput = DateTimeUtil.getSelectCalendar(bloodGlucose.getTakeDate(), DateTimeUtil.NORMAL_PATTERN);
            hour = calendarInput.get(Calendar.HOUR);
            minute = calendarInput.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        input_time_et.setText(bloodGlucose.getTakeDate());
    }

    /**
     * 预先填充测量时间
     */
    private void getGlucose_time_Text(){

        String afterOrBeforeStr = "";
        if(afterOrBefore == 0){
            afterOrBeforeStr = "前";
        }else{
            afterOrBeforeStr="后";
        }
        String dinnerType = "";

         switch(takeTag) {

            case 0:
                dinnerType = "早餐";
                break;
             case 1:
                 dinnerType = "中餐";
                 break;
             case 2:
                 dinnerType = "晚餐";
                 break;
             case 3:
                 dinnerType = "其他";
                 break;
             default:
                 dinnerType = "早餐";
                 break;
        }
        glucose_time_dialog_tv.setText(dinnerType+afterOrBeforeStr);
    }

    /**
     * 清空选择
     */
    private  void clearValue(){

        glucose_value_et.setText("");
        memo_et.setText("");
    }



    public void cancel(View view){

        this.finish();
    }


    /**
     * 存入数据库
     */
    public void save(){
        IBaseDao<BloodGlucose>  daoFactory= DaoFactory.createGenericDao(mContext, BloodGlucose.class);
        float value = 0.0f;
        if(!TextUtils.isEmpty(glucose_value_et.getText())){
            try {
                value = Float.parseFloat(glucose_value_et.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(mContext,getString(R.string.input_tip),Toast.LENGTH_LONG).show();
            return;
        }
        if(calendarInput ==null){
            calendarInput = Calendar.getInstance();
        }
        String updateOrInsertTime =  DateTimeUtil.getSelectedDate(calendarInput,DateTimeUtil.NORMAL_PATTERN);
        String user_id = PatientApplication.getInstance().getUserName();
        BloodGlucose bloodGlucose = new BloodGlucose(server_id,value,takeTag,updateOrInsertTime,afterOrBefore,user_id);
        //根据server_id来生成数据
        daoFactory.insertOrUpdate(bloodGlucose,"server_id");
        finish();
    }

    /*

    序号	参数	描述
1	uName	用户名
2	takeDate	提交日期及时间
3	bloodGlucose	血糖值
4	takeTag	录入时间段(0,1,2,3) 0早饭，1 中饭，2晚饭3，其他
5	AfterOrBefore	0之前，1之后

     */
    private void saveToServer(){

        String url = Constant.getServier() + WebServiceName.glucosseInput;
        Map<String, String> parameter = new HashMap<String, String>();
        String userName = PatientApplication.getInstance().getUserName();
        if(calendarInput ==null){
            calendarInput = Calendar.getInstance();
        }
        float value = 0.0f;
        if(!TextUtils.isEmpty(glucose_value_et.getText())){
            try {
                value = Float.parseFloat(glucose_value_et.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(mContext,getString(R.string.input_tip),Toast.LENGTH_LONG).show();
            return;
        }

        parameter.put("uName", userName);
        parameter.put("takeDate", DateTimeUtil.getSelectedDate(calendarInput,""));
        parameter.put("bloodGlucose", value+"");

        parameter.put("takeTag", takeTag+"");
        parameter.put("AfterOrBefore", afterOrBefore+"");
        this.request(parameter, url, INPUT_GLUCOSE, this, new SourceJsonHandler());
    }


    private Handler hander = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case INPUT_GLUCOSE:

                    break;
                case INPUT_GLUCOSE_ERROR:
                    String messageStr = msg.getData().getString("message");
                    Toast.makeText(mContext, messageStr, Toast.LENGTH_LONG).show();
                    break;

            }
            //无论数据库成功与失败，都会保存到本地数据中
            save();
            super.handleMessage(msg);
            dissProgressDialog();
        }
    };


    @Override
    public void onSuccess(Object obj, int reqCode) {


        if (reqCode == INPUT_GLUCOSE) {
            JSONObject jsobject = (JSONObject) obj;
            Message msg = new Message();
            Bundle b = new Bundle();
            try {
                b.putString("message", jsobject.getString("message"));
                msg.setData(b);

                if (jsobject.getString("code").equals("BG_UPDATE_SUCCESS")) {


                    msg.what = INPUT_GLUCOSE;


                } else {
                    msg.what = INPUT_GLUCOSE_ERROR;
                }
                hander.sendMessage(msg);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        super.onSuccess(obj, reqCode);
    }

    @Override
    public void onFail(int errorCode, int reqCode) {
        super.onFail(errorCode, reqCode);
    }



    public void clear(View view){

        clearValue();
    }


}
