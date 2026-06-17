package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;

@Controller
@RequestMapping("/user")
public class ReviewController {

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("{bookId}/review")
	public String showReviewPage(
			@PathVariable Integer bookId,
			Model model) {

		Optional<Book> optionalBook = bookRepository.findById(bookId);
		model.addAttribute("book", optionalBook);
		return "ReviewPage";
	}

	@PostMapping("/{bookId}/review")
	public String addReview() {
		return "BookDetail";

	}

}
