package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "likebooks")
public class Likebook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//お気に入りID

	@ManyToOne
	@JoinColumn(name = "user_id") //ユーザID
	private User user;

	@ManyToOne
	@JoinColumn(name = "book_id") //本ID
	private Book book;

	@Column(name = "delete_judge")
	private boolean deleteJudge;//t:削除済み f:未削除

	//コンストラクタ

	public Likebook() {
	}

	public Likebook(User user, Book book, boolean deleteJudge) {
		this.user = user;
		this.book = book;
		this.deleteJudge = deleteJudge;
	}

	public Likebook(Integer id, User user, Book book, boolean deleteJudge) {
		this.id = id;
		this.user = user;
		this.book = book;
		this.deleteJudge = deleteJudge;
	}

	//ゲッターセッター

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public boolean isDeleteJudge() {
		return deleteJudge;
	}

	public void setDeleteJudge(boolean deleteJudge) {
		this.deleteJudge = deleteJudge;
	}

}
