package com.safeness.doctor.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.safeness.doctor.R;
import com.safeness.doctor.bussiness.GridViewData;
import com.safeness.doctor.ui.activity.patient.PatientMainActivity;
import com.safeness.e_saveness_common.base.AppBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
//首页
public class MainFragment extends AppBaseFragment {


    private static final String TAG = "MainFragment";
    //定位
    public LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public MyLocationListener mMyLocationListener;

    private TextView city_text;


    private GridView mGridView;
    ArrayList<HashMap<String, Object>> lstImageItems = new ArrayList<HashMap<String, Object>>();
    SimpleAdapter saImageItems;

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            String city = location.getCity();
            if(!TextUtils.isEmpty(city)){
                city_text.setText(city);
                mLocationClient.stop();
            }

        }
    }



    @Override
    protected int getLayoutId() {
        return R.layout.home;
    }

    @Override
    protected void setupView() {
        inititalGridView();
        initialView();
    }

    private void setLocation(){

        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mGeofenceClient = new GeofenceClient(getActivity().getApplicationContext());

        LocationClientOption option = new LocationClientOption();
        LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Battery_Saving;
        option.setLocationMode(tempMode);//设置定位模式
        String tempcoor="gcj02";
        option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        int span=5000;

        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void initialView(){

        city_text = (TextView)getActivity().findViewById(R.id.city_text);
    }


    /**
     * 初始化GridView
     */
    private  void inititalGridView(){
        mGridView = (GridView)getActivity().findViewById(R.id.dragGridView);
        lstImageItems.clear();
        //初始化GirdView
        String[] gridTexts = getResources().getStringArray(R.array.gridTexts);
        for (int i = 0; i <gridTexts.length ; i++) {
            HashMap<String, Object> map = new HashMap<String,Object>();
            map.put("item_image", GridViewData.gridDrableImage[i]);
            map.put("item_text",gridTexts[i]);
            lstImageItems.add(map);
        }
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
         saImageItems = new SimpleAdapter(this.getActivity(), //没什么解释
                lstImageItems,//数据来源
                R.layout.home_item,//night_item的XML实现

                //动态数组与ImageItem对应的子项
                new String[] {"item_image","item_text"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.item_image,R.id.item_text});
        //添加并且显示
        mGridView.setAdapter(saImageItems);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToOtherFragment(lstImageItems.get(position).get("item_text").toString());
            }
        });
    }

    /**
     * 程序其他模块入口
     * @param fragmentName
     */
    private void goToOtherFragment(String fragmentName){
        Intent it = new Intent();
        if(fragmentName.equals("患者管理")){
            it.setClass(getActivity(), PatientMainActivity.class);
            getActivity().startActivity(it);
        }

    }

    @Override
    protected void initializedData() {
        setLocation();

    }


}
