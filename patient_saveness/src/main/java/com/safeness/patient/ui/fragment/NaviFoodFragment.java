package com.safeness.patient.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.e_saveness_common.dao.QueryResult;
import com.safeness.e_saveness_common.net.SourceJsonHandler;
import com.safeness.e_saveness_common.util.Constant;
import com.safeness.patient.R;
import com.safeness.patient.bussiness.WebServiceName;
import com.safeness.patient.model.Food;
import com.safeness.patient.model.U_f;
import com.safeness.patient.ui.activity.HistoryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//饮食
public class NaviFoodFragment extends AppBaseFragment {

    private String Pid;

    private  TextView txv_dateText;
    private ListView listView;

    private MyAdapter adapter = null;
    private LinearLayout ll_segment;

    private ImageView btn_food_nav_item;
    private LinearLayout btn_eated;
    private ImageView btn_eated_status;
    private TextView btn_food_nav_sync;


    private  final  int OPEN_CALENDAR_RQ=11;
    private  final int SET_DATE_RESULT = 12;

    //日期控件选择的时间
    private  Calendar saveCalendar = Calendar.getInstance();

    private int cutTab = 1;
    private Date selectDate = Calendar.getInstance().getTime();

    private IBaseDao<Food> foodDao;
    private IBaseDao<U_f> u_fdDao;

    private Handler handler=null;
    PatientApplication app;

    private boolean hasSyncData = false;
    //
    @Override
    protected int getLayoutId() {
        return R.layout.navi_hp_food;
    }


    @Override
    public void onStart() {
        super.onStart();
        btn_eated_status.setVisibility(View.INVISIBLE);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WebServiceName.GETFOOD_RQ:

                        Toast.makeText(getActivity(),msg.getData().getString("message"), Toast.LENGTH_SHORT).show();

                        adapter = new MyAdapter(getActivity(),getLocalData(),R.layout.food_listitem,
                                new String[]{"title","desc","calorie","status","_id"},new int[]{R.id.food_list_item_title,R.id.food_list_item_desc});
                        listView.setAdapter(adapter);
                        break;
                    default:
                        Toast.makeText(getActivity(),msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected void setupView() {
        getViews();

        app = (PatientApplication) getActivity().getApplication();

        //segment点击事件
        for (int i = 0; i < ll_segment.getChildCount(); i++) {
            View v = ll_segment.getChildAt(i);
            if ( v instanceof LinearLayout){
                LinearLayout ll_segment_item = (LinearLayout)ll_segment.getChildAt(i);
                ll_segment_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearSegmentItemSelectStatus();
                        if ( v instanceof LinearLayout){
                            LinearLayout ll_segment_item = (LinearLayout)v;
                            ll_segment_item.setBackgroundResource(R.drawable.segment_btn_center_pressed);
                            for (int j = 0; j < ll_segment_item.getChildCount(); j++){
                                if (ll_segment_item.getChildAt(j) instanceof TextView){
                                    TextView ll_segment_item_title = (TextView)ll_segment_item.getChildAt(j);
                                    ll_segment_item_title.setTextColor(0xffffffff);
                                    if (ll_segment_item.getId() ==R.id.food_ll_item_1){cutTab = 1;}
                                    else if (ll_segment_item.getId() ==R.id.food_ll_item_2){cutTab = 2;}
                                    else if (ll_segment_item.getId() ==R.id.food_ll_item_3){cutTab = 3;}
                                    else if (ll_segment_item.getId() ==R.id.food_ll_item_4){cutTab = 4;}
                                    adapter = new MyAdapter(getActivity(),getLocalData(),R.layout.food_listitem,
                                            new String[]{"title","desc","calorie","status","_id"},new int[]{R.id.food_list_item_title,R.id.food_list_item_desc});
                                    listView.setAdapter(adapter);
                                    //listView.setAdapter(adapter);

                                    btn_eated_status.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                });
            }
        }
        //list
        //mData= new ArrayList<Map<String,Object>>();


        adapter = new MyAdapter(getActivity(),getLocalData(),R.layout.food_listitem,
                new String[]{"title","desc","calorie","status","_id"},new int[]{R.id.food_list_item_title,R.id.food_list_item_desc});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.mItemList.get(position).put("status", Integer.parseInt(adapter.mItemList.get(position).get("status").toString()) > 0 ? -1 : 1);
                adapter.notifyDataSetChanged();

                btn_eated_status.setVisibility(View.INVISIBLE);
            }
        });

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //saveCalendar = DateTimeUtil.getSelectCalendar(df.format(date), DateTimeUtil.NORMAL_PATTERN);

        txv_dateText.setText(df.format(Calendar.getInstance().getTime()));

        btn_food_nav_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1=new Intent(getActivity(),HistoryActivity.class);
                in1.putExtra("chart_type","food");
                startActivity(in1);
            }
        });

        btn_food_nav_sync.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getList();
             }
         });


        btn_eated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* List<User> insertUserList = new ArrayList<User>();
        for(int i = 0; i<10;++i){
            insertUserList.add(new User(i+10, "BBB"+i));
        }
        userDao.batchInsert(insertUserList);*/
                for(int i = 0; i<adapter.mItemList.size();++i){
                    U_f u_f = new U_f();
                    u_f.setLife_status(Integer.parseInt(adapter.mItemList.get(i).get("status").toString()));
                    u_fdDao.update(u_f, "_id=?", adapter.mItemList.get(i).get("_id").toString());
                }
                btn_eated_status.setVisibility(View.VISIBLE);
            }
        });

    }

    private void clearSegmentItemSelectStatus(){
        for (int i = 0; i < ll_segment.getChildCount(); i++) {
            View v = ll_segment.getChildAt(i);
            if ( v instanceof LinearLayout){
                LinearLayout ll_segment_item = (LinearLayout)ll_segment.getChildAt(i);
                ll_segment_item.setBackgroundResource(R.drawable.segment_btn_center_normal);
                for (int j = 0; j < ll_segment_item.getChildCount(); j++){
                    if (ll_segment_item.getChildAt(j) instanceof TextView){
                        TextView ll_segment_item_title = (TextView)ll_segment_item.getChildAt(j);
                        ll_segment_item_title.setTextColor(0xff666666);
                    }
                }
            }
        }
    }
    @Override
    protected void initializedData() {

    }
    //初始化下层切换
    private void getViews() {
        listView = (ListView)getActivity().findViewById(R.id.food_list);
        ll_segment = (LinearLayout)getActivity().findViewById(R.id.food_ll_segment);
        txv_dateText = (TextView)getActivity().findViewById(R.id.txv_food_nav_date_text);

        btn_food_nav_item = (ImageView)getActivity().findViewById(R.id.btn_food_nav_item);
        btn_eated = (LinearLayout)getActivity().findViewById(R.id.food_ll_bottomBar);
        btn_eated_status = (ImageView)getActivity().findViewById(R.id.food_ll_bottomBar_status);

        btn_food_nav_sync = (TextView)getActivity().findViewById(R.id.txv_food_nav_sync);
    }

    private String getDateBg(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return df.format(date);
    }
    private String getDateBg(Date date, boolean isshort){
        if (isshort){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        }
        else{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            return df.format(date);
        }

    }
    private String getDateEd(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return df.format(date);
    }

    /**
     * 从日历控件返回后，改变当前的日期,并加载当前数据
     *
     * @param date
     */
    public void setSelectedDate(Date date) {
        try {
            selectDate = date;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //saveCalendar = DateTimeUtil.getSelectCalendar(df.format(date), DateTimeUtil.NORMAL_PATTERN);

            txv_dateText.setText(df.format(date));

            adapter = new MyAdapter(getActivity(),getLocalData(),R.layout.food_listitem,
                    new String[]{"title","desc","calorie","status","_id"},new int[]{R.id.food_list_item_title,R.id.food_list_item_desc});
            listView.setAdapter(adapter);
            //setCalText(selected_calendar);

            //fillChartData();
            //loadCurrentTimeValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取处方id
     *
     */
    private void getList() {
        String url = Constant.getServier() + WebServiceName.prescription;
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("uName", app.getUserName());
        java.text.SimpleDateFormat format = new  java.text.SimpleDateFormat("yyyy-MM-dd");
        parameter.put("date", format.format(selectDate));

        this.request(parameter, url, WebServiceName.GETPRESCRIPTION_ID, this, new SourceJsonHandler());
    }

    private ArrayList<Map<String,Object>> getLocalData(){
        ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();

        foodDao = DaoFactory.createGenericDao(getActivity(), Food.class);
        u_fdDao = DaoFactory.createGenericDao(getActivity(), U_f.class);

        List<U_f> u_fList = u_fdDao.queryByCondition("u_sid=? and suggestDate between ? and ? and type=?",  app.getUserID(), getDateBg(selectDate, true),getDateEd(selectDate),String.valueOf(cutTab));

        for(int i =0; i < u_fList.size(); i++) {

            List<Food> tempFList = foodDao.queryByCondition("_id=?",u_fList.get(i).getF_id().toString());
            if (tempFList.size() > 0){
                Food tempF = tempFList.get(0);
                Map<String,Object> item = new HashMap<String,Object>();
                item.put("title", tempF.getFoodname());
                item.put("desc", tempF.getDesc());
                item.put("calorie", tempF.getCalorie());
                item.put("status", u_fList.get(i).getLife_status());
                item.put("_id",u_fList.get(i).get_id());
                mData.add(item);
            }
        }
        return mData;

    }

    private class MyAdapter extends SimpleAdapter {
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

            TextView tv_title = (TextView)view.findViewById(R.id.food_list_item_title);
            TextView tv_desc = (TextView)view.findViewById(R.id.food_list_item_desc);
            TextView tv_calorie = (TextView)view.findViewById(R.id.food_list_item_calorie);

            ImageView imageview = (ImageView)view.findViewById(R.id.food_list_item_status);
            imageview.setVisibility(Integer.parseInt(map.get("status").toString())>0 ? View.VISIBLE : View.INVISIBLE);
            tv_title.setText(map.get("title").toString());
            tv_desc.setText(map.get("desc").toString());
            tv_calorie.setText(map.get("calorie").toString() +"卡路里");
            return view;
        }
    }


    @Override
    public void onSuccess(Object obj, int reqCode) {
        JSONObject jsobject = (JSONObject) obj;
        Message msg = new Message();
        Bundle b = new Bundle();
        switch (reqCode){

            case WebServiceName.GETFOOD_RQ:
                try {
                    b.putString("message", jsobject.getString("message"));
                    msg.setData(b);

                    if (jsobject.getString("code").equals("FOOD_GETLIST_SUCCESS")) {

                        insertFood(jsobject.getJSONArray("data").getJSONObject(0).getJSONArray("food"));
                        IBaseDao<U_f> u_fDao = DaoFactory.createGenericDao(getActivity(), U_f.class);

                        String uid = app.getUserID();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_bg = sdf.parse(jsobject.getJSONArray("data").getJSONObject(0).getString("startdate"));
                        Date date_ed = sdf.parse(jsobject.getJSONArray("data").getJSONObject(0).getString("enddate"));

                        JSONArray foodList = jsobject.getJSONArray("data").getJSONObject(0).getJSONArray("food");


                        int gapCount = getGapCount(date_bg,date_ed);

                        Date tempDate = date_bg;

                        List<U_f> batchU_F = new ArrayList<U_f>();
                        for (int dateIndex = 0; dateIndex < gapCount; dateIndex++) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(tempDate);
                            cal.add(Calendar.DAY_OF_YEAR,1);
                            tempDate = cal.getTime();
                            String tempDate_Str  = sdf.format(tempDate);

                            for (int i = 0; i < foodList.length(); ++i) {
                                JSONObject o = (JSONObject) foodList.get(i);
                                int f_type = o.getInt("type");
                                JSONArray foodDetail = o.getJSONArray("foodDetail");
                                for (int j=0; j<foodDetail.length(); j++){
                                    String f_id = foodDetail.getJSONObject(j).getString("ids");
                                    U_f u_f = new U_f();
                                    u_f.setU_sid(uid);	u_f.setF_id(f_id);	u_f.setSuggestDate(tempDate_Str);	u_f.setType(f_type);
                                    batchU_F.add(u_f);
                                }
                            }
                        }
                        u_fDao.batchInsert(getInsertList(batchU_F,sdf.format(date_bg),sdf.format(date_ed)));

                        this.dissProgressDialog();
                        msg.what = WebServiceName.GETFOOD_RQ;
                        handler.sendMessage(msg);



                    } else {
                        // msg.what = LOGIN_ERROR_RQ;
                        this.dissProgressDialog();
                    }
                    //hander.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case WebServiceName.GETPRESCRIPTION_ID:
                try {
                    b.putString("message", jsobject.getString("message"));
                    msg.setData(b);

                    if (jsobject.getString("code").equals("GET_PID_SUCCESS")) {
                        msg.what = WebServiceName.GETFOOD_RQ;
                        Pid = jsobject.getJSONArray("data").getJSONObject(0).getString("ids");


                        String url = Constant.getServier() + WebServiceName.foodplan;
                        Map<String, String> parameter = new HashMap<String, String>();
                        PatientApplication app = (PatientApplication) this.getActivity().getApplication();
                        parameter.put("uName", app.getUserName());
                        parameter.put("did", Pid);
                        this.request(parameter, url, WebServiceName.GETFOOD_RQ, this, new SourceJsonHandler());

                    } else {
                        // msg.what = LOGIN_ERROR_RQ;
                        this.dissProgressDialog();
                    }
                    //getActivity().hander.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            default:
                this.dissProgressDialog();
                break;
        }
    }

    @Override
    public void onFail(int errorCode, int reqCode) {
        super.onFail(errorCode, reqCode);
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("message", "errorCode: " + errorCode + ", reqCode: " + reqCode);
        msg.setData(b);
        handler.sendMessage(msg);
        this.dissProgressDialog();
    }

    private static int getGapCount(Date startDate, Date endDate){
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY,0);
        fromCalendar.set(Calendar.MINUTE,0);
        fromCalendar.set(Calendar.SECOND,0);
        fromCalendar.set(Calendar.MILLISECOND,0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY,0);
        toCalendar.set(Calendar.MINUTE,0);
        toCalendar.set(Calendar.SECOND,0);
        toCalendar.set(Calendar.MILLISECOND,0);

        return (int)((toCalendar.getTime().getTime()-fromCalendar.getTime().getTime())/(1000*60*60*24));

    }

    private void insertFood(JSONArray foodList_web) throws JSONException {
        //不重复的食品名称
        ArrayList<String[]> foodList = new ArrayList<String[]>();

        foodDao = DaoFactory.createGenericDao(getActivity(), Food.class);
        List<QueryResult> foodList_local = foodDao.execQuerySQL("select _id from food");

        //取得不重复的食品名称（网络数据）
        for (int i = 0; i < foodList_web.length(); ++i) {
            JSONObject o = (JSONObject) foodList_web.get(i);
            JSONArray foodDetail = o.getJSONArray("foodDetail");
            for (int j=0; j<foodDetail.length(); j++){
                String f_ids = foodDetail.getJSONObject(j).getString("ids");

                boolean hasExistFood = false;
                for (int k = 0; k < foodList.size(); k++) {
                    if (foodList.get(k)[3].equals(f_ids)){
                        hasExistFood = true;
                        break;
                    }
                }
                if (hasExistFood == false){
                    String f_name = foodDetail.getJSONObject(j).getString("name");
                    String f_desc = foodDetail.getJSONObject(j).getString("scqty") + foodDetail.getJSONObject(j).getString("scunit");
                    String f_rl = foodDetail.getJSONObject(j).getString("rl");
                    foodList.add(new String[]{f_name, f_desc, f_rl, f_ids});
                }
            }
        }
        for (int i = 0; i < foodList_local.size(); i++) {
            String f_id = foodList_local.get(i).getStringProperty("_id");

            for (int j = 0; j < foodList.size(); j++) {
                if (foodList.get(j)[3].equals(f_id)){
                    foodList.remove(j);
                }
            }
        }

        List<Food> batchList =  new ArrayList<Food>();
        for (int j = 0; j < foodList.size(); j++) {
            Food food = new Food();
            food.setFoodname(foodList.get(j)[0]);
            food.setDesc(foodList.get(j)[1]);
            food.setCalorie(2);
            food.set_id(foodList.get(j)[3]);
            batchList.add(food);
        }
        if (!batchList.isEmpty()){
            foodDao.batchInsert(batchList);}

    }

    private List<U_f> getInsertList(List<U_f> list, String bgDate, String edDate){
        List<U_f> reList = list;
        u_fdDao = DaoFactory.createGenericDao(getActivity(), U_f.class);
        List<QueryResult> u_FList_local = u_fdDao.execQuerySQL("select * from u_f where u_sid=? and suggestDate between ? and ?",app.getUserID(),bgDate,edDate);

        for (int i = 0; i < u_FList_local.size(); i++) {
            String suggestDate = u_FList_local.get(i).getStringProperty("suggestDate");
            String type = u_FList_local.get(i).getStringProperty("type");
            String f_id = u_FList_local.get(i).getStringProperty("f_id");
         for (int j = 0; j < reList.size(); j++) {
                if(reList.get(j).getType().toString().equals(type) && reList.get(j).getSuggestDate().equals(suggestDate) &&
                        reList.get(j).getF_id().toString().equals(f_id)  ){
                    reList.remove(j);
                    break;
                }
            }
        }
        return reList;
    }
}
