package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Reservationdetail;
import com.example.demo.model.Account;
import com.example.demo.model.ReservationsCart;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReservationRepository;

//大森
@Controller
@RequestMapping("/user")
public class ReservationController {

	private final Account account;
	private final ReservationsCart reservationsCart;
	private final BookRepository bookRepository;
	private final ReservationRepository reservationRepository;

	public ReservationController(Account account, ReservationsCart reservationsCart, BookRepository bookRepository,
			ReservationRepository reservationRepository) {
		this.account = account;
		this.reservationsCart = reservationsCart;
		this.bookRepository = bookRepository;
		this.reservationRepository = reservationRepository;
	}

	//予約画面の表示
	@GetMapping("/reservations")
	public String store() {
		return "myReservations";
	}

	@PostMapping("/reservations")
	public String myReservations(Model model) {
		Integer userId = account.getId();
		LocalDate reservationDate = LocalDate.now();
		List<Reservationdetail> reservationdetails = new ArrayList<>();

		//reservationRepository.save(userId, reservationDate, reservationdetails);
		return "redirect:/user/reservations/confirmed";
	}

	@GetMapping("/reservations/confirmed")
	public String myReservationsConfirmed(Model model) {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
		model.addAttribute("today", today.format(formatter));
		return "myReservationsConfirmed";
	}

	//	//削除
	//	@PostMapping("/user/{id}/delete")
	//	public String delete2(
	//			@PathVariable int id,
	//			Model model) {
	//		//テーブルのレコードを削除
	//		return "redirect:/user/reservations";
	//	}

}
