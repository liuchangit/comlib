package com.liuchangit.comlib.server;

import com.liuchangit.comlib.server.TAsyncServer.MessageBuffer;

public abstract class Message {
	protected final MessageBuffer messageBuffer;
	protected long seqNo;
	
	public Message(MessageBuffer mb) {
		this.messageBuffer = mb;
	}
	
	public MessageBuffer getMessageBuffer() {
		return this.messageBuffer;
	}

	public long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}
	
	
}
