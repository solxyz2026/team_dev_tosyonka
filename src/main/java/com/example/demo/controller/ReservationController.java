package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Reservationdetail;
import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.ReservationdetailRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class ReservationController {

	private final Account account;
	private final BookRepository bookRepository;
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;
	private final ReservationdetailRepository reservationdetailRepository;

	private static final String SESSION_RESERVATION_CART = "reservationCart";

	public ReservationController(Account account,
			BookRepository bookRepository,
			ReservationRepository reservationRepository,
			UserRepository userRepository,
			ReservationdetailRepository reservationdetailRepository) {

		this.account = account;
		this.bookRepository = bookRepository;
		this.reservationRepository = reservationRepository;
		this.userRepository = userRepository;
		this.reservationdetailRepository = reservationdetailRepository;
	}

	/**
	 * 予約カートに追加
	 */
	@GetMapping("/user/reservation/{book_id}")
	public String addToReservationCart(@PathVariable Integer book_id,
			HttpSession session) {

		try {
			// 本取得
			Book book = bookRepository.findById(book_id)
					.orElseThrow(() -> new IllegalArgumentException("Book not found"));

			// セッションカート取得
			@SuppressWarnings("unchecked")
			List<Book> cart = (List<Book>) session.getAttribute(SESSION_RESERVATION_CART);

			if (cart == null) {
				cart = new ArrayList<>();
				session.setAttribute(SESSION_RESERVATION_CART, cart);
			}

			// 重複チェック
			boolean exists = cart.stream()
					.anyMatch(b -> b.getId().equals(book_id));

			if (exists) {
				session.setAttribute("error", "すでに予約カートに入っています");
				return "redirect:/user/books/" + book_id;
			}

			cart.add(book);

			session.setAttribute("message",
					"「" + book.getTitle() + "」を予約カートに追加しました");

		} catch (Exception e) {
			session.setAttribute("error", "エラーが発生しました");
			return "redirect:/user/search";
		}

		return "redirect:/user/books/" + book_id;
	}

	/**
	 * カート削除
	 */
	@PostMapping("/user/reservation/{book_id}/delete")
	public String deleteCart(@PathVariable Integer book_id,
			HttpSession session) {

		@SuppressWarnings("unchecked")
		List<Book> cart = (List<Book>) session.getAttribute(SESSION_RESERVATION_CART);

		if (cart != null) {
			cart.removeIf(b -> b.getId().equals(book_id));
			session.setAttribute("message", "カートから削除しました");
		}

		return "redirect:/user/reservations";
	}

	/**
	 * カート画面表示（前と同じView名）
	 */
	@GetMapping("/user/reservations")
	public String showCart(HttpSession session, Model model) {

		@SuppressWarnings("unchecked")
		List<Book> cart = (List<Book>) session.getAttribute(SESSION_RESERVATION_CART);

		if (cart == null) {
			cart = new ArrayList<>();
		}

		model.addAttribute("books", cart);

		if (cart.isEmpty()) {
			model.addAttribute("msg", "予約カートは空です");
		}

		return "myReservations"; // ← 前のまま
	}

	/**
	 * 予約確定
	 */
	@PostMapping("/user/reservations")
	public String confirmReservation(HttpSession session, Model model) {

		@SuppressWarnings("unchecked")
		List<Book> cart = (List<Book>) session.getAttribute(SESSION_RESERVATION_CART);

		if (cart == null || cart.isEmpty()) {
			return "redirect:/user/reservations";
		}

		try {
			User user = userRepository.findById(account.getId())
					.orElseThrow(() -> new IllegalArgumentException("User not found"));

			LocalDate today = LocalDate.now();

			Reservation reservation = new Reservation(user, today);
			reservationRepository.save(reservation);

			List<Reservationdetail> details = new ArrayList<>();

			for (Book book : cart) {

				// ★ここが重要：すでに予約されている本はスキップ or エラー
				boolean alreadyReserved = reservationdetailRepository
						.existsByBookId(book.getId());

				if (alreadyReserved) {
					model.addAttribute("books", cart);
					model.addAttribute("error", "カート内にすでに予約されている本があります");
					return "myReservations"; // ★redirectしない
				}

				details.add(new Reservationdetail(reservation, book, !book.isLoans()));
			}

			if (!details.isEmpty()) {
				reservationdetailRepository.saveAll(details);
			}

			session.removeAttribute(SESSION_RESERVATION_CART);

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("error", "予約処理に失敗しました");
			return "redirect:/user/reservations";
		}

		return "redirect:/user/reservations/confirmed";
	}

	/**
	 * 確定画面（前のView名に戻す）
	 */
	@GetMapping("/user/reservations/confirmed")
	public String confirmed(Model model) {

		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

		model.addAttribute("today", today.format(formatter));

		List<Reservation> list = reservationRepository.findByReservationDate(today);

		model.addAttribute("reservationsConfirmed", list);

		return "myReservationsConfirmed"; //
	}

	@GetMapping("/admin/reservations")
	public String showReservations(
			@RequestParam(name = "keyword", required = false) String keyword,
			Model model) {

		List<Reservation> reservationsList;

		// キーワードがあれば名前で絞り込み
		if (keyword != null && !keyword.isBlank()) {
			reservationsList = reservationRepository.findByUser_NameContaining(keyword.trim());
		} else {
			reservationsList = reservationRepository.findAll();
		}
		System.out.println(reservationsList.size());

		model.addAttribute("reservationsList", reservationsList);
		model.addAttribute("keyword", keyword);

		return "AdminReservationList";
	}

	@PostMapping("/admin/reservation/delete/{id}")
	public String cancel(
			@PathVariable Integer id,
			Model model) {

		reservationdetailRepository.deleteById(id);
		List<Reservationdetail> checkList = reservationdetailRepository.findByReservationId(id);
		if (checkList.isEmpty()) {
			reservationRepository.deleteById(id);
		}
		return "redirect:/admin/reservations";

	}
}