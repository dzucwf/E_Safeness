package com.safeness.e_saveness_common.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * @author laixuezhu
 * 
 */
public abstract class AppBaseFragment extends Fragment implements
        ResultCallback {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	protected static final String TAG = "AppBaseFragment";

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// setContentView(getLayoutId());

		// TODO Auto-generated method stub
		return inflater.inflate(getLayoutId(), container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mAsyncRequests = new ArrayList<AsyncBaseRequest>();
		mDefaultThreadPool = DefaultThreadPool.getInstance();

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage(getString(R.string.msg_network_progress));
		// 初始化组件
		setupView();
		// 初始化数据
		initializedData();


		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * 布局文件ID
	 * 
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
						// conn.disconnect();
						System.out.println("onDestroy disconnect URL: "
								+ conn.getURL());
						// mAsyncRequests.remove(request);//在此删除会报
						// java.util.ConcurrentModificationException
					} catch (UnsupportedOperationException e) {
						// do nothing .
					}
				}
			}
			mAsyncRequests.removeAll(delList);
		}
	}

	/**
	 * 显示Toast形式的提示信息
	 * 
	 * @param message
	 */
	protected void show(String message) {
		Toast.makeText(getActivity().getApplicationContext(), message,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub

		cancelRequest();
		dissProgressDialog();

		super.onDestroyView();
	}

	/**
	 * 显示进度框
	 */
	protected void showProgressDialog(String msg) {
		if (!mProgressDialog.isShowing()) {
			if (msg != null) {
				mProgressDialog.setMessage(msg);
			}
			mProgressDialog.show();
		}
	}

	/**
	 * 取消进度框
	 */
	protected void dissProgressDialog() {
		if (mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

	// send request to server
	protected void request(Map<String, String> parameter, String url,
			int reqCode, ResultCallback callback, ParseCallback jsonHandler) {
		// 检查网络状态
		if (!NetworkDetector.detect(getActivity())) {
			show(getString(R.string.msg_network_unavailable));
			return;
		}
		showProgressDialog(null);
		AsyncBaseRequest request = new AsyncHttpPost(url, parameter,
				jsonHandler, callback, reqCode, getActivity());
		mDefaultThreadPool.execute(request);
		mAsyncRequests.add(request);
	}

	// 网络请求成功回调方法，在子类中重写
	@Override
	public void onSuccess(Object obj, int reqCode) {
		// TODO Auto-generated method stub

	}

	// 网络请求失败回调方法，在子类中重写
	@Override
	public void onFail(int errorCode, int reqCode) {
		// TODO Auto-generated method stub
	}
}
