package com.liuchangit.comlib.servicehandle;

/**
 * 请求状态
 * @author suny
 *
 */
public enum CallStatus {
	
	PENDING("PENDING", 0),  SUCCESS("SUCCESS", 1), SENDFAIL("SENDFAIL", 2), SVRERROR("SVRERROR", 3),
	TIMEDOUT("TIMEDOUT", 4), SVREJECT("SVREJECT", 5), SENDTIMEOUT("SENDTIMEOUT", 6), NOSERVER("NOSERVER", 7);

	private int value;
	private String name;
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private CallStatus (String name, int value){
		this.name = name;
		this.value = value;
	}
	
	public static String parseName(int value){
		CallStatus [] status = values();
		for (CallStatus s : status){
			if (s.getValue()==value){
				return s.getName();
			}
		}
		return "UNKNOW STATUS";
	}
	
	@Override
	public String toString() {
		return name;
	}
}
