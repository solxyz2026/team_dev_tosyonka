package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.entity.Review;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;

@Controller
public class ReviewController {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@GetMapping("/user/{bookId}/review")
	public String showReviewPage(
			@PathVariable Integer bookId,
			Model model) {

		Optional<Book> optionalBook = bookRepository.findById(bookId);
		model.addAttribute("book", optionalBook);

		return "ReviewPage";
	}

	@PostMapping("/user/{bookId}/review")
	public String addReview() {
		return "BookDetail";
	}

	@GetMapping("/user/books/{book_id}/reviews")
	public String index(
			@PathVariable Integer bookId,
			Model model) {

		Book book = bookRepository.findById(bookId).orElse(null);
		model.addAttribute("book", book);

		List<Review> reviews = reviewRepository.findByBookId(bookId);
		model.addAttribute("reviews", reviews);

		return "BookDetail";
	}

	@PostMapping("/user/reviews")
	public String store(
			@RequestParam Integer bookId,
			@RequestParam String bookReview,
			Model model) {

		Book book = bookRepository.findById(bookId).orElse(null);

		if (bookReview.equals("") || bookReview == null) {
			return "redirect:/user/" + bookId + "/review";
		}

		Review review = new Review(book, bookReview);
		reviewRepository.save(review);

		return "redirect:/user/books/" + bookId;
	}

	@PostMapping("/admin/reviews/{review_id}/delete")
	public String delete(
			@PathVariable Integer review_id) {

		Review review = reviewRepository.findById(review_id).orElse(null);

		Integer bookId = review.getBook().getId();

		reviewRepository.delete(review);

		return "redirect:/admin/books/" + bookId;
	}

	//	↓ボツですが、コメント編集機能実装の機会があれば復活させます
	//	@GetMapping("/user/reviews/{review_id}/edit")
	//	public String editPage(
	//			@PathVariable Integer review_id,
	//			Model model) {
	//		Review review = reviewRepository.findById(review_id).orElse(null);
	//		model.addAttribute("review", review);
	//		return "ReviewEditPage";
	//	}

	@PostMapping("/user/reviews/{review_id}/edit")
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