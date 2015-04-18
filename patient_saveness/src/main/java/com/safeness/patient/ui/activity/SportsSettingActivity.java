package com.safeness.patient.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.e_saveness_common.dao.DaoFactory;
import com.safeness.e_saveness_common.dao.IBaseDao;
import com.safeness.e_saveness_common.dao.QueryResult;
import com.safeness.patient.R;

import com.safeness.e_saveness_common.chart.GenericChart;
import com.safeness.patient.model.Sports;
import com.safeness.patient.model.U_s;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//运动
public class SportsSettingActivity extends AppBaseActivity {

    //private TextView txv_timer;       //分
    //private TextView btn_timer;       //秒
    private TextView btn_forget;           //忘记按钮
    private TextView btn_finish;           //开始按钮
    private ImageView btn_back;
    private boolean isPaused = true;
    private String timeUsed;
    private int timeUsedInsec;

    private TextView txv_sports_name;
    private TextView txv_sports_desc;

    private WebView myWebView;
    private WebView web_cal;

    private String _id = null;
    private Date selectDate = null;
    PatientApplication app;

    private IBaseDao<Sports> sportsDao;
    private IBaseDao<U_s> u_sDao;
    private Sports sports = new Sports();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*取出Intent中附加的数据*/
        Intent intent =getIntent();
        _id = intent.getStringExtra("sports_id");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            selectDate =df.parse(intent.getStringExtra("select_date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SportsSettingActivity.this.finish();
            }
        });

        if (_id != null){
            app = (PatientApplication) this.getApplication();
            sportsDao = DaoFactory.createGenericDao(this, Sports.class);
            u_sDao = DaoFactory.createGenericDao(this, U_s.class);
            List<QueryResult> sportsList = sportsDao.execQuerySQL(
                    "select * from sports where [_id] = ?", _id);

            if (sportsList != null && sportsList.size() > 0){
                sports.setSportsName(sportsList.get(0).getStringProperty("sportsName"));
                sports.setDesc1(sportsList.get(0).getStringProperty("desc1"));
                sports.setCalorie(sportsList.get(0).getStringProperty("calorie").length() <= 0 ? -1 : sportsList.get(0).getDoubleProperty("calorie"));
                sports.set_id(_id);

            }
            txv_sports_name.setText(sports.getSportsName());
            txv_sports_desc.setText(sports.getDesc1());
        }



    }

    @Override
    protected int getLayoutId() {
        return R.layout.sports_setting;
    }

/*
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
*/
    @Override
    protected void setupView() {
        getViews();
    }

    @Override
    protected void initializedData() {

    }
    //初始化下层切换
    private void getViews() {

        txv_sports_name = (TextView)this.findViewById(R.id.txv_sports_name);

        txv_sports_desc = (TextView)this.findViewById(R.id.txv_sports_desc);
        //txv_timer = (TextView)this.findViewById(R.id.txv_sports_Timer);

        //btn_timer = (TextView)this.findViewById(R.id.btn_sports_timer);
        btn_forget = (TextView)this.findViewById(R.id.btn_sports_forget);
        btn_finish = (TextView)this.findViewById(R.id.btn_sports_finish);

        btn_back  = (ImageView)this.findViewById(R.id.btn_sports_setting_back);
        /*
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
*/
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                U_s u_s = new U_s();
                u_s.setU_sid(app.getUserID());
                u_s.setS_id(_id);
                u_s.setCalorie(-1);
                u_s.setCount(-1);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                u_s.setFinishedDate(df.format(new Date().getTime()));
                u_s.setUpdate_datetime(df.format(new Date().getTime()));
                u_s.setLife_status(2);
                if(u_sDao.insertOrUpdate(u_s, "u_sid","s_id")){
                    btn_finish.setBackgroundResource(R.drawable.sport_btn_pressed);
                    btn_forget.setBackgroundResource(R.drawable.sport_btn_normal);
                    Dialog alertDialog = new AlertDialog.Builder(SportsSettingActivity.this).
                            setTitle("您的运动数据已保存成功").
                            setIcon(R.drawable.ic_launcher).
                            setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).
                            create();
                    alertDialog.show();
                }
            }
        });
        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                U_s u_s = new U_s();
                u_s.setU_sid(app.getUserID());
                u_s.setS_id(_id);
                u_s.setCalorie(-1);
                u_s.setCount(-1);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                u_s.setFinishedDate(df.format(new Date().getTime()));
                u_s.setUpdate_datetime(df.format(new Date().getTime()));
                u_s.setLife_status(3);
                if(u_sDao.insertOrUpdate(u_s, "u_sid","s_id")){
                    btn_finish.setBackgroundResource(R.drawable.sport_btn_normal);
                    btn_forget.setBackgroundResource(R.drawable.sport_btn_pressed);
                    Dialog alertDialog = new AlertDialog.Builder(SportsSettingActivity.this).
                            setTitle("您的运动数据已保存成功").
                            setIcon(R.drawable.ic_launcher).
                            setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).
                            create();
                    alertDialog.show();
                }
            }
        });
        myWebView = (WebView)this.findViewById(R.id.web_sports_chart);
        web_cal = (WebView)this.findViewById(R.id.web_sports_cal);

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
                    web_cal.loadUrl("javascript:update('"+ca.toString()+"','"+sports.getCalorie()+"','卡路里kCal');");
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
