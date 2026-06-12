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
@Table(name = "rentaldetails")
public class Rentaldetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "rental_date")
	private LocalDate rentalDate;

	@Column(name = "drop_date")
	private LocalDate dropDate;

	@Column(name = "return_date")
	private LocalDate returnDate;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}