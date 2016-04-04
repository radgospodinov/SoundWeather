package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "genres")
public class Genre {

	@Id 
	@GeneratedValue (strategy = GenerationType.TABLE)
	@Column(name = "genre_id")
	private int genreId;
	@Column(name = "genre_name")
	private String genreName;
	
	
	
	public int getGenreId() {
		return genreId;
	}
	public Genre setGenreId(int genreId) {
		this.genreId = genreId;
		return this;
	}
	public String getGenreName() {
		return genreName;
	}
	public Genre setGenreName(String genreName) {
		this.genreName = genreName;
		return this;
	}
	
	
}
