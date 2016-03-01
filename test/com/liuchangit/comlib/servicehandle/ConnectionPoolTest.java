package com.liuchangit.comlib.servicehandle;

import org.junit.Test;

import com.liuchangit.comlib.servicehandle.ConnectionGuard;
import com.liuchangit.comlib.servicehandle.ServiceAddress;
import com.liuchangit.comlib.servicehandle.ServicePool;

public class ConnectionPoolTest {
	
	@Test
	public void testGetConnection() {
		ServicePool pool = new ServicePool();
		ServiceAddress address = new ServiceAddress("183.60.215.83", 7000);
		pool.putAddress("default", "default", address);
		
		for (int i = 0; i < 5; i++) {
			ConnectionGuard conn = address.getConnection(i);
			System.out.println(conn);
		}
	}
}
