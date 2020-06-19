package fr.epita.epitrello.serviceInterface;


/**Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 * <br> Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * @author Annanya and Prashanth
 * @version 1.0
 * 
 *This an interface created containing the list of required functionalities to be implemented in the Project.
 * 
 */
public interface EpitrelloDSInterface {
	
	String addUser(String userName);

	String addList(String listName);

	String addTask(String listName, String taskName, int estimatedTime, int priority, String description);

	String editTask(String taskName, int estimatedTime, int priority, String description);

	String assignTask(String taskName, String userName);
	
	String printTask(String taskName);

	String completeTask(String taskName);

	String printUsersByPerformance();

	String printUsersByWorkload();

	String printUnassignedTasksByPriority();

	String deleteTask(String taskName);
	
	String deleteList(String listName);

	String printAllUnfinishedTasksByPriority();

	String moveTask(String taskName, String listName);

	String printList(String listName);

	String printAllLists();

	String printUserTasks(String userName);
	
	int printTotalEstimatedTime();
	
	int printTotalRemainingTime();
	
	int userWorkload(String userName);
	
	String printUserUnfinishedTasks(String userName);
}
