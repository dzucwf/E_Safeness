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

import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.e_saveness_common.dao.QueryResult;
import com.safeness.e_saveness_common.net.SourceJsonHandler;
import com.safeness.e_saveness_common.util.Constant;
import com.safeness.patient.R;
import com.safeness.patient.bussiness.WebServiceName;
import com.safeness.patient.model.Drug;
import com.safeness.patient.model.Drug_plan;
import com.safeness.patient.model.U_d;
import com.safeness.patient.model.U_f;
import com.safeness.patient.ui.activity.DrugSettingActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//用药
public class NaviDrugFragment extends AppBaseFragment {

    private String Pid;

    private ListView listView;
    private  TextView txv_dateText;
    private TextView btn_drug_nav_sync;
    IBaseDao<Drug> drugDao;

    private ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();
    private MyAdapter adapter = null;

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
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            List<QueryResult> drugList = drugDao.execQuerySQL(
                    "select drug._id, drugName, drug.life_status from drug, drug_plan where drug.[_id] = drug_plan.[F_sid] and drug_plan.[startdate] <= ? and drug_plan.[enddate] >= ? and u_sid = ?",
                    df.format(selectDate),df.format(selectDate), app.getUserID());
            if (drugList != null){
                if (mData.size() == 0){
                    mData = getListDataLocal();
                }
                for(int i =0; i < drugList.size(); i++) {
                    for (int j = 0; j < mData.size(); j++) {
                        if (mData.get(j).get("_id").toString().equals(drugList.get(i).getStringProperty("_id"))){
                            mData.get(j).put("imgAlert", drugList.get(i).getIntProperty("life_status"));
                        }
                    }
                }
            }
            adapter.mItemList = mData;
            listView.setAdapter(adapter);
        }

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WebServiceName.GETDRUG_RQ:

                        Toast.makeText(getActivity(),msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                        adapter = new MyAdapter(getActivity(),getListDataLocal(),R.layout.drug_listitem,
                                new String[]{"title","imgAlert","_id"},new int[]{R.id.drug_listview_item_title,R.id.drug_listview_item_imgAlert});
                        adapter.notifyDataSetChanged();
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

        app = (PatientApplication) getActivity().getApplication();

        listView = (ListView)getActivity().findViewById(R.id.listView);
        //listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,getData()));

        txv_dateText = (TextView)getActivity().findViewById(R.id.txv_drug_nav_date_text);
        btn_drug_nav_sync = (TextView)getActivity().findViewById(R.id.txv_drug_nav_sync);
        drugDao = DaoFactory.createGenericDao(getActivity(), Drug.class);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) NaviDrugFragment.this.adapter
                        .getItem(position);
                Object _id = map.get("_id");
                Object title = map.get("title");

                Intent in=new Intent(getActivity(),DrugSettingActivity.class);
                in.putExtra("drug_id",  _id.toString());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                in.putExtra("select_date",  df.format(selectDate));
                startActivity(in);
            }

        });

        btn_drug_nav_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        txv_dateText.setText(df.format(Calendar.getInstance().getTime()));

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

            adapter = new MyAdapter(getActivity(),getListDataLocal(),R.layout.drug_listitem,
                    new String[]{"title","imgAlert","_id"},new int[]{R.id.drug_listview_item_title,R.id.drug_listview_item_imgAlert});
            listView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private List<String> getData(){

        List<String> data = new ArrayList<String>();

        return data;
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

            case WebServiceName.GETDRUG_RQ:
                try {
                    b.putString("message", jsobject.getString("message"));
                    msg.setData(b);
                    if (jsobject.getString("code").equals("DRUG_GETLIST_SUCCESS")) {

                        insertDrug(jsobject.getJSONArray("data"));
                        insertDrug_plan(jsobject.getJSONArray("data"));
                        this.dissProgressDialog();
                        msg.what = WebServiceName.GETDRUG_RQ;
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
                        Pid = jsobject.getJSONArray("data").getJSONObject(0).getString("ids");

                        String url = Constant.getServier() + WebServiceName.drugplan;
                        Map<String, String> parameter = new HashMap<String, String>();
                        PatientApplication app = (PatientApplication) this.getActivity().getApplication();
                        parameter.put("uName", app.getUserName());
                        parameter.put("did", Pid);
                        this.request(parameter, url, WebServiceName.GETDRUG_RQ, this, new SourceJsonHandler());

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

    private void insertDrug(JSONArray webList) throws JSONException {
        List<Drug> batchList =  new ArrayList<Drug>();
        for (int i = 0; i < webList.length(); ++i) {
            JSONObject o = (JSONObject) webList.get(i);
            Drug drug = new Drug();
            drug.set_id(o.getString("ids"));
            drug.setDrugName(o.getString("name"));
            drug.setDesc(o.getString("desc") == "null" ? null:o.getString("desc"));
            batchList.add(drug);
        }
        if (!batchList.isEmpty()){
            IBaseDao<Drug> drugDao = DaoFactory.createGenericDao(getActivity(), Drug.class);
            drugDao.batchInsert(batchList);}
    }
    private void insertDrug_plan(JSONArray webList) throws JSONException, ParseException {
        IBaseDao<Drug_plan> drug_planDao = DaoFactory.createGenericDao(getActivity(), Drug_plan.class);
        for (int i = 0; i < webList.length(); ++i) {
            JSONObject o = (JSONObject) webList.get(i);
            Drug_plan drug_plan = new Drug_plan();
            drug_plan.setU_sid(app.getUserID());
            drug_plan.setF_sid(o.getString("ids"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            drug_plan.setStartdate(df.format(df.parse(o.getString("startdate"))));
            drug_plan.setEnddate(df.format(df.parse(o.getString("enddate"))));
            drug_plan.setMedtime(o.getInt("medtime"));
            drug_plan.setEverytime(o.getInt("everytime"));

            boolean b = drug_planDao.insertOrUpdate(drug_plan, "u_sid", "F_sid","startdate","enddate");
        }
    }
    private ArrayList<Map<String,Object>> getListDataLocal(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (drugDao == null){
            drugDao = DaoFactory.createGenericDao(getActivity(), Drug.class);
        }
        List<QueryResult> drugList = drugDao.execQuerySQL(
                "select drug._id, drugName, drug.life_status from drug, drug_plan where drug.[_id] = drug_plan.[F_sid] and drug_plan.[startdate] <= ? and drug_plan.[enddate] >= ? and u_sid = ?",
                df.format(selectDate),df.format(selectDate), app.getUserID());

        ArrayList<Map<String,Object>> remData= new ArrayList<Map<String,Object>>();

        if (drugList != null){
            for(int i =0; i < drugList.size(); i++) {
                Map<String,Object> item = new HashMap<String,Object>();
                item.put("title", drugList.get(i).getStringProperty("drugName"));
                item.put("imgAlert", drugList.get(i).getIntProperty("life_status"));
                item.put("_id",drugList.get(i).getStringProperty("_id"));
                remData.add(item);
            }
        }

        return remData;
    }
}
