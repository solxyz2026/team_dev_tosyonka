package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;

@Controller
@RequestMapping("/admin")
public class AdminBookController {

	private final Book book;
	private final BookRepository bookrepository;

	public AdminBookController(Book book, BookRepository bookrepository) {
		this.book = book;
		this.bookrepository = bookrepository;
	}

}
