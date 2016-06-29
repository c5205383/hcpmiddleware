package com.hcp.objective.util;

import org.junit.Test;

import com.hcp.objective.BaseSpringTestCase;
import com.hcp.objective.component.Util;

public class UtilTester extends BaseSpringTestCase {

	@Test
	public void testTimeStamp() {
		System.out.println(Util.timeStamp());
	}

	@Test
	public void testTimeStamp2Date1() {
		String seconds = "1466073285";
		String format = null;
		System.out.println("testTimeStamp2Date1: " + Util.timeStamp2Date(seconds, format));
	}

	@Test
	public void testTimeStamp2Date2() {
		String seconds = "1466073285" + "000";
		String format = null;
		System.out.println("testTimeStamp2Date2: " + Util.timeStamp2Date(seconds, format));
	}

	@Test
	public void testTimeStamp2Date3() {
		String seconds = "1466073285";
		String format = "yyyy-MM-dd";
		System.out.println("testTimeStamp2Date3: " + Util.timeStamp2Date(seconds, format));
	}

	@Test
	public void testTimeStamp2Date4() {
		String seconds = "1466073285";
		String format = "yyyy-MM-dd'T'HH:mm:ss";
		System.out.println("testTimeStamp2Date4: " + Util.timeStamp2Date(seconds, format));
	}

	@Test
	public void testDate2TimeStamp1() {
		String date_str = "2016-06-16";
		String format = null;
		System.out.println("testDate2TimeStamp1: " + Util.date2TimeStamp(date_str, format));
	}

	@Test
	public void testDate2TimeStamp2() {
		String date_str = "2016-06-16";
		String format = null;
		System.out.println("testDate2TimeStamp2: " + Util.timeStamp2Date(Util.date2TimeStamp(date_str, format), "yyyy-MM-dd'T'HH:mm:ss"));
	}
}
