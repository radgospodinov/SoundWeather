package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "albums")
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "album_id")
	private int albumId;
	@Column(name = "album_title")
	private String albumTitle;
	@ManyToOne
	private User albumAuthor;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Sound> albumTracks;
	@Column(name = "album_cover")
	private String fileName; // (photo)
	@ManyToMany
	@Column(name = "album_genres") // mai tva beshe one-to-many pregledai
	private List<Genre> albumGenres;
	
	
	public Album() {
		albumGenres = new ArrayList<>();
		albumTracks = new ArrayList<>();
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}

	public Album setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public Album setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public User getAlbumAuthor() {
		return albumAuthor;
	}

	public Album setAlbumAuthor(User albumAuthor) {
		this.albumAuthor = albumAuthor;
		return this;
	}

	public List<Sound> getAlbumTracks() {
		return albumTracks;
	}

	public void setAlbumTracks(List<Sound> albumTracks) {
		this.albumTracks = albumTracks;
	}

	public List<Genre> getAlbumGenres() {
		return albumGenres;
	}

	public Album setAlbumGenres(List<Genre> albumGenres) {
		this.albumGenres = albumGenres;
		return this;
	}

	public void removeSound(Sound sound) {
		albumTracks.remove(sound);
	}

	public void addGenre(Genre genre) {
		albumGenres.add(genre);
	}

	public void addSound(Sound sound) {
		albumTracks.add(sound);
		
	}

}
