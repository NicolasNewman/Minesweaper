package game.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Used to output information to the console or a file for debugging
 * @author QuantumPie
 *
 */
public class Debugger {
	
	private static BufferedWriter logBW = null;
	private static FileWriter logFW = null;
	private static File logFile = null;
	private static boolean logWriterCreated = false;
	
	/**
	 * Initalizes the log writter
	 */
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
	
	/**
	 * Closes the log writer to save data
	 * Called in the Main class on exit
	 */
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
	
	/**
	 * Logs a event to the console as well as the log file IF log is true
	 * @param event ID of the event to be logged
	 * @param message information that is displayed
	 * @param log weather or not to save the information to the log
	 */
	public static void DEBUG_print(String event, String message, boolean log) {
		if(Global.DEBUG_MODE) {
			String msg = "[" + Global.getNow().format(Global.logFormat) + " " + event + "] " + message;
			System.out.println(msg);
			if(log && logWriterCreated) {
				DEBUG_log(msg);
			}
		}
	}
	
	/**
	 * Writes a pre-created log event to the log
	 * @param message
	 */
	public static void DEBUG_log(String message) {
		try {
			logBW.write(message + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns two int cords as a string 
	 * @param x cord
	 * @param y cord
	 * @return cords in the format (x,y)
	 */
	public static String DEBUG_getCordsString(int x, int y) {
		return "(" + x + "," + y + ")";
	}

}
