package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.mockito.internal.exceptions.util.ScenarioPrinter;

import scala.collection.immutable.ListMap;

@Entity
@Table (name = "sounds")
public class Sound {

	@Id 
	@GeneratedValue (strategy = GenerationType.TABLE)
	@Column(name = "sound_id")
	private int soundId;
	@Column(name = "sound_title")
	private String soundTitle;
	@Column(name = "sound_view_count")
	private int soundViewCount;
	@Column(name = "sound_rating")
	private double soundRating; //function of view-count and likes
	@ManyToOne
	//@Column(name = "sound_author")
	private User soundAuthor;
	@ManyToOne	
	private Album album;
	@Column(name = "file_name")
	private String fileName;
	@OneToMany
	@Column(name = "sound_comments") // mapped by -> comment
	private List<Comment> soundComments;
	@ManyToMany
	@Column(name = "sound_fans")
	private List<User> soundFans; //fans.size() == number of likes
	@ManyToMany
	@Column(name = "sound_genres")
	private List<Genre> soundGenres;
	
	public Sound () {
		this.soundComments = new Vector<Comment>();
		this.soundFans = new Vector<User>();
		this.soundGenres = new ArrayList<Genre>();
	}
	
	
	public int getSoundId() {
		return soundId;
	}
	public Sound setSoundId(int soundId) {
		this.soundId = soundId;
		return this;
	}
	public String getSoundTitle() {
		return soundTitle;
	}
	public Sound setSoundTitle(String soundTitle) {
		this.soundTitle = soundTitle;
		return this;
	}
	public int getSoundViewCount() {
		return soundViewCount;
	}
	public Sound setSoundViewCount(int soundViewCount) {
		this.soundViewCount = soundViewCount;
		return this;
	}
	public double getSoundRating() {
		return soundRating;
	}
	public Sound setSoundRating(double soundRating) {
		this.soundRating = soundRating;
		return this;
	}
	public User getSoundAuthor() {
		return soundAuthor;
	}
	public Sound setSoundAuthor(User soundAuthor) {
		this.soundAuthor = soundAuthor;
		return this;
	}
	public Album getAlbum() {
		return album;
	}
	public Sound setAlbum(Album album) {
		this.album = album;
		return this;
	}
	public List<Comment> getSoundComments() {
		return soundComments;
	}
	public void setSoundComments(List<Comment> soundComments) {
		this.soundComments = soundComments;
	}
	public List<User> getSoundFans() {
		return soundFans;
	}
	public void setSoundFans(List<User> soundFans) {
		this.soundFans = soundFans;
	}
	private List<Genre> getSoundGenres() {
		return soundGenres;
	}
	public void setSoundGenres(List<Genre> soundGenres) {
		this.soundGenres = soundGenres;
	}

	public String getFileName() {
		return fileName;
	}

	public Sound setFileName(String filesPath) {
		this.fileName = filesPath;
		return this;
	}
	public void addListOfGenres(List<Genre> genres) {
		soundGenres.addAll(genres);
	}
	public void addGenre(Genre genre) {
		soundGenres.add(genre);
	}
	
	
	
	
	
	
}
