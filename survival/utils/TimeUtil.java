package mc.server.survival.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class TimeUtil 
{
	public static String hour() 
	{
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH");
		
		return format.format(now) + "";
	}
	
	public static String minute() 
	{
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("mm");
		
		return format.format(now) + "";
	}

	public static int getDifferenceInMinutes(String lastDate)
	{
		String minutes = lastDate.substring(lastDate.length() - 5, lastDate.length() - 3);
		int minute = Integer.parseInt(minutes);
		String hours = lastDate.substring(lastDate.length() - 8, lastDate.length() - 6);
		int hour = Integer.parseInt(hours);
		String days = lastDate.substring(lastDate.length() - 11, lastDate.length() - 9);
		int day = Integer.parseInt(days);
		String months = lastDate.substring(lastDate.length() - 14, lastDate.length() - 12);
		int month = Integer.parseInt(months);
		String years = lastDate.substring(lastDate.length() - 19, lastDate.length() - 15);
		int year = Integer.parseInt(years);
		long time = Duration.between(LocalDateTime.of(year, month, day, hour, minute, 0), LocalDateTime.now()).toMinutes();
		String str = Long.toString(time);

		return Integer.parseInt(str);
	}

	public static int getDifferenceInSeconds(String lastDate)
	{
		String seconds = lastDate.substring(lastDate.length() - 2);
		int second = Integer.parseInt(seconds);
		String minutes = lastDate.substring(lastDate.length() - 5, lastDate.length() - 3);
		int minute = Integer.parseInt(minutes);
		String hours = lastDate.substring(lastDate.length() - 8, lastDate.length() - 6);
		int hour = Integer.parseInt(hours);
		String days = lastDate.substring(lastDate.length() - 11, lastDate.length() - 9);
		int day = Integer.parseInt(days);
		String months = lastDate.substring(lastDate.length() - 14, lastDate.length() - 12);
		int month = Integer.parseInt(months);
		String years = lastDate.substring(lastDate.length() - 19, lastDate.length() - 15);
		int year = Integer.parseInt(years);
		long time = Duration.between(LocalDateTime.of(year, month, day, hour, minute, second), LocalDateTime.now()).toMillis() / 1000;
		String str = Long.toString(time);

		return Integer.parseInt(str);
	}
}