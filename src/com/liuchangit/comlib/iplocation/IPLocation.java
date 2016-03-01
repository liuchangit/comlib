package com.liuchangit.comlib.iplocation;

/** 
 * <pre> 
 * 用来封装ip相关信息，目前只有两个字段，ip所在的国家和地区 
 * </pre> 
 */
public class IPLocation {
	private String city;
	private String area;

	public IPLocation() {
		city = "";
		area = "";
	}

	public IPLocation getCopy() {
		IPLocation ret = new IPLocation();
		ret.city = city;
		ret.area = area;
		return ret;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}