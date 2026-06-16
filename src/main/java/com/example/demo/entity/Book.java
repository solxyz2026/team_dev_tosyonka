package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "books")

public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 本のid

	private String title; // タイトル
	private String publisher;//出版社
	private String summary;//内容の要約
	private boolean loans;//貸出状況　true : 貸出中 false : 未貸し出し
	private LocalDate date;//出版日付

	@ManyToOne
	@JoinColumn(name = "writer_id") //出版社id
	private Writer writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id") //カテゴリid
	private Category category;

	public Book() {

	}

	public Book(Integer id, String title, String publisher, String summary, boolean loans, LocalDate date,
			Writer writer, Category category) {
		this.id = id;
		this.title = title;
		this.publisher = publisher;
		this.summary = summary;
		this.loans = loans;
		this.date = date;
		this.writer = writer;
		this.category = category;
	}

	public Book(Integer id, String title, String publisher, String summary, LocalDate date, Writer writer,
			Category category) {
		super();
		this.id = id;
		this.title = title;
		this.publisher = publisher;
		this.summary = summary;
		this.date = date;
		this.writer = writer;
		this.category = category;
	}

	public Book(Integer id, boolean loans) {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public boolean isLoans() {
		return loans;
	}

	public void setLoans(boolean loans) {
		this.loans = loans;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}