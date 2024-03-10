package model;

import javafx.scene.control.TableView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URI;

/**
 *
 * This class adds the ability to store information such as title,
 * artist, duration, and amount of plays per session of each song
 * it is able to iterate through the song directory, and read metadata of each song
 * and store it in this class, to be read by the TableView
 *
 * @author Jackson Burns
 */
public class Song {
	private String title;
	private String artist;
	private int plays;
	private String fileName;
	private MediaPlayer mp;
	private String duration;
	private String path;
	private TableView<?> table;

	public Song(String fileName, String artist, String duration, int timesPlayed, TableView<?> table) {
		this.title = fileName;
		this.fileName = fileName;
		this.path = "songfiles/" + fileName;
		this.table = table;
		this.artist = artist;
		this.duration = duration;
		this.plays = timesPlayed;

		File file = new File(path);
		URI uri = file.toURI();
		Media media = new Media(uri.toString());

		this.mp = new MediaPlayer(media);
		mp.setOnReady(new MetadataFinder(this, media, mp));

	}

	public String getTitle() {
		return title;

	}

	public String getFileName() {
		return fileName;
	}

	public String getArtist() {
		return artist;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	private void setArtist(String artist) {
		this.artist = artist;
	}

//
	public String getDuration() {
		return duration;

	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getPlays() {
		return plays;
	}

	public void incrementPlayCounter() {
		plays++;
	}

	/**
	 * This class reads the metadata of each song and sets the values of the song class
	 * @Author Jackson Burns
	 */
	private class MetadataFinder implements Runnable {

		private MediaPlayer mp;
		private Media media;
		private Song song;

		public MetadataFinder(Song song, Media media, MediaPlayer mp) {
			this.song = song;
			this.media = media;
			this.mp = mp;

		}

		@Override
		public void run() {
			int seconds = (int) Math.floor(mp.getTotalDuration().toSeconds());
			int minutes = seconds / 60;
			seconds %= 60;
			String duration = String.format("%02d:%02d", minutes, seconds);

			mp.dispose();
			song.setDuration(duration);
			try {
				song.setArtist(((String) media.getMetadata().get("artist")).strip().trim());
			} catch (NullPointerException ex) {
				song.setArtist("Unknown");
			}
			try {
				song.setTitle(((String) media.getMetadata().get("title")).strip().trim());
			} catch (NullPointerException ex) {
				song.setTitle(song.getTitle().split("\\.")[0]);
			}

			table.refresh();

		}
	}

}
