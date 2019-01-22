package com.naswork.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

public class DateUtil {
	/**
	 * 
	 * 求日期的下一天
	 * 
	 * @param 字符串格式的日期
	 * @return 返回 日期下一天的日期
	 * @exception 异常描述
	 */
	private static String nextDayAfterDate(String date) {
		String[] current = date.split("-");
		// 分隔字符串格式的日期
		int year = Integer.parseInt(current[0]);
		int month = Integer.parseInt(current[1]);
		int day = Integer.parseInt(current[2]);
		// 如果日数少于该月的最大天数，天数加一天
		if (day < DateUtil.maxDay(year, month)) {
			day++;
		} else {
			// 如果月份少于12，则月份加一个月
			if (month < 12) {

				month++;
			} else {
				// 如果月份大于等于12，则月份置1，年份加1年
				month = 1;
				year++;
			}
			// 如果日数超过该月的天数，天数置1
			day = 1;
		}
		// 计算出处理后的日期
		date = year + "-" + DateUtil.formatData(month) + "-"
				+ DateUtil.formatData(day);

		return date;
	}

	/**
	 * 
	 * 日期数据格式的处理
	 * 
	 * @param 整型
	 *            data
	 * @return 返回String类型的新数据
	 * @exception 异常描述
	 */
	private static String formatData(int data) {
		// 如果数字小于10，数字前加0
		if (data < 10) {
			return "0" + data;
		} else {
			return data + "";
		}

	}

	/**
	 * 
	 * 判断是否是闰年
	 * 
	 * @param 整型
	 *            year
	 * @return 返回 boolean
	 * @exception 异常描述
	 */
	private static boolean isLeap(int year) {
		// 如果是闰年返回true，否则为false
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 求一个月的最大一天
	 * 
	 * @param 年，月
	 * @return 返回 整型天数
	 * @exception 异常描述
	 */
	private static int maxDay(int year, int month) {
		// 初始化12个月份的天数
		int[] months = { 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		// 如果不是2月则返回该月份的天数
		if (month != 2) {
			return months[month - 1];
		} else {
			// 如果是闰年返回为29天，否则28天
			if (isLeap(year)) {
				return 29;
			} else {
				return 28;
			}
		}
	}

	/**
	 * 
	 * 比较两个字符串的日期的大小
	 * 
	 * @param 两个日期
	 * @return 返回 boolean
	 * @exception 异常描述
	 */
	@SuppressWarnings("unused")
	private static boolean compareToDate(String dateFrom, String dateTo) {
		// 如果开始日期小于等于结束日期返回为true,否则为false
		if (dateFrom.compareTo(dateTo) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 由日历获取星期几
	 * 
	 * @param 年，月，日
	 * @return 返回整型星期 1：星期日；2：星期一；3：星期二；4：星期三；5：星期四；6：星期五；7：星期六；
	 * @exception 异常描述
	 */
	@SuppressWarnings("unused")
	private static int getWeekValue(int year, int month, int day) {
		// 实例化Calendar
		Calendar now = Calendar.getInstance();
		// 设置某年某月某日的参数
		now.set(Calendar.YEAR, year);
		now.set(Calendar.MONTH, month - 1);
		now.set(Calendar.DATE, day);
		return now.get(Calendar.DAY_OF_WEEK);
	}

	public enum DateFormatType {
		FULL_YEAR("yyyy"),
		FULL_MONTH("MM"),
		FULL_DAY("dd"),
		
		/**
		 * 格式为：yyyy-MM-dd HH:mm:ss
		 */
		DATE_FORMAT_STR("yyyy-MM-dd HH:mm:ss"),
		
		DATE_FORMAT_STR_SF("yyyy-MM-dd HH:mm"),
		/**
		 * 格式为：yyyyMMddHHmmss
		 */
		SIMPLE_DATE_TIME_FORMAT_STR("yyyyMMddHHmmss"),

		/**
		 * 格式为：yyyy-MM-dd
		 */
		SIMPLE_DATE_FORMAT_STR("yyyy-MM-dd"),

		/**
		 * 格式为：yyyy/MM/dd
		 */
		SIMPLE_DATE_FORMAT_VIRGULE_STR("yyyy/MM/dd"),

		/**
		 * 格式为：HH:mm:ss
		 */
		HOUR_MINUTE_SECOND("HH:mm:ss"),

		/**
		 * 格式为：HH:mm
		 */
		HOUR_MINUTE("HH:mm");

		private final String value;

		DateFormatType(String formatStr) {
			this.value = formatStr;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 获取当前时间日期的字符串
	 */
	public static String getCurrentDateStr(DateFormatType dateFormatType) {
		Date date = getCurrentDate();
		return (String) OpearationDate(date, dateFormatType.getValue());
	}
	
	/**
	 * 获取当前时间日期的字符串
	 */
	public static String getCurrentDateStr(String FormatType) {
		Date date = getCurrentDate();
		return (String) OpearationDate(date, FormatType);
	}

	/**
	 * 时间、日期格式化成字符串
	 */
	public static String formatDate(Date date, DateFormatType dateFormatType) {
		return (String) OpearationDate(date, dateFormatType.getValue());
	}

	/**
	 * 从字符串解析成时间、日期
	 */
	public static Date parseDate(String dateStr, DateFormatType dateFormatType) {
		return (Date) OpearationDate(dateStr, dateFormatType.getValue());
	}

	/**
	 * 获取当前系统时间(原始格式)
	 */
	public static Date getCurrentDate() {
		Date date = new Date(System.currentTimeMillis());
		return date;
	}

	/**
	 * 获取当前日期的年、月、日、时、分、秒
	 */
	public static int getCurrentTime(TimeFormatType timeFormatType) {
		return getTime(getCurrentDate(), timeFormatType);
	}

	/**
	 * 获取指定日期的年、月、日、时、分、秒
	 */
	public static int getTime(Date date, TimeFormatType timeFormatType) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int type = timeFormatType.getValue();
			int i = c.get(type);
			return type == 2 ? i + 1 : i;
		} catch (Exception e) {
			throw new RuntimeException("获取失败", e);
		}
	}

	/**
	 * 获取指定日期的毫秒数
	 */
	public static long getMillis(Date date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 日期相加、减操作
	 * 
	 * 所返回结果单位为:天数
	 */
	public static int operationDate(Date date, Date diffDate,
			DateOperationType dateOperationType) {
		long add = getMillis(date) + getMillis(diffDate);
		long diff = getMillis(date) - getMillis(diffDate);
		return (int) ((dateOperationType.getValue() ? add : diff) / (24 * 3600 * 1000));
	}

	/**
	 * 日期月份相加、减操作
	 */
	public static Date operationDateOfMonth(Date date, int month,
			DateOperationType dateOperationType) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, dateOperationType.getValue() ? month : month
				- (month * 2));
		return c.getTime();
	}

	/**
	 * 日期天数相加、减操作
	 */
	public static Date operationDateOfDay(Date date, int day,
			DateOperationType dateOperationType) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		long millis = c.getTimeInMillis();
		long millisOfday = day * 24 * 3600 * 1000;
		long sumMillis = dateOperationType.getValue() ? (millis + millisOfday)
				: (millis - millisOfday);
		c.setTimeInMillis(sumMillis);
		return c.getTime();
	}

	private static Object OpearationDate(Object object, String formatStr) {
		if (object == null || null == formatStr || "".equals(formatStr)) {
			throw new RuntimeException("参数不能为空");
		}
		
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			if (object instanceof Date){
				return format.format(object);
			}else{
				
				String d=object.toString();
				//当被转换的字符串不包含时分秒且格式定义了“ HH:mm:ss”时，特殊处理
				if (d.length()==10 && formatStr.equals(DateUtil.DateFormatType.DATE_FORMAT_STR.getValue()))
					d=object.toString()+" 00:00:00";
				return format.parse(d);
			}
		} catch (Exception e) {
			throw new RuntimeException("操作失败", e);
		}

	}

	public enum DateOperationType {

		/**
		 * 加操作
		 */
		ADD(true),

		/**
		 * 减操作
		 */
		DIFF(false);

		private final boolean value;

		DateOperationType(boolean operation) {
			this.value = operation;
		}

		public boolean getValue() {
			return value;
		}
	}

	public enum TimeFormatType {

		YEAR(1), MONTH(2), DAY(5), HOUR(11), MINUTE(12), SECOND(13);
		private final int value;

		TimeFormatType(int formatStr) {
			this.value = formatStr;
		}

		public int getValue() {
			return value;
		}
	}
	
	public static final String MIN_DATE="1899-12-31";
	public static final String MAX_DATE="2100-12-31";
	
	
	/**
	 * 日期检查正则表达式
	 * @author zcf
	 *
	 */
	public enum PattenDate {
		/**
		 * 格式为：yyyy-MM-dd或yyyy/MM/dd/或yyyy\MM\dd或yyyy.MM.dd
		 */
		DATE_FORMAT_ALL("^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$"),

		/**
		 * 格式为：yyyy-MM-dd
		 */
		DATE_FORMAT_1("^\\d{4}(\\-)\\d{1,2}\\1\\d{1,2}$"),
		
		/**
		 * 格式为：yyyy/MM/dd
		 */
		DATE_FORMAT_2("^\\d{4}(\\/)\\d{1,2}\\1\\d{1,2}$");


		private final String value;

		PattenDate(String pattenStr) {
			this.value = pattenStr;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * 检查日期的字符串格式是否正确
	 */
	public static boolean checkDatePatten(String value,PattenDate pattenValue) {
		Pattern patten =  Pattern.compile(pattenValue.getValue());
		
		return patten.matcher(value).find();
	}
	
	/**
	 * 检查日期的字符串格式是否为“yyyy-MM-dd”
	 */
	public static boolean checkDatePatten(String value) {
		Pattern patten =  Pattern.compile(PattenDate.DATE_FORMAT_1.getValue());
		return patten.matcher(value).find();
	}
	
	/**
	 * Timestamp转成字符串日期
	 * @param time
	 * @return
	 * @since 2015年8月26日 上午9:49:48
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static String timestamp2String(Timestamp time,String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(time);
	}
	
	/**
	 * 将数据中的日期类型都转成对应的字符串
	 * @param map
	 * @return
	 * @since 2015年8月28日 下午4:56:04
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static Map<String, Object> convertTime(Map<String, Object> map,String format){
		SimpleDateFormat df =  new SimpleDateFormat(format);
		for (String key : map.keySet()) {
			Object obj = map.get(key);
			if(obj instanceof Date){
				map.put(key, df.format(obj));
			}else if(obj instanceof Timestamp){
				map.put(key, df.format(obj));
			}
		}
		
		return map;
	}
	
	// 日期转化为大小写
    public static String dateToUpper(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        return numToUpper(year) + "年" + monthToUppder(month) + "月" + dayToUppder(day) + "日";
    }
 
    /***
     * <b>function:</b> 将数字转化为大写
     * @createDate 2010-5-27 上午10:28:12
     * @param num 数字
     * @return 转换后的大写数字
     */
    private static String numToUpper(int num) {
        // String u[] = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        //String u[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String u[] = {"○", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + u[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }
 
    /***
     * <b>function:</b> 月转化为大写
     * @createDate 2010-5-27 上午10:41:42
     * @param month 月份
     * @return 返回转换后大写月份
     */
    private static String monthToUppder(int month) {
        if (month < 10) {
            return numToUpper(month);
        } else if (month == 10) {
            return "十";
        } else {
            return "十" + numToUpper(month - 10);
        }
    }
 
    /***
     * <b>function:</b> 日转化为大写
     * @createDate 2010-5-27 上午10:43:32
     * @param day 日期
     * @return 转换大写的日期格式
     */
    private static String dayToUppder(int day) {
        if (day < 20) {
            return monthToUppder(day);
        } else {
            char[] str = String.valueOf(day).toCharArray();
            if (str[1] == '0') {
                return numToUpper(Integer.parseInt(str[0] + "")) + "十";
            } else {
                return numToUpper(Integer.parseInt(str[0] + "")) + "十" + numToUpper(Integer.parseInt(str[1] + ""));
            }
        }
    }
    
    /**
     * 校验日期是否合法
     * @param s
     * @return
     * @since 2016年3月16日 下午5:16:57
     * @author doudou<doudou@naswork.com>
     * @version v1.0
     */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	
	 /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
      
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        
        return dateStr;
    }
    
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        
        return dateStr;
    }
    
    public static void main(String[] args) {
    	System.out.println(dateToUpper(new Date()));
	}
}
