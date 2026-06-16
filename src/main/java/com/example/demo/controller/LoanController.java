/*編集、作成　原田大瑚　6月15日　13時
 * 
 * */

package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.entity.Rental;
import com.example.demo.entity.Rentaldetail;
import com.example.demo.entity.Writer;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.RentaldetailRepository;

@Controller
@RequestMapping("/admin")
public class LoanController {

	private final Writer writer;

	private final RentaldetailRepository rentaldetailRepository;
	private final RentalRepository rentalRepository;
	private final BookRepository bookRepository;

	public LoanController(RentaldetailRepository rentaldetailRepository, RentalRepository rentalRepository,
			BookRepository bookRepository, Writer writer) {
		super();
		this.rentaldetailRepository = rentaldetailRepository;
		this.rentalRepository = rentalRepository;
		this.bookRepository = bookRepository;
		this.writer = writer;
	}

	//貸し出し本一覧表示
	@GetMapping("/rental")
	public String index(Model model) {
		List<Rental> rentalList = rentalRepository.findByReturnDateIsNull();
		model.addAttribute("rentalList", rentalList);
		return "Rentalshow";
	}

	@PostMapping("/rental/{rentalId}/return")
	public String returnRental(@PathVariable Integer rentalId) {

		Rental rental = rentalRepository.findById(rentalId)
				.orElseThrow(() -> new IllegalArgumentException("Rental not found"));

		// すでに返却済みなら何もしない
		if (rental.getReturnDate() != null) {
			return "redirect:/admin/rental";
		}

		// ① 伝票内のすべての本を返却
		for (Rentaldetail detail : rental.getRentaldetail()) {

			Book book = detail.getBook();

			// 貸出フラグを戻す
			book.setLoans(false);

			bookRepository.save(book);
		}

		// ② 伝票の返却日を更新
		rental.setReturnDate(LocalDate.now());

		rentalRepository.save(rental);

		return "redirect:/admin/rental";
	}
}
