package com.example.demo.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "reservations")
@Component
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //予約ID

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user; //顧客ID usersテーブルのid

	@Column(name = "reservation_date")
	private LocalDate reservationDate; //予約日

	@OneToMany(mappedBy = "reservation") //Reservationdetailの中身をすべて取り出す
	private List<Reservationdetail> reservationdetails;

	//コンストラクタ
	public Reservation() {
	}

	public Reservation(Integer id, User user, LocalDate reservationDate) {
		this.id = id;
		this.user = user;
		this.reservationDate = reservationDate;
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

	public LocalDate getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}

}
