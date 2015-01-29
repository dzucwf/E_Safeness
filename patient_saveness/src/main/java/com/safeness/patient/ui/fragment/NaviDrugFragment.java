package com.safeness.patient.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

//用药
public class NaviDrugFragment extends AppBaseFragment {


    private ListView listView;

    private ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();
    private MyAdapter adapter = null;
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

        IBaseDao<Drug> drugDao = DaoFactory.createGenericDao(getActivity(), Drug.class);





        List<Drug> drugList = drugDao.queryByCondition("");

        Drug drug = new Drug();
        if (drugList.isEmpty()){//写入测试数据

            drug.setDrugName("格列齐特缓释片");
            drug.setDesc("当单用饮食疗法、运动治疗和减轻体重不足以控制血糖水平的成人非胰岛素依赖型糖尿病（2型）。");
            drugDao.insert(drug);

            drug.setDrugName("拜唐苹(阿卡波糖片)");
            drug.setDesc("配合饮食控制，用于：1.II型糖尿病。2.降低糖耐量减低者的餐后血糖。");
            drugDao.insert(drug);

            drug.setDrugName("诺和龙(瑞格列奈片)");
            drug.setDesc("用于饮食控制、减轻体重及运动锻炼不能有效控制其高血糖的2型糖尿病（非胰岛素依赖型）患者。瑞格列奈片可与二甲双胍并用。二者并用时对控制血糖比各自单独");
            drugDao.insert(drug);

            drug.setDrugName("格华止(盐酸二甲双胍片)");
            drug.setDesc("1.本品首选用于单纯饮食控制及体育锻炼治疗无效的2型糖尿病，特别是肥胖的2型糖尿病。2.对于1型或2型糖尿病，本品与胰岛素合用，可增加胰岛素的降血糖作用");
            drugDao.insert(drug);

            drug.setDrugName("消渴丸");
            drug.setDesc("滋肾养阴，益气生津。用于多饮，多尿，多食，消瘦，体倦无力，眠差腰痛，尿糖及血糖升高之气阴两虚型消渴症。");
            drugDao.insert(drug);

            drug.setDrugName("糖脉康颗粒");
            drug.setDesc("养阴清热，活血化瘀，益气固肾。用于气阴两虚血瘀所致的口渴喜饮，倦怠乏力，气短懒言，自汗，盗汗，五心烦热，胸中闷痛，肢体麻木或刺痛，便秘，糖尿病Ⅱ");
            drugDao.insert(drug);

            drug.setDrugName("降糖宁胶囊");
            drug.setDesc("益气，养阴，生津。用于糖尿病属气阴两虚者。");
            drugDao.insert(drug);

            drug.setDrugName("金路捷(注射用鼠神经生长因子)");
            drug.setDesc("正已烷中毒性周围神经病。");
            drugDao.insert(drug);

            drug.setDrugName("盐酸吡格列酮片");
            drug.setDesc("对于2型糖尿病（非胰岛素依赖性糖尿病，NIDDM）患者，盐酸吡格列酮可与饮食控制和体育锻炼联合以改善血糖控制。盐酸吡格列酮可单独使用，当饮食控制、体育");
            drugDao.insert(drug);

            drug.setDrugName("玉泉丸");
            drug.setDesc("养阴生津，止渴除烦，益气和中。用于治疗因胰岛功能减退而引起的物质代谢、碳水化合物代谢紊乱，血糖升高之糖尿病(亦称消渴症)，肺胃肾阴亏损，热病后期。");
            drugDao.insert(drug);

            drug.setDrugName("糖适平(格列喹酮片)");
            drug.setDesc("2型糖尿病（即非胰岛素依赖型糖尿病）。");
            drugDao.insert(drug);

            drugList = drugDao.queryByCondition("life_status>0");
        }

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
            return pos;
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
