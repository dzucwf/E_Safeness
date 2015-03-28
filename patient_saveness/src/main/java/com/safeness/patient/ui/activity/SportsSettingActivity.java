package com.safeness.patient.ui.activity;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.patient.R;

import com.safeness.e_saveness_common.chart.GenericChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

//运动
public class SportsSettingActivity extends AppBaseFragment {

    private TextView txv_timer;       //分
    private TextView btn_timer;       //秒
    private TextView btn_finish;           //开始按钮
    private boolean isPaused = true;
    private String timeUsed;
    private int timeUsedInsec;

    private WebView myWebView;
    private WebView web_cal;

    @Override
    protected int getLayoutId() {
        return R.layout.navi_hp_sports;

    }


    private Handler uiHandle = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case 1:
                    if(!isPaused) {
                        addTimeUsed();
                        updateClockUI();
                    }
                    uiHandle.sendEmptyMessageDelayed(1, 1000);
                    break;
                default: break;
            }
        }
    };
    private void updateClockUI(){
        txv_timer.setText(getHour() + ":" + getMin() + ":" + getSec());
    }
    public void addTimeUsed(){
        timeUsedInsec = timeUsedInsec + 1;
        timeUsed = this.getMin() + ":" + this.getSec();
    }
    public CharSequence getHour(){
        int sec = timeUsedInsec / 60/60;
        return sec <10 ? "0"+sec : String.valueOf(sec);
    }
    public CharSequence getMin(){
        int sec = timeUsedInsec / 60;

        return sec <10 ? "0"+sec : String.valueOf(sec);
    }
    public CharSequence getSec(){
        int sec = timeUsedInsec % 60;
        return sec < 10? "0" + sec :String.valueOf(sec);
    }
    private void startTime(){
        uiHandle.sendEmptyMessageDelayed(1, 1000);
    }
    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        isPaused = true;
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
        txv_timer = (TextView)getActivity().findViewById(R.id.txv_sports_Timer);

        btn_timer = (TextView)getActivity().findViewById(R.id.btn_sports_timer);
        btn_finish = (TextView)getActivity().findViewById(R.id.btn_sports_finish);

        btn_timer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isPaused == true){
                    btn_timer.setText("暂停计时");
                    uiHandle.removeMessages(1);
                    startTime();
                }
                else{
                    btn_timer.setText("继续计时");
                }
                isPaused = !isPaused;
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        myWebView = (WebView)getActivity().findViewById(R.id.web_sports_chart);
        web_cal = (WebView)getActivity().findViewById(R.id.web_sports_cal);

        web_cal.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptEnabled(true);

        web_cal.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        web_cal.getSettings().setDefaultTextEncodingName("utf-8");
        myWebView.getSettings().setDefaultTextEncodingName("utf-8");

        web_cal.setBackgroundColor(Color.TRANSPARENT);
        myWebView.setBackgroundColor(Color.TRANSPARENT);

        web_cal.loadUrl("file:///android_asset/www/pie2.htm");
        myWebView.loadUrl("file:///android_asset/www/sports.htm");

        web_cal.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                try {
                    // 首先最外层是{}，是创建一个对象
                    JSONArray ca = new JSONArray();
                    ca.put(30).put(70);
                    web_cal.loadUrl("javascript:update('"+ca.toString()+"','170','卡路里kCal');");
                }
                catch (Exception ex) {
                    // 键为null或使用json不支持的数字格式(NaN, infinities)
                    throw new RuntimeException(ex);
                }
            }
        });
/*
        myWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                try {
                    // 首先最外层是{}，是创建一个对象
                    JSONArray series = new JSONArray();

                    JSONObject series1 = new JSONObject();
                    JSONArray s1name = new JSONArray();
                    s1name.put("饭前血糖值");
                    series1.put("name", s1name);

                    JSONArray s1data = new JSONArray();
                    s1data.put(1).put(2).put(3).put(4);
                    series1.put("data", s1data);

                    JSONObject series2 = new JSONObject();
                    JSONArray s2name = new JSONArray();
                    s2name.put("饭后血糖值");
                    series2.put("name", s2name);

                    JSONArray s2data = new JSONArray();
                    s2data.put(3).put(4).put(5).put(6);
                    series2.put("data", s2data);

                    series.put(series1).put(series2);
                    myWebView.loadUrl("javascript:update('"+series.toString()+"','[1,2,3,4,5,6,7,8,9,10]','[1]');");
                }
                catch (JSONException ex) {
                    // 键为null或使用json不支持的数字格式(NaN, infinities)
                    throw new RuntimeException(ex);
                }
            }
        });

*/



        /*
        String[] xAxis= {"6时","12时","18时","0时"};
        String yAxisTitle = "血糖值(mmol/L)";
        double[] series = {4.5,7.8,5.6,3.2};
        String test = GenericChart.getChartStr(getActivity(), xAxis, yAxisTitle, series);
        Log.v("e",test);
        myWebView.loadDataWithBaseURL("about:blank", test, "text/html",  "utf-8", null);
        //myWebView.loadData(test, "text/html", "utf-8");*/

    }

}
