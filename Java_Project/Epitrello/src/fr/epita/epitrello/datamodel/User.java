package fr.epita.epitrello.datamodel;



/**
 *Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 * <br> Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * 
 * @author Annanya and Prashanth
 * @version 1.0
 * 
 *This is a datamodel class for the User, it is specially used for sorting the workload method
 */

public class User {
	private String userName;
	private int workloadOrPerformance;

	public User(String userName, int workloadOrPerformance) {
		this.userName = userName;
		this.workloadOrPerformance = workloadOrPerformance;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getWorkloadOrPerformance() {
		return workloadOrPerformance;
	}

	public void setWorkloadOrPerformance(int workloadOrPerformance) {
		this.workloadOrPerformance = workloadOrPerformance;
	}

	public User(String userName) {
		this.userName = userName;
	}
	
	

}
