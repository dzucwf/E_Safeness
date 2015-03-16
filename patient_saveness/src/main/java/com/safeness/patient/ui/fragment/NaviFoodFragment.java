package com.safeness.patient.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.patient.R;
import com.safeness.patient.model.Food;
import com.safeness.patient.model.U_f;
import com.safeness.patient.ui.activity.HistoryActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//饮食
public class NaviFoodFragment extends AppBaseFragment {

    private  TextView txv_dateText;
    private ListView listView;

    private MyAdapter adapter = null;
    private LinearLayout ll_segment;

    private ImageView btn_food_nav_item;
    private LinearLayout btn_eated;
    private ImageView btn_eated_status;

    private  final  int OPEN_CALENDAR_RQ=11;
    private  final int SET_DATE_RESULT = 12;

    //日期控件选择的时间
    private  Calendar saveCalendar = Calendar.getInstance();

    private int cutTab = 1;
    private Date selectDate = Calendar.getInstance().getTime();

    private IBaseDao<Food> foodDao;
    private IBaseDao<U_f> u_fdDao;
    //
    @Override
    protected int getLayoutId() {
        return R.layout.navi_hp_food;
    }

    @Override
    public void onStart() {
        super.onStart();

        btn_eated_status.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void setupView() {
        getViews();

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
                                    adapter = new MyAdapter(getActivity(),getData(),R.layout.food_listitem,
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


        adapter = new MyAdapter(getActivity(),getData(),R.layout.food_listitem,
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
    }

    private String getDateBg(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return df.format(date);
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

            adapter = new MyAdapter(getActivity(),getData(),R.layout.food_listitem,
                    new String[]{"title","desc","calorie","status","_id"},new int[]{R.id.food_list_item_title,R.id.food_list_item_desc});
            listView.setAdapter(adapter);
            //setCalText(selected_calendar);

            //fillChartData();
            //loadCurrentTimeValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Map<String,Object>> getData(){
        ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();

        foodDao = DaoFactory.createGenericDao(getActivity(), Food.class);
        u_fdDao = DaoFactory.createGenericDao(getActivity(), U_f.class);

        List<U_f> u_fList = u_fdDao.queryByCondition("u_sid=? and suggestDate between ? and ? and type=?", "1", getDateBg(selectDate),getDateEd(selectDate),String.valueOf(cutTab));
        //List<U_f> u_fList = u_fdDao.queryByCondition("u_sid=? and suggestDate between '2015-01-31 00:00:00' and '2015-01-31 23:59:59' and type=?", "1",String.valueOf(cutTab));
/*
        if (u_fdDao.queryByCondition("").isEmpty()){//写入测试数据
            initSqlData(foodDao);
            initSqlDataUF(u_fdDao);

            u_fList = u_fdDao.queryByCondition("u_sid=? and suggestDate between ? and ? and type=?", "1", getDateBg(selectDate),getDateEd(selectDate),String.valueOf(cutTab));
        }
*/
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

}
