package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
//作成　北嶋
@Controller
public class UserBookController {
	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/user/search")
	public String search(@RequestParam(value = "keyword", required = false) String keyword,
			HttpSession session, Model model) {
		Long userId = (Long) session.getAttribute("userId");
		
		if (userId == null) {
			return "redirect:/login";
		}
		
		String userName = (String) session.getAttribute("userName");
		model.addAttribute("userName", userName);

		List<Book> books;
		if (keyword != null && !keyword.trim().isEmpty()) {
			// タイトルと著者名 両方で検索
			books = bookRepository.searchByTitleOrWriter(keyword);
			System.out.println("検索: " + keyword);
			System.out.println("検索結果: " + books.size() + "冊");
			model.addAttribute("keyword", keyword);
		} else {
			// すべての本を取得
			books = bookRepository.findAll();
			System.out.println("全本取得: " + books.size() + "冊");
		}

		model.addAttribute("books", books);
		return "user/book/list";
	}
}