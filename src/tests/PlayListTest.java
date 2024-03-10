package tests;

/**
 * This code will play a few songs assuming that file is in folder songfiles.
 *
 * @author Jackson Burns, Jose Juan Velasquez
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.PlayList;

public class PlayListTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane pane = new BorderPane();
		PlayList p = new PlayList();
		Label label = new Label();
		pane.setCenter(label);
		p.queueUpNextSong("Capture.mp3");
		label.setText(p.getCurrentQueue().peek());
		System.out.println(p.getCurrentQueue());

		// p.queueUpNextSong("troll.mp3");
		// System.out.println(p.nextSong());
		p.queueUpNextSong("LopingSting.mp3");
		label.setText(p.getCurrentQueue().peek());
		System.out.println(p.getCurrentQueue());

		// System.out.println(p.size());
		p.queueUpNextSong("UntameableFire.mp3");
		label.setText(p.getCurrentQueue().peek());
		System.out.println(p.getCurrentQueue());

		p.queueUpNextSong("Capture.mp3");
		label.setText(p.getCurrentQueue().peek());

		// System.out.println(p.getCurrentQueue());
		// p.playSong();
		System.out.println(p.getCurrentQueue());
		Scene scene = new Scene(pane, 255, 85); // 255 pixels wide, 85 pixels tall
		stage.setScene(scene);
		// Don't forget to show the running app:
		stage.show();
	}
}
