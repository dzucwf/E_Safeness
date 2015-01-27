package com.safeness.patient.ui.fragment;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.patient.R;

import com.safeness.e_saveness_common.chart.GenericChart;

//运动
public class NaviSportsFragment extends AppBaseFragment {


    private WebView myWebView;
    @Override
    protected int getLayoutId() {
        return R.layout.navi_hp_sports;

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
        myWebView = (WebView)getActivity().findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        String[] xAxis= {"6时","12时","18时","0时"};
        String yAxisTitle = "血糖值(mmol/L)";
        double[] series = {4.5,7.8,5.6,3.2};
        myWebView.loadData(GenericChart.getChartStr(getActivity(),xAxis,yAxisTitle,series), "text/html", "utf-8");
    }

}
