package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;

/**
 * this class creates a Queue where a user can add songs. the queue will run
 * immediately, and stop when empty, with a 2 second pause between songs
 * 
 * @author Jackson Burns, Jose Juan Velasquez
 */
public class PlayList {

	private Queue<String> queue;
	private ObservableList<String> observableQueue;

	private final String pathStart;
	private int songsPlayed;

	private MediaPlayer mediaPlayer;

	@SuppressWarnings("unchecked")
	public PlayList() {
		queue = new LinkedList<>();
		observableQueue = FXCollections.observableArrayList();
		String filename = "savedPlaylist.ser";
		try {
			FileInputStream rawBytes = new FileInputStream(filename); // Read the .ser file just created
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);
			queue = (Queue<String>) inFile.readObject();
			observableQueue = FXCollections.observableArrayList(queue);
			inFile.close();
		} catch (FileNotFoundException | ClassNotFoundException e) {
			try {
				// create new database if one doesnt exist
				FileOutputStream bytesToDisk = new FileOutputStream(filename);
				ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
				outFile.writeObject(filename);
				outFile.close(); // Always close the output file!
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} catch (IOException e) {
			save();
		}
		pathStart = "songfiles/";
		songsPlayed = 0;
		System.out.println(getCurrentQueue());
	}

	public void queueUpNextSong(String songToAdd) {
		queue.add(songToAdd);
		observableQueue.add(songToAdd);
		if (mediaPlayer == null) {
			playSong();
		}
	}

	public void clearPlaylist() {
		queue = new LinkedList<>();
		observableQueue = FXCollections.observableArrayList();
	}

	public void playSong() {
		if (queue.isEmpty()) {
			System.out.println("playlist empty, waiting for new song");
			return;
		}

		System.out.println("Currently Playing: " + getSong());
		String path = pathStart + queue.peek();
		File file;
		try {
			file = new File(path);
		} catch (Exception FileNotFoundException) {
			System.out.println("could not find song, skipping");
			mediaPlayer.dispose();
			return;
		}
		URI uri = file.toURI();
		Media media = new Media(uri.toString());
		mediaPlayer = new MediaPlayer(media);
		resumeSong();
		mediaPlayer.setOnEndOfMedia(new Waiter());

	}

	public void save() {
		try {
			String filename = "savedPlaylist.ser";
			FileOutputStream bytesToDisk = new FileOutputStream(filename);
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);
			outFile.writeObject(queue);
			outFile.close(); // Always close the output file!
		} catch (IOException ioe) {
			System.out.println("Writing objects failed");
		}
	}

	public String getSong() {
		return queue.peek();
	}


	public void resumeSong() {
		mediaPlayer.play();
	}

	public Queue<String> getCurrentQueue() {
		return queue;
	}

	public int size() {
		return queue.size();
	}

	public int getSongsPlayed() {
		return songsPlayed;
	}

	public ObservableList<String> getObservableQueue() {
		return observableQueue;
	}

	private class Waiter implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			songsPlayed++;
			queue.poll();
			observableQueue.remove(0);
			if (queue.isEmpty()) {
				mediaPlayer.dispose();
				mediaPlayer = null;
			} else {
				System.out.println("Song ended, play song #" + (songsPlayed + 1) + ": " + getSong());
			}
			playSong();

		}
	}

}