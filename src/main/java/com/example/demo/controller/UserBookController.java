package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;

@Controller
public class UserBookController {
	@Autowired
	private BookRepository bookRepository;

	
	@GetMapping("user/search")
	public String searchGet(HttpSession session, Model model) {
	    Integer userId = (Integer) session.getAttribute("userId");
	    String userName = (String) session.getAttribute("userName");

	    if (userId == null) {
	        return "redirect:/user/login";
	    }

	    model.addAttribute("userName", userName);

	    try {
	        // ✅ すべての本を取得
	        List<Book> books = bookRepository.findAll();
	        
	        // ✅ ID の若い順にソート
	        books.sort(Comparator.comparing(Book::getId));
	        
	        System.out.println("📚 全本取得: " + books.size() + "冊");
	        model.addAttribute("books", books);
	    } catch (Exception e) {
	        System.out.println("❌ 全本取得エラー: " + e.getMessage());
	        model.addAttribute("books", new ArrayList<>());
	    }

	    return "UserSearch";
	}

	
	@PostMapping("/user/search")
	public String search(@RequestParam(value = "keyword", required = false) String keyword,
			HttpSession session, Model model) {
		
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/user/login";  // 
		}

		String userName = (String) session.getAttribute("userName");
		model.addAttribute("userName", userName);

		List<Book> books;
		if (keyword != null && !keyword.trim().isEmpty()) {
			
			books = bookRepository.searchByTitleOrWriter(keyword);
			System.out.println("検索: " + keyword);
			System.out.println("検索結果: " + books.size() + "冊");
			model.addAttribute("keyword", keyword);
		} else {
	
			books = bookRepository.findAll();
			System.out.println("全本取得: " + books.size() + "冊");
		}

		model.addAttribute("books", books);
		return "UserSearch";
	}
}