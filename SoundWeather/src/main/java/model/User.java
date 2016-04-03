package model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Entity
@Table(name = "users")
public class User {

	@Id
	// @GeneratedValue (strategy = GenerationType.TABLE)
	// @Column(name = "user_id")
	// private int userId;
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
	private String location; // Play list to be generated according to the
								// weather description for the location City
	@ManyToMany
	@JoinTable(name = "ownsounds_table")
	private List<Sound> sounds;
	@ManyToMany
	@JoinTable(name = "favorites_table")
	private List<Sound> favorites;
	@ManyToMany
	@JoinTable(name = "playlists_table")
	private List<Sound> playlist;
	@ManyToMany
	@Column(name = "albums")
	private List<Album> albums;
	@ManyToMany
	@JoinTable(name = "followers_table")
	private List<User> followers;
	@ManyToMany
	@JoinTable(name = "following_table")
	private List<User> following;
	@OneToMany
	@Column(name = "comments")
	private List<Comment> comments;

	// public int getUserId() {
	// return userId;
	// }
	// public void setUserId(int userId) {
	// this.userId = userId;
	// }
	
	public User(String username) {
		this.username=username;
		sounds = new ArrayList<>();
		favorites = new ArrayList<>();
		playlist = new ArrayList<>();
		albums = new ArrayList<>();
		followers = new ArrayList<>();
		following = new ArrayList<>();
		comments = new ArrayList<>();
	}
	
	public String getUsername() {
		return username;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	private String getMD5Hash(String from) {
		String rv = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(from.getBytes("UTF-8"));
			rv = Base64.encode(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// u
			e.printStackTrace();
		}

		return rv;
	}

	public User setPassword(String password) {
		this.password = getMD5Hash(password+this.username);
		return this;
	}

	public boolean comparePasswords(String password) {
		return this.password.equals(getMD5Hash(password+this.username));
	}

	public String getBirthYear() {
		return birthYear;
	}

	public User setBirthYear(String birthYear) {
		this.birthYear = birthYear;
		return this;
	}

	public String getBirthMonth() {
		return birthMonth;
	}

	public User setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
		return this;
	}

	public String getGender() {
		return gender;
	}

	public User setGender(String gender) {
		this.gender = gender;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public User setLocation(String location) {
		this.location = location;
		return this;
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
