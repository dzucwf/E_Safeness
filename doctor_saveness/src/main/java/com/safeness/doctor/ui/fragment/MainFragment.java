package com.safeness.doctor.ui.fragment;


import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.safeness.doctor.R;
import com.safeness.doctor.bussiness.GridViewData;
import com.safeness.e_saveness_common.base.AppBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

//首页
public class MainFragment extends AppBaseFragment {



    private GridView mGridView;
    ArrayList<HashMap<String, Object>> lstImageItems = new ArrayList<HashMap<String, Object>>();
    SimpleAdapter saImageItems;
    @Override
    protected int getLayoutId() {
        return R.layout.home;
    }

    @Override
    protected void setupView() {
        inititalGridView();
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
    }

    @Override
    protected void initializedData() {

    }


}
