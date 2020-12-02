import java.util.Date;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Logger {

	private static Logger logger;

	private Logger() {

	}

	public static synchronized Logger getInstance() { // getInstance method for singleton pattern

		if (logger == null) {

			logger = new Logger();
		}

		return logger;
	}

	public void print(Date date, String message) { // printing related information to console
		System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SS").format(date) + " " + message);
		File logFile = new File("info.log");	
		
		
		try {
			logFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("file doesn't exist");
			e.printStackTrace();
		} // if file already exists will do nothing
		try {
			
			// Open given file in append mode.
			BufferedWriter outInfo = new BufferedWriter(new FileWriter(logFile, true));
			outInfo.write(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SS").format(date) + " " + message + "\n");
			outInfo.close();
		} catch (IOException e) {
			System.out.println("exception occoured" + e);
		}

	}

	public void error(Date date, String message) { // printing related error to console
		System.err.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SS").format(date) + " " + message);
		File errorFile = new File("error.log");
		//System.out.println("file created");
		try {
			errorFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} // if file already exists will do nothing
		try {

			// Open given file in append mode.
			BufferedWriter outError = new BufferedWriter(new FileWriter(errorFile, true));
			outError.write(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SS").format(date) + " " + message + "\n");
			outError.close();
		} catch (IOException e) {
			System.out.println("exception occoured" + e);
		}


	}
}
