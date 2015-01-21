package com.safeness.e_saveness_common.base;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
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

public abstract class AppBaseIntentService extends IntentService  implements ResultCallback {

	
	
	
	public AppBaseIntentService() {
		super("AppBaseIntentService");
		// TODO Auto-generated constructor stub
	}
	
	public AppBaseIntentService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	

	protected List<AsyncBaseRequest> mAsyncRequests;
    protected DefaultThreadPool mDefaultThreadPool;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		mAsyncRequests = new ArrayList<AsyncBaseRequest>();
        mDefaultThreadPool = DefaultThreadPool.getInstance();
        initService();
	}
	
	protected abstract void initService();
	
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
                    	Log.i(AppBaseIntentService.class.getName(), "onDestroy disconnect URL: " + conn.getURL());
//                        mAsyncRequests.remove(request);//�ڴ�ɾ���ᱨ java.util.ConcurrentModificationException
                        delList.add(request);
                    } catch (UnsupportedOperationException e) {
                        //do nothing .
                    }
                }
            }
            mAsyncRequests.removeAll(delList);
        }
    }
	
	 //send request to server
  	protected void request(Map<String, String> parameter, String url, int reqCode, ResultCallback callback,ParseCallback jsonHandler) {
  		//检查网络状态
		if(!NetworkDetector.detect(this)) {
			show(getString(R.string.msg_network_unavailable));
			return;
		}
  		
  		AsyncBaseRequest request = new AsyncHttpPost(url,
  				parameter, jsonHandler, callback, reqCode, this);
  		mDefaultThreadPool.execute(request);
  		mAsyncRequests.add(request);
  	}
  	

    protected void show(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
	@Override
	public void onDestroy() {
    
        cancelRequest();
        super.onDestroy();
    }
}
