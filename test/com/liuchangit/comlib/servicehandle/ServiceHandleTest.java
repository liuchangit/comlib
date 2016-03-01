package com.liuchangit.comlib.servicehandle;
/*package com.easou.ps.common.servicehandle;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.easou.ps.master.rpc.ServerAddress;

public class ServiceHandleTest {
	private static ServicePool pool;
	
	@BeforeClass
	public static void setUp() {
		pool = new ServicePool("Server", 2, 500, true);
		pool.putAddress("general", "1", new ServiceAddress("183.60.198.190:6000"));
		pool.putAddress("general", "1", new ServiceAddress("183.60.198.190:7000"));
		pool.putAddress("general", "1", new ServiceAddress("183.60.198.190:9000"));
		pool.putAddress("general", "2", new ServiceAddress("121.9.222.190:6000"));
		pool.putAddress("general", "2", new ServiceAddress("121.9.222.190:7000"));
	}
	
	@Test
	public void testDefaultGroup(){
		pool = new ServicePool("Server", 2, 500, true);
		pool.putShardAddress("2", new ServiceAddress("121.9.222.190:6000"));
		pool.putShardAddress("1", new ServiceAddress("121.9.222.190:7000"));
		List<ServiceHandle> handles = pool.getServiceHandles();
		assertEquals(2, handles.size());
	}
	
	@Test
	public void testGetHandle() {
//		List<ServiceAddress> addresses = pool.selectAddresses("general");
//		assertEquals(2, addresses.size());
//		assertTrue(pool.getGroup("general").getShards().get(0).contains(addresses.get(0)));
//		assertTrue(pool.getGroup("general").getShards().get(1).contains(addresses.get(1)));
		
//		List<ServiceHandle> handles = pool.getServiceHandles("general");
//		assertEquals(2, handles.size());
//		
//		pool.putAddress("general", "3", new ServiceAddress("121.9.222.190:9000"));
//		handles = pool.getServiceHandles("general");
//		assertEquals(3, handles.size());
//		
//		assertEquals(6, pool.getServiceHandle("general", "1").connSize());
//		assertEquals(4, pool.getServiceHandle("general", "2").connSize());
//		assertEquals(2, pool.getServiceHandle("general", "3").connSize());
//		
//		Call call = new Call(1000, "search", null, null);
		ServiceHandle handle = pool.getServiceHandle("general", "1");
//		handle.putCall(call);
//
//		pool.removeAddress("general", "1", new ServiceAddress("183.60.198.190", 7000));
//		call = new Call(1000, "search", null, null);
//		handle.putCall(call);
//		assertEquals(4, handle.connSize());
//		
//		pool.putAddress("general", "1", new ServiceAddress("183.60.198.190", 7000));
//		call = new Call(1000, "search", null, null);
//		handle.putCall(call);
//		
		
//		System.out.println(new Date() +" " + handle);
//		assertEquals(6, handle.connSize());
//		List<ServiceAddress> addrs = pool.selectAddresses("general");
//		for(ServiceAddress addr : addrs){
//			System.out.println(addr +" inactivate ");
//			addr.inactivate();
//		}
//		System.out.println(new Date() +" " + handle);
//		
//		for (int i=0; i<1000; i++){
//			System.out.println(new Date() +" " + handle);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		pool.removeAddress("general", "3", new ServiceAddress("121.9.222.190:9000"));
//		assertEquals(0, pool.getServiceHandle("general", "3").connSize());
		
		
	}
	
	@Test
	public void testHashMapRemove() {
		
//		int size =10000;
//		Map<Integer, String> map = new HashMap<Integer, String>();
//		for(int i=0; i<size; i++){
//			map.put(i, ""+i);
//		}
//		
//		long start = System.currentTimeMillis();
//		for(Integer en : map.keySet()){
//			if (en%10 ==0){
//				map.remove(en);
//			}
//		}
//		long cost1 = System.currentTimeMillis()-start;
//		System.out.println(map.size());
//		
//		map.clear();
//		for(int i=0; i<size; i++){
//			map.put(i, ""+i);
//		}
//		start = System.nanoTime();
//		Set<Map.Entry<Integer, String>> set = map.entrySet();
//		for(Iterator<Map.Entry<Integer, String>> it=set.iterator();it.hasNext();){
//			Map.Entry<Integer, String> en= it.next();
//			if (en.getKey()%10 ==0){
//				System.out.println(en.getKey() +":" +en.getValue());
//				it.remove();
//			}
//		} 
//		long cost2 = System.nanoTime()-start;
//		System.out.println(map.size());
//		
//		map.clear();
//		for(int i=0; i<size; i++){
//			map.put(i, ""+i);
//		}
//		List<Integer> key = new ArrayList<Integer>();
//		start = System.nanoTime();
//		for(Map.Entry<Integer, String> en : map.entrySet()){
//			if (en.getKey()%10 ==0){
//				key.add(en.getKey());
//			}
//		}
//		for (int i=0; i<key.size(); i++){
//			System.out.println(key.get(i) +":" +map.remove(key.get(i)));
//		}
//		long cost3 = System.nanoTime()-start;
//		System.out.println(map.size());
//		
//		System.out.println("cost1:" +0+"; cost2:" +cost2+", cost3:" +cost3);
	}
	
	@Test
	public void testInetSocketAddress (){
		InetSocketAddress addr = new InetSocketAddress ("121.9.222.190", 7000);
		System.out.println(addr.hashCode());
		System.out.println("121.9.222.190".hashCode() + 7000);
		
//		ServerAddress address = new ServerAddress("121.9.222.190", 7000, "vip1", 100);
//		ServiceAddress serviceAddr = address.toServiceAddress();
		ServiceAddress serviceAddr = new ServiceAddress("121.9.222.190", 7000);
		
		Map<ServiceAddress, Integer> map = new HashMap<ServiceAddress, Integer>();
		map.put(serviceAddr, 0);
		System.out.println(map.get(new ServiceAddress("121.9.222.190:7000")));
		
	}
}
*/