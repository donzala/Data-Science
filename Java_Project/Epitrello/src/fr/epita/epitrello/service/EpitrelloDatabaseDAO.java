package fr.epita.epitrello.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import fr.epita.epitrello.logging.FileLogger;

/**Epitrello is a program that implements a system to Manage and Control the tasks for the users.
 * <br> Also to make better decisions about assignments and tasks based on priority and estimated time for each task,
 * @author Annanya and Prashanth
 * @version 1.0
 * 
 *This is a DAO service which interacts with Database.
 *Database data is not used for any computation.
 *USERS table is only used for storing UserName, if he is Active and his workload.
 */

public class EpitrelloDatabaseDAO {
	
	//Basic Constant values assigned to improve quality
	private static final String INSERT_QUERY = "INSERT INTO USERS(USERNAME, ACTIVE, WORKLOAD) VALUES (?, ?, ?)";
	private static final String SELECT_QUERY = "SELECT USERNAME FROM USERS";
	private static final String UPDATE_WORKLOAD_QUERY="UPDATE USERS SET WORKLOAD=? WHERE USERNAME=?";
	private static final String ERRLOG="Error info ";
	private static final String DB_URL="db.url";
	private static final String DB_ID="db.id";
	private static final String DB_PW="db.passwrd";
	
	static FileLogger logger = new FileLogger();
	
	EpitrelloDatabaseDAO() {

	}
	
	/**
	 * This method is used to insert user into the Table USERS, it is called each time a new user is added to Epitrello.
	 * @param userName name of the new user to be added into the Table.
	 * @return It returns 1 when the user is successfully inserted , returns 0 if the Insert command fails.
	 */
	public static int create(String userName) {
		logger.log("Inside the Insert SQL operation");
		try (Connection connection = DriverManager.getConnection(Configuration.getValue(DB_URL), Configuration.getValue(DB_ID), Configuration.getValue(DB_PW));
			 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);) {

			preparedStatement.setString(1, userName);
			preparedStatement.setBoolean(2, true);
			preparedStatement.setInt(3, 0);
			return preparedStatement.executeUpdate();

		} catch (Exception e) {
			//throw a custom exception
			logger.log(ERRLOG+ e.getMessage());
			
		}
		return 0;

	}
	
	/**
	 * This method is used to get the list of userName from the Table USERS, it is called before insertion of new user to check if it already available in Database.
	 * @return This returns the List of userName present in the Database under the Table USERS.
	 */
	public static ArrayList<String> getQuery() {
		logger.log("Inside the Select Query SQL operation to get User Names");
		ArrayList<String> users= new ArrayList<String>();
		try (Connection connection = DriverManager.getConnection(Configuration.getValue(DB_URL), Configuration.getValue(DB_ID), Configuration.getValue(DB_PW));
			 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);) {
			ResultSet rs = preparedStatement.executeQuery();//to get the list of User Names from DB
			while (rs.next())//Iterates for each row in Table
		      {
		        String userName = rs.getString("USERNAME");
		        users.add(userName);
		       }
			rs.close();
			return users;
		} catch (Exception e) {
			//throw a custom exception
			logger.log(ERRLOG+ e.getMessage());
		}
		return users;

	}
	
	/**
	 * This method is used to update the workload of each user present in the Table USERS, it is called when ever a task is assigned to user and whenever a task or list is deleted.
	 * @param userName Name of the user whose workload needs to be updated
	 * @param workload the workload which needs to be updated.
	 * @return This returns 1 when the workload has been updated in Database, it returns 0 when there is any failure in update.
	 */
	public static int update(String userName, int workload) {
		logger.log("Inside the Update SQL operation to update user workload in DB");
		try (Connection connection = DriverManager.getConnection(Configuration.getValue(DB_URL), Configuration.getValue(DB_ID), Configuration.getValue(DB_PW));
			 PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_WORKLOAD_QUERY);) {

			preparedStatement.setInt(1, workload);
			preparedStatement.setString(2, userName);
			return preparedStatement.executeUpdate();

		} catch (Exception e) {
			//throw a custom exception
			logger.log(ERRLOG+ e.getMessage());
			
		}
		return 0;

	}
	


}
