package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.entity.Rental;
import com.example.demo.entity.Rentaldetail;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.RentaldetailRepository;

@RequestMapping("/admin")
@Controller
public class LoanController {
	@Autowired
	private RentalRepository rentalRepository;

	@Autowired
	private RentaldetailRepository rentaldetailRepository;

	@Autowired
	private BookRepository bookRepository;

	/**
	 * 貸し出し本一覧表示
	 */
	@GetMapping("/rental")
	public String index(Model model) {
		List<Rental> rentalList = rentalRepository.findByReturnDateIsNull();
		model.addAttribute("rentalList", rentalList);
		return "Rentalshow";
	}

	/**
	 * 本の返却処理
	 */
	@Transactional  // ← 重要：これを追加
	@PostMapping("/rental/{rentalId}/return")
	public String returnRental(@PathVariable Integer rentalId) {
		System.out.println("返却処理開始：rentalId = " + rentalId);

		// 1. 貸出伝票を取得
		Rental rental = rentalRepository.findById(rentalId)
				.orElseThrow(() -> new IllegalArgumentException("Rental not found: " + rentalId));

		System.out.println("✅ 貸出伝票取得：" + rental.getId());

		// 2. すでに返却済みなら何もしない
		if (rental.getReturnDate() != null) {
			System.out.println("⚠️ すでに返却済み");
			return "redirect:/admin/rental";
		}

		// 3. 伝票内のすべての本を返却
		List<Rentaldetail> details = rental.getRentaldetail();
		System.out.println("📚 返却対象の本数：" + (details != null ? details.size() : 0));

		if (details != null && !details.isEmpty()) {
			for (Rentaldetail detail : details) {
				Book book = detail.getBook();
				System.out.println("  返却中：" + book.getTitle());

				// 貸出フラグを戻す
				book.setLoans(false);
				bookRepository.save(book);
			}
		}

		// 4. 貸出伝票の返却日を更新
		rental.setReturnDate(LocalDate.now());
		rentalRepository.save(rental);

		System.out.println("✅ 返却処理完了");

		return "redirect:/admin/rental";
	}
}