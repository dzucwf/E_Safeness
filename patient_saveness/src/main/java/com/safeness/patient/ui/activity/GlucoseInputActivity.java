package com.safeness.patient.ui.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.e_saveness_common.util.DateTimeUtil;
import com.safeness.patient.R;
import com.safeness.patient.dao.DaoFactory;
import com.safeness.patient.dao.IBaseDao;
import com.safeness.patient.model.BloodGlucose;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by EISAVISA on 2015/1/24.
 */
public class GlucoseInputActivity extends AppBaseActivity {


    private  final static  String TAG = "GlucoseInputActivity";
    Context mContext;

    private TextView glucose_time_dialog_tv;

    private EditText glucose_value_et;

    private EditText memo_et;

    private TextView input_time_et;

    int hour;

    int minute;

    boolean isInsert = true;

    private int server_id;

    private int afterOrBefore;

    private int takeTag;

    private  String inputTime;
    Calendar calendarInput;
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
        server_id = getIntent().getIntExtra("server_id",0);
        afterOrBefore = getIntent().getIntExtra("afterOrBefore",0);
        takeTag = getIntent().getIntExtra("takeTag",0);;
        inputTime = getIntent().getStringExtra("inputTime");

        try {
            calendarInput = DateTimeUtil.getSelectCalendar(inputTime,"yyyy-MM-dd HH:mm:ss");
            hour = calendarInput.get(Calendar.HOUR);
            minute = calendarInput.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getGlucose_time_Text();
        clearValue();
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
                Log.d(TAG,s.toString());
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
                }
            },hour,minute,true);
        }
    };
    @Override
    protected void initializedData() {

       IBaseDao<BloodGlucose>  daoFactory= DaoFactory.createGenericDao(mContext, BloodGlucose.class);

        try {
            BloodGlucose bloodGlucose =  daoFactory.queryUniqueRecord("server_id=?",server_id+"");
            if(bloodGlucose == null){
                isInsert = true;
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

        glucose_value_et.setText(bloodGlucose.getBloodGlucose()+"");
        glucose_value_et.setTextColor(setTextColorByValue(bloodGlucose.getBloodGlucose()));
        if(!TextUtils.isEmpty(bloodGlucose.getMemo())){
            memo_et.setText(bloodGlucose.getMemo());
        }
        input_time_et.setText(inputTime);
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
    }

    private  int setTextColorByValue(double value){

        if(value >9 || value <3){
            return getResources().getColor(R.color.red);
        }else{
            return getResources().getColor(R.color.blue);
        }
    }

    public void cancel(View view){

        this.finish();
    }


    /**保存
     * @param view
     */
    public void save(View view){
        IBaseDao<BloodGlucose>  daoFactory= DaoFactory.createGenericDao(mContext, BloodGlucose.class);
        double value = 0;
        if(!TextUtils.isEmpty(glucose_value_et.getText())){
                value = Double.parseDouble(glucose_value_et.getText().toString());
        }else{
            Toast.makeText(mContext,getString(R.string.input_tip),Toast.LENGTH_LONG).show();
        }
        if(calendarInput ==null){
            calendarInput = Calendar.getInstance();
        }
        String updateOrInsertTime =  DateTimeUtil.getSelectedDate(calendarInput,"yyyy-MM-dd HH:mm:ss");
        BloodGlucose bloodGlucose = new BloodGlucose(server_id,value,takeTag,updateOrInsertTime);
        daoFactory.insertOrUpdate(bloodGlucose);
        finish();
    }

    public void clear(View view){

        clearValue();
    }
}
