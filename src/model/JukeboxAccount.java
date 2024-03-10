package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Jukebox account which stores a username, password, songs played today, and
 * the date, which can be serialized. it also contains operations to increment
 * the currentdate counter, and dynamically check the date when played.
 *
 * @author Jackson Burns, Jose Juan Velasquez
 */
public class JukeboxAccount implements Serializable {
	private final String username;
	private final String password;
	private int songsPlayedToday;
	private LocalDate currentDate;

	public JukeboxAccount(String username, String password) {
		this.username = username;
		this.password = password;
		this.songsPlayedToday = 0;
		currentDate = LocalDate.now();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean canPlaySong() {
		return songsPlayedToday < 3;
	}

	public int getSongsPlayedToday() {
		if (!currentDate.toString().equals(LocalDate.now().toString())) {
			songsPlayedToday = 0;
			currentDate = LocalDate.now();
		}
		return songsPlayedToday;
	}

	public boolean playSong() {
		if (!currentDate.toString().equals(LocalDate.now().toString())) {
			songsPlayedToday = 0;
			currentDate = LocalDate.now();
		}
		if (canPlaySong()) {
			songsPlayedToday++;
			return true;
		}
		return false;
	}

}
