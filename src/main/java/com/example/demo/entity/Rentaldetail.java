package com.example.demo.entity;

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
	private Integer id;//貸出ID

	@ManyToOne
	@JoinColumn(name = "rental_id") //貸出伝票ID
	private Rental rental;

	@ManyToOne
	@JoinColumn(name = "book_id") //本ID
	private Book book;
}