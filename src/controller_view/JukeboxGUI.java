package controller_view;

import java.time.LocalDate;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.AccountOperations;
import model.PlayList;
import model.Song;

/**
 * This program is intended to emulate a jukebox, where different users can
 * request songs from playlists and play them in a queue, up to 3 songs a day
 *
 * @author Jackson Burns, Jose Juan Velasquez
 */
public class JukeboxGUI extends Application {

	private AccountOperations login;
	private LoginCreateAccountPane loginPane;
	private TableViewJukebox tableView;
	private BorderPane songQ = new BorderPane();
	private BorderPane everything;
	private PlayList p;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		login = new AccountOperations();
		p = new PlayList();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Click cancel to start with a new playlist");
		alert.setContentText("To load the saved playlist, click OK");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.CANCEL) {
			p.clearPlaylist();
		}


		if (p.size() != 0) {
			p.playSong();
		}

		LayoutGUI();
		setOnPlay();
		Scene scene = new Scene(everything, 900, 600);


		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(windowEvent -> {
			login.save();

			alert.setHeaderText("Click cancel to not save any changes");
			alert.setContentText("To save the current PlayList, click OK");
			Optional<ButtonType> result2 = alert.showAndWait();
			if (result2.get() == ButtonType.OK) {
				p.save();
			}

		});
	}

	private void LayoutGUI() {
		everything = new BorderPane();
		loginPane = new LoginCreateAccountPane(login);
		tableView = new TableViewJukebox();
		everything.setBottom(loginPane);

	}

	private void setOnPlay() {
		Button play = new Button("Play");

		play.setOnAction(event -> {
			TableView<?> theTable;
			theTable = tableView.getTable();
			Song tableSelect = (Song) theTable.getSelectionModel().getSelectedItem();
			if (tableSelect != null) {
				if (loginPane.isLoggedIn()) {
					if (loginPane.getCurrentUser().playSong()) {
						p.queueUpNextSong(tableSelect.getFileName());
						tableSelect.incrementPlayCounter();
						login.update(loginPane.getCurrentUser());
						int remainingSongs = 3 - loginPane.getCurrentUser().getSongsPlayedToday();
						loginPane.setLoginStatus("You are now logged in, you can add 3 songs a day\n"
								+ "Songs left today (" + LocalDate.now() + "): " + remainingSongs);
						update();
						theTable.refresh();
					} else {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setHeaderText("Your play limit maxed out. Try again tomorrow.");
						alert.showAndWait();
					}
				} else {
					loginPane.setLoginStatus("Login First to add changes to the Current PlayList.");
				}

			} else {
				if (loginPane.isLoggedIn()) {
					loginPane.setLoginStatus("Please select a song to add to the queue.");
				} else {
					loginPane.setLoginStatus("Login First to add changes to the Current PlayList.");
				}

			}
		});


		VBox table = new VBox();

		table.getChildren().addAll(tableView);
		table.setMaxWidth(500);
		final Label label = new Label("Song Queue");
		label.setFont(new Font("Arial", 20));

		songQ.setTop(label);
		update();
		VBox buttonPlay = new VBox();

		buttonPlay.getChildren().add(play);
		buttonPlay.setAlignment(Pos.BASELINE_CENTER);
		buttonPlay.setStyle("-fx-padding: 150 0 0 0;");
		VBox songQueue = new VBox();

		songQueue.getChildren().add(songQ);

		HBox container = new HBox();

		container.getChildren().addAll(table, buttonPlay, songQueue);
		container.setSpacing(15);
		everything.setCenter(container);
		everything.setMaxHeight(100);
	}

	private void update() {

		ListView<String> listView = new ListView<>();
		listView.setItems(p.getObservableQueue());
		songQ.setCenter(listView);
		listView.getSelectionModel().select(0);

	}

}