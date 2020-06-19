package fr.epita.epitrello.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fr.epita.epitrello.datamodel.Task;
import fr.epita.epitrello.datamodel.User;
import fr.epita.epitrello.logging.FileLogger;
import fr.epita.epitrello.serviceInterface.EpitrelloDSInterface;


/**Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 *  Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * @author Annanya and Prashanth
 * @version 1.0
 * 
 * This is a Service class which has the implementation of the interface with all the service methods of the project.
 * This forms the core part and performs most of the functionalities.
 *
 */
public class EpitrelloDataServerice implements EpitrelloDSInterface {
	ArrayList<String>users=new ArrayList<String>();
	ArrayList<String> lst=new ArrayList<String>();
	ArrayList<Task> taskList=new ArrayList<Task>();
	FileLogger logger = new FileLogger();
	
	//Basic Constant values assigned to improve quality
	private static final String SUCCESS="Success";
	private static final String NOLIST="List does not exist";
	private static final String UNASSIGNED="Unassigned";
	
	
	/**
	 *This method is used to add a new user.
	 *@param userName name of the new user is entered using this parameter.
	 *Checks if the entered name is unique or not.
	 *@return If the username already exists returns a string that says user already exists.
	 *<p>If the username is unique, returns success after adding the new user in the database,
	 * or returns failure if it is unable to insert the username in the database.</p>
	 *
	 */
	@Override
	public String addUser(String userName) {

		logger.log("Inside Add User method for "+ userName);
		users=EpitrelloDatabaseDAO.getQuery();//gets the exisiting User list from the Database
		
		if(!users.contains(userName))//checks if the user name already exists
			{
			users.add(userName);//adds user to the System user list
			int k=EpitrelloDatabaseDAO.create(userName);//Inserts the user into the database
			if(k!=0)
				return (SUCCESS);
			else
				return ("Failure - Not able to Insert to Database");
			}
		return ("User already exists");
	}

	
	
 /**
  * This method is used to add a new List to the system.
 *@param listName name of the new list is entered using this parameter.
 *Checks if the entered list exist or not.
 *@return If the listName already exists, returns a string that says list already exists.
 *<p>If the listName is unique, returns success after adding the new list</p>
 *
 */
@Override
	public String addList(String listName) {

		logger.log("Inside Add List method for "+ listName);
		if(!lst.contains(listName))//checks if the list name already exists
		{
			lst.add(listName);//adds list name into the System
			return (SUCCESS);
		}
	return ("List string already exists");
		
	}


	/**This method is used to add tasks to the system.
	 *@param listName is used to check if the list already exists or not.
	 *@param taskName is checked if it already exists or not.
	 *@param estimatedTime defines the estimated time of the task.
	 *@param priority defines the priority of the task.
	 *@param description defines the description of the task.
	 *@return If the the list already exists, returns a string that says list already exist.
	 *This method iterates the taskList to check the task name.
	 *If the task already exists, returns a string that says task already exist.
	 * If not, a new task is added to the system and returns success.
	 */
	@Override
	public String addTask(String listName, String taskName, int estimatedTime, int priority, String description) {

		logger.log("Inside Add Task method for "+ taskName);
		if(!lst.contains(listName)) //checks if the list name  exists
		{
			return (NOLIST);
		}
		 
		for (Task task : taskList) {
			if (task.getTaskName().equals(taskName)) {//checks if the task already exists
				return("Task already exists");
			}
		}
		taskList.add(new Task(taskName,estimatedTime,priority,description,0,UNASSIGNED,listName));//adds the new task to the System
		return (SUCCESS);
	}

	/**This method is used to edit the existing tasks in the system.
	 * @param taskName is the name of the task to be edited.
	 * @param estimatedTime is the new estimated time for the task.
	 * @param priority is the new priority to be set for the task.
	 * @param description is the new description for the task.
	 * 
	 *@return returns success once the task is edited.
	 */
	@Override
	public String editTask(String taskName, int estimatedTime, int priority, String description) {

		logger.log("Inside Edit Task method for "+ taskName);
		for (Task task : taskList) {//Iterates for each task in taskList
			if (task.getTaskName().equals(taskName)) {//checks if the iterate task is the required task and edits it
				task.setEstimatedTime(estimatedTime);
				task.setPriority(priority);
				task.setDescription(description);
			}
		}
		return (SUCCESS);
	}

	/**This method is used to assign tasks to the user.
	 * @param userName is used to check if the user already exist or not.
	 *@param taskName is used to assign the task to the given userName.
	 *@return If the user does not exist, returns a string.
	 *If the task is successfully assigned to the user, returns success. 
	 */
	@Override
	public String assignTask(String taskName, String userName) {

		logger.log("Inside Assign Task method for "+ taskName);
		if(!users.contains(userName))//Checks if provided userName is valid in the System
		{
		return ("User does not exist");
		}
		for (Task task : taskList) {//Iterates for each task in taskList
			if (task.getTaskName().equals(taskName)) {//checks if the iterate task is the required task and assigns the user to it
				task.setAssignedUser(userName);
				EpitrelloDatabaseDAO.update(userName,userWorkload(userName));// updates the workload of the user into the Database
			}
		}
		return (SUCCESS);
		
	}

	/**This method is used to print the given task.
	 * @param taskName is used to check if the task exist in the taskList.
	 * @return returns the variable res with all the parameters of the given taskName.
	 *
	 */
	@Override
	public String printTask(String taskName) {

		logger.log("Inside Print Task method for "+ taskName);
		String res=taskName;
		for (Task task  : taskList) {//Iterates for each task in taskList
			if (task.getTaskName().equals(taskName)) {//checks if the iterate task is the required task and builds its contents into a String
				if(!task.getAssignedUser().equals(UNASSIGNED))
					res=res+"\n"+task.getDescription()+"\nPriority: "+task.getPriority()+"\nEstimated Time: "+task.getEstimatedTime()+"\nAssigned To: "+task.getAssignedUser()+"\n";
				else
					res=res+"\n"+task.getDescription()+"\nPriority: "+task.getPriority()+"\nEstimated Time: "+task.getEstimatedTime()+"\nUnassigned\n";
			}
		}
		
		return res;
	}

	/**This method is used to complete the task.
	 * @param taskName is used to check if the given taskName exists in the taskList.
	 *  If yes, then the status is set to 1.
	 * @return Returns success if the status is set to 1.
	 *
	 */
	@Override
	public String completeTask(String taskName) {

		logger.log("Inside Complete Task method for "+ taskName);
		for (Task task  : taskList) {//Iterates for each task in taskList
			if (task.getTaskName().equals(taskName)) {//checks if the iterate task is the required task and puts its status to finished
				task.setStatus(1);
			}
		}
		return (SUCCESS);
	}

	/**This method is used to print the total estimated time.
	 *@return Sum returns the total estimated time.
	 */
	@Override
	public int printTotalEstimatedTime() {

		logger.log("Inside Total Estimated Time calculcation method");
		int sum=0;
		for (Task task  : taskList) {//Iterates for each task in taskList
			if(!task.getAssignedUser().equals(UNASSIGNED))//Filters out the assigned tasks in the iterated task
				sum=sum+task.getEstimatedTime();
		}
		return sum;
	}
	
	

	/**This method is used to print the total remaining time.
	 * @return sum is returned with the total remaining time.
	 *
	 */
	@Override
	public int printTotalRemainingTime() {

		logger.log("Inside total remaining time calculation method");
		int sum=0;
		for (Task task  : taskList) {//Iterates for each task in taskList
			if((!task.getAssignedUser().equals(UNASSIGNED)) && task.getStatus()!=1)//Filters out the assigned tasks and completed tasks from the iterated task
				sum=sum+task.getEstimatedTime();
		}

		return sum;
	}

	/**This method is used to calculate the work load of the given user.
	 *@param userName is used to get the name of the user for whom the work load has to be calculated.
	 *
	 *@return sum is returned with calculated workload of the given user.
	 */
	@Override
	public int userWorkload(String userName) {

		logger.log("Inside user workload calculation method for "+ userName);
		if(!users.contains(userName))//checks if the user is valid
			return 0;
		int sum=0;
		for (Task task  : taskList) {//Iterates for each task in taskList
			if(task.getAssignedUser().equals(userName))//checks if the username matches the assigned user of task and sums up the EstimatedTime
				sum=sum+task.getEstimatedTime();
		}
		return sum;
	}

	/**This method is used to print the user sorted based on their performance.
	 * @return a string res, is returned with all the names of the users that is 
	 * sorted by their performance. 
	 *
	 */
	@Override
	public String printUsersByPerformance() {

		logger.log("Inside print Users by Performance method ");
		ArrayList<User> performance = new ArrayList<User>(); 
		for (String user : users) {//Iterates for each user in System
			int sum=0;
			for (Task task  : taskList) {//Iterates Tasks and checks if the task is assigned to the iterated user and the task is completed
				if(task.getAssignedUser().equals(user) && task.getStatus()==1)
					sum=sum+task.getEstimatedTime();
			}
			performance.add(new User(user,sum));
		}
		Comparator<User> time = Comparator.comparing(User::getWorkloadOrPerformance);
		Collections.sort(performance,Collections.reverseOrder(time));//Sorts the users based on their performance in descending order
		String res="";
		for(User user:performance)
			res=res+user.getUserName()+"\n";
		
		return res;
	}

	/**This method is used to print all the users sorted based on their work load.
	 * @return a string res, is returned with all the names of the users that is 
	 * sorted by their workload.
	 *
	 */
	@Override
	public String printUsersByWorkload() {

		logger.log("Inside print user by work load method");
		ArrayList<User> performance = new ArrayList<User>(); 
		for (String user : users) //Iterates for each user in System
			performance.add(new User(user,userWorkload(user)));//calls the userWorkLoad method to compute workload of user.
		
		Comparator<User> time = Comparator.comparing(User::getWorkloadOrPerformance);
		Collections.sort(performance,time);//sorts user based on workload
		String res="";
		for(User user:performance)
			res=res+user.getUserName()+"\n";
		
		return res;
		
	}

	
	/**This method is used to print all the unassigned task sorted by priority.
	 *@return returns a string res, with all the unassigned tasks which is sorted based on their priority.
	 */
	@Override
	public String printUnassignedTasksByPriority() {

		logger.log("Inside print unassigned tasks by priority");
		String res="";
		ArrayList<Task> priorUnassignTask= new ArrayList<Task>();
		for (Task task  : taskList) {//Iterates for each task
			
			if(task.getAssignedUser().equals(UNASSIGNED))//checks for unassigned tasks and adds to list
				priorUnassignTask.add(task);
		}
		Comparator<Task> prior = Comparator.comparing(Task::getPriority);
		Collections.sort(priorUnassignTask,prior);//sorts tasks based on priorityunassigned 
		
		for(Task task:priorUnassignTask)//merges all tasks in the requested  string formated
				res=res+task.getPriority()+" | "+task.getTaskName()+" | Unassigned | "+task.getEstimatedTime()+"h\n";			
			
		return res;
	}

	/**This method is used to delete the task.
	 * @param taskName is the name of the task to be deleted.
	 * @return returns success once the given task is deleted.
	 *
	 */
	@Override
	public String deleteTask(String taskName) {

		logger.log("Inside Delete Task method for "+ taskName);
		for (int i = 0; i < taskList.size()	; i++) {//iterates for each task
		Task task=taskList.get(i);
		if(task.getTaskName().equals(taskName))//checks if tasks is the required one
		{
			taskList.remove(i);//deleted from System.
			EpitrelloDatabaseDAO.update(task.getAssignedUser(),userWorkload(task.getAssignedUser()));//Updates the new workload of user in DB
		}
		}
		
		return (SUCCESS);
	}
	
	

	/**This method is used to delete the specified list and its tasks.
	 * @param listName is the name of the list to be deleted.
	 *@return returns success once the given list is deleted successfully.
	 */
	@Override
	public String deleteList(String listName) {

		logger.log("Inside Delete list method for "+ listName);
		for(int i=0; i<lst.size();i++) {//Iterates for each list
			if(lst.get(i).equals(listName))//Checks if the list is the required list
				lst.remove(i);//removes from system
		}
			
		for (int i = taskList.size()-1; i >=0	; i--) {//Iterates for tasks
			Task task=taskList.get(i); 
			if(task.getAssignedList().equals(listName)) {//Checks if tasks is assigned to deleted List
				taskList.remove(i);//Removes task from system
				EpitrelloDatabaseDAO.update(task.getAssignedUser(),userWorkload(task.getAssignedUser()));//Updates the new workload of the user
			}
		}	
			
		
		return (SUCCESS);
	}

	/**This method is used to print all the unfinished task sorted by priority.
	 * @return returns a string res, with all the unfinished tasks which is sorted based on their priority.
	 *
	 */
	@Override
	public String printAllUnfinishedTasksByPriority() {

		logger.log("Inside print all unfinished tasks by priority");
		String res="";
		ArrayList<Task> priorUnfinishTask= new ArrayList<Task>();
		for (Task task  : taskList) {//Iterates for each Task
			
			if(task.getStatus()==0)//checks for unfinished task and puts them in list
				priorUnfinishTask.add(task);
		}
		Comparator<Task> prior = Comparator.comparing(Task::getPriority);
		Collections.sort(priorUnfinishTask,prior);// sorts the list of unfinished tasks based on priority
		
		for(Task task:priorUnfinishTask)//forms a String to put the tasks details in the requested format.
				res=res+task.getPriority()+" | "+task.getTaskName()+" | "+task.getAssignedUser()+" | "+task.getEstimatedTime()+"h\n";			
			
		return res;
	}

	/**This method is used to move the task to the given list name.
	 * @param taskName defines the name of the task to be moved.
	 * @param listName defines the name of the list
	 * @return return no list if the list name does not exist.
	 *  returns success if the task is moved to the given list.
	 *
	 */
	@Override
	public String moveTask(String taskName, String listName) {

		logger.log("Inside move Task method for "+ taskName);
		if(!lst.contains(listName))// checks if the given list exisits in system
		{
			return (NOLIST);
		}
		for (Task task  : taskList) {//Iterates and checks if the task is the required on and assigns it to the requested list
			if(task.getTaskName().equals(taskName))
				task.setAssignedList(listName);
		}
		return (SUCCESS);
	}	

	/**This method is used to print the details of the given list.
	 * @param listName is used to specify the name of the list.
	 * @return If the given list name does not exist, returns no list
	 *  If the list exists, a string res, is returned with the details of the given list name.
	 *
	 */
	@Override
	public String printList(String listName) {

		logger.log("Inside Print list method for "+ listName);
		if(!lst.contains(listName))//Checks if the Lists exists
		{
			return (NOLIST);
		}
		
		String res="\nList Code\n";
		
		for (Task task  : taskList) {//Gathers all tasks in the lists to the requested  String format 
			if(task.getAssignedList().equals(listName))
				res=res+task.getPriority()+" | "+task.getTaskName()+" | "+ task.getAssignedUser()+" | "+task.getEstimatedTime()+"h\n";
		}
		
		return res;
	}

	/**This method is used to print all the lists.
	 * @return returns a string with details of every list.
	 *
	 */
	@Override
	public String printAllLists() {

		logger.log("Inside print all lists method");
		String res="";
		for (String list : lst) {//Iterates for each list in System
			res=res+"\nlist "+list+"\n";
			for (Task task  : taskList) {//Gathers all tasks in the lists to the requested  String format
				if(task.getAssignedList().equals(list))
					res=res+task.getPriority()+" | "+task.getTaskName()+" | "+ task.getAssignedUser()+" | "+task.getEstimatedTime()+"h\n";
			}
		}
		
		
		return res;
		
	}

	/**This method is used to print the tasks for the given user.
	 * @param userName is used to give the name of the user
	 * @return returns the string res, with all the details 
	 * of the tasks to be printed for the given user
	 *
	 */
	@Override
	public String printUserTasks(String userName) {

		logger.log("Inside print User tasks for "+ userName);
		String res="";
		for (Task task  : taskList) {
			if(task.getAssignedUser().equals(userName))//Gathers the Users Tasks Details in the System to the requested  String format
					res=res+task.getPriority()+" | "+task.getTaskName()+" | "+task.getAssignedUser()+" | "+task.getEstimatedTime()+"h\n";
			
		}
		return res;
	}

	/**This method is used to print the unfinished tasks of the given user.
	 * @param userName is used to give the name of the user.
	 * @return returns a string res, with the details of the task for the given user.
	 *
	 */
	@Override
	public String printUserUnfinishedTasks(String userName) {

		logger.log("Inside print user unfinished tasks "+ userName);
		String res="";
		for (Task task  : taskList) {//Gathers the user  unfinished tasks in the lists to the requested  String format
			if(task.getAssignedUser().equals(userName) && task.getStatus()!=1)
					res=res+task.getPriority()+" | "+task.getTaskName()+" | "+task.getAssignedUser()+" | "+task.getEstimatedTime()+"h\n";
			
		}
		return res;
	}
	
	

}

