package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Rental;
import com.example.demo.entity.Rentaldetail;
import com.example.demo.entity.Reservationdetail;
import com.example.demo.entity.User;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.RentaldetailRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.ReservationdetailRepository;
import com.example.demo.repository.UserRepository;

@RequestMapping("/admin")
@Controller
public class AdminMenuController {

	private final AnnouncementRepository announcementRepository;

	private final Rental rental;

	private final ReservationController reservationController;

	private final Reservationdetail reservationdetail;

	@Autowired
	private RentalRepository rentalRepository;

	@Autowired
	private RentaldetailRepository rentaldetailRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ReservationdetailRepository reservationdetailRepository;

	private static final String SESSION_RENTAL_CART = "rentalCart";
	private static final String SESSION_MESSAGE = "message";
	private static final String SESSION_ERROR = "error";
	private static final String SESSION_RESERVED = "reserved";

	AdminMenuController(Reservationdetail reservationdetail, ReservationController reservationController, Rental rental,
			AnnouncementRepository announcementRepository) {
		this.reservationdetail = reservationdetail;
		this.reservationController = reservationController;
		this.rental = rental;
		this.announcementRepository = announcementRepository;
	}

	/**
	 * GET /admin/ - 管理者ホーム画面（貸し出し画面）
	 */
	@GetMapping("/")
	public String index(HttpSession session, Model model) {
		System.out.println("\n========== 管理者ホーム ==========");

		// 1. セッションのカートを取得
		@SuppressWarnings("unchecked")
		List<CartItem> rentalCart = (List<CartItem>) session.getAttribute(SESSION_RENTAL_CART);
		if (rentalCart == null) {
			rentalCart = new ArrayList<>();
			session.setAttribute(SESSION_RENTAL_CART, rentalCart);
		}

		model.addAttribute("rentalCart", rentalCart);
		model.addAttribute("cartCount", rentalCart.size());

		// 2. メッセージを取得して表示
		if (session.getAttribute(SESSION_MESSAGE) != null) {
			model.addAttribute("message", session.getAttribute(SESSION_MESSAGE));
			session.removeAttribute(SESSION_MESSAGE);
		}
		if (session.getAttribute(SESSION_ERROR) != null) {
			model.addAttribute("error", session.getAttribute(SESSION_ERROR));
			session.removeAttribute(SESSION_ERROR);
		}
		if (session.getAttribute(SESSION_RESERVED) != null) {
			model.addAttribute("reserved", session.getAttribute(SESSION_RESERVED));
			session.removeAttribute(SESSION_RESERVED);
		}

		// ★★★ 【新規追加】直近の貸し出し情報を取得 ★★★
		Rental latestRental = rentalRepository.findFirstByOrderByIdDesc();
		if (latestRental != null) {
			System.out.println("✅ 直近の貸出: Rental ID = " + latestRental.getId());
			System.out.println("   ユーザー: " + latestRental.getUser().getName());
			System.out.println("   貸出日: " + latestRental.getRentalDate());

			// 関連する Rentaldetail を取得
			List<Rentaldetail> rentalDetails = rentaldetailRepository.findByRentalId(latestRental.getId());
			System.out.println("   本数: " + rentalDetails.size() + "冊");

			// Model に追加
			model.addAttribute("latestRental", latestRental);
			model.addAttribute("latestRentalDetails", rentalDetails);
		}
		// ★★★ ここまで ★★★

		System.out.println("========== ホーム画面表示 ==========\n");

		return "AdminHome";
	}

	/**
	 * POST /admin/rental/add-to-cart - カートに本を追加
	 */
	@PostMapping("/rental/add-to-cart")
	public String addToCart(@RequestParam Integer bookId, HttpSession session) {
		System.out.println("\n========== カート追加処理 ==========");
		System.out.println("bookId: " + bookId);

		try {
			// 1. 本を取得
			Book book = bookRepository.findById(bookId).orElse(null);

			if (book == null) {
				System.out.println("❌ 本が見つかりません");
				session.setAttribute(SESSION_ERROR, "本が見つかりません（ID: " + bookId + "）");
				return "redirect:/admin/";
			}

			System.out.println("✅ 本を取得: " + book.getTitle());

			// 2. セッションのカートを取得
			@SuppressWarnings("unchecked")
			List<CartItem> rentalCart = (List<CartItem>) session.getAttribute(SESSION_RENTAL_CART);
			if (rentalCart == null) {
				rentalCart = new ArrayList<>();
				session.setAttribute(SESSION_RENTAL_CART, rentalCart);
			}

			// 3. 重複チェック（同じ本がカート内にあるか）
			boolean exists = rentalCart.stream()
					.anyMatch(item -> item.getBookId().equals(bookId));

			if (exists) {
				System.out.println("⚠️ この本は既にカートに入っています");
				session.setAttribute(SESSION_ERROR, "この本は既にカートに入っています");
				return "redirect:/admin/";
			}

			// ★★★ 【方法1】loans フラグをチェック ★★★
			if (book.isLoans()) {
				System.out.println("⚠️ この本は現在貸し出し中です");
				session.setAttribute(SESSION_ERROR, "「" + book.getTitle() + "」は現在貸し出し中です");
				return "redirect:/admin/";
			}
			// ★★★ ここまで ★★★
			Reservationdetail reservation = reservationdetailRepository
					.findByBookIdAndDeleteJudgeFalse(bookId)
					.orElse(null);

			if (reservation != null) {
				User reservedUser = reservation.getReservation().getUser();
				System.out.println("📌 予約あり: ID=" + reservedUser.getId() + " " + reservedUser.getName());
				session.setAttribute(SESSION_RESERVED,
						"⚠️「" + book.getTitle() + "」は ID=" + reservedUser.getId()
								+ " の " + reservedUser.getName() + " さんが予約しています");
			}
			// 4. CartItem を作成
			CartItem item = new CartItem();
			item.setBookId(book.getId());
			item.setTitle(book.getTitle());
			item.setAuthor(book.getWriter().getWriterName());
			item.setCategory(book.getCategory().getCategoryName());
			item.setTag("新着");

			// 5. カートに追加
			rentalCart.add(item);

			System.out.println("✅ カートに追加: " + book.getTitle());
			System.out.println("   カート内の冊数: " + rentalCart.size());
			session.setAttribute(SESSION_MESSAGE, "「" + book.getTitle() + "」をカートに追加しました");

		} catch (Exception e) {
			System.out.println("❌ エラー: " + e.getMessage());
			e.printStackTrace();
			session.setAttribute(SESSION_ERROR, "エラーが発生しました");
		}

		System.out.println("========== 処理完了 ==========\n");

		return "redirect:/admin/";
	}

	/**
	 * POST /admin/rental/remove-from-cart - カートから削除
	 */
	@PostMapping("/rental/remove-from-cart")
	public String removeFromCart(@RequestParam Integer bookId, HttpSession session) {
		System.out.println("\n========== カート削除処理 ==========");
		System.out.println("bookId: " + bookId);

		try {
			// 1. セッションのカートを取得
			@SuppressWarnings("unchecked")
			List<CartItem> rentalCart = (List<CartItem>) session.getAttribute(SESSION_RENTAL_CART);

			if (rentalCart == null || rentalCart.isEmpty()) {
				System.out.println("⚠️ カートが空です");
				session.setAttribute(SESSION_ERROR, "カートが空です");
				return "redirect:/admin/";
			}

			// 2. 削除対象を検索
			CartItem targetItem = rentalCart.stream()
					.filter(item -> item.getBookId().equals(bookId))
					.findFirst()
					.orElse(null);

			if (targetItem == null) {
				System.out.println("⚠️ 指定された本がカートに入っていません");
				session.setAttribute(SESSION_ERROR, "指定された本がカートに入っていません");
				return "redirect:/admin/";
			}

			// 3. 削除
			rentalCart.remove(targetItem);

			System.out.println("✅ カートから削除: " + targetItem.getTitle());
			System.out.println("   カート内の冊数: " + rentalCart.size());
			session.setAttribute(SESSION_MESSAGE, "「" + targetItem.getTitle() + "」を削除しました");

		} catch (Exception e) {
			System.out.println("❌ エラー: " + e.getMessage());
			e.printStackTrace();
			session.setAttribute(SESSION_ERROR, "エラーが発生しました");
		}

		System.out.println("========== 処理完了 ==========\n");

		return "redirect:/admin/";
	}

	/**
	 * POST /admin/rental/create - 貸し出し確定
	 */
	@Transactional
	@PostMapping("/rental/create")
	public String createRental(@RequestParam Integer userId,
			HttpSession session) {

		System.out.println("\n========== 貸し出し確定処理 ==========");

		try {

			// =========================
			// カート取得
			// =========================
			@SuppressWarnings("unchecked")
			List<CartItem> rentalCart = (List<CartItem>) session.getAttribute(SESSION_RENTAL_CART);

			if (rentalCart == null || rentalCart.isEmpty()) {
				session.setAttribute(SESSION_ERROR, "カートに本が入っていません");
				return "redirect:/admin/";
			}

			// =========================
			// ユーザー取得
			// =========================
			User user = userRepository.findById(userId).orElse(null);

			if (user == null) {
				session.setAttribute(SESSION_ERROR, "ユーザーが見つかりません");
				return "redirect:/admin/";
			}

			System.out.println("\n==========処理チェック1 ==========");

			// =========================================================
			// ★★★ 予約チェック（新設計：delete_judge + reservation_status）★★★
			// =========================================================
			for (CartItem item : rentalCart) {

				Optional<Reservationdetail> optReservation = reservationdetailRepository
						.findByBookIdAndDeleteJudgeFalse(
								item.getBookId());

				if (optReservation.isPresent()) {

					Reservationdetail rd = optReservation.get();

					Integer reservedUserId = rd.getReservation().getUser().getId();

					if (!reservedUserId.equals(userId)) {

						session.setAttribute(
								SESSION_ERROR,
								"予約の入っている本は貸し出しできません");

						return "redirect:/admin/";
					}
				}
			}
			final int MAX_RENTAL = 4;
			List<Rental> currentRentals = rentalRepository.findByUserIdAndReturnDateIsNull(userId);
			int currentCount = 0;

			for (Rental r : currentRentals) {
			    if (r.getRentaldetail() != null) {
			       
			        long unreturnedCount = r.getRentaldetail().stream()
			            .filter(detail -> detail.getReturnDate() == null)
			            .count();
			        currentCount += (int) unreturnedCount;
			    }
			}

			// =========================
			// 貸出伝票作成
			// =========================
				 Rental rental = new Rental();
			     rental.setUser(user);
			     rental.setRentalDate(LocalDate.now());
			     rental.setDropDate(LocalDate.now().plusDays(14));
			     rentalRepository.save(rental); // 1冊ごとに伝票が発行される
			int successCount = 0;

			System.out.println("\n========== 処理チェック2 ==========");

			// =========================
			// 貸出処理
			// =========================
			for (CartItem item : rentalCart) {

				Book book = bookRepository.findById(item.getBookId()).orElse(null);

				if (book == null)
					continue;

			

			        // 貸出明細作成（この rental に紐づく）
			        Rentaldetail detail = new Rentaldetail();
			        detail.setRental(rental);
			        detail.setBook(book);
			        rentaldetailRepository.save(detail);


				// 貸出フラグ更新
				book.setLoans(true);
				bookRepository.save(book);

				// =========================================================
				// ★★★ 予約消化処理（追加：delete_judge = true）★★★
				// =========================================================
				Optional<Reservationdetail> optReservation = reservationdetailRepository
						.findByBookIdAndDeleteJudgeFalse(
								book.getId());

				System.out.println("\n========== 処理チェック3 ==========");
				optReservation.ifPresent(reservationDetail -> {

					Integer reservedUserId = reservationDetail.getReservation().getUser().getId();

					// 予約者本人の貸出なら予約を消化
					if (reservedUserId.equals(userId)) {
						System.out.println("処理済みに変更");

						reservationDetail.setDeleteJudge(true); // ★追加：処理済みに変更
						reservationdetailRepository.save(reservationDetail);
					}
				});

				successCount++;
			}

			// =========================
			// カートクリア
			// =========================
			rentalCart.clear();

			session.setAttribute(
					SESSION_MESSAGE,
					successCount + "冊の貸し出しが完了しました");

		} catch (RuntimeException e) {

			session.setAttribute(SESSION_ERROR, e.getMessage());
			return "redirect:/admin/";

		} catch (Exception e) {

			e.printStackTrace();
			session.setAttribute(SESSION_ERROR, "貸し出し処理に失敗しました");
			return "redirect:/admin/";
		}

		return "redirect:/admin/";
	}

	/**
	 * POST /admin/rental/detail/{rentaldetailId}/return - 本を1冊ずつ返却
	 * 
	 * 🔑 このメソッドが新しい本ごと返却の実装
	 */
	@Transactional
	@PostMapping("/rental/detail/{rentaldetailId}/return")
	public String returnRentalDetail(
			@PathVariable Integer rentaldetailId,
			HttpSession session) {
		
		System.out.println("\n========== 本ごとの返却処理開始 ==========");
		System.out.println("rentaldetailId: " + rentaldetailId);
		
		try {
			// 1. Rentaldetail を取得
			Rentaldetail detail = rentaldetailRepository.findById(rentaldetailId)
					.orElseThrow(() -> new IllegalArgumentException("Rentaldetail not found: " + rentaldetailId));
			
			System.out.println("✅ 貸出詳細取得: ID = " + detail.getId());
			System.out.println("   本: " + detail.getBook().getTitle());
			
			// 2. すでに返却済みなら何もしない
			if (detail.getReturnDate() != null) {
				System.out.println("⚠️ この本は既に返却済み");
				session.setAttribute(SESSION_ERROR, "この本は既に返却済みです");
				return "redirect:/admin/rental";
			}
			
			// 3. 本の返却処理
			Book book = detail.getBook();
			
			// 3-1. 本ごとの返却日を設定 ← ここが重要！
			detail.setReturnDate(LocalDate.now());
			rentaldetailRepository.save(detail);
			System.out.println("  📝 返却日を記録: " + LocalDate.now());
			
			// 3-2. 貸出フラグを戻す
			book.setLoans(false);
			bookRepository.save(book);
			System.out.println("  ✅ 貸出フラグを解除");
			
			// 3-3. この本に有効な予約があれば「貸し出し可」に変更
			Reservationdetail reservation = reservationdetailRepository
					.findByBookIdAndDeleteJudgeFalse(book.getId())
					.orElse(null);
			
			if (reservation != null) {
				reservation.setReservationStatus(true);
				reservationdetailRepository.save(reservation);
				System.out.println("  📌 予約を「貸し出し可」に更新");
			}
			
			System.out.println("✅ 返却処理完了: " + book.getTitle());
			
			session.setAttribute(SESSION_MESSAGE,
					"「" + book.getTitle() + "」の返却が完了しました");
			
		} catch (Exception e) {
			System.out.println("❌ エラー: " + e.getMessage());
			e.printStackTrace();
			session.setAttribute(SESSION_ERROR, "返却処理に失敗しました");
		}
		
		System.out.println("========== 処理完了 ==========\n");
		
		return "redirect:/admin/rental";
	}
}