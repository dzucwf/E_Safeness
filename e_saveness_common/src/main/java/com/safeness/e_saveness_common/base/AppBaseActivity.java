package com.safeness.e_saveness_common.base;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.safeness.e_saveness_common.R;
import com.safeness.e_saveness_common.net.AsyncBaseRequest;
import com.safeness.e_saveness_common.net.AsyncHttpPost;
import com.safeness.e_saveness_common.net.DefaultThreadPool;
import com.safeness.e_saveness_common.net.ParseCallback;
import com.safeness.e_saveness_common.net.ResultCallback;
import com.safeness.e_saveness_common.util.NetworkDetector;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * 有网络请求的页面继承该Activity,方便处理网络请求
 *
 *
 */
public abstract class AppBaseActivity extends FragmentActivity implements ResultCallback {



    /**
     * 当前activity所持有的所有网络请求
     */
    protected List<AsyncBaseRequest> mAsyncRequests;

    protected DefaultThreadPool mDefaultThreadPool;
    /**
     * 网络请求过程进度提示
     */
    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(getLayoutId());

        mAsyncRequests = new ArrayList<AsyncBaseRequest>();
        mDefaultThreadPool = DefaultThreadPool.getInstance();
        
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.msg_network_progress));
       
       
        // 初始化组件
        setupView();
        // 初始化数据
        initializedData();
        

    }
    
    /**
     * 布局文件ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化组件
     */
    protected abstract void setupView();

    /**
     * 初始化数据
     */
    protected abstract void initializedData();

    @Override
    protected void onPause() {
        /**
         * 在activity销毁的时候同时设置停止请求，停止线程请求回调
         */
//        cancelRequest();
        super.onPause();
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		
		}
		return false;
	}

    private void cancelRequest() {
        if (mAsyncRequests != null && mAsyncRequests.size() > 0) {
        	List<AsyncBaseRequest> delList = new ArrayList<AsyncBaseRequest>();
            for (AsyncBaseRequest request : mAsyncRequests) {
            	
            	Thread thread = new Thread(request);  
                if (thread.isAlive() || !Thread.interrupted()) {  
                    request.setInterrupted(true);  
                } 
                
                HttpURLConnection conn = request.getRequestConn();
                if (conn != null) {
                    try {
//                        conn.disconnect();
                        System.out.println("onDestroy disconnect URL: " + conn.getURL());
//                        mAsyncRequests.remove(request);//在此删除会报 java.util.ConcurrentModificationException
                    } catch (UnsupportedOperationException e) {
                        //do nothing .
                    }
                }
            }
            mAsyncRequests.removeAll(delList);
        }
    }

    /**
     * 显示Toast形式的提示信息
     * @param message
     */
    protected void show(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        /**
         * 在activity销毁的时候同时设置停止请求，停止线程请求回调
         */
        cancelRequest();
        dissProgressDialog();

        super.onDestroy();
    }
    /**
     * 显示进度框
     */
    protected void showProgressDialog(String msg) {
		if(!mProgressDialog.isShowing()) {
			if(msg != null) {
				mProgressDialog.setMessage(msg);
			}
			mProgressDialog.show();
		}
	}
    /**
     * 取消进度框
     */
    protected void dissProgressDialog() {
    	if(mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}
    //send request to server
  	protected void request(Map<String, String> parameter, String url, int reqCode, ResultCallback callback,ParseCallback jsonHandler) {
  		//检查网络状态
		if(!NetworkDetector.detect(this)) {
			show(getString(R.string.msg_network_unavailable));
			return;
		}
  		showProgressDialog(null);
  		AsyncBaseRequest request = new AsyncHttpPost(url,
  				parameter, jsonHandler, callback, reqCode, this);
  		mDefaultThreadPool.execute(request);
  		mAsyncRequests.add(request);
  	}
  	//网络请求成功回调方法，在子类中重写
  	@Override
  	public void onSuccess(Object obj, int reqCode) {
  		// TODO Auto-generated method stub
  		
  	}
  	//网络请求失败回调方法，在子类中重写
  	@Override
  	public void onFail(int errorCode, int reqCode) {
  		// TODO Auto-generated method stub
  	}

    /*
    * 以下是聊天方法
    * */




}
