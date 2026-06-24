package com.example.demo.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Announcement;
import com.example.demo.entity.Rental;
import com.example.demo.entity.Rentaldetail;
import com.example.demo.entity.Reservationdetail;
import com.example.demo.model.Account;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.ReservationdetailRepository;

import tools.jackson.databind.ObjectMapper;

//お知らせ、予約、返却リマインド、カレンダー期限表示
 
@Controller
@RequestMapping("/user")
public class UserMenuController {

	private final Account account;
	private final AnnouncementRepository announcementRepository;
	private final ReservationRepository reservationRepository;
	private final RentalRepository rentalRepository;
	
	@Autowired
	private ReservationdetailRepository reservationdetailRepository;
	// JSON変換用（Spring Boot標準のJackson）
	private final ObjectMapper objectMapper = new ObjectMapper();

	public UserMenuController(Account account, AnnouncementRepository announcementRepository,
			ReservationRepository reservationRepository, RentalRepository rentalRepository) {
		this.account = account;
		this.announcementRepository = announcementRepository;
		this.reservationRepository = reservationRepository;
		this.rentalRepository = rentalRepository;
	}

	//ユーザーのメイン画面を表示

	@GetMapping("/")
	public String index(Model model) {
		int userId = account.getId();

		// お知らせ内容の取得
		
		List<Announcement> newsList = announcementRepository.findAll();
		model.addAttribute("newsList", newsList);

		// 予約内容の取得
		
		List<Reservationdetail> reservationsList = reservationdetailRepository
				.findByReservation_User_IdAndDeleteJudgeFalse(userId);
		model.addAttribute("reservationsList", reservationsList);

		
		//翌日返却日の本の取得（リマインダー用）
		
		LocalDate today = LocalDate.now();
		List<Rental> rental = rentalRepository.findByUserIdAndDropDateBeforeAndReturnDateIsNull(userId, today);

		// 返却本数
		model.addAttribute("rentalSize", rental.size());

		// リマインド用データをリスト化
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
		model.addAttribute("reminderBooks", reminderBooks);

		// カレンダー用：返却期限の日付データを抽出
		
		List<Rental> rentalList = rentalRepository.findByUserIdAndReturnDateIsNull(userId);
		Map<String, List<String>> deadlinesByMonth = new HashMap<>();

		for (Rental rentalItem : rentalList) {
			if (rentalItem.getDropDate() != null && rentalItem.getReturnDate() == null) {
				LocalDate dropDate = rentalItem.getDropDate();
				int day = dropDate.getDayOfMonth();
				YearMonth yearMonth = YearMonth.from(dropDate);
				String monthKey = yearMonth.toString(); // "2026-01" 形式

				deadlinesByMonth.computeIfAbsent(monthKey, k -> new ArrayList<>())
						.add(String.valueOf(day));
			}
		}

		// MapをJSON文字列に変換してModelに追加
		
		String deadlinesJson = "{}";
		try {
			deadlinesJson = objectMapper.writeValueAsString(deadlinesByMonth);
		} catch (Exception e) {
			System.err.println("期限データのJSON変換に失敗: " + e.getMessage());
		}
		model.addAttribute("deadlinesJson", deadlinesJson);

		//現在の日付情報を追加
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
		String formattedDate = today.format(dateFormatter);
		String[] dayOfWeekJa = {"日", "月", "火", "水", "木", "金", "土"};
		String dayOfWeekName = dayOfWeekJa[today.getDayOfWeek().getValue() % 7];

		model.addAttribute("todayDate", formattedDate);
		model.addAttribute("todayDayOfWeek", dayOfWeekName);

		// デバッグログ出力
		
		System.out.println("========== ユーザーメニュー情報 ==========");
		System.out.println("  ユーザーID: " + userId);
		System.out.println("  貸出中の本数: " + rentalList.size());
		System.out.println("  期限切れの本数: " + rental.size());
		System.out.println("  リマインダー対象: " + reminderBooks);
		System.out.println("  カレンダー期限データ(Map): " + deadlinesByMonth);
		System.out.println("  カレンダー期限データ(JSON): " + deadlinesJson);
		System.out.println("==========================================");

		return "userMenu";
	}
}