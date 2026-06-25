package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Rental;
import com.example.demo.entity.Reservationdetail;
import com.example.demo.model.Account;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.ReservationdetailRepository;

/**
 * マイページコントローラー
 * ・ユーザーの貸出・予約情報を表示
 */
@Controller
@RequestMapping("/user/mypage")
public class MyPageController {

	private final Account account;
	private final RentalRepository rentalRepository;
	private final ReservationRepository reservationRepository;

	@Autowired
	private ReservationdetailRepository reservationdetailRepository;

	public MyPageController(Account account, RentalRepository rentalRepository,
			ReservationRepository reservationRepository) {
		this.account = account;
		this.rentalRepository = rentalRepository;
		this.reservationRepository = reservationRepository;
	}

	/**
	 * マイページ表示
	 */
	@GetMapping("")
	public String myPage(Model model) {
		int userId = account.getId();
		String userName = account.getName();
		model.addAttribute("userId", userId);
		model.addAttribute("userName", userName);

		// 予約内容の取得
		List<Reservationdetail> reservationsList = reservationdetailRepository
				.findByReservation_User_IdAndDeleteJudgeFalseAndBook_DeleteJudgeFalse(userId);
		model.addAttribute("reservationsList", reservationsList);

		// 返却期限が切れた本の取得
		LocalDate today = LocalDate.now();
		List<Rental> expiredList = rentalRepository.findByUserIdAndDropDateBeforeAndReturnDateIsNull(userId, today);
		model.addAttribute("expiredList", expiredList);

		// 🔧 修正: 貸し出し中の本を取得（Rentaldetail.returnDate をチェック）
		// 修正前: findByUserIdAndReturnDateIsNull(userId)
		// 修正後: findByUserIdAndUnreturnedBooks(userId)
		List<Rental> rentalList = rentalRepository.findByUserIdAndUnreturnedBooks(userId);
		model.addAttribute("rentalList", rentalList);

		// 貸し出し履歴
		List<Rental> rentalAllList = rentalRepository.findByUserId(userId);
		model.addAttribute("rentalAllList", rentalAllList);

		return "myPage";
	}
}