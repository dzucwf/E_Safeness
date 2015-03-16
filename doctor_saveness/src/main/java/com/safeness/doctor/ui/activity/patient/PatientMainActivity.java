package com.safeness.doctor.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.safeness.doctor.R;
import com.safeness.doctor.util.silderbar.CharacterParser;
import com.safeness.doctor.util.silderbar.PinyinComparator;
import com.safeness.doctor.util.silderbar.SideBar;
import com.safeness.doctor.util.silderbar.SortAdapter;
import com.safeness.doctor.util.silderbar.SortModel;
import com.safeness.e_saveness_common.base.AppBaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by EISAVISA on 2015/3/11.
 */
public class PatientMainActivity extends AppBaseActivity {


    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ImageView mClearEditText;

    private EditText search;

    private Context mContext;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected int getLayoutId() {
        return R.layout.patient;
    }

    @Override
    protected void setupView() {
        mContext = this;
        initialView();
    }

    private void initialView() {
        search = (EditText) this.findViewById(R.id.search);
        mClearEditText = (ImageView) this.findViewById(R.id.search_delete);
        sortListView = (ListView) this.findViewById(R.id.listView);
        dialog = (TextView) this.findViewById(R.id.mDialogText);
        sideBar = (SideBar) this.findViewById(R.id.sideBar);

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.data));

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);


        //根据输入框输入值的改变来过滤搜索
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //设置添加病人的按钮可见
        View btnAdd = this.findViewById(R.id.btn2_iv);
        btnAdd.setVisibility(View.VISIBLE);

    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    protected void initializedData() {

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn2_iv:
                openAddPatient();
                break;
            default:
                break;
        }
    }

    /**
     * 打开添加病人接口
     */
    private void openAddPatient(){
        Intent it  = new Intent();
        //TODO:add class
        it.setClass(this,PatientAddOrModifyActivity.class);
        startActivity(it);
    }
}
