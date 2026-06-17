package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Account;
import com.example.demo.repository.ReservationRepository;

//大森
@Controller
@RequestMapping("/user")
public class ReservationController {

	private final Account account;
	private final ReservationRepository reservationRepository;

	public ReservationController(Account account, ReservationRepository reservationRepository) {
		this.account = account;
		this.reservationRepository = reservationRepository;
	}

	//予約画面の表示
	@GetMapping("/reservations")
	public String myReservations(Model model) {
		return "myReservations";
	}

}
