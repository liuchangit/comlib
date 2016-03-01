package com.liuchangit.comlib.config;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;

/**
 * static, readonly configuration. the structure of a Config object is a tree.
 * options are represented by tree nodes. a tree node has a name representing option name
 * and a value(or a list of values) representing option value. root node represents the Config object.
 * ie:
 * root
 * 	|__branch1
 * 	|		|__leaf1(val)
 * 	|		|__leaf2(v1, v2, v3)
 * 	|		|__branch2
 * 	|				|__leaf3
 * 	|
 * 	|__branch3
 * 
 * the 3 statements below will get the same value val:
 * <code>
 * root.get("branch1.leaf1")
 * root.getBranch("branch1").get("leaf1")
 * root.getBranch("branch1.leaf1").get()
 * </code>
 * 
 * this statement will get an array of 3 values(v1,v2,v3):
 * <code>
 * root.get("branch1.leaf2")
 * <code>
 * 
 * the following statement:
 * <code>
 * root.getBranch("branch1").getOptionNames()
 * <code>
 * will get [leaf1, leaf2, branch2]
 * 
 * @author charles
 *
 */
public interface Config {
	
	static String DEFAULT_SEPARATOR = "[ ,]+";
	
	public String get();
	public int getInt() throws ConfigException;
	public long getLong() throws ConfigException;
	public boolean getBool() throws ConfigException;
	public float getFloat() throws ConfigException;
	public double getDouble() throws ConfigException;
	public Date getDate() throws ConfigException;
	public InetSocketAddress getInetSocketAddress() throws ConfigException;
	
	public String get(String optionName);
	public int getInt(String optionName) throws ConfigException;
	public long getLong(String optionName) throws ConfigException;
	public boolean getBool(String optionName) throws ConfigException;
	public float getFloat(String optionName) throws ConfigException;
	public double getDouble(String optionName) throws ConfigException;
	public Date getDate(String optionName) throws ConfigException;
	public InetSocketAddress getInetSocketAddress(String optionName) throws ConfigException;
	
	public String get(String optionName, String defVal);
	public int getInt(String optionName, int defVal);
	public long getLong(String optionName, long defVal);
	public boolean getBool(String optionName, boolean defVal);
	public float getFloat(String optionName, float defVal);
	public double getDouble(String optionName, double defVal);
	public Date getDate(String optionName, Date defVal);
	public InetSocketAddress getInetSocketAddress(String optionName, String defVal);
	
	public String[] gets(String optionName);
	public int[] getInts(String optionName) throws ConfigException;
	public long[] getLongs(String optionName) throws ConfigException;
	public boolean[] getBools(String optionName) throws ConfigException;
	public float[] getFloats(String optionName) throws ConfigException;
	public double[] getDoubles(String optionName) throws ConfigException;
	public Date[] getDates(String optionName) throws ConfigException;
	public InetSocketAddress[] getInetSocketAddresses(String optionName) throws ConfigException;
	
	public String[] gets(String optionName, String separator);
	public int[] getInts(String optionName, String separator) throws ConfigException;
	public long[] getLongs(String optionName, String separator) throws ConfigException;
	public boolean[] getBools(String optionName, String separator) throws ConfigException;
	public float[] getFloats(String optionName, String separator) throws ConfigException;
	public double[] getDoubles(String optionName, String separator) throws ConfigException;
	public Date[] getDates(String optionName, String separator) throws ConfigException;
	public InetSocketAddress[] getInetSocketAddresses(String optionName, String separator) throws ConfigException;
	
	public Config getBranch(String branchName);

	public String[] getOptionNames();
	
	public Map<String, String> toMap();
	public <T> Map<String, T> toMap(Class<T> valueClazz);
}
