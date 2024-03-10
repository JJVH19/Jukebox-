package model;

import java.io.*;
import java.util.ArrayList;

/**
 * This class does the serialization of the JukeboxAccount object. it saves on
 * quit, reads the list of accounts on load if it exists, and can verify the
 * login of a user
 *
 * @author Jackson Burns, Jose Juan Velasquez
 */
public class AccountOperations {

	private ArrayList<JukeboxAccount> stackList;
	private String fileName = "userDatabase.ser";

	@SuppressWarnings("unchecked")
	public AccountOperations() {
		stackList = new ArrayList<>();
		try {
			FileInputStream rawBytes = new FileInputStream(fileName); // Read the .ser file just created
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			stackList = (ArrayList<JukeboxAccount>) inFile.readObject();
			inFile.close();
		} catch (FileNotFoundException | ClassNotFoundException e) {
			try {
				// create new database if one doesnt exist
				FileOutputStream bytesToDisk = new FileOutputStream(fileName);
				ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
				outFile.writeObject(stackList);
				outFile.close(); // Always close the output file!
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} catch (IOException e) {
			save();
		}

	}

	public JukeboxAccount verifyLogin(String userName, String password) {
		if (userName.length() != 0 && password.length() != 0) {
			for (JukeboxAccount acct : stackList) {
				if (acct.getUsername().equals(userName.trim()) && acct.getPassword().equals(password.trim())) {
					return acct;
				}
			}

			return null;
		}

		return null;
	}

	public boolean createNewUser(String newUser, String newPwd) {
		boolean containsAcct = false;
		for (JukeboxAccount acct : stackList) {
			if (acct.getUsername().equals(newUser)) {
				containsAcct = true;
			}
		}
		if (!containsAcct) {
			stackList.add(new JukeboxAccount(newUser, newPwd));

			return true;
		}

		return false;
	}

	@SuppressWarnings("unused")
	public void update(JukeboxAccount acct) {
		for (int i = 0; i < stackList.size(); i++) {
			if (stackList.get(i).getUsername().equals(acct.getUsername())) {
				stackList.set(i, acct);
			}
		}

	}

	public void save() {
		try {
			FileOutputStream bytesToDisk = new FileOutputStream(fileName);
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			outFile.writeObject(stackList);
			outFile.close(); // Always close the output file!
		} catch (IOException ioe) {
			System.out.println("Writing objects failed");
		}
	}

}
