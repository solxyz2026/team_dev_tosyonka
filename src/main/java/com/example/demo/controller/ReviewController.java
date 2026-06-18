package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.entity.Review;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;

@Controller
@RequestMapping("/user")
public class ReviewController {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ReviewRepository reviewRepository;

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

	@GetMapping("/books/{book_id}/reviews")
	public String index(
			@PathVariable Integer bookId,
			Model model) {

		Book book = bookRepository.findById(bookId).orElse(null);
		model.addAttribute("book", book);

		List<Review> reviews = reviewRepository.findByBookId(bookId);
		model.addAttribute("reviews", reviews);

		return "BookDetail";
	}

	@PostMapping("/reviews")
	public String store(
			@RequestParam Integer bookId,
			@RequestParam String bookReview,
			Model model) {

		System.out.println("AAA");

		Book book = bookRepository.findById(bookId).orElse(null);

		Review review = new Review(book, bookReview);
		reviewRepository.save(review);

		return "redirect:/user/books/" + bookId;
	}

	@PostMapping("reviews/{review_id}/delete")
	public String delete(
			@PathVariable Integer review_id) {

		Review review = reviewRepository.findById(review_id).orElse(null);

		Integer bookId = review.getBook().getId();

		reviewRepository.delete(review);

		return "redirect:/user/books/" + bookId;
	}

	@GetMapping("reviews/{review_id}/edit")
	public String editPage(
			@PathVariable Integer review_id,
			Model model) {
		Review review = reviewRepository.findById(review_id).orElse(null);
		model.addAttribute("review", review);
		return "ReviewEditPage";
	}

	@PostMapping("reviews/{review_id}/edit")
	public String edit(
			@PathVariable Integer review_id,
			@RequestParam String bookReview) {

		Review review = reviewRepository.findById(review_id).orElse(null);
		review.setBookReview(bookReview);

		reviewRepository.save(review);

		Integer bookId = review.getBook().getId();

		return "redirect:/user/books/" + bookId;
	}

}