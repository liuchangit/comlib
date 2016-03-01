package com.liuchangit.comlib.servicehandle;

public interface Callback {
	public void onSuccess(Call call);
	public void onError(Call call);
	public void onReject(Call call);
}
