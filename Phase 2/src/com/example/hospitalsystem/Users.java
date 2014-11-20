package com.example.hospitalsystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class of users, which tracks username and password as well as whether each user is a physician or nurse.
 */
public class Users implements Serializable {

	private static final long serialVersionUID = 1701493152083507226L;
	
	/**
	 * List of users. Resizable to allow addition/removal of valid users.
	 */
	private ArrayList<User> users;
	
	public Users(File userData) {
		try {
			
			Scanner s = new Scanner(userData);
			while(s.hasNextLine()){
				String[] userString = s.nextLine().split(",");
				users.add(new User(userString[0], userString[1], userString[2]));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the user that has the username login and password password, or null if no such user exists.
	 * @param login The username.
	 * @param password The password.
	 * @return User with username=login and password=password; null if no such user exists.
	 */
	public User login(String login, String password) {
		for(User user: users){
			if (user.validCredentials(login, password)) {
				return user;
			}
		}
		return null;
	}
	
	/**
	 * Serializes this object, saving it to location dir with the name of file being fileName.
	 * @param dir The location this object will be serialized to.
	 * @param fileName The name of the file being made.
	 */
	public void Serialize(File dir, String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the Users object saved at a particular location with a particular file name.
	 * @param dir The file object containing the location of the users file.
	 * @param fileName The name of the users file.
	 * @return
	 */
	public static Users deserialize(File dir, String fileName) {
		try{
			FileInputStream fis = new FileInputStream(new File(dir, fileName));
			ObjectInputStream ois = new ObjectInputStream(fis);
			Users u = (Users) ois.readObject();
			ois.close();
			fis.close();
			return u;
		} catch (FileNotFoundException e) {
				
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
