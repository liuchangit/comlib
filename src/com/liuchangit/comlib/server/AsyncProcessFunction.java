package com.liuchangit.comlib.server;

import org.apache.thrift.TBase;
import org.apache.thrift.protocol.TProtocol;

import com.liuchangit.comlib.server.TAsyncServer.MessageBuffer;

public abstract class AsyncProcessFunction<I, T extends TBase> {
	
	protected final String methodName;
	
	public AsyncProcessFunction(String methodName) {
		this.methodName = methodName;
	}
	
	public abstract void preprocess(MessageBuffer msg, TProtocol iprot, I processor) throws Exception;
	public abstract T postprocess(Message msg, I processor) throws Exception;

}
