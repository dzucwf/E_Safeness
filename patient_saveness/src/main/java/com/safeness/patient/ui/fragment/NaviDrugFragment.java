package com.safeness.patient.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.patient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemClickListener;

//用药
public class NaviDrugFragment extends AppBaseFragment {


    private ListView listView;

    private ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();
    private SimpleAdapter adapter = null;
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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


       mData= new ArrayList<Map<String,Object>>();

        for(int i =0; i < 20; i++) {
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("title", "弗莱明");
            item.put("imgAlert", i%2>0);
            item.put("_id", i+1);
            mData.add(item);
        }
        adapter = new SimpleAdapter(getActivity(),mData,R.layout.drug_listitem,
                new String[]{"title","imgAlert"},new int[]{R.id.drug_listview_item_title,R.id.drug_listview_item_imgAlert});
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

                android.support.v4.app.Fragment drug_settingFr = new Drug_SettingFragment();

                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.navi_view_pager, drug_settingFr);
                ft.addToBackStack(null);
                ft.commit();
            }

        });

    }


    private List<String> getData(){

        List<String> data = new ArrayList<String>();

        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");

        return data;
    }
}
