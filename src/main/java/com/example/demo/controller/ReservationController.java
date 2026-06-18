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
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Reservationdetail;
import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.ReservationdetailRepository;
import com.example.demo.repository.UserRepository;

//大森
@Controller
@RequestMapping("/user")
public class ReservationController {

	private final Account account;
	private final BookRepository bookRepository;
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;
	private final ReservationdetailRepository reservationdetailRepository;

	private static final String SESSION_RESERVATION_CART = "reservationCart";

	public ReservationController(Account account, BookRepository bookRepository,
			ReservationRepository reservationRepository, UserRepository userRepository,
			ReservationdetailRepository reservationdetailRepository) {
		this.account = account;
		this.bookRepository = bookRepository;
		this.reservationRepository = reservationRepository;
		this.userRepository = userRepository;
		this.reservationdetailRepository = reservationdetailRepository;
	}

	@GetMapping("/reservation/{book_id}")
	public String addToReservationCart(
			@PathVariable Integer book_id,
			HttpSession session,
			Model model) {
		try {
			// 1. 本を取得
			Book book = bookRepository.findById(book_id)
					.orElseThrow(() -> new IllegalArgumentException("Book not found: " + book_id));

			System.out.println("✅ 本を取得: " + book.getTitle());

			// 2. セッションから予約カートを取得
			@SuppressWarnings("unchecked")
			List<Book> reservationCart = 
				(List<Book>) session.getAttribute(SESSION_RESERVATION_CART);

			if (reservationCart == null) {
				reservationCart = new ArrayList<>();
				session.setAttribute(SESSION_RESERVATION_CART, reservationCart);
			}

			// 3. 重複チェック
			boolean exists = reservationCart.stream()
					.anyMatch(b -> b.getId().equals(book_id));

			if (exists) {
				
				session.setAttribute("error", "この本は既に予約カートに入っています");
				return "redirect:/user/books/" + book_id;
			}

			// 4. 予約カートに追加
			reservationCart.add(book);

			session.setAttribute("message", "「" + book.getTitle() + "」を予約カートに追加しました");

		} catch (IllegalArgumentException e) {
			session.setAttribute("error", "本が見つかりません");
			return "redirect:/user/search";
		} catch (Exception e) {
			return "redirect:/user/search";
		}
		return "redirect:/user/books/" + book_id;
	}

	//カートから削除
	 
	@PostMapping("/reservation/{book_id}/delete")
	public String deleteCart(
			@PathVariable Integer book_id,
			HttpSession session) {
		@SuppressWarnings("unchecked")
		List<Book> reservationCart = 
			(List<Book>) session.getAttribute(SESSION_RESERVATION_CART);

		if (reservationCart != null) {
			boolean removed = reservationCart.removeIf(b -> b.getId().equals(book_id));
			if (removed) {
				session.setAttribute("message", "カートから削除しました");
			}
		}
		return "redirect:/user/reservations";
	}

	//GET /user/reservations - 予約カート画面の表示
	 
	@GetMapping("/reservations")
	public String store(HttpSession session, Model model) {

		// セッションからカートを取得
		@SuppressWarnings("unchecked")
		List<Book> books = (List<Book>) session.getAttribute(SESSION_RESERVATION_CART);

		if (books == null) {
			books = new ArrayList<>();
		}

		model.addAttribute("books", books);

		if (books.isEmpty()) {
			
			model.addAttribute("msg", "・予約カートは空です。");
		} else {
		
		}
		return "myReservations";
	}

	/**
	 * POST /user/reservations - 予約カートの中身を確定させる
	 */
	@PostMapping("/reservations")
	public String myReservations(HttpSession session, Model model) {

	
		// セッションからカートを取得
		@SuppressWarnings("unchecked")
		List<Book> books = (List<Book>) session.getAttribute(SESSION_RESERVATION_CART);

		if (books == null || books.isEmpty()) {
			
			return "redirect:/user/reservations";
		}

		try {
			LocalDate today = LocalDate.now();
			User user = userRepository.findById(account.getId())
					.orElseThrow(() -> new IllegalArgumentException("User not found"));
			// Reservation に本以外の情報を登録
			Reservation reservation = new Reservation(user, today);
			reservationRepository.save(reservation);


			// 確定したidを利用してカートの中身をReservationdetailに登録
			List<Reservationdetail> details = new ArrayList<>();
			for (Book book : books) {
				// reservation_status: true = 予約可能, false = 貸出中
				details.add(new Reservationdetail(reservation, book, !book.isLoans()));
				
			}
			reservationdetailRepository.saveAll(details);
			// セッションのカートをクリア
			session.removeAttribute(SESSION_RESERVATION_CART);

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("error", "予約確定中にエラーが発生しました");
			return "redirect:/user/reservations";
		}

		return "redirect:/user/reservations/confirmed";
	}

	//予約確定後の詳細ページを表示
	
	@GetMapping("/reservations/confirmed")
	public String myReservationsConfirmed(Model model) {

		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
		model.addAttribute("today", today.format(formatter));

		// 確定した予約詳細を取得
		List<Reservation> reservationsConfirmed = reservationRepository.findByReservationDate(today);

		model.addAttribute("reservationsConfirmed", reservationsConfirmed);
		return "myReservationsConfirmed";
	}
}