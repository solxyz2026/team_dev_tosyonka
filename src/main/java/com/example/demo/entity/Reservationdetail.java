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
@Table(name = "reservationdetails")
@Component
public class Reservationdetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //予約伝票ID

	@ManyToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation; //予約ID reservationテーブルのid

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book; //本ID booksテーブルのid

	@Column(name = "reservation_status")
	private boolean reservationStatus; //t:貸し出し可 f:返却待ち

	@Column(name = "delete_judge")
	private boolean deleteJudge;

	//コンストラクタ
	public Reservationdetail() {
	}

	public Reservationdetail(Reservation reservation, Book book, Boolean reservationStatus) {
		this.reservation = reservation;
		this.book = book;
		this.reservationStatus = reservationStatus;
	}

	public Reservationdetail(Integer id, Reservation reservation, Book book, Boolean reservationStatus) {
		this.id = id;
		this.reservation = reservation;
		this.book = book;
		this.reservationStatus = reservationStatus;
	}

	//ゲッターセッター
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public boolean getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(boolean reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	public String getStatus() {
		if (reservationStatus) {
			return "貸し出し可";
		}
		return "返却待ち";
	}

	public void setDeleteJudge(boolean deleteJudge) {
		this.deleteJudge = deleteJudge;
	}

	public boolean getDeleteJudge() {
		return deleteJudge;
	}

}
