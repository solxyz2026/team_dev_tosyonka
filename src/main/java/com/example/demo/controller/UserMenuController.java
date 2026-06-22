package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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

//大森
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
		LocalDate today = LocalDate.now();
        List<Rental> rental = rentalRepository.findByUserIdAndDropDateBeforeAndReturnDateIsNull(userId, today);

        // 返却本数
        model.addAttribute("rentalSize", rental.size());

		//リマインド用データをリスト化
		List<String> reminderBooks = new ArrayList<>();
        for (Rental rent : rental) {
            List<Rentaldetail> rentalDetails = rent.getRentaldetail();
            if (rentalDetails != null && !rentalDetails.isEmpty()) {
                for (Rentaldetail detail : rentalDetails) {
                    if (detail.getBook() != null && detail.getBook().getTitle() != null) {
                        reminderBooks.add(detail.getBook().getTitle());
                    }
                }
            }
        }

        System.out.println("=== リマインダーデバッグ情報 ===");
        System.out.println("ユーザーID: " + userId);
        System.out.println("今日の日付: " + today);
        System.out.println("期限切れ本数: " + rental.size());
        System.out.println("期限切れの本: " + reminderBooks);
        System.out.println("================================");

        model.addAttribute("reminderBooks", reminderBooks);
		return "userMenu";
	}
}