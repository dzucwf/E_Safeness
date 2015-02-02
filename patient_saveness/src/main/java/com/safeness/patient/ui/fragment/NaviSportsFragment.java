package com.safeness.patient.ui.fragment;

import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.patient.R;

import com.safeness.e_saveness_common.chart.GenericChart;

import org.w3c.dom.Text;

//运动
public class NaviSportsFragment extends AppBaseFragment {

    private TextView txv_timer;       //分
    private TextView btn_timer;       //秒
    private TextView btn_finish;           //开始按钮
    private boolean isPaused = true;
    private String timeUsed;
    private int timeUsedInsec;

    private WebView myWebView;
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
    public void onResume() {
        super.onResume();
        isPaused = false;
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


        myWebView = (WebView)getActivity().findViewById(R.id.web_sports_chart);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        String[] xAxis= {"6时","12时","18时","0时"};
        String yAxisTitle = "血糖值(mmol/L)";
        double[] series = {4.5,7.8,5.6,3.2};
        myWebView.loadData(GenericChart.getChartStr(getActivity(),xAxis,yAxisTitle,series), "text/html", "utf-8");

    }

}
