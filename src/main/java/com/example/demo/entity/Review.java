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

@Entity
@Table(name = "reviews")
@Component
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //レビューID

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book; //本ID booksテーブルのid

	@Column(name = "book_review")
	private String bookReview; //レビュー内容

	//コンストラクタ
	public Review() {
	}

	public Review(Book book, String bookReview) {
		this.book = book;
		this.bookReview = bookReview;
	}

	public Review(Integer id, Book book, String bookReview) {
		this.id = id;
		this.book = book;
		this.bookReview = bookReview;
	}

	//ゲッターセッター
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getBookReview() {
		return bookReview;
	}

	public void setBookReview(String bookReview) {
		this.bookReview = bookReview;
	}

}
