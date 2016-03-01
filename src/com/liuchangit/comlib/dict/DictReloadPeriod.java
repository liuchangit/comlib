package com.liuchangit.comlib.dict;

public enum DictReloadPeriod {
	ONE_MIN(60), FIVE_MIN(5*60), FIFTEEN_MIN(15*60), HALF_HOUR(30*60), ONE_HOUR(60*60), TWO_HOUR(2*60*60), SIX_HOUR(6*60*60), HALF_DAY(12*60*60), ONE_DAY(24*60*60), ONE_WEEK(7*24*60*60), ONE_MONTH(30*24*60*60), NEVER(0);
	
	private long secs;
	
	private DictReloadPeriod(long secs) {
		this.secs = secs;
	}
	
	public long getSeconds() {
		return secs;
	}
}
