//大森

package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Announcement;
import com.example.demo.entity.Reservation;
import com.example.demo.model.Account;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.ReservationRepository;

@Controller
@RequestMapping("/user")
public class UserMenuController {

	private final Account account;
	private final AnnouncementRepository announcementRepository;
	private final ReservationRepository reservationRepository;

	public UserMenuController(Account account, AnnouncementRepository announcementRepository,
			ReservationRepository reservationRepository) {
		this.account = account;
		this.announcementRepository = announcementRepository;
		this.reservationRepository = reservationRepository;
	}

	//メイン画面の表示
	@GetMapping("/")
	public String index(Model model) {
		//int userId = account.getId();
		int userId = 1;

		//お知らせ内容の取得
		List<Announcement> newsList = announcementRepository.findAll();
		model.addAttribute("newsList", newsList);

		//予約内容の取得
		List<Reservation> reservationsList = new ArrayList<>(); //reservationRepository.findAll();//ByUserId(userId);
		model.addAttribute("reservationsList", reservationsList);
		System.out.println(reservationsList.size());

		return "userMenu";
	}
}
