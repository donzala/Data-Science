package fr.epita.epitrello.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 * <br> Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * @author Annanya and Prashanth
 * @version 1.0
 * 
 *This class is used to save the properties specified in the configuration file to the system memory.
 * 
 */

public class Configuration {

	private static Properties properties = new Properties();
	private static boolean isInit = false;
	
	
	/**
	   * @throws FileNotFoundException Throws this exception when the configuration file is not available in given path.
	   * @throws IOException Throws this exception if the configuration file is locked and when there is nothing to be loaded into the properties.
	   */
	public static void init() throws FileNotFoundException, IOException {
		String location = "./conf.properties";
		properties.load(new FileInputStream(new File(location)));
		isInit = true;
	}

	public static String getValue(String key) {
		if (!isInit) {
			try {
				init();
			} catch (Exception e) {

			}
		}
		return properties.getProperty(key);
	}

}
