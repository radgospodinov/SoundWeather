package model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.omg.CORBA.PUBLIC_MEMBER;

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
	@Column(name = "avatar_file_name")
	private String avatarName;
	@Column(name = "activation")
	private boolean isActive;
	@Column(name ="reg_time")
	private long registerDate;
	@ManyToMany
	@JoinTable(name = "ownsounds_table")
	private List<Sound> sounds;
	@ManyToMany
	@JoinTable(name = "favorites_table")
	private List<Sound> favorites;
	@ManyToMany
	@JoinTable(name = "playlists_table")   //   LIKES TABLE   !!!
	private List<Sound> playlist;          //   LIKES TABLE   !!!
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
	public User() {
		sounds = new ArrayList<Sound>();
		favorites = new ArrayList<Sound>();
		playlist = new ArrayList<Sound>();
		albums = new ArrayList<Album>();
		followers = new Vector<User>();
		following = new ArrayList<User>();
		comments = new ArrayList<Comment>();
		isActive = false;
		avatarName = "defaultAvatar";
		registerDate = new Date().getTime();
	}

	public User(String username) {
		this.username = username;
		sounds = new ArrayList<>();
		favorites = new ArrayList<>();
		playlist = new ArrayList<>();
		albums = new ArrayList<>();
		followers = new ArrayList<>();
		following = new ArrayList<>();
		comments = new ArrayList<>();
		avatarName = "defaultAvatar";
		isActive = false;
		registerDate = new Date().getTime();
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
		this.password = getMD5Hash(password + this.username);
		return this;
	}

	public boolean comparePasswords(String password) {
		return this.password.equals(getMD5Hash(password + this.username));
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

	public String getAvatarName() {
		return avatarName;
	}

	public User setAvatarName(String avatarName) {
		this.avatarName = avatarName;
		return this;
	}

	public void addSoundToSounds(Sound sound) {
		sounds.add(sound);
	}

	public void addSoundToLiked(Sound sound) {
		playlist.add(sound);
	}
	public void removeSoundFromLiked(Sound sound) {
		playlist.remove(sound);
	}

	public void addSoundToFavorites(Sound sound) {
		favorites.add(sound);
	}
	public void removeSoundFromSounds(Sound sound) {
		sounds.remove(sound);
	}

	public void addAlbum(Album album) {
		albums.add(album);
	}

	public void removeAlbumFromAlbums(Album album) {
		albums.remove(album);
		
	}


	public void addToFollowing(User target) {
		following.add(target);
		
	}

	public void addToFollowers(User current) {
		followers.add(current);
		
	}

	public void addComment(Comment newComment) {
		comments.add(newComment);
		
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void removeFromFavorites(Sound sound) {
		this.favorites.remove(sound);
	}

	public void removeFromFollowing(User u) {
		this.following.remove(u);
	}

	public void removeFromFollowers(User u) {
		this.followers.remove(u);
	}

}
