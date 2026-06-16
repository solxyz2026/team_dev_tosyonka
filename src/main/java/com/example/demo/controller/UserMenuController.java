package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Announcement;
import com.example.demo.entity.Rental;
import com.example.demo.entity.Rentaldetail;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Reservationdetail;
import com.example.demo.model.Account;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.ReservationRepository;

@Controller
@RequestMapping("/user")
public class UserMenuController {

	private final Account account;
	private final AnnouncementRepository announcementRepository;
	private final ReservationRepository reservationRepository;
	private final RentalRepository rentalRepository;

	public UserMenuController(Account account, AnnouncementRepository announcementRepository,
			ReservationRepository reservationRepository, RentalRepository rentalRepository) {
		this.account = account;
		this.announcementRepository = announcementRepository;
		this.reservationRepository = reservationRepository;
		this.rentalRepository = rentalRepository;
	}

	//大森
	//メイン画面の表示
	@GetMapping("/")
	public String index(Model model) {
		int userId = account.getId();

		//お知らせ内容の取得
		List<Announcement> newsList = announcementRepository.findAll();
		model.addAttribute("newsList", newsList);
		System.out.println(12345678);

		//予約内容の取得

		List<Reservation> reservationsList = reservationRepository.findByUserId(userId);
		model.addAttribute("reservationsList", reservationsList);
		System.out.println(reservationsList.size());

		if (reservationsList.size() != 0) {
			List<Reservationdetail> detail = reservationsList.get(0).getReservationdetails();
			System.out.println("a = " + detail.size());
			model.addAttribute("detail", detail);
		}

		//翌日返却日の本の取得
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		System.out.println(tomorrow);
		List<Rental> rental = rentalRepository.findByUserIdAndDropDate(userId, tomorrow);
		System.out.println(rental.size());
		model.addAttribute("rentalSize", rental.size());

		String msg = "リマインド\n明日までに以下の本を返却してください。\n\n";

		for (Rental rent : rental) {
			for (Rentaldetail detail : rent.getRentaldetail()) {
				msg += "・" + detail.getBook().getTitle() + "\n";
			}
		}

		model.addAttribute("msg", msg);

		return "userMenu";
	}
}
