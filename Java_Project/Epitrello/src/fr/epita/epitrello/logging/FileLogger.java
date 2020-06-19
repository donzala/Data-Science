package fr.epita.epitrello.logging;

/**Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 * <br> Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * @author Annanya & Prashanth
 * @version 1.0
 * 
 *This class is used to put the application logs to the file specified in the configuration file.
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import fr.epita.epitrello.service.Configuration;

public class FileLogger extends Logger {


	private File file = new File(Configuration.getValue("log.file"));
	private PrintWriter writer;

	public FileLogger() {
		try {
			this.writer = new PrintWriter(new FileOutputStream(this.file, true));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}
	
	@Override
	protected void print(String message) {

		this.writer.println(message);
		this.writer.flush();

	}

	public void terminate() {
		this.writer.close();
	}

}