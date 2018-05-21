package game.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Debugger {
	
	private static BufferedWriter logBW = null;
	private static FileWriter logFW = null;
	private static File logFile = null;
	private static boolean logWriterCreated = false;
	
	public static void initLogWriter() {
		try {
			logFile = new File(Global.LOG_PATH);
			logFW = new FileWriter(logFile.getAbsolutePath(), true);
			logBW = new BufferedWriter(logFW);
			logWriterCreated = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeLogWriter() {
		if(logBW != null) {
			try {
				logBW.flush();
				logBW.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void DEBUG_print(String event, String message, boolean log) {
		if(Global.DEBUG_MODE) {
			String msg = "[" + Global.getNow().format(Global.logFormat) + " " + event + "] " + message;
			System.out.println(msg);
			if(log && logWriterCreated) {
				DEBUG_log(msg);
			}
		}
	}
	
	public static void DEBUG_log(String message) {
		try {
			logBW.write(message + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void DEBUG_printCords(int x, int y) {
		System.out.println("(" + x + "," + y + ")");
	}
	
	public static String DEBUG_getCordsString(int x, int y) {
		return "(" + x + "," + y + ")";
	}

}
