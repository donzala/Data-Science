package fr.epita.epitrello.launcher;

import java.io.FileOutputStream;
import java.io.PrintStream;

import fr.epita.epitrello.logging.FileLogger;
import fr.epita.epitrello.service.Configuration;
import fr.epita.epitrello.service.EpitrelloDataServerice;

/**Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 * <br> Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * @author Annanya and Prashanth
 * @version 1.0
 * 
 *This the Main class which invokes the services and produces the output.file , it only contains data of last execution.
 *We have set the condition to overwrite, i.e., to remove previous run data and to reduce the space consumption.
 *We have overridden the System.out from console to file, to gather all the output to the file specified in the configuration file
 */

public class Main {

	public static void main(String[] args) throws Exception {
		
		FileLogger logger = new FileLogger();
		logger.log("Start of Main class");

		PrintStream fileOut = new PrintStream(new FileOutputStream(Configuration.getValue("output.file"), false));
		//The file will be over written each time the code is executed.
		
		
		logger.log("Initialization of output File");
		System.setOut(fileOut); // overriding the System.out from console to file.
		
		logger.log("Overriding the System.out operation to output the result in  File ");
		
		
		EpitrelloDataServerice dataserverice = new EpitrelloDataServerice();

		logger.log("Executing the commands ");
		
		System.out.println( dataserverice.addUser("Thomas") ); // addUser(string username)
		System.out.println( dataserverice.addUser("AmirAli") );
		System.out.println( dataserverice.addUser("Rabih") );
		
		
		System.out.println( dataserverice.addList("Code") ); //addList(string name)
		System.out.println( dataserverice.addList("Description") );
		System.out.println( dataserverice.addList("Misc") );

	    System.out.println( dataserverice.addTask("Code", "Do Everything", 12, 1, "Write the whole code") ); 
	    /* addTask(string list, string name, unsigned int estimatedTime, unsigned int priority, string description) */
	    System.out.println( dataserverice.editTask("Do Everything", 12, 10, "Write the whole code") );
	    /* editTask(string task, unsigned int estimatedTime, unsigned int priority, string description) */

	    System.out.println( dataserverice.assignTask("Do Everything", "Rabih") ); // assignTask(string task, string user)
	    System.out.println( dataserverice.printTask("Do Everything") ); // printTask(string task)

	    System.out.println( dataserverice.addTask("Code", "Destroy code formatting", 1, 2, "Rewrite the whole code in a worse format") );
		System.out.println( dataserverice.assignTask("Destroy code formatting", "Thomas") );

		System.out.println( dataserverice.addTask("Description", "Write Description", 3, 1, "Write the damn description") );
		System.out.println( dataserverice.assignTask("Write Description", "AmirAli") );
		System.out.println( dataserverice.addTask("Misc", "Upload Assignment", 1, 1, "Upload it") );

		System.out.println( dataserverice.completeTask("Do Everything") ); // completeTask(string task)
		System.out.println( dataserverice.printUsersByPerformance() );
	    System.out.println( dataserverice.printUsersByWorkload() );

	    System.out.println( dataserverice.printUnassignedTasksByPriority() );
		System.out.println( dataserverice.deleteTask("Upload Assignment") ); // deleteTask(string task)
		System.out.println( dataserverice.printAllUnfinishedTasksByPriority() );

		System.out.println( dataserverice.addTask("Misc", "Have fun", 10, 2, "Just do it") );
		System.out.println( dataserverice.moveTask("Have fun", "Code") ); // moveTask(string task, string list)
		System.out.println( dataserverice.printTask("Have fun") );

	    System.out.println( dataserverice.printList("Code") ); // printList(string list)

	    System.out.println( dataserverice.printAllLists() );

	    System.out.println( dataserverice.printUserTasks("AmirAli") ); // printUserTasks(string user)

	    System.out.println( dataserverice.printUnassignedTasksByPriority() );

	    System.out.println( dataserverice.printAllUnfinishedTasksByPriority() );

	    
	    
	}
}

