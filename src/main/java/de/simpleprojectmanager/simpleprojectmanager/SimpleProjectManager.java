package de.simpleprojectmanager.simpleprojectmanager;

import de.simpleprojectmanager.simpleprojectmanager.exception.user.create.*;
import de.simpleprojectmanager.simpleprojectmanager.user.UserManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.Scanner;


@SpringBootApplication
@SpringBootConfiguration
public class SimpleProjectManager {

	//Database connection
	private static Connection DB_CON;

	public static void main(String[] args) {
		try {
			//Creates the connection
			Class.forName("org.sqlite.JDBC");
			DB_CON = DriverManager.getConnection("jdbc:sqlite:simpleprojectmanager.db");

			//SQL-Statements that should be executed
			String[] execute = {
					//User table
					"CREATE TABLE IF NOT EXISTS `user` ( `id` INTEGER NOT NULL , `nickname` TEXT NOT NULL , `passhash` TEXT NOT NULL , `passsalt` TEXT NOT NULL , `email` TEXT NOT NULL , `emailVerified` BOOLEAN NOT NULL , `firstname` TEXT NOT NULL , `lastname` TEXT NOT NULL , `emailResetToken` TEXT NULL , `csrfToken` TEXT NOT NULL , `sessionToken` TEXT NOT NULL , PRIMARY KEY (`id`));",
					//Group table
					"CREATE TABLE IF NOT EXISTS `group` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL,`permissions` INTEGER NOT NULL, `color` INTEGER NOT NULL, `hierarchy` INTEGER NOT NULL, PRIMARY KEY ('id'));",
					//Reference between user and group
					"CREATE TABLE IF NOT EXISTS `user_group` (`userID` INTEGER NOT NULL, `groupID` INTEGER NOT NULL, PRIMARY KEY (`userID`,`groupID`));",
			};

			//Executes the instructions
			for(String s:execute)
				DB_CON.createStatement().execute(s);

			//Checks if the root-user should be reset
			if(args.length>0 && args[0].equalsIgnoreCase("-reset")){
				DB_CON.createStatement().execute("DELETE FROM `user` WHERE `id`=1");
				System.out.println("Deleted root user.");
			}

			//Checks if the root user exists
			if(!DB_CON.prepareStatement("SELECT * FROM `user` WHERE `id`=1").executeQuery().next()){
				System.out.println("No root-user found, starting creating...");
				//Creates the default user
				createDefaultUser();
			}

			System.out.println("Opened database successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to the database: " + e.getMessage());
			System.exit(0);
		}

		//Fires up the spring boot application
		SpringApplication.run(SimpleProjectManager.class, args);
	}

	public static Connection getDbCon() {
		return DB_CON;
	}

	/**
	 * Creates the default user if the database is empty
	 * */
	public static void createDefaultUser(){
			//Creates the scanner
			Scanner sc = new Scanner(System.in);

			//Gets the input from the user
			System.out.println("Please specify a root-first-name (Min 2,Max 20):");
			String firstname = sc.nextLine();
			System.out.println("Please specify a root-last-name (Min 2,Max 20):");
			String lastname = sc.nextLine();
			System.out.println("Please specify a root-email (Max 40):");
			String email = sc.nextLine();
			System.out.println("Please specify a root-nick (Min 4,Max 20):");
			String nickname = sc.nextLine();
			System.out.println("Please specify a root-password (Min 8,Max 20,1 special character):");
			String password = sc.nextLine();
		do{
			try {
				//Tries to create the user
				UserManager.getInstance().createUser(nickname,firstname,lastname,email,password);
				System.out.println("Created root user. Please restart application!\nIt is highly recommended to clear your CLI, because the default root password can be seen.");
				System.exit(0);
				return;
			} catch (InvalidEmailException e) {
				System.out.println("Invalid email given, please specify a valid one:");
				email=sc.nextLine();
			} catch(InvalidPasswordException e){
				System.out.println("Invalid password given, please specify a valid one:");
				password=sc.nextLine();
			} catch(InvalidFirstNameException e){
				System.out.println("Invalid first-name given, please specify a valid one:");
				firstname=sc.nextLine();
			} catch(InvalidLastNameException e){
				System.out.println("Invalid last-name given, please specify a valid one:");
				lastname=sc.nextLine();
			} catch(InvalidNickNameException e){
				System.out.println("Invalid nickname given, please specify a valid one:");
				nickname=sc.nextLine();
			} catch(UserCreateException e){
				System.out.println("Critical error detected, shutting down...");
				System.exit(-1);
			}
		}while(true);
	}
}
