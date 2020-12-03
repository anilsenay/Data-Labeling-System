import java.util.Date;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

// Logger class for printing every kind of operations to log file and console. 
// Implemented Singleton Pattern.
public class Logger {

	// Variables for logger class.
	private static Logger logger; // For singleton pattern it has a static keyword.
	private final String dateToString;
	
	// No arg constructor. 
	private Logger() {
		this.dateToString =(new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()));
	}
	
	// getInstance method for singleton pattern.
	public static synchronized Logger getInstance() { 

		if (logger == null) {

			logger = new Logger();
		}

		return logger;
	}
	
	// Printing related information to console.
	public void print(Date date, String message) { 

		System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(date) + " " + message);
		File logFile = new File("info_"+this.dateToString+".log");
		
		try {
			logFile.createNewFile();
		} catch (IOException e) {
			
			System.out.println("file doesn't exist");
			e.printStackTrace();
		} 

		// If file already exists will do nothing.
		try {
			
			// Open given file in append mode.
			BufferedWriter outInfo = new BufferedWriter(new FileWriter(logFile, true));
			outInfo.write(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(date) + " " + message + "\n");
			outInfo.close();
		} catch (IOException e) {
			System.out.println("exception occurred" + e);
		}

	}
	
	// Printing related error to console.
	public void error(Date date, String message) { 
		System.err.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(date) + " " + message);
		File errorFile = new File("error_"+this.dateToString+".log");
		
		try {
			errorFile.createNewFile();
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} 

		// If file already exists will do nothing.
		try {

			// Open given file in append mode.
			BufferedWriter outError = new BufferedWriter(new FileWriter(errorFile, true));
			outError.write(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS").format(date) + " " + message + "\n");
			outError.close();
		} catch (IOException e) {
			System.out.println("exception occurred" + e);
		}


	}
}
