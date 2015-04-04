package com.safeness.patient.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.e_saveness_common.net.SourceJsonHandler;
import com.safeness.e_saveness_common.util.Constant;
import com.safeness.e_saveness_common.util.DateTimeUtil;
import com.safeness.patient.R;
import com.safeness.patient.bussiness.WebServiceName;
import com.safeness.patient.model.BloodGlucose;
import com.safeness.patient.ui.activity.GlucoseInputActivity;
import com.safeness.patient.ui.activity.MoreActivity;
import com.safeness.patient.ui.view.GlucoseInputView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//血糖
public class NaviGlucoseFragment extends AppBaseFragment implements ViewPager.OnPageChangeListener {


    private static final String TAG = "NaviGlucoseFragment";

    private LinearLayout mImageIndex;
    private ViewPager mViewPager;

    TextView dateTextViw;
    TextView glucose_time_tv;

    ImageView btn_set;

    private Context mContext;

    private int selectTime = 0;

    private static final int FILL_GLUCOSE = 21 ;
    private static final int FILL_GLUCOSE_ERROR = 22;

    //界面中的录入血糖圆圈
    ArrayList<View> glucoseViewList;


    Calendar selected_calendar = Calendar.getInstance();


    final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
    private WebView chartWebView;

    final SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtil.NORMAL_PATTERN);
    private void getViews(View view) {


        mContext = getActivity();
        chartWebView = (WebView) getActivity().findViewById(R.id.glucoseChart);
        chartWebView.getSettings().setJavaScriptEnabled(true);
        chartWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        chartWebView.getSettings().setDefaultTextEncodingName("utf-8");

        chartWebView.setBackgroundColor(Color.TRANSPARENT);
        //不使用缓存
        chartWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        chartWebView.loadUrl("file:///android_asset/www/h.htm");
        chartWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                //queryGlucoseFromServer();
            }
        });

        dateTextViw = (TextView) getActivity().findViewById(R.id.date_glucose_tv);
        glucose_time_tv = (TextView) getActivity().findViewById(R.id.glucose_time_tv);
        glucose_time_tv.setText(getString(R.string.breakfast));
        Calendar c = Calendar.getInstance();
        setCalText(c);
        mImageIndex = (LinearLayout) view.findViewById(R.id.imageNavi);
        mImageIndex.removeAllViews();
        mViewPager = (ViewPager) view.findViewById(R.id.glucose_view_pager);
        glucoseViewList = new ArrayList<View>();
        //添加输入血糖控件
        for (int i = 0; i < 4; i++) {

            GlucoseInputView glucoseinputview = new GlucoseInputView(this.getActivity());
            //得到点击的是哪个输入框
            glucoseinputview.setCallBack(new GlucoseInputView.ICallBack() {
                @Override
                public void onClickButton(String s) {

                    long yearKey = Integer.parseInt(selected_calendar.get(Calendar.YEAR) + "" + selected_calendar.get(Calendar.MONTH) + 1 + "" +
                            selected_calendar.get(Calendar.DATE) + "" + selectTime);
                    Intent it = new Intent(getActivity(), GlucoseInputActivity.class);
                    long yearKey_final = yearKey;
                    if (s.equals("before")) {
                        yearKey_final = Long.parseLong(yearKey_final + "" + 0);
                        it.putExtra("afterOrBefore", 0);
                    } else {
                        yearKey_final = Long.parseLong(yearKey_final + "" + 1);
                        it.putExtra("afterOrBefore", 1);
                    }
                    it.putExtra("server_id", yearKey_final+"");
                    it.putExtra("takeTag", selectTime);
                    it.putExtra("inputTime", DateTimeUtil.getSelectedDate(selected_calendar, DateTimeUtil.NORMAL_PATTERN));
                    getActivity().startActivity(it);

                }
            });
            glucoseinputview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            glucoseViewList.add(glucoseinputview);
            // add for index container
            ImageView index = new ImageView(getActivity());
            index.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            index.setPadding(8, 8, 8, 8);
            index.setImageResource(R.drawable.shelf_circle_selector);
            index.setSelected(i == 0 ? true : false);
            mImageIndex.addView(index);


        }

        btn_set = (ImageView) view.findViewById(R.id.set_btn);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(getActivity(), MoreActivity.class);
                getActivity().startActivity(it1);
            }
        });
        mViewPager.setAdapter(new ViewPagerAdapter(glucoseViewList));
        mViewPager.setOnPageChangeListener(this);

    }






    /**
     * 为webview填充数据
     */
    private void LoadChartData(Context context, String[] xAxis, String yAxisTitle, final double[] series) {

        try {
            JSONArray j_series = new JSONArray();
            JSONArray j_xAxis = new JSONArray();

            for (int i = 0; i < xAxis.length; i++) {
                j_xAxis.put(xAxis[i]);
            }

            JSONObject series1 = new JSONObject();
            JSONArray s1name = new JSONArray();
            s1name.put("血糖值");
            series1.put("name", s1name);

            JSONArray s1data = new JSONArray();
            for (int i = 0; i < series.length; i++) {
                s1data.put(series[i]);
            }
            if (s1data.length() == 0){s1data.put(null).put(null).put(null).put(null);}
            series1.put("data", s1data);
            j_series.put(series1);
            chartWebView.loadUrl("javascript:update('"+j_series.toString()+"','"+(j_xAxis.length() == 0 ? "[6,12,18,0]" : j_xAxis.toString())+"','"+yAxisTitle+"');");
        }
        catch (Exception ex) {
            // 键为null或使用json不支持的数字格式(NaN, infinities)
            throw new RuntimeException(ex);
        }
    }

    /**
     * 从日历控件返回后，改变当前的日期,并加载当前数据
     *
     * @param date
     */
    public void setSelectedDate(String date) {
        try {
            selected_calendar = DateTimeUtil.getSelectCalendar(date, DateTimeUtil.NORMAL_PATTERN);
            setCalText(selected_calendar);

            queryGlucoseFromServer();


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    public void onResume() {

        super.onResume();
        queryGlucoseFromServer();



    }



    private Handler hander = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case FILL_GLUCOSE:

                    //从服务器获取数据后登填充数据
                    fillChartData(queryFromLocal());
                    loadCurrentTimeValue(queryFromLocal());
                    break;
                case FILL_GLUCOSE_ERROR:
                    String messageStr = msg.getData().getString("message");
                    Toast.makeText(mContext, messageStr, Toast.LENGTH_LONG).show();
                    break;

            }
            super.handleMessage(msg);
            dissProgressDialog();
        }
    };

    @Override
    public void onSuccess(Object obj, int reqCode) {

        if (reqCode == FILL_GLUCOSE) {
            JSONObject jsobject = (JSONObject) obj;
            Message msg = new Message();
            Bundle b = new Bundle();
            String userName = PatientApplication.getInstance().getUserName();
            try {
                b.putString("message", jsobject.getString("message"));
                msg.setData(b);

                if (jsobject.getString("code").equals("BG_GETLIST_SUCCESS")) {

                    List<BloodGlucose> sourceValueList = new ArrayList<BloodGlucose>();

                    JSONArray data = jsobject.getJSONArray("data");

                    for (int i = 0; i <data.length() ; i++) {
                        JSONObject jsobjectModel =  data.getJSONObject(i);

                        float value = (float) jsobjectModel.getDouble("value");
                        String takeDate = jsobjectModel.getString("takeDate");
                        int takeTag =jsobjectModel.getInt("takeTag");
                        int afterOrBefore = jsobjectModel.getInt("afterorbefore");
                        String serverID = jsobjectModel.getString("ids");
                        BloodGlucose bloodGlucose = new BloodGlucose(serverID,value,takeTag,takeDate,afterOrBefore,userName);
                        sourceValueList.add(bloodGlucose);

                    }
                    IBaseDao<BloodGlucose> daoFactory = DaoFactory.createGenericDao(getActivity(), BloodGlucose.class);
                    daoFactory.batchInsert(sourceValueList);
                    msg.what = FILL_GLUCOSE;


                } else {
                    msg.what = FILL_GLUCOSE_ERROR;
                }
                hander.sendMessage(msg);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        super.onSuccess(obj, reqCode);
    }

    @Override
    public void onFail(int errorCode, int reqCode) {
        super.onFail(errorCode, reqCode);
    }

    private void queryGlucoseFromServer(){
        String queryStartTime = DateTimeUtil.getSelectedDate(selected_calendar, "yyyy-MM-dd 00:00:00");
        String queryEndTime = DateTimeUtil.getSelectedDate(selected_calendar, "yyyy-MM-dd 23:59:59");
        String userName = PatientApplication.getInstance().getUserName();
        //查询当天的血糖曲线
        List<BloodGlucose> dataBaseList = queryFromLocal();
        //先查询本地数据库，如果本地没有数据就查询服务器并把服务器的数据放到本地
        if(dataBaseList == null || dataBaseList.size()==0 ){
            String url = Constant.getServier() + WebServiceName.getGlucosseList;
            Map<String, String> parameter = new HashMap<String, String>();


            parameter.put("uName", userName);
            parameter.put("startdate", queryStartTime);
            parameter.put("enddate", queryEndTime);

            this.request(parameter, url, FILL_GLUCOSE, this, new SourceJsonHandler());
        }else{
            fillChartData(dataBaseList);
            loadCurrentTimeValue(dataBaseList);
        }
    }

    private List<BloodGlucose> queryFromLocal(){
        String queryStartTime = DateTimeUtil.getSelectedDate(selected_calendar, "yyyy-MM-dd 00:00:00");
        String queryEndTime = DateTimeUtil.getSelectedDate(selected_calendar, "yyyy-MM-dd 23:59:59");
        IBaseDao<BloodGlucose> daoFactory = DaoFactory.createGenericDao(getActivity(), BloodGlucose.class);

        String userName = PatientApplication.getInstance().getUserName();
        List<BloodGlucose> dataBaseList = daoFactory.queryByCondition("user_id = ? and takeDate between ? and ? order by takeDate", new String[]{userName,queryStartTime, queryEndTime});
        return dataBaseList;

    }

    /**
     * 加载完成后初始化图表数据
     */
    private void fillChartData(List<BloodGlucose> sourceValueList ) {
        try {


            if (sourceValueList != null && sourceValueList.size() > 0) {
                final String[] xAxis = new String[sourceValueList.size()];
                final double[] series = new double[sourceValueList.size()];
                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat sf2=new SimpleDateFormat("HH:mm");
                for (int i = 0; i < sourceValueList.size(); i++) {
                    xAxis[i] = sf2.format(sf.parse(sourceValueList.get(i).getTakeDate()));
                    series[i] = sourceValueList.get(i).getBloodGlucose();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadChartData(getActivity(), xAxis, "血糖图表", series);
                    }
                });


            } else {
                LoadChartData(getActivity(), new String[0], "血糖图表", new double[0]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 设置选择的日期
     *
     * @param c
     */
    private void setCalText(Calendar c) {

        String strNow = c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月"
                + c.get(Calendar.DATE) + "日";
        dateTextViw.setText(strNow);


    }


    private View.OnClickListener glucoseListenser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "之后", Toast.LENGTH_LONG).show();
        }
    };

    /*
    * 设置选中的输入血糖时间
    * */
    private void setGlucoseTxt(int position) {
        selectTime = position;
        switch (position) {
            case 0:
                glucose_time_tv.setText(getString(R.string.breakfast));
                break;
            case 1:
                glucose_time_tv.setText(getString(R.string.lunch));
                break;
            case 2:
                glucose_time_tv.setText(getString(R.string.supper));
                break;
            case 3:
                glucose_time_tv.setText(getString(R.string.other));
                break;
            default:
                break;


        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }


    //切换
    @Override
    public void onPageSelected(int index) {

        setGlucoseTxt(index);
        loadCurrentTimeValue(queryFromLocal());

        int cnt = mImageIndex.getChildCount();
        for (int i = 0; i < cnt; i++) {
            mImageIndex.getChildAt(i).setSelected(i == index ? true : false);
        }

    }


    /**
     * 读取当前选择圆圈的血糖值
     */

    private void loadCurrentTimeValue(List<BloodGlucose> sourceValueList) {

        GlucoseInputView inputView = (GlucoseInputView) glucoseViewList.get(selectTime);

        //恢复初始状态
        for (View view : glucoseViewList) {
            GlucoseInputView glucoseView = (GlucoseInputView)view;
            glucoseView.fillBeforeGlucose("",View.VISIBLE);
            glucoseView.fillAfterGlucose("", View.VISIBLE);
        }

        List<BloodGlucose> smallGroup = new ArrayList<BloodGlucose>();
        if (sourceValueList != null && sourceValueList.size() > 0) {
            for (BloodGlucose bloodGlucose : sourceValueList) {


                if (bloodGlucose.getTakeTag() == selectTime) {
                    smallGroup.add(bloodGlucose);
                }

            }

            for (int i = 0; i < smallGroup.size(); i++) {
                if (smallGroup.get(i).getLife_status() == 0) {
                    inputView.fillBeforeGlucose(smallGroup.get(i).getBloodGlucose() + "", View.INVISIBLE);
                } else {
                    inputView.fillAfterGlucose(smallGroup.get(i).getBloodGlucose() + "", View.INVISIBLE);
                }
            }
        }


    }

    private class ViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> mList;

        public ViewPagerAdapter(ArrayList<View> views) {
            mList = views;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {

            Log.d(TAG, "container" + position);
            View view = mList.get(position);
            ((ViewPager) container).addView(view, 0);
            return view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Log.d(TAG, "ViewGroup" + position);
            return super.instantiateItem(container, position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public Parcelable saveState() {
            return super.saveState();
        }

        @Override
        public void startUpdate(View container) {
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mList.get(position));
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.navi_hp_glucose;
    }

    @Override
    protected void setupView() {

        getViews(this.getView());

    }

    @Override
    protected void initializedData() {

    }

}
