package com.liuchangit.comlib.dict;

import static org.junit.Assert.*;

import org.junit.Test;

import com.liuchangit.comlib.dict.BasicDict;
import com.liuchangit.comlib.dict.CaseSensitive;
import com.liuchangit.comlib.dict.DictFactory;
import com.liuchangit.comlib.dict.MatchMode;


public class BasicDictTest {
	
	@Test
	public void testGetBasicDict() {
		BasicDict dict = DictFactory.getBasicDict(this.getClass().getResourceAsStream("/badword.txt"));
		assertTrue(dict.size() > 0);
	}
	
	@Test
	public void testGetClassPathBasicDict() {
		BasicDict dict = DictFactory.getClassPathBasicDict("badword.txt");
		assertTrue(dict.size() > 0);
	}
	
	@Test
	public void testGetBasicDictFail() {
		BasicDict dict = DictFactory.getBasicDict("badword.txt");
		assertNull(dict);
	}
	
	@Test
	public void testSingleFieldCaseInsensitiveExactBasicDict() {
		BasicDict dict = DictFactory.getBasicDict(this.getClass().getResourceAsStream("/badword.txt"));
		assertNull(dict.search("18岁以下fadf"));
		assertEquals("18岁以下", dict.search("18岁以下"));
		assertTrue(dict.contains("18岁以下"));
		assertTrue(dict.contains("av"));
		assertTrue(dict.contains("AV"));
		assertFalse(dict.contains("18岁以下fdafa"));
	}
	
	@Test
	public void testSingleFieldCaseInsensitiveFuzzyBasicDict() {
		BasicDict dict = DictFactory.getBasicDict(this.getClass().getResourceAsStream("/badword.txt"), "UTF-8", CaseSensitive.No, MatchMode.Fuzzy);
		assertEquals("18岁以下", dict.search("18岁以下fadf"));
		assertEquals("18岁以下", dict.search("18岁以下"));
		assertTrue(dict.contains("18岁以下"));
		assertTrue(dict.contains("av"));
		assertTrue(dict.contains("AV"));
		assertTrue(dict.contains("18岁以下fdafa"));
	}
	
	@Test
	public void testSingleFieldCaseSensitiveExactBasicDict() {
		BasicDict dict = DictFactory.getBasicDict(this.getClass().getResourceAsStream("/badword.txt"), "UTF-8", CaseSensitive.Yes, MatchMode.Exact);
		assertNull(dict.search("18岁以下fadf"));
		assertEquals("18岁以下", dict.search("18岁以下"));
		assertTrue(dict.contains("18岁以下"));
		assertFalse(dict.contains("av"));
		assertTrue(dict.contains("AV"));
		assertFalse(dict.contains("18岁以下fdafa"));
	}
	
	@Test
	public void testSingleFieldCaseSensitiveFuzzyBasicDict() {
		BasicDict dict = DictFactory.getBasicDict(this.getClass().getResourceAsStream("/badword.txt"), "UTF-8", CaseSensitive.Yes, MatchMode.Fuzzy);
		assertEquals("18岁以下", dict.search("18岁以下fadf"));
		assertEquals("18岁以下", dict.search("18岁以下"));
		assertTrue(dict.contains("18岁以下"));
		assertFalse(dict.contains("av"));
		assertTrue(dict.contains("AV"));
		assertTrue(dict.contains("18岁以下fdafa"));
	}

}
