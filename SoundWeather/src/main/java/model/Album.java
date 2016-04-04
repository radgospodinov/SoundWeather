package model;

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
@Table (name = "albums")
public class Album {

	@Id 
	@GeneratedValue (strategy = GenerationType.TABLE)
	@Column(name = "album_id")
	private int albumId;
	@Column(name = "album_title")
	private String albumTitle;
	@ManyToOne
	private User albumAuthor;
	@OneToMany (fetch = FetchType.EAGER)
	private List<Sound> albumTracks;
	@Column(name = "album_cover")
	private Byte[] albumCover; //(photo)
	@ManyToMany
	@Column(name = "album_genres") // mai tva beshe one-to-many pregledai
	private List<Genre> albumGenres;
	
	
	
	public int getAlbumId() {
		return albumId;
	}
	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}
	public String getAlbumTitle() {
		return albumTitle;
	}
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
	public User getAlbumAuthor() {
		return albumAuthor;
	}
	public void setAlbumAuthor(User albumAuthor) {
		this.albumAuthor = albumAuthor;
	}
	public List<Sound> getAlbumTracks() {
		return albumTracks;
	}
	public void setAlbumTracks(List<Sound> albumTracks) {
		this.albumTracks = albumTracks;
	}
	public Byte[] getAlbumCover() {
		return albumCover;
	}
	public void setAlbumCover(Byte[] albumCover) {
		this.albumCover = albumCover;
	}
	public List<Genre> getAlbumGenres() {
		return albumGenres;
	}
	public void setAlbumGenres(List<Genre> albumGenres) {
		this.albumGenres = albumGenres;
	}
	
	
	
}
