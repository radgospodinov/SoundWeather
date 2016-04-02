package model;

import java.util.List;

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
	@Column(name = "sound_cover_photo")
	private Byte[] soundCoverPhoto; //(photo)
	@Lob
	@Column(name = "audio_file")
	private Byte[] audioFile;
	@OneToMany
	@Column(name = "sound_comments")
	private List<Comment> soundComments;
	@ManyToMany
	@Column(name = "sound_fans")
	private List<User> soundFans; //fans.size() == number of likes
//	@Column(name = "sound_genres")
	//private List<String> soundGenres;
	
	public int getSoundId() {
		return soundId;
	}
	public void setSoundId(int soundId) {
		this.soundId = soundId;
	}
	public String getSoundTitle() {
		return soundTitle;
	}
	public void setSoundTitle(String soundTitle) {
		this.soundTitle = soundTitle;
	}
	public int getSoundViewCount() {
		return soundViewCount;
	}
	public void setSoundViewCount(int soundViewCount) {
		this.soundViewCount = soundViewCount;
	}
	public double getSoundRating() {
		return soundRating;
	}
	public void setSoundRating(double soundRating) {
		this.soundRating = soundRating;
	}
	public User getSoundAuthor() {
		return soundAuthor;
	}
	public void setSoundAuthor(User soundAuthor) {
		this.soundAuthor = soundAuthor;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public Byte[] getSoundCoverPhoto() {
		return soundCoverPhoto;
	}
	public void setSoundCoverPhoto(Byte[] soundCoverPhoto) {
		this.soundCoverPhoto = soundCoverPhoto;
	}
	public Byte[] getAudioFile() {
		return audioFile;
	}
	public void setAudioFile(Byte[] audioFile) {
		this.audioFile = audioFile;
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
	//public List<String> getSoundGenres() {
	//	return soundGenres;
	//}
//	public void setSoundGenres(List<String> soundGenres) {
//		this.soundGenres = soundGenres;
//	}
	
	
	
	
	
	
}
