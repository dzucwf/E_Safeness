package com.safeness.e_saveness_common.net;

import android.util.Log;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class DefaultThreadPool {

	private static ArrayBlockingQueue<Runnable> mBlockingQueue = new ArrayBlockingQueue<Runnable>(
			15, true);

	private static AbstractExecutorService mThreadPoolExecutor = new ThreadPoolExecutor(
			5, 7, 10, TimeUnit.SECONDS, mBlockingQueue,
			new ThreadPoolExecutor.DiscardOldestPolicy());

	private static DefaultThreadPool instance = null;

	public static DefaultThreadPool getInstance() {
		if (instance == null) {
			instance = new DefaultThreadPool();
		}
		return instance;
	}

	public void execute(Runnable r) {
		mThreadPoolExecutor.execute(r);
	}


	public static void shutdown() {
		if (mThreadPoolExecutor != null) {
			mThreadPoolExecutor.shutdown();
			Log.i(DefaultThreadPool.class.getName(),
					"DefaultThreadPool shutdown");
		}
	}


	public static void shutdownRightnow() {
		if (mThreadPoolExecutor != null) {
			mThreadPoolExecutor.shutdownNow();
			try {
				// ���ó�ʱ���̣�ǿ�ƹر���������
				mThreadPoolExecutor.awaitTermination(1, TimeUnit.MICROSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Log.i(DefaultThreadPool.class.getName(),
					"DefaultThreadPool shutdownRightnow");
		}
	}

}
