package com.safeness.patient.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.safeness.patient.model.Sports;
import com.safeness.patient.model.Sports_plan;
import com.safeness.patient.ui.activity.SportsSettingActivity;

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

//运动
public class NaviSportsFragment extends AppBaseFragment {

    private String Pid;

    private ListView listView;
    private  TextView txv_dateText;
    private TextView btn_sports_nav_sync;
    IBaseDao<Sports> sportsDao;

    private ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();
    private SportsAdapter adapter = null;

    private Date selectDate = Calendar.getInstance().getTime();

    private Handler handler=null;

    PatientApplication app;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null){
            adapter.mItemList = getListDataLocal();
            adapter.notifyDataSetChanged();
        }

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WebServiceName.GETSPORTS_RQ:

                        Toast.makeText(getActivity(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                        adapter.mItemList = getListDataLocal();
                        listView.setAdapter(adapter);
                        break;
                    case WebServiceName.GETPRESCRIPTION_ID:
                        Toast.makeText(getActivity(),msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.navi_hp_sports;
    }

    @Override
    protected void setupView() {
        app = (PatientApplication) getActivity().getApplication();
        getViews();
    }

    @Override
    protected void initializedData() {

    }

    //初始化下层切换
    private void getViews() {
        listView = (ListView)getActivity().findViewById(R.id.sports_listView);
        txv_dateText = (TextView)getActivity().findViewById(R.id.txv_sports_nav_date_text);
        btn_sports_nav_sync = (TextView)getActivity().findViewById(R.id.txv_sports_nav_sync);

        sportsDao = DaoFactory.createGenericDao(getActivity(), Sports.class);

        adapter = new SportsAdapter(getActivity(),getListDataLocal(),R.layout.sports_listitem,
                new String[]{"title","desc","calorie","_id"},new int[]{R.id.sports_list_item_title,R.id.sports_list_item_desc,R.id.sports_list_item_calorie});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) NaviSportsFragment.this.adapter
                        .getItem(position);
                Object _id = map.get("_id");
                Object title = map.get("title");

                Intent in=new Intent(getActivity(),SportsSettingActivity.class);
                in.putExtra("sports_id",  _id.toString());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                in.putExtra("select_date",  df.format(selectDate));
                startActivity(in);
            }

        });

        btn_sports_nav_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        txv_dateText.setText(df.format(Calendar.getInstance().getTime()));

    }

    private class SportsAdapter extends SimpleAdapter {
        int count = 0;
        private List<Map<String, Object>> mItemList;
        public SportsAdapter(Context context, List<? extends Map<String, Object>> data,
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

            TextView tv_title = (TextView)view.findViewById(R.id.sports_list_item_title);
            TextView tv_desc = (TextView)view.findViewById(R.id.sports_list_item_desc);
            TextView tv_calorie = (TextView)view.findViewById(R.id.sports_list_item_calorie);
            ImageView imageview = (ImageView)view.findViewById(R.id.sports_list_item_status);

            //imageview.setVisibility(Integer.parseInt(map.get("status").toString())>0 ? View.VISIBLE : View.INVISIBLE);
            tv_title.setText(map.get("title").toString());
            tv_desc.setText(map.get("desc").toString());
            tv_calorie.setText(map.get("calorie").toString() +"卡路里");
            return view;
        }


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

            txv_dateText.setText(df.format(date));

            adapter.mItemList = getListDataLocal();
            listView.setAdapter(adapter);

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

    @Override
    public void onSuccess(Object obj, int reqCode) {
        JSONObject jsobject = (JSONObject) obj;
        Message msg = new Message();
        Bundle b = new Bundle();
        switch (reqCode){

            case WebServiceName.GETSPORTS_RQ:
                try {
                    b.putString("message", jsobject.getString("message"));
                    msg.setData(b);
                    if (jsobject.getString("code").equals("SPORTS_GETLIST_SUCCESS")) {

                        insertSports(jsobject.getJSONArray("data"));
                        insertSports_plan(jsobject.getJSONArray("data"));
                        this.dissProgressDialog();

                    } else {
                        // msg.what = LOGIN_ERROR_RQ;
                    }

                } catch (JSONException e) {
                    b.putString("message", "写入数据时发生格式转换错误");
                    e.printStackTrace();
                } catch (ParseException e) {
                    b.putString("message", "写入数据时发生格式转换错误");
                    e.printStackTrace();
                }
                finally {
                    msg.what = WebServiceName.GETSPORTS_RQ;
                    handler.sendMessage(msg);
                    this.dissProgressDialog();
                }
                break;
            case WebServiceName.GETPRESCRIPTION_ID:
                try {
                    b.putString("message", jsobject.getString("message"));
                    msg.setData(b);

                    if (jsobject.getString("code").equals("GET_PID_SUCCESS")) {
                        Pid = jsobject.getJSONArray("data").getJSONObject(0).getString("ids");

                        String url = Constant.getServier() + WebServiceName.sportsplan;
                        Map<String, String> parameter = new HashMap<String, String>();
                        PatientApplication app = (PatientApplication) this.getActivity().getApplication();
                        parameter.put("uName", app.getUserName());
                        parameter.put("did", "63b2a73dc4fe44b5b7e9770a692a6cbc"/*Pid*/);
                        this.request(parameter, url, WebServiceName.GETSPORTS_RQ, this, new SourceJsonHandler());

                    } else {
                        msg.what = WebServiceName.GETPRESCRIPTION_ID;
                        this.dissProgressDialog();
                        handler.sendMessage(msg);
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
    }

    private void insertSports(JSONArray webList) throws JSONException {
        List<Sports> batchList =  new ArrayList<Sports>();
        for (int i = 0; i < webList.length(); ++i) {
            JSONObject o = (JSONObject) webList.get(i);
            Sports sports = new Sports();
            sports.set_id(o.getString("ids"));
            sports.setSportsName(o.getString("sportsName"));
            sports.setCalorie(o.getString("calori").length() <= 0 ? -1 : o.getDouble("calori"));
            sports.setDesc1(
                    "注意事项: " + o.getString("attention")+ "\n" +
                            "不适合并发症: " + o.getString("complications") + "\n" +
                            "理想效果: " + o.getString("effect")+ "\n" +
                            "不适合用药: " + o.getString("drug")+ "\n" +
                            "运动强度: " + o.getString("type"));
            batchList.add(sports);
        }
        if (!batchList.isEmpty()){
            IBaseDao<Sports> sportsDao = DaoFactory.createGenericDao(getActivity(), Sports.class);
            sportsDao.batchInsert(batchList);}
    }
    private void insertSports_plan(JSONArray webList) throws JSONException, ParseException {
        IBaseDao<Sports_plan> sports_planDao = DaoFactory.createGenericDao(getActivity(), Sports_plan.class);
        for (int i = 0; i < webList.length(); ++i) {
            JSONObject o = (JSONObject) webList.get(i);
            Sports_plan sports_plan = new Sports_plan();
            sports_plan.setU_sid(app.getUserID());
            sports_plan.setS_sid(o.getString("ids"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            sports_plan.setStartdate(df.format(df.parse(o.getString("startdate"))));
            sports_plan.setEnddate(df.format(df.parse(o.getString("enddate"))));

            sports_plan.setComplications(o.getString("complications"));
            sports_plan.setEffect(o.getString("effect"));
            sports_plan.setCalori(o.getString("calori"));
            sports_plan.setType(o.getString("type"));
            sports_plan.setAttention(o.getString("attention"));
            sports_plan.setDrug(o.getString("drug"));

            boolean b = sports_planDao.insertOrUpdate(sports_plan, "u_sid", "s_sid","startdate","enddate");
        }
    }
    private ArrayList<Map<String,Object>> getListDataLocal(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (sportsDao == null){
            sportsDao = DaoFactory.createGenericDao(getActivity(), Sports.class);
        }
        List<QueryResult> sportsList = sportsDao.execQuerySQL(
                "select sports._id, sportsName, sports.[calorie], sports_plan.type from sports, sports_plan where sports.[_id] = sports_plan.[s_sid] and sports_plan.[startdate] <= ? and sports_plan.[enddate] >= ? and u_sid = ?",
                df.format(selectDate),df.format(selectDate), app.getUserID());

        ArrayList<Map<String,Object>> remData= new ArrayList<Map<String,Object>>();

        if (sportsList != null){
            for(int i =0; i < sportsList.size(); i++) {
                Map<String,Object> item = new HashMap<String,Object>();
                item.put("_id",sportsList.get(i).getStringProperty("_id"));
                item.put("title", sportsList.get(i).getStringProperty("sportsName"));
                item.put("calorie", sportsList.get(i).getIntProperty("calorie"));
                item.put("desc",sportsList.get(i).getStringProperty("type"));
                remData.add(item);
            }
        }

        return remData;
    }

}
