package fr.epita.epitrello.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.epita.epitrello.service.Configuration;

/**Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 * <br> Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * @author Annanya and Prashanth
 * @version 1.0
 * 
 *This class is used to generate the application logs with timestamp.
 * 
 */

public class Logger {
	
	public void log(String message) {
		SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getValue("timeformat"));
		String timestamp = sdf.format(new Date());
		
		print(format(message, timestamp));
		
	}

	protected void print(String message) {
		System.out.println(message);
	}

	private String format(String message, String timestamp) {
		return timestamp + " -- " + message;
	}

}