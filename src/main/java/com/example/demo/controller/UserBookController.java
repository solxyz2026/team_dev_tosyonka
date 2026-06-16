package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.entity.Category;
import com.example.demo.model.Account;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;

@Controller
@RequestMapping("/user")
public class UserBookController {
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private Account account;

	@GetMapping("/search")
	public String searchGet(HttpSession session, Model model) {
		Integer userId = account.getId();
		String userName = account.getName();

		if (userId == null) {
			return "redirect:/user/login";
		}

		model.addAttribute("userName", userName);

		try {
			// すべての本を取得
			List<Book> books = bookRepository.findAll();

			//　ID の若い順にソート
			books.sort(Comparator.comparing(Book::getId));

			System.out.println("全本取得: " + books.size() + "冊");
			model.addAttribute("books", books);
		} catch (Exception e) {
			System.out.println("全本取得エラー: " + e.getMessage());
			model.addAttribute("books", new ArrayList<>());
		}

		try {
			List<Category> categories = categoryRepository.findAll();
			System.out.println("カテゴリ取得: " + categories.size() + "件");
			model.addAttribute("categories", categories);
		} catch (Exception e) {
			System.out.println("カテゴリ取得エラー: " + e.getMessage());
			model.addAttribute("categories", new ArrayList<>());
		}
		return "UserSearch";
	}

	@PostMapping("/search")
	public String search(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId,
			HttpSession session, Model model) {

		Integer userId = account.getId();
		String userName = account.getName();
		//セッションチェック
		if (userId == null) {
			return "redirect:/user/login";
		}
		model.addAttribute("userName", userName);

		if (keyword != null && !keyword.isEmpty()) {
			model.addAttribute("keyword", keyword);
		}
		if (categoryId != null) {
			model.addAttribute("categoryId", categoryId);
		}

		List<Book> books = new ArrayList<>();

		try {
			if (keyword != null && !keyword.isEmpty() && categoryId != null && categoryId > 0) {
				books = bookRepository.searchByKeywordAndCategory(keyword, categoryId);
			} else if (keyword != null && !keyword.trim().isEmpty()) {
				// キーワードのみで検索
				System.out.println("キーワードのみで検索: " + keyword);
				books = bookRepository.searchByTitleOrWriter(keyword);
				System.out.println("📚 検索結果: " + books.size() + "冊");

			} else if ((keyword == null || keyword.isEmpty()) && categoryId > 0) {
				// カテゴリのみで検索
				System.out.println("カテゴリのみで検索: カテゴリID=" + categoryId);
				books = bookRepository.findByCategoryId(categoryId);
				System.out.println("検索結果: " + books.size() + "冊");

			} else {
				// 検索条件がない場合は全件取得
				System.out.println("検索条件がない → すべての本を表示");
				books = bookRepository.findAll();
				System.out.println("全本取得: " + books.size() + "冊");
			}
		} catch (Exception e) {
			System.out.println("検索エラー: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "検索中にエラーが発生しました");
		}
		books.sort(Comparator.comparing(Book::getId));
		model.addAttribute("books", books);
		try {
			List<Category> categories = categoryRepository.findAll();
			model.addAttribute("categories", categories);
		} catch (Exception e) {
			model.addAttribute("categories", new ArrayList<>());

		}

		return "UserSearch";
	}
	@GetMapping("/book/{book_id}")
	public String show(
			@PathVariable Integer book_id,
			HttpSession session, Model model) {
		
	
		// セッションチェック
		Integer userId = account.getId();
		String userName = account.getName();
 
		if (userId == null) {
			System.out.println("セッションなし → ログイン画面へリダイレクト");
			return "redirect:/user/login";
		}
 
		
		model.addAttribute("userName", userName);
 
		try {
			Book book = bookRepository.findById(book_id).orElse(null);
			
			if (book == null) {
				System.out.println("本が見つかりません: book_id=" + book_id);
				model.addAttribute("error", "本が見つかりませんでした。");
				return "UserSearch";
			}
			
			
			model.addAttribute("book", book);
			
		} catch (Exception e) {
			System.out.println("本詳細取得エラー: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "本の詳細情報取得中にエラーが発生しました。");
		}
 
		
		return "BookDetail";
	}
}