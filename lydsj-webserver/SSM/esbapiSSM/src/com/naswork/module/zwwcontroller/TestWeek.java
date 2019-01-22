//package com.naswork.module.zwwcontroller;
//
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.naswork.module.tjv2.TjnewsssjController;
//
//
//public class TestWeek {
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@Test
//	public void test() throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date curDate=sdf.parse("2018-09-01");//当前时间
//		System.out.println(curDate);
//		System.out.println(new Date());
//		System.out.println(curDate.compareTo(new Date()));
//		String week=TjnewsssjController.getWeekOfDate(curDate);
//		System.out.println(week);
//	}
//
//}
