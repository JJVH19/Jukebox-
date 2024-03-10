package controller_view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.AccountOperations;
import model.JukeboxAccount;

import java.time.LocalDate;

/**
 * This class creates a subclass of BorderPane to display a login window. it
 * opens a menu to create a new login, and uses the fields to send data to the
 * Account Operations class which serializes the data
 * 
 * @author Jackson Burns, Jose Juan Velasquez
 */
public class LoginCreateAccountPane extends BorderPane {

	private final AccountOperations login;
	private final TextField userTextField = new TextField();
	private final TextField pwdTextField = new PasswordField();
	private final Label loginStatus = new Label("Please Login");
	private final Button loginButton = new Button("Login");
	private final Button logoutButton = new Button("Log out");
	private final Button createUserButton = new Button("Create New User");
	private JukeboxAccount currentUser;
	private static final int width = 700;
	private static final int height = 110;
	private boolean loggedIn;

	@SuppressWarnings("unused")
	public LoginCreateAccountPane(AccountOperations login) {
		this.login = login;
		loggedIn = false;
		Stage stage = new Stage();
		layoutWindow();
		currentUser = null;

		loginButton.setOnAction(event -> {
			if (currentUser != null) {
				loginStatus.setText("You are already logged in, please logout");
				return;
			}
			String userName = userTextField.getText();
			String password = pwdTextField.getText();
			currentUser = login.verifyLogin(userName, password);
			if (currentUser != null) {
				loginStatus.setText("You are now logged in, you can add 3 songs a day\n" + "Songs left today ("
						+ LocalDate.now() + "): " + (3 - currentUser.getSongsPlayedToday()));

				userTextField.setText("");
				pwdTextField.setText("");
				loggedIn = true;
			} else {
				loginStatus.setText("Incorrect Username or Password. Try Again");
				userTextField.setText("");
				pwdTextField.setText("");
			}

		});

		logoutButton.setOnAction(event -> {
			loggedIn = false;
			currentUser = null;
			loginStatus.setText("Please Login");
		});

		createUserButton.setOnAction(event -> {
			if (!loggedIn) {
				layoutAddWindow();
			} else {
				loginStatus.setText(
						"You can't create a new user while another user is logged in. Log out first then try again.");
			}

		});

	}


	public boolean isLoggedIn() {
		return loggedIn;
	}

	public JukeboxAccount getCurrentUser() {
		return currentUser;
	}

	public void setLoginStatus(String update) {
		loginStatus.setText(update);
	}

	private void layoutAddWindow() {
		TextField newUserTextField = new TextField();
		TextField newPwdTextField = new TextField();
		BorderPane newWindow = new BorderPane();

		HBox newAccountLane = new HBox();
		newAccountLane.getChildren().add(new Label("Account Name"));
		newAccountLane.getChildren().add(newUserTextField);
		newAccountLane.setAlignment(Pos.BASELINE_CENTER);
		newAccountLane.setStyle("-fx-padding: 15 40 5 10;");
		newAccountLane.setSpacing(5);

		HBox newPasswordLane = new HBox();
		newPasswordLane.getChildren().add(new Label("Password"));
		newPasswordLane.getChildren().add(newPwdTextField);
		newPasswordLane.setAlignment(Pos.BASELINE_CENTER);
		newPasswordLane.setStyle("-fx-padding: 5 0 5 0;");
		newPasswordLane.setSpacing(5);

		VBox newLoginPane = new VBox();
		newLoginPane.getChildren().addAll(newAccountLane, newPasswordLane);
		newWindow.setCenter(newLoginPane);

		HBox buttons = new HBox();
		Button cancel = new Button("Cancel");
		Button OK = new Button("OK");
		buttons.getChildren().addAll(OK, cancel);
		buttons.setAlignment(Pos.BASELINE_CENTER);
		buttons.setSpacing(15);
		newWindow.setBottom(buttons);
		Stage addWindow = new Stage();
		Scene addScene = new Scene(newWindow, width / 2, height);
		addWindow.setTitle("Create New User");
		addWindow.setScene(addScene);
		addWindow.show();

		OK.setOnAction(ok -> {
			if (login.createNewUser(newUserTextField.getText().trim(), newPwdTextField.getText().trim())) {
				loginStatus.setText("New User Created");
				login.save();
			} else {
				loginStatus.setText("Username is already taken. Try another one.");
			}
			addWindow.close();
		});
		cancel.setOnAction(Cancel -> {
			addWindow.close();
		});
	}

	private void layoutWindow() {
		HBox topLabel = new HBox();
		topLabel.getChildren().add(loginStatus);
		topLabel.setAlignment(Pos.BASELINE_CENTER);

		HBox accountLane = new HBox();
		accountLane.getChildren().add(new Label("Account Name"));
		accountLane.getChildren().add(userTextField);
		accountLane.getChildren().add(loginButton);
		accountLane.setAlignment(Pos.BASELINE_CENTER);
		accountLane.setStyle("-fx-padding: 5 50 5 10;");
		accountLane.setSpacing(5);

		HBox pwdLane = new HBox();
		pwdLane.getChildren().add(new Label("Password"));
		pwdLane.getChildren().add(pwdTextField);
		pwdLane.getChildren().add(logoutButton);
		pwdLane.setAlignment(Pos.BASELINE_CENTER);
		pwdLane.setStyle("-fx-padding: 5 0 5 0;");
		pwdLane.setSpacing(5);

		HBox newUserLane = new HBox();
		newUserLane.getChildren().add(createUserButton);
		newUserLane.setAlignment(Pos.BASELINE_CENTER);

		VBox loginPane = new VBox();
		loginPane.getChildren().addAll(topLabel, accountLane, pwdLane, newUserLane);
		this.setCenter(loginPane);

	}
}