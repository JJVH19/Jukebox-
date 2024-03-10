package tests;

/**
 * This code will play one song assuming that file is in folder songfiles.
 *
 * @author Jackson Burns, Jose Juan Velasquez
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.PlayList;
import model.Song;

public class SongTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane pane = new BorderPane();
		PlayList p = new PlayList();
		Label label = new Label();
		pane.setCenter(label);
		// Song s = new Song("Capture.mp3");

		// System.out.println("printed from tester using getter"+ s.getDuration());
		Scene scene = new Scene(pane, 255, 85); // 255 pixels wide, 85 pixels tall
		stage.setScene(scene);
		// Don't forget to show the running app:
		stage.show();
	}
}
