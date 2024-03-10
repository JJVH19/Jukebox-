package controller_view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import model.Song;

import java.io.File;
import java.util.ArrayList;


/**
 * This class creates a table view of each song in the directory of songfiles/
 * it utalizes the Song class, which dynamically adds songs to the tableview
 * if they are in the directory. it is NOT hard coded
 *
 * @author Jackson Burns, Jose Juan Velasquez
 */
public class TableViewJukebox extends BorderPane {

	private TableView<Song> table;
	private static ObservableList<Song> observableList = FXCollections.observableArrayList();

	public TableViewJukebox() {
		final Label label = new Label("Song List");
		label.setFont(new Font("Arial", 20));
		this.setTop(label);
		table = new TableView<Song>();
		TableColumn<Song, String> title = new TableColumn<>("Title");
		TableColumn<Song, String> artist = new TableColumn<>("Artist");
		TableColumn<Song, String> duration = new TableColumn<>("Time");
		TableColumn<Song, String> timesPlayed = new TableColumn<>("Plays");

		timesPlayed.setCellValueFactory(new PropertyValueFactory<Song, String>("Plays"));
		title.setCellValueFactory(new PropertyValueFactory<Song, String>("title"));
		artist.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
		duration.setCellValueFactory(new PropertyValueFactory<Song, String>("duration"));

		table.setItems(observableList);
		title.setMinWidth(250);
		artist.setMinWidth(150);
		duration.setMinWidth(20);

		table.getColumns().addAll(timesPlayed, title, artist, duration);
		addAllSongs();
		this.setCenter(table);

	}


	public TableView<Song> getTable() {
		return table;
	}

	public ObservableList<Song> getList() {
		return observableList;
	}

	public void addAllSongs() {
		ArrayList<Song> theSongs = createTheSongs();
		observableList.addAll(theSongs);

	}

	public ArrayList<Song> createTheSongs() {
		ArrayList<Song> theSongs = new ArrayList<>();
		File[] dir = new File("songfiles/").listFiles();
		for (int i = 0; i < dir.length; i++) {
			Song song = new Song(dir[i].getName(), "", "", 0, table);
			theSongs.add(song);
		}
		return theSongs;
	}
}
