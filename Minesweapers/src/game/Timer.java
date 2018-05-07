package game;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {
	
	private long startTime;
	
	public Timer() {
		start();
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public double getTimeInSeconds() {
		long stopTime = System.currentTimeMillis();
		return (stopTime-startTime) / 1000.0;
	}
	
	public double getTimeInMinutes() {
		long stopTime = System.currentTimeMillis();
		return ((stopTime-startTime) / 1000.0) / 60.0;
	}
	
	public String getFormatedTime(String format) {
		long stopTime = System.currentTimeMillis();
		String date = new SimpleDateFormat(format).format(new Date(stopTime-startTime));
		return date;
	}
}
