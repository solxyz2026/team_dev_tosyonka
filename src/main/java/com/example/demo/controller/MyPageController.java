package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Rental;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Reservationdetail;
import com.example.demo.model.Account;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.ReservationRepository;

//大森
@Controller
@RequestMapping("/user")
public class MyPageController {

	private final Account account;
	private final ReservationRepository reservationRepository;
	private final RentalRepository rentalRepository;

	public MyPageController(Account account, ReservationRepository reservationRepository,
			RentalRepository rentalRepository) {
		this.account = account;
		this.reservationRepository = reservationRepository;
		this.rentalRepository = rentalRepository;
	}

	//マイページ画面の表示
	@GetMapping("/mypage")
	public String myPage(Model model) {
		int userId = account.getId();
		String userName = account.getName();
		model.addAttribute("userId", userId);
		model.addAttribute("userName", userName);

		//予約内容の取得
		List<Reservation> reservationsList = reservationRepository.findByUserId(userId);
		model.addAttribute("reservationsList", reservationsList);

		if (reservationsList.size() != 0) {
			List<Reservationdetail> detail = reservationsList.get(0).getReservationdetails();
			model.addAttribute("detail", detail);
		}

		//返却期限が切れた本の取得
		LocalDate today = LocalDate.now();
		List<Rental> expiredList = rentalRepository.findByUserIdAndDropDateBeforeAndReturnDateIsNull(userId, today);
		model.addAttribute("expiredList", expiredList);

		//貸し出し中（返却期限切れ込み）の本を取得
		List<Rental> rentalList = rentalRepository.findByUserIdAndReturnDateIsNull(userId);
		model.addAttribute("rentalList", rentalList);

		//貸し出し履歴
		List<Rental> rentalAllList = rentalRepository.findByUserId(userId);
		model.addAttribute("rentalAllList", rentalAllList);

		return "myPage";
	}

}
