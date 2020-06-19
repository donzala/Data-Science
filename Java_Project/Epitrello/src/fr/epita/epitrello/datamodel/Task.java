package fr.epita.epitrello.datamodel;

/**Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 * <br> Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * @author Annanya and Prashanth
 * @version 1.0
 * 
 *This is a DAO service which interacts with Database.
 *Database data is not used for any computation.
 *USERS table is only used for storing UserName, if he is Active and his workload.
 */

public class Task {

	private String taskName;
	private String assignedList;
	private int priority;
	private int estimatedTime;
	private String description;
	private String assignedUser;
	private int status;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getAssignedList() {
		return assignedList;
	}
	public void setAssignedList(String assignedList) {
		this.assignedList = assignedList;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Task(String taskName, int estimatedTime, int priority, String description, int status, String assignedUser,
			String assignedList) {
		this.taskName = taskName;
		this.estimatedTime = estimatedTime;
		this.priority = priority;
		this.description = description;
		this.status = status;
		this.assignedUser = assignedUser;
		this.assignedList = assignedList;
	}
	
	
	
}
