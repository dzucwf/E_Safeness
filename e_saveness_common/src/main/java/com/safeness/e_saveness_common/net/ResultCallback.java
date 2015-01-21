package com.safeness.e_saveness_common.net;


public interface ResultCallback {
	public void onSuccess(Object obj, int reqCode);

	public void onFail(int errorCode, int reqCode);

}

