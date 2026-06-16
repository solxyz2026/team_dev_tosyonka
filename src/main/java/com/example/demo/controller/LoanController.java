/*編集、作成　原田大瑚　6月15日　13時
 * 
 * */

package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.entity.Rental;
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
		List<Rental> rentalList = rentalRepository.findAll();
		model.addAttribute("rentalList", rentalList);
		return "Rentalshow";
	}

	//返却処理
	@PostMapping("/rental/{bookId}/return")
	public String returnBook(
			@PathVariable Integer bookId,
			Model model) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		if (optionalBook.isPresent()) {
			Book book = optionalBook.get();
			book.setLoans(false);
			bookRepository.save(book);
		}

		return "redirect:/admin/rental";
	}

}
