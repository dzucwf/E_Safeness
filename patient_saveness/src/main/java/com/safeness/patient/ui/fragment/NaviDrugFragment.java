package com.safeness.patient.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.patient.R;
import com.safeness.patient.model.Drug;
import com.safeness.patient.ui.activity.DrugSettingActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

//用药
public class NaviDrugFragment extends AppBaseFragment {


    private ListView listView;
    private  TextView txv_dateText;
    private TextView btn_drug_nav_sync;
    IBaseDao<Drug> drugDao;

    private ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();
    private MyAdapter adapter = null;

    private Date selectDate = Calendar.getInstance().getTime();

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null){
            List<Drug> drugList = drugDao.queryByCondition("");

            for(int i =0; i < drugList.size(); i++) {
                for (int j = 0; j < mData.size(); j++) {
                    if (mData.get(j).get("_id").toString().equals(drugList.get(i).get_id().toString())){
                        mData.get(j).put("imgAlert", drugList.get(i).getLife_status());
                    }
                }
            }
            adapter.mItemList = mData;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.navi_hp_drug;
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
        listView = (ListView)getActivity().findViewById(R.id.listView);
        //listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,getData()));

        txv_dateText = (TextView)getActivity().findViewById(R.id.txv_drug_nav_date_text);
        btn_drug_nav_sync = (TextView)getActivity().findViewById(R.id.txv_drug_nav_sync);

        mData= new ArrayList<Map<String,Object>>();

        drugDao = DaoFactory.createGenericDao(getActivity(), Drug.class);
        List<Drug> drugList = drugDao.queryByCondition("");

        for(int i =0; i < drugList.size(); i++) {
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("title", drugList.get(i).getDrugName());
            item.put("imgAlert", drugList.get(i).getLife_status());
            item.put("_id",drugList.get(i).get_id());
            mData.add(item);
        }
        adapter = new MyAdapter(getActivity(),mData,R.layout.drug_listitem,
                new String[]{"title","imgAlert","_id"},new int[]{R.id.drug_listview_item_title,R.id.drug_listview_item_imgAlert});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) NaviDrugFragment.this.adapter
                        .getItem(position);
                Object _id = map.get("_id");
                Object title = map.get("title");
                //NaviDrugFragment.this.info.setText("选中的数据项ID是：" + _id + "，名称是："+ name);
                Log.v("e", "选中的数据项ID是：" + _id + "，名称是：" + title);
/*
                android.support.v4.app.Fragment drug_settingFr = new Drug_SettingFragment();

                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.navi_view_pager, drug_settingFr);
                ft.addToBackStack(null);
                ft.commit();*/
                Intent in=new Intent(getActivity(),DrugSettingActivity.class);
                in.putExtra("drug_id",  Integer.parseInt(_id.toString()));
                startActivity(in);
            }

        });

        btn_drug_nav_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getList();
            }
        });


    }

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
            ImageView imageview = (ImageView)view.findViewById(R.id.drug_listview_item_imgAlert);
            TextView tv = (TextView)view.findViewById(R.id.drug_listview_item_title);
            imageview.setVisibility(Integer.parseInt(map.get("imgAlert").toString())>0 ? View.VISIBLE : View.INVISIBLE);

            tv.setText(map.get("title").toString());
            return view;
        }


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

            adapter = new MyAdapter(getActivity(),mData,R.layout.drug_listitem,
                    new String[]{"title","imgAlert","_id"},new int[]{R.id.drug_listview_item_title,R.id.drug_listview_item_imgAlert});
            listView.setAdapter(adapter);
            //setCalText(selected_calendar);

            //fillChartData();
            //loadCurrentTimeValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private List<String> getData(){

        List<String> data = new ArrayList<String>();

        return data;
    }
}
