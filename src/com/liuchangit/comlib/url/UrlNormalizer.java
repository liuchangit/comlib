package com.liuchangit.comlib.url;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * url 补全机制
 * 
 * @author hugh
 * 
 */
public class UrlNormalizer {

	static final int CHAR_QUERY = 1; // Valid in the username/password field.
	static final int CHAR_USERINFO = 2; // Valid in a IPv4 address (digits plus
	// dot and 'x' for hex).
	static final int CHAR_IPV4 = 4; // Valid in an ASCII-representation of a hex
	// digit (as in %-escaped).
	static final int CHAR_HEX = 8; // Valid in an ASCII-representation of a
	// decimal digit.
	static final int CHAR_DEC = 16; // Valid in an ASCII-representation of an
	// octal digit.
	static final int CHAR_OCT = 32; // Characters that do not require escaping
	// in encodeURIComponent. Characters
	static final int CHAR_COMPONENT = 64;

	static final String unreserved = "-._~";
	static final String reserved = "!#$&'()*+,/:;=?@[]";
	static final String unescape = "%";

	static final int kUnreserved = 1;
	static final int kReserved = 2;
	static final int kUnEscape = 4;
	static final int kEscape = 8;

	static int[] tbl = new int[256];

	static {
		for (int i = 0; i < 256; i++)
			tbl[i] = 0;
		for (int i = 'a'; i <= 'z'; ++i)
			tbl[i] = kUnreserved | kUnEscape;
		for (int i = 'A'; i <= 'Z'; ++i)
			tbl[i] = kUnreserved | kUnEscape;
		for (int i = '0'; i <= '9'; ++i)
			tbl[i] = kUnreserved | kUnEscape;
		for (int i = 0; i < unreserved.length(); ++i) {
			tbl[unreserved.charAt(i)] = kUnreserved | kUnEscape;
		}
		for (int i = 0; i < reserved.length(); ++i) {
			tbl[reserved.charAt(i)] = kReserved | kUnEscape;
		}
		for (int i = 0; i < unescape.length(); ++i) {
			tbl[unescape.charAt(i)] = kUnEscape;
		}
	}

	static int[] kCharToHexLookup = { 0, // 0x00 - 0x1f
			'0', // 0x20 - 0x3f: digits 0 - 9 are 0x30 - 0x39
			'A' - 10, // 0x40 - 0x5f: letters A - F are 0x41 - 0x46
			'a' - 10, // 0x60 - 0x7f: letters a - f are 0x61 - 0x66
			0, // 0x80 - 0x9F
			0, // 0xA0 - 0xBF
			0, // 0xC0 - 0xDF
			0, // 0xE0 - 0xFF
	};

	static int[] kSharedCharTypeTable = {
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0, // 0x00 - 0x0f
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0, // 0x10 - 0x1f
			0, // 0x20 ' ' (escape spaces in queries)
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x21 !
			0, // 0x22 "
			0, // 0x23 # (invalid in query since it marks the ref)
			CHAR_QUERY | CHAR_USERINFO, // 0x24 $
			CHAR_QUERY | CHAR_USERINFO, // 0x25 %
			CHAR_QUERY | CHAR_USERINFO, // 0x26 &
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x27 '
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x28 (
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x29 )
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x2a *
			CHAR_QUERY | CHAR_USERINFO, // 0x2b +
			CHAR_QUERY | CHAR_USERINFO, // 0x2c ,
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x2d -
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_COMPONENT, // 0x2e .
			CHAR_QUERY, // 0x2f /
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_OCT | CHAR_COMPONENT, // 0x30 0
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_OCT | CHAR_COMPONENT, // 0x31 1
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_OCT | CHAR_COMPONENT, // 0x32 2
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_OCT | CHAR_COMPONENT, // 0x33 3
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_OCT | CHAR_COMPONENT, // 0x34 4
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_OCT | CHAR_COMPONENT, // 0x35 5
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_OCT | CHAR_COMPONENT, // 0x36 6
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_OCT | CHAR_COMPONENT, // 0x37 7
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_COMPONENT, // 0x38 8
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_DEC
					| CHAR_COMPONENT, // 0x39 9
			CHAR_QUERY, // 0x3a :
			CHAR_QUERY, // 0x3b ;
			0, // 0x3c < (Try to prevent certain types of XSS.)
			CHAR_QUERY, // 0x3d =
			0, // 0x3e > (Try to prevent certain types of XSS.)
			CHAR_QUERY, // 0x3f ?
			CHAR_QUERY, // 0x40 @
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x41
			// A
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x42
			// B
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x43
			// C
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x44
			// D
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x45
			// E
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x46
			// F
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x47 G
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x48 H
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x49 I
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x4a J
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x4b K
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x4c L
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x4d M
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x4e N
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x4f O
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x50 P
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x51 Q
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x52 R
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x53 S
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x54 T
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x55 U
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x56 V
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x57 W
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_COMPONENT, // 0x58 X
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x59 Y
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x5a Z
			CHAR_QUERY, // 0x5b [
			CHAR_QUERY, // 0x5c '\'
			CHAR_QUERY, // 0x5d ]
			CHAR_QUERY, // 0x5e ^
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x5f _
			CHAR_QUERY, // 0x60 `
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x61
			// a
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x62
			// b
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x63
			// c
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x64
			// d
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x65
			// e
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_HEX | CHAR_COMPONENT, // 0x66
			// f
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x67 g
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x68 h
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x69 i
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x6a j
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x6b k
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x6c l
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x6d m
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x6e n
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x6f o
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x70 p
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x71 q
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x72 r
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x73 s
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x74 t
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x75 u
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x76 v
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x77 w
			CHAR_QUERY | CHAR_USERINFO | CHAR_IPV4 | CHAR_COMPONENT, // 0x78 x
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x79 y
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x7a z
			CHAR_QUERY, // 0x7b {
			CHAR_QUERY, // 0x7c |
			CHAR_QUERY, // 0x7d }
			CHAR_QUERY | CHAR_USERINFO | CHAR_COMPONENT, // 0x7e ~
			0, // 0x7f
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0x80 - 0x8f
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0x90 - 0x9f
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0xa0 - 0xaf
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0xb0 - 0xbf
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0xc0 - 0xcf
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0xd0 - 0xdf
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0xe0 - 0xef
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 // 0xf0 - 0xff
	};

	public static String getSec(String url) {
		String sec = "http://";
		if (url.matches("^(F|f)(t|T)(p|P).*")) {
			sec = "ftp://";
		}else if (url.matches("^(H|h)(t|T){2}(p|P)(s|S).*")) {
			sec = "https://";
		}
		return sec;
	}

	public static String getUrl(String url) {
		url = parse(url.trim());
		StringBuffer sb = new StringBuffer(getSec(url));
		List<String> paras = new ArrayList<String>();
		url = url.replaceAll("\\\\", "/");
		String rex = "^((H|h|F|f)(t|T){1,2}(p|P))?[^a-zA-Z0-9-]*";
		url = url.replaceAll(rex, "");
		boolean flag = url.endsWith("/");
		String[] urls = url.split("/");
		for (int i = 0, j = 0; i < urls.length; i++) {
			String str = urls[i];
			if (i == 0) {// 过滤域名后面的端口号
				str = str.toLowerCase().replaceAll(":80", "") + "/";
			}
			if (str.equals("."))
				continue;// 当前目录跳过
			int pos = str.indexOf("#");// 遇到#获取有效部分跳出循环
			if (pos != -1) {
				str = str.substring(0, pos);
			}
			if (i + 1 == urls.length && str.endsWith("?")) {
				str = str.substring(0, str.indexOf("?"));
			}
			paras.add(j++, str);
			if (pos != -1)
				break;
		}
		List<String> mid = new ArrayList<String>();
		for (Iterator it = paras.iterator(); it.hasNext();) {
			String para = (String) it.next();
			if (para.equals("..")) {
				if (mid.size() != 1) {
					mid.remove(mid.size() - 1);
				}
			} else {
				mid.add(para);
			}
		}
		for (int i = 0; i < mid.size(); i++) {
			String para = mid.get(i);
			sb.append(para);
			if (para.endsWith("/"))
				continue;
			if (mid.size() > i + 1 || flag) {
				sb.append("/");
			}
		}
		System.out.println(EncodeQuery(sb.toString()));
		return EncodeQuery(sb.toString());
	}

	public static String parse(String url) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < url.length(); i++) {
			char c = url.charAt(i);
			if (c == '%') {
				char hex_high = toupper(url.charAt(i + 1)), hex_low = toupper(url
						.charAt(i + 2));
				char decoded_char = DecodeChar(hex_high, hex_low);
				if (IsType(decoded_char, 1)) {
					sb.append(decoded_char);
				} else {
					sb.append(c).append(hex_high).append(hex_low);
				}
				i += 2;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static char toupper(char c) {
		return (char) ((c + "").toUpperCase()).charAt(0);
	}

	public static boolean IsType(final char c, final int type) {
		int flag = tbl[c] & type;
		if (flag > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean IsHexChar(char c) {
		int hex = kSharedCharTypeTable[(int) c] & CHAR_HEX;
		if (hex > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static char DecodeChar(final char hex_high, final char hex_low) {
		char c = (char) ((HexCharToValue(hex_high) << 4) | HexCharToValue(hex_low));
		return c;
	}

	public static String EncodeQuery(String src) {
		StringBuffer sb = new StringBuffer();
		String hex_map = "0123456789ABCDEF";
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);

			if (IsType(c, kUnEscape)) {
				sb.append(c);
			} else {
				sb.append('%').append(hex_map.charAt(c >> 4)).append(hex_map.charAt(c & 0x0f));
			}
		}
		return sb.toString();
	}

	public static char HexCharToValue(char hex_high) {
		return (char) (hex_high - kCharToHexLookup[hex_high / 0x20]);
	}

	public static void main(String[] args) {
		getUrl("http://www.newsmth.net/nForum/elite/file?v=%252Fgroups%252Fliteral.faq%252FWisdom%252Fbuddhism%252Fyanjiu%252Fxxgs%252Fychyl%252FM.981970730.A");
	} 
}
