package game.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used to keep track of the amount of time a game has been running for
 * @author QuantumPie
 *
 */
public class Timer {
	
	private long startTime;
	
	public Timer() {
		Debugger.DEBUG_print("Instance Created", "Instance of Timer created", true);
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
