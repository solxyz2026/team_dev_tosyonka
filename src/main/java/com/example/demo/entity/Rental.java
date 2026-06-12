package com.example.demo.entity;

import java.time.LocalDate;

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
@Table(name = "rentals")
public class Rental {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//貸出ID

	@Column(name = "rental_date")
	private LocalDate rentalDate;//貸出日

	@Column(name = "drop_date")
	private LocalDate dropDate;//返却日

	@Column(name = "return_date")
	private LocalDate returnDate;//実返却日

	@ManyToOne
	@JoinColumn(name = "user_id") //顧客ID
	private User user;

	public Rental() {

	}

	public Rental(Integer id, LocalDate rentalDate, LocalDate dropDate, LocalDate returnDate, User user) {
		this.id = id;
		this.rentalDate = rentalDate;
		this.dropDate = dropDate;
		this.returnDate = returnDate;
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getRentalDate() {
		return rentalDate;
	}

	public void setRentalDate(LocalDate rentalDate) {
		this.rentalDate = rentalDate;
	}

	public LocalDate getDropDate() {
		return dropDate;
	}

	public void setDropDate(LocalDate dropDate) {
		this.dropDate = dropDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}