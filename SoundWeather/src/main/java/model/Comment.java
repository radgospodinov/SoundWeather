package model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table (name = "comments")
public class Comment {

	@Id 
	@GeneratedValue (strategy = GenerationType.TABLE)
	@Column(name = "comment_id")
	private int commentId;
	@Column(name = "comment_posting_date_time")
	private LocalDateTime commentPostingDateTime;
	@ManyToOne
	//@Column(name = "comment_author")
	private User commentAuthor;
	@Column(name = "comment_body")
	private String commentBody;
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public LocalDateTime getCommentPostingDateTime() {
		return commentPostingDateTime;
	}
	public Comment setCommentPostingDateTime(LocalDateTime commentPostingDateTime) {
		this.commentPostingDateTime = commentPostingDateTime;
		return this;
	}
	public User getCommentAuthor() {
		return commentAuthor;
	}
	public Comment setCommentAuthor(User commentAuthor) {
		this.commentAuthor = commentAuthor;
		return this;
	}
	public String getCommentBody() {
		return commentBody;
	}
	public Comment setCommentBody(String commentBody) {
		this.commentBody = commentBody;
		return this;
	}
	
	
	
	
	
}
