package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Reservationdetail;
import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.model.ReservationsCart;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.ReservationdetailRepository;
import com.example.demo.repository.UserRepository;

//大森
@Controller
@RequestMapping("/user")
public class ReservationController {

	private final Account account;
	private final ReservationsCart reservationsCart;
	private final BookRepository bookRepository;
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;
	private final ReservationdetailRepository reservationdetailRepository;

	public ReservationController(Account account, ReservationsCart reservationsCart, BookRepository bookRepository,
			ReservationRepository reservationRepository, UserRepository userRepository,
			ReservationdetailRepository reservationdetailRepository) {
		this.account = account;
		this.reservationsCart = reservationsCart;
		this.bookRepository = bookRepository;
		this.reservationRepository = reservationRepository;
		this.userRepository = userRepository;
		this.reservationdetailRepository = reservationdetailRepository;
	}

	//本詳細から予約カートに本を追加する
	@GetMapping("/{id}/reservaion")
	public String delete(
			@PathVariable Integer id,
			Model model) {

		//ここにモデルReservationsCartに予約したい本のidを格納していく
		Book book = bookRepository.findById(id).get();
		reservationsCart.add(book);

		return "redirect:/user/book/" + id;
	}

	// 指定した商品をカートから削除
	@PostMapping("/{id}/delete")
	public String deleteCart(@PathVariable Integer id) {
		// カート情報から削除
		System.out.println("本のID＝" + id);
		reservationsCart.delete(id);
		return "redirect:/user/reservations";
	}

	//予約カート画面の表示
	@GetMapping("/reservations")
	public String store(Model model) {

		//カートの中身を取得
		List<Book> books = reservationsCart.getBooks();
		model.addAttribute("books", books);

		return "myReservations";
	}

	//予約カートの中身を確定させる
	@PostMapping("/reservations")
	public String myReservations(Model model) {
		LocalDate today = LocalDate.now();
		User user = userRepository.findById(account.getId()).get();

		//Reservationに本以外の情報を登録し、idを確定させる
		Reservation reservation = new Reservation(user, today);
		reservationRepository.save(reservation);

		//確定したidを利用しカートの中身をReservationに登録する
		List<Reservationdetail> details = new ArrayList<>();
		List<Book> books = reservationsCart.getBooks();
		for (Book book : books) {
			details.add(new Reservationdetail(reservation, book, !book.isLoans()));
		}
		reservationdetailRepository.saveAll(details);

		//カートの中身を削除
		reservationsCart.clear();

		return "redirect:/user/reservations/confirmed";
	}

	//予約カート確定後の詳細ページを表示
	@GetMapping("/reservations/confirmed")
	public String myReservationsConfirmed(Model model) {
		//今日の日付
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
		model.addAttribute("today", today.format(formatter));

		//確定した予約詳細を取得
		List<Reservation> reservatonsConfirmed = reservationRepository.findByReservationDate(today);
		model.addAttribute("reservatonsConfirmed", reservatonsConfirmed);

		return "myReservationsConfirmed";
	}

}
