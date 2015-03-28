package com.safeness.patient.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.e_saveness_common.dao.QueryResult;
import com.safeness.e_saveness_common.remind.ReminderManager;
import com.safeness.patient.R;
import com.safeness.patient.model.Drug;
import com.safeness.patient.model.Drug_plan;
import com.safeness.patient.model.U_d;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lionnd on 2015/1/29.
 */
public class DrugSettingActivity extends AppBaseActivity {

    private ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();
    private  IBaseDao<Drug> drugDao;
    private  IBaseDao<U_d> u_dDao;
    private  IBaseDao<Drug_plan> drug_planDao;
    private  List<Drug> drugList;
    private List<U_d> u_dList;
    private List<Drug_plan> drug_planList;

    private Button btn_delete;
    private TextView txb_desc;
    private Button btn_finish;
    private TextView txb_name;
    private ListView lst_remind_list;
    private TextView txb_remind;

    private ImageView btn_back;
    private ImageView img_cell_remind;
    private ImageView img_cell_addRemind;

    private ImageView img_cell_switch;

    private MyAdapter adapter;

    private String _id = null;
    private Date selectDate = null;
    PatientApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*取出Intent中附加的数据*/
        Intent intent =getIntent();
        _id = intent.getStringExtra("drug_id");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            selectDate =df.parse(intent.getStringExtra("select_date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (_id != null){

            app = (PatientApplication)this.getApplication();
            drugDao = DaoFactory.createGenericDao(this, Drug.class);
            drug_planDao = DaoFactory.createGenericDao(this, Drug_plan.class);
            u_dDao = DaoFactory.createGenericDao(this, U_d.class);

            drugList = drugDao.queryByCondition("_id=?",_id);

            if (drugList.size() >0){
                txb_name.setText(drugList.get(0).getDrugName());
                txb_desc.setText(drugList.get(0).getDesc());
                txb_remind.setText(drugList.get(0).getLife_status() > 0 ? "提醒(已开启)" : "提醒(已关闭)");
                img_cell_switch.setBackgroundResource(drugList.get(0).getLife_status() > 0 ?R.drawable.switch_on : R.drawable.switch_off);


                drug_planList = drug_planDao.queryByCondition("u_sid=? and f_sid=? and [startdate] <= ? and [enddate] >= ?",app.getUserID(),_id,df.format(selectDate),df.format(selectDate));
                u_dList = u_dDao.queryByCondition("u_sid=? and d_id=? and [hintDay] = ?",app.getUserID(),_id,df.format(selectDate));
                mData= new ArrayList<Map<String,Object>>();
                int everyTime = drug_planList.get(0).getMedtime();
                int count = drug_planList.get(0).getEverytime();
                for(int i =0; i < everyTime; i++) {
                    Map<String,Object> item = new HashMap<String,Object>();
                    item.put("title", getTimeInterval(everyTime,i));
                    item.put("status", 1);
                    item.put("_id", null);
                    item.put("index", i);
                    item.put("count", count);
                    if (u_dList != null)
                    {
                        for (int j = 0; j < u_dList.size(); j++) {
                            if (u_dList.get(j).getHintIndex() == i)
                            item.put("status", u_dList.get(j).getLife_status());
                            item.put("_id", u_dList.get(j).get_id());
                        }
                    }
                    mData.add(item);
                }
                //Collections.sort(mData, new SortByID());
                adapter = new MyAdapter(this,mData,R.layout.drug_setting_listitem,
                        new String[]{"title","status"},new int[]{R.id.drug_setting_remind_list_label_time,R.id.drug_setting_remind_list_image_status});
                lst_remind_list.setAdapter(adapter);

                lst_remind_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Map<String, Object> map = (Map<String, Object>) DrugSettingActivity.this.adapter
                                .getItem(position);
                        U_d u_d = new U_d();
                        u_d.setU_sid(app.getUserID());
                        u_d.setD_id(_id);
                        u_d.setHintDay(df.format(selectDate));
                        u_d.setHintTime(map.get("title").toString());
                        u_d.setHintIndex(Integer.parseInt(map.get("index").toString()));
                        u_d.setLife_status(Integer.parseInt(map.get("status").toString()) > 0 ? -1 : 1);
                        if(u_dDao.insertOrUpdate(u_d, "u_sid","d_id","hintDay","hintIndex")){
                            mData.get(position).put("status", Integer.parseInt(map.get("status").toString()) > 0 ? -1 : 1);
                            adapter.mItemList = mData;
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrugSettingActivity.this.finish();
                    }
                });

                img_cell_remind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drugList.get(0).setLife_status(drugList.get(0).getLife_status() > 0 ? -1 : 1);
                        if(drugDao.update(drugList.get(0),"_id=?",_id.toString())){
                            drugList = drugDao.queryByCondition("_id=?",_id.toString());
                            txb_remind.setText(drugList.get(0).getLife_status() > 0 ? "提醒(已开启)" : "提醒(已关闭)");
                            img_cell_switch.setBackgroundResource(drugList.get(0).getLife_status() > 0 ?R.drawable.switch_on : R.drawable.switch_off);
                            switchVisible();
                        }

                    }
                });
                img_cell_addRemind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showDialog(R.id.drug_setting_remind_list_label_time);
                    }
                });

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //buildDialog(DrugSettingActivity.this).show();
                    }
                });
                switchVisible();
            }


        }

    }
    private void switchVisible(){
        img_cell_addRemind.setVisibility(drugList.get(0).getLife_status() > 0 ? View.VISIBLE: View.INVISIBLE);
        TextView textView = (TextView)findViewById(R.id.textView12);
        textView.setVisibility(drugList.get(0).getLife_status() > 0 ? View.VISIBLE: View.INVISIBLE);
        ImageView imageView = (ImageView)findViewById(R.id.imageView12);

        imageView.setVisibility(drugList.get(0).getLife_status() > 0 ? View.VISIBLE: View.INVISIBLE);
        lst_remind_list.setVisibility(drugList.get(0).getLife_status() > 0 ? View.VISIBLE: View.INVISIBLE);
        btn_finish.setVisibility(drugList.get(0).getLife_status() > 0 ? View.VISIBLE: View.INVISIBLE);

    }
    @Override
    protected int getLayoutId() {
        return R.layout.drug_setting;
    }

    @Override
    protected void setupView() {
        getViews();
    }

    @Override
    protected void initializedData() {

    }

    //初始化下层切换
    private void getViews() {
        txb_desc = (TextView)findViewById(R.id.txb_drug_setting_desc);
        txb_desc.setMovementMethod(ScrollingMovementMethod.getInstance());
        btn_finish = (Button)findViewById(R.id.btn_drug_setting_finish);
        txb_name = (TextView)findViewById(R.id.txb_drug_setting_name);
        txb_remind = (TextView)findViewById(R.id.txv_drugsetting_cell_remind);
        lst_remind_list = (ListView)findViewById(R.id.drug_setting_remind_list);


        btn_delete = (Button)findViewById(R.id.btn_drugsetting_delete);
        btn_back = (ImageView)findViewById(R.id.btn_drugsetting_back);
        img_cell_remind = (ImageView)findViewById(R.id.img_drugsetting_cell_bg3);
        img_cell_addRemind = (ImageView)findViewById(R.id.img_drugsetting_cell_bg4);
        img_cell_switch = (ImageView)findViewById(R.id.img_drugsetting_cell_remind_switch);

        lst_remind_list = (ListView)findViewById(R.id.drug_setting_remind_list);
        ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();




    }
    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id) {
            case R.id.drug_setting_remind_list_label_time:
                Date curDate   =   new   Date(System.currentTimeMillis());
                return  new TimePickerDialog(
                        this, mTimeSetListener, curDate.getHours(), curDate.getMinutes(), true);
        }
        return null;
    }
    private Dialog buildDialog(Context context) {
        AlertDialog.Builder builder = new  AlertDialog.Builder(context);
        builder.setTitle("您确定要删除么？");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        u_dDao.delete("u_sid=? and d_id=?","1",_id.toString());
                        Drug drug= new Drug();
                        drug.setLife_status(-1);
                        drugDao.update(drug,"_id=?",_id.toString());
                        DrugSettingActivity.this.finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return builder.create();
    }

    private  void testInsertRemind(Calendar cal){
        ReminderManager manager  = new ReminderManager(this);
        manager.saveState("测试1","测试2",cal,"测试3","测试4",true);

    }
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener()
            {
                public void onTimeSet(
                        TimePicker view, int hourOfDay, int minuteOfHour)
                {

                    SimpleDateFormat timeFormat = new SimpleDateFormat("1970-1-1 HH:mm:00");
                    Date date = new Date(0,0,0, hourOfDay, minuteOfHour);
                    String strDate = timeFormat.format(date);
                    Log.v("v",strDate);
                    U_d u_d = new U_d();
                    u_d.setU_sid("1");
                    u_d.setD_id(_id);
                    u_d.setCount(-1);
                    u_d.setHintDay("2000-1-1");
                    u_d.setHintTime(strDate);
                    Calendar c = Calendar.getInstance();

                    c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    c.set(Calendar.MINUTE,minuteOfHour);
                    testInsertRemind(c);

                    if(u_dDao.insert(u_d)){
                        HashMap<String ,Object> map = new HashMap<String, Object>();
                        SimpleDateFormat ttt = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try{
                            date = ConvertToDate(strDate);

                            map.put("title",ttt.format(date));}
                        catch(Exception ex){}
                        map.put("status", 1);
                        List<QueryResult> aaaad = u_dDao.execQuerySQL("select _id from u_d order by _id desc LIMIT 1");
                        map.put("_id", aaaad.size() > 0 ? Integer.parseInt(aaaad.get(0).getNameValueMap().get("_id").toString()) : -1);
                        mData.add(map);
                        Collections.sort(mData, new SortByID());
                        adapter.mItemList =  mData;
                        adapter.notifyDataSetChanged();
                    }
                }
            };
    private class MyAdapter extends SimpleAdapter{
        int count = 0;
        private List<Map<String, Object>> mItemList;
        public MyAdapter(Context context, List<? extends Map<String, Object>> data,
                         int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mItemList = (List<Map<String, Object>>) data;
            if(data == null){
                count = 0;
            }else{
                count = data.size();
            }
        }
        public int getCount() {
            return mItemList.size();
        }

        public Object getItem(int pos) {
            return mItemList.get(pos);
        }

        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String ,Object> map = mItemList.get(position);
            View view = super.getView(position, convertView, parent);
            ImageView imageview = (ImageView)view.findViewById(R.id.drug_setting_remind_list_image_status);
            TextView tv = (TextView)view.findViewById(R.id.drug_setting_remind_list_label_time);
            imageview.setVisibility(Integer.parseInt(map.get("status").toString())>0 ? View.VISIBLE : View.INVISIBLE);
            SimpleDateFormat ttt = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try{
            Date date = ConvertToDate(map.get("title").toString());

            tv.setText(ttt.format(date));}
            catch(Exception ex){}
            return view;
        }


    }

    class SortByID implements Comparator {
        public int compare(Object o1, Object o2) {
            Map<String,Object> s1 = (Map<String,Object>) o1;
            Map<String,Object> s2 = (Map<String,Object>) o2;
            return s2.get("_id").toString().compareTo(s1.get("_id").toString());
        }
    }
    private  Date ConvertToDate(String strDate) throws Exception{
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dt.parse(strDate);

    }

    private String getTimeInterval(int medtime, int index){
        String reStr = "";
        int hourInterval = getMyInt(getMyInt(medtime,60),60);
        switch (hourInterval){
            case 1:
                reStr = "12:00";
                break;
            case 2:
                switch (index){
                    case 1:
                        reStr = "9:00";
                        break;
                    case 2:
                        reStr = "17:00";
                        break;
                }
                break;
            case 3:
                switch (index){
                    case 1:
                        reStr = "8:00";
                        break;
                    case 2:
                        reStr = "12:00";
                        break;
                    case 3:
                        reStr = "18:00";
                        break;
                }
                break;
            case 4:
                switch (index){
                    case 1:
                        reStr = "8:00";
                        break;
                    case 2:
                        reStr = "12:00";
                        break;
                    case 3:
                        reStr = "16:00";
                    case 4:
                        reStr = "20:00";
                        break;
                }
                break;
            default:
                reStr =String.valueOf(getMyInt(14,hourInterval)*index+6)+":00" ;
                break;
        }
        return reStr;
    }
    private int getMyInt(int a,int b) {
        return(((double)a/(double)b)>(a/b)?a/b+1:a/b);
    }
}
