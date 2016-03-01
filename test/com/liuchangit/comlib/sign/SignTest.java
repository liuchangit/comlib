package com.liuchangit.comlib.sign;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

import com.liuchangit.comlib.sign.Sign;

public class SignTest {
	
	@Test
	public void testSign() {
		long sign = Sign.getSign64("百度");
		assertEquals(5530337515242231241L, sign);
		
		sign = Sign.getSign64("三国演义");
		System.out.println(sign);
		System.out.println(Long.toHexString(sign));
		BigInteger ss = new BigInteger("15841707695569045241");
		System.out.println(ss.toString(16));
		assertEquals(ss.toString(16), Long.toHexString(sign));
		
		sign = Sign.getSign64("3g.qq.com");
		assertEquals(1862557926758918068L, sign);
		
		sign = Sign.getSign64("www.shuqi.com");
		assertEquals(1953454339760696148L, sign);
	}

}
