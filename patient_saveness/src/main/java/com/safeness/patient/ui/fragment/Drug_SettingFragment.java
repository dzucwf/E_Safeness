package com.safeness.patient.ui.fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.patient.R;

/**
 * Created by Lionnd on 2015/1/28.
 */
public class Drug_SettingFragment extends AppBaseFragment {
    private ListView listView;
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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



    private void getViews() {/*
        listView = (ListView)getActivity().findViewById(R.id.drug_setting_remind_list);
        ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();

        for(int i =0; i < 20; i++) {
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("title", "20:00");
            item.put("status", i%2>0);
            item.put("id", i+1);
            mData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),mData,R.layout.drug_listitem,
                new String[]{"title","status"},new int[]{R.id.drug_setting_remind_list_label_time,R.id.drug_setting_remind_list_image_status});
        listView.setAdapter(adapter);*/
    }
}
