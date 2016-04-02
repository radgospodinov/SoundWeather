package model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table (name = "users")
public class User {

	@Id 
	@GeneratedValue (strategy = GenerationType.TABLE)
	@Column(name = "user_id")
	private int userId;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "birth_year")
	private String birthYear;
	@Column(name = "birth_month")
	private String birthMonth;
	@Column(name = "gender")
	private String gender;
	@Column(name = "email")
	private String email;
	@Column(name = "location")
	private String location; //Play list to be generated according to the weather description for the location City
	@ManyToMany
	@JoinTable (name="ownsounds_table")
	private List<Sound> sounds;
	@ManyToMany
	@JoinTable (name="favorites_table")
	private List<Sound> favorites;
	@ManyToMany
	@JoinTable (name="playlists_table")
	private List<Sound> playlist;
	@ManyToMany
	@Column(name = "albums")
	private List<Album> albums;
	@ManyToMany
	@JoinTable (name="followers_table")
	private List<User> followers;
	@ManyToMany
	@JoinTable(name="following_table")
	private List<User> following;
	@OneToMany
	@Column(name = "comments")
	private List<Comment> comments;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(String birthYear) {
		this.birthYear = birthYear;
	}
	public String getBirthMonth() {
		return birthMonth;
	}
	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<Sound> getSounds() {
		return sounds;
	}
	public void setSounds(List<Sound> sounds) {
		this.sounds = sounds;
	}
	public List<Sound> getFavorites() {
		return favorites;
	}
	public void setFavorites(List<Sound> favorites) {
		this.favorites = favorites;
	}
	public List<Sound> getPlaylist() {
		return playlist;
	}
	public void setPlaylist(List<Sound> playlist) {
		this.playlist = playlist;
	}
	public List<Album> getAlbums() {
		return albums;
	}
	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
	public List<User> getFollowers() {
		return followers;
	}
	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
	public List<User> getFollowing() {
		return following;
	}
	public void setFollowing(List<User> following) {
		this.following = following;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
	
	
}
