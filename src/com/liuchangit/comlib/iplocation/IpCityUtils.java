package com.liuchangit.comlib.iplocation;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpCityUtils {
	
	/**
	 * 通过IP获取 IP所在的城市区域
	 * @param ip
	 * @return 一个city区域的 长串（北京市海淀区五道口），调用方需要根据具体需求来 截取返回数据中的有效信息
	 */
	public static String getCityByIp(String ip){
		String city = "";
		
		IPLocation loc = IPSeekerNew.getInstance().getIPLocation(ip, false);
		if (loc != null) {
			city = loc.getCity();
		}
		
		return city;
	}
	
	/**
	 * 通过IP获取 IP所在的城市区域，并与用户citys参数进行对比处理。
	 * @param ip
	 * @param citys
	 * @return
	 */
	public static String getCityByIp(String ip, Set<String> citys){
		String city = "";
		
		IPLocation loc = IPSeekerNew.getInstance().getIPLocation(ip, false);
		if (loc != null) {
			city = getCityByName(loc.getCity(), citys);
		}
		
		return city;
	}
	
	
	/**
	 * 通过实际的地址，获得在集合中的城市名
	 * @param longCityName 实际的地址，比较长 如 北京市海淀区五道口
	 * @param citys    地址集合，一般为城市名字 如 北京 天津（没有市等级别名）
	 * @return
	 */
	public static String getCityByName(String longCityName, Set<String> citys){
		if (longCityName != null && longCityName.length() > 0) {
			return "";
		}
		String cityName = null;
		for (Iterator<String> iterator = citys.iterator(); iterator.hasNext();) {
			String oneCity = iterator.next();
			if(longCityName.contains(oneCity)){
				if(null == cityName){
					cityName = oneCity;
				}else if(longCityName.indexOf(oneCity) < longCityName.indexOf(cityName)){
					// 城市名越靠前，越重要，如 深圳市北京师范大学，city为深圳
					cityName = oneCity;
				}
			}
		}
		return cityName;
	} 
	
	/**
	 * 通过实际的地址，获得在集合中的城市名
	 * @param longCityName 实际的地址，比较长 如 北京市海淀区五道口
	 * @param如 (北京|天津)
	 * @return
	 */
	public static String getCountryByName(String longCityName, Pattern cityPattern){
		if (longCityName != null && longCityName.length() > 0) {
			return "";
		}
		Matcher m = cityPattern.matcher(longCityName);
		if (m.find()) {
			return m.group(1);
		}
		return "";
		
	}


	
	public static void main(String[] args) {
		IPSeekerNew ip1 = IPSeekerNew.getInstance();
		String ips = "124.192.33.194";
		String country;
//		area = ip1.getArea(ips);
		country = ip1.getAddress(ips);
		System.out.println("result： " + country );
//		国家名： 北京市 地区名：电信通
	}

}
