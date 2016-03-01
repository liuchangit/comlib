package com.liuchangit.comlib.iplocation;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * <pre>
 *  
 * 用来读取qqwry.dat文件，以根据ip获得好友位置，qqwry.dat的格式是 
 * 一. 文件头，共8字节 
 *     1. 第一个起始IP的绝对偏移， 4字节 
 *     2. 最后一个起始IP的绝对偏移， 4字节 
 * 二. &quot;结束地址/国家/区域&quot;记录区 
 *     四字节ip地址后跟的每一条记录分成两个部分 
 *     1. 国家记录 
 *     2. 地区记录 
 *     但是地区记录是不一定有的。而且国家记录和地区记录都有两种形式 
 *     1. 以0结束的字符串 
 *     2. 4个字节，一个字节可能为0x1或0x2 
 *   a. 为0x1时，表示在绝对偏移后还跟着一个区域的记录，注意是绝对偏移之后，而不是这四个字节之后 
 *        b. 为0x2时，表示在绝对偏移后没有区域记录 
 *        不管为0x1还是0x2，后三个字节都是实际国家名的文件内绝对偏移 
 *   如果是地区记录，0x1和0x2的含义不明，但是如果出现这两个字节，也肯定是跟着3个字节偏移，如果不是 
 *        则为0结尾字符串 
 * 三. &quot;起始地址/结束地址偏移&quot;记录区 
 *     1. 每条记录7字节，按照起始地址从小到大排列 
 *        a. 起始IP地址，4字节 
 *        b. 结束ip地址的绝对偏移，3字节 
 * 
 * 注意，这个文件里的ip地址和所有的偏移量均采用little-endian格式，而java是采用big-endian格式的，要注意转换 
 * </pre>
 * 
 */
public class IPSeekerNew {
	
	private static final String QQWRY_DAT = "qqwry.dat";

	// 一些固定常量，比如记录长度等等
	private static final int IP_RECORD_LENGTH = 7;
	private static final byte AREA_FOLLOWED = 0x01;
	private static final byte NO_AREA = 0x2;

	// 用来做为cache，查询一个ip时首先查看cache，以减少不必要的重复查找
//	private Hashtable<String, IPLocation> ipCache;// 是否还需要？！

	private byte[] fileDataBytes;
	// 内存映射文件
	// private ByteBuffer mbb;

	// 起始地区的开始和结束的绝对偏移
	private int ipBeginOffset;
	private int ipEndOffSet;

	// 单一模式实例
	private static IPSeekerNew instance = new IPSeekerNew();

	/**
	 * @return 单一实例
	 */
	public static IPSeekerNew getInstance() {
		return instance;
	}

	/**
	 * 私有构造函数
	 */
	private IPSeekerNew() {
		
//		ipCache = new Hashtable<String, IPLocation>();
		ByteBuffer mbb = null;
		try {
			String file = this.getClass().getClassLoader().getResource(QQWRY_DAT).getFile();
			// 随机文件访问类
			RandomAccessFile ipFile = new RandomAccessFile(file, "r");

			mbb = ByteBuffer.allocate((int) ipFile.length());
			ipFile.getChannel().read(mbb, 0);
			mbb.order(ByteOrder.LITTLE_ENDIAN);

			fileDataBytes = mbb.array();

			ipFile.close();
		} catch (Exception e) {
			System.out.println(IPSeekerNew.class.getResource(QQWRY_DAT).toString());
			System.out.println("IP地址信息文件没有找到，IP显示功能将无法使用");
			fileDataBytes = new byte[0];

		}
		// 如果打开文件成功，读取文件头信息
		if (mbb != null) {
			try {
				ipBeginOffset = readInt4(mbb, 0);
				ipEndOffSet = readInt4(mbb, 4);
				System.out.println("ipBegin:" + ipBeginOffset + ",ipEnd:" + ipEndOffSet);
				if (ipBeginOffset == -1 || ipEndOffSet == -1) {
					mbb.clear();
					mbb = null;
				}
			} catch (Exception e) {
				System.out.println("IP地址信息文件格式有错误，IP显示功能将无法使用");
				mbb = null;
			}
		}
	}

	private ByteBuffer getNewByteBuffer() {
		ByteBuffer mbb;
		mbb = ByteBuffer.wrap(fileDataBytes);
		mbb.order(ByteOrder.LITTLE_ENDIAN);
		return mbb;
	}

	/**
	 * 从offset位置读取4个字节为一个long，因为java为big-endian格式，所以没办法 用了这么一个函数来做转换
	 * 
	 * @param offset
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private int readInt4(ByteBuffer mbb, int offset) {
		int ret = 0;
		try {
			mbb.position();
			mbb.position(offset);
			ret |= (mbb.get() & 0xFF);
			ret |= ((mbb.get() << 8) & 0xFF00);
			ret |= ((mbb.get() << 16) & 0xFF0000);
			ret |= ((mbb.get() << 24) & 0xFF000000);
			return ret;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 从内存映射文件的offset位置开始的3个字节读取一个int
	 * 
	 * @param offset
	 * @return
	 */
	private int readInt3(ByteBuffer mbb, int offset) {
		mbb.position(offset);
		return mbb.getInt() & 0x00FFFFFF;
	}

	/**
	 * 从内存映射文件的当前位置开始的3个字节读取一个int
	 * 
	 * @return
	 */
	private int readInt3(ByteBuffer mbb) {
		return mbb.getInt() & 0x00FFFFFF;
	}

	/**
	 * 从offset位置读取四个字节的ip地址放入ip数组中，读取后的ip为big-endian格式，但是
	 * 文件中是little-endian形式，将会进行转换
	 * 
	 * @param offset
	 * @param ip
	 */
	private void readIP(ByteBuffer mbb, int offset, byte[] ip) {
		mbb.position(offset);
		mbb.get(ip);
		byte temp = ip[0];
		ip[0] = ip[3];
		ip[3] = temp;
		temp = ip[1];
		ip[1] = ip[2];
		ip[2] = temp;
	}

	/**
	 * 把类成员ip和beginIp比较，注意这个beginIp是big-endian的
	 * 
	 * @param ip
	 *            要查询的IP
	 * @param beginIp
	 *            和被查询IP相比较的IP
	 * @return 相等返回0，ip大于beginIp则返回1，小于返回-1。
	 */
	private int compareIP(byte[] ip, byte[] beginIp) {
		for (int i = 0; i < 4; i++) {
			int r = compareByte(ip[i], beginIp[i]);
			if (r != 0)
				return r;
		}
		return 0;
	}

	/**
	 * 把两个byte当作无符号数进行比较
	 * 
	 * @param b1
	 * @param b2
	 * @return 若b1大于b2则返回1，相等返回0，小于返回-1
	 */
	private int compareByte(byte b1, byte b2) {
		if ((b1 & 0xFF) > (b2 & 0xFF)) // 比较是否大于
			return 1;
		else if ((b1 ^ b2) == 0)// 判断是否相等
			return 0;
		else
			return -1;
	}

	/**
	 * 这个方法将根据ip的内容，定位到包含这个ip国家地区的记录处，返回一个绝对偏移 方法使用二分法查找。
	 * 
	 * @param ip
	 *            要查询的IP
	 * @return 如果找到了，返回结束IP的偏移，如果没有找到，返回-1
	 */
	private int locateIP(ByteBuffer mbb, byte[] ip) {
		int m = 0;
		int r;
		// 比较第一个ip项
		byte[] ipBytes = new byte[4];
		readIP(mbb, ipBeginOffset, ipBytes);
		r = compareIP(ip, ipBytes);
		if (r == 0)
			return ipBeginOffset;
		else if (r < 0)
			return -1;
		// 开始二分搜索
		for (int i = ipBeginOffset, j = ipEndOffSet; i < j;) {
			m = getMiddleOffset(i, j);
			readIP(mbb, m, ipBytes);
			r = compareIP(ip, ipBytes);
			// log.debug(Utils.getIpStringFromBytes(b));
			if (r > 0)
				i = m;
			else if (r < 0) {
				if (m == j) {
					j -= IP_RECORD_LENGTH;
					m = j;
				} else
					j = m;
			} else
				return readInt3(mbb, m + 4);
		}
		// 如果循环结束了，那么i和j必定是相等的，这个记录为最可能的记录，但是并非 肯定就是，还要检查一下，
		// 如果是，就返回结束地址区的绝对偏移
		m = readInt3(mbb, m + 4);
		readIP(mbb, m, ipBytes);
		r = compareIP(ip, ipBytes);
		if (r <= 0)
			return m;
		else
			return -1;
	}

	/**
	 * 得到begin偏移和end偏移中间位置记录的偏移
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	private int getMiddleOffset(int begin, int end) {
		int records = (end - begin) / IP_RECORD_LENGTH;
		records >>= 1;
		if (records == 0)
			records = 1;
		return begin + records * IP_RECORD_LENGTH;
	}

	/**
	 * @param offset
	 * @return
	 */
	private String readArea(ByteBuffer mbb, int offset) {
		mbb.position(offset);
		byte b = mbb.get();
		if (b == 0x01 || b == 0x02) {
			int areaOffset = readInt3(mbb);
			if (areaOffset == 0)
				return "";// 未知地区
			else
				return readString(mbb, areaOffset);
		} else
			return readString(mbb, offset);
	}

	/**
	 * 从内存映射文件的offset位置得到一个0结尾字符串
	 * 
	 * @param offset
	 * @return
	 */
	private String readString(ByteBuffer mbb, int offset) {
		try {
			byte[] buf = new byte[100];
			mbb.position(offset);
			int i;
			for (i = 0, buf[i] = mbb.get(); buf[i] != 0; buf[++i] = mbb.get())
				;
			if (i != 0)
				return IpUtils.getString(buf, 0, i, "GBK");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	/**
	 * @param offset
	 * @return
	 */
	private IPLocation getIPLocation(ByteBuffer mbb, int offset,boolean includeArea) {

		IPLocation loc = new IPLocation();
		// 跳过4字节ip
		mbb.position(offset + 4);
		// 读取第一个字节判断是否标志字节
		byte b = mbb.get();
		if (b == AREA_FOLLOWED) {
			// 读取国家偏移
			int cityOffset = readInt3(mbb);
			// 跳转至偏移处
			mbb.position(cityOffset);
			// 再检查一次标志字节，因为这个时候这个地方仍然可能是个重定向
			b = mbb.get();
			if (b == NO_AREA) {
				cityOffset = readInt3(mbb);
				loc.setCity(readString(mbb,cityOffset));
				mbb.position(cityOffset + 4);
			} else{
				loc.setCity(readString(mbb, cityOffset)) ;
			}
			// 读取地区标志
			if(includeArea){
				loc.setArea(readArea(mbb, mbb.position()));
			}
			
		} else if (b == NO_AREA) {
			
			int cityOffset = readInt3(mbb);
			loc.setCity(readString(mbb, cityOffset));
			if(includeArea){
				loc.setArea(readArea(mbb, offset + 8)) ;
			}
			
		} else {
			
			int cityOffset = mbb.position() - 1;
			loc.setCity(readString(mbb, cityOffset));
			if(includeArea){
				loc.setArea(readArea(mbb, mbb.position())) ;
			}
		}
		return loc;
	}

	/**
	 * 根据ip搜索ip信息文件，得到IPLocation结构，所搜索的ip参数从类成员ip中得到
	 * 
	 * @param ip
	 *            要查询的IP
	 * @return IPLocation结构
	 */
	public IPLocation getIPLocation(String ipStr,boolean includeArea) {
		// 先检查cache中是否已经包含有这个ip的结果，没有再搜索文件
		// if (ipCache != null && ipCache.containsKey(ipStr)) {
		// IPLocation loc = (IPLocation) ipCache.get(ipStr);
		// return loc;
		// }

		IPLocation loc = null;
		 
		byte[] ip = IpUtils.getIpByteArrayFromString(ipStr);

		ByteBuffer mbb = getNewByteBuffer();
		int offset = locateIP(mbb, ip);
		if (offset != -1){
			loc = getIPLocation(mbb, offset,includeArea);
		}
		mbb.clear();

		// if (loc != null) {
		// ipCache.put(ipStr, loc);
		// }

		return loc;
	}
	
	public IPLocation getIPLocation(String ipStr) {
		return getIPLocation(ipStr, true);
	}

	public String getAddress(String ip) {

		IPLocation loc = getIPLocation(ip);
		if (loc != null) {
			return loc.getCity() + "," + loc.getArea();
		}

		return "";
	}

	
//	/**
//	 * 给定一个地点的不完全名字，得到一系列包含s子串的IP范围记录
//	 * 
//	 * @param s
//	 *            地点子串
//	 * @return 包含IPEntry类型的List
//	 */
//	public List getIPEntries(String s) {
//		List ret = new ArrayList();
//		try {
//			// 映射IP信息文件到内存中
//			if (mbb == null) {
//				FileChannel fc = ipFile.getChannel();
//				mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
//				mbb.order(ByteOrder.LITTLE_ENDIAN);
//			}
//
//			int endOffset = (int) ipEnd;
//			for (int offset = (int) ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
//				int temp = readInt3(offset);
//				if (temp != -1) {
//					IPLocation loc = getIPLocation(temp);
//					// 判断是否这个地点里面包含了s子串，如果包含了，添加这个记录到List中，如果没有，继续
//					if (loc.city.indexOf(s) != -1 || loc.area.indexOf(s) != -1) {
//						IPEntry entry = new IPEntry();
//						entry.city = loc.city;
//						entry.area = loc.area;
//						// 得到起始IP
//						readIP(offset - 4, b4);
//						entry.beginIp = Utils.getIpStringFromBytes(b4);
//						// 得到结束IP
//						readIP(temp, b4);
//						entry.endIp = Utils.getIpStringFromBytes(b4);
//						// 添加该记录
//						ret.add(entry);
//					}
//				}
//			}
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
//		return ret;
//	}
}