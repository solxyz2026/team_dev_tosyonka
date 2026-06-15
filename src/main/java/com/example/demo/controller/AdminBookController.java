package com.example.demo.controller;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
=======
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
>>>>>>> branch 'main' of https://github.com/solxyz2026/team_dev_tosyonka.git

@Controller
@RequestMapping("/admin")
public class AdminBookController {

<<<<<<< HEAD
	@GetMapping("/books/add")
	public String addForm() {
		return "AdminBooksAdd";
	}

	@PostMapping("/books")
	public String store(
			@RequestParam(defaultValue = "") String title,
			@RequestParam(defaultValue = "") String writer_id,
			@RequestParam(defaultValue = "") String publisher,
			@RequestParam(defaultValue = "") String summary,
			Model model) {

		List<String> errors = new ArrayList<>();

		if (title.equals("")) {
			errors.add("タイトルが未入力です");
		}
		if (writer_id.equals("")) {
			errors.add("著者名が未入力です");
		}
		if (writer_id.equals("")) {
			errors.add("出版社名が未入力です");
		}
		if (publisher.equals("")) {
			errors.add("出版月が未入力です");
		}
		if (summary.equals("")) {
			errors.add("本の要約文が未入力です");
			if (errors.size() > 0) {
				model.addAttribute("errors", errors);
				return "AdminBooksAdd";
			}
		}
		return "redirect:/admin/books/add";
	}
=======
	private final Book book;
	private final BookRepository bookrepository;

	public AdminBookController(Book book, BookRepository bookrepository) {
		this.book = book;
		this.bookrepository = bookrepository;
	}

>>>>>>> branch 'main' of https://github.com/solxyz2026/team_dev_tosyonka.git
}
