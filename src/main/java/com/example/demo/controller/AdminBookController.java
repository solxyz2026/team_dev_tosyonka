package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
import com.example.demo.entity.Reservationdetail;
import com.example.demo.entity.Review;
import com.example.demo.entity.Writer;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.WriterRepository;

@Controller
@RequestMapping("/admin")
public class AdminBookController {

	private final Reservationdetail reservationdetail;

	private final UserAccountController userAccountController;

	private final AdminAccountController adminAccountController;
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private WriterRepository writerRepository;

	@Autowired
	private Writer writer;

	@Autowired
	private ReviewRepository reviewRepository;

	AdminBookController(AdminAccountController adminAccountController, UserAccountController userAccountController,
			Reservationdetail reservationdetail) {
		this.adminAccountController = adminAccountController;
		this.userAccountController = userAccountController;
		this.reservationdetail = reservationdetail;
	}

	@GetMapping("/search")
	public String index(HttpSession session, Model model) {

		try {
			// すべての本を取得
			List<Book> books = bookRepository.findByDeleteJudgeFalse();

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
		return "AdminbookSearch";
	}

	@PostMapping("/search")
	public String search(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "categoryId", required = false, defaultValue = "0") Integer categoryId,
			HttpSession session, Model model) {

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
				books = bookRepository.findByCategoryIdAndDeleteJudgeFalse(categoryId);
				System.out.println("検索結果: " + books.size() + "冊");

			} else {
				// 検索条件がない場合は全件取得
				System.out.println("検索条件がない → すべての本を表示");
				books = bookRepository.findByDeleteJudgeFalse();
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

		return "AdminbookSearch";
	}

	//本登録画面表示
	@GetMapping("/books/add")
	public String addForm(Model model) {
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categoryList", categoryList);
		return "AdminBooksAdd";
	}

	//本登録処理
	@PostMapping("/books")
	public String store(
			@RequestParam(defaultValue = "") String title,
			@RequestParam(defaultValue = "") String writer,
			@RequestParam(defaultValue = "") String publisher,
			@RequestParam(defaultValue = "") String summary,
			@RequestParam(defaultValue = "") String categoryName,
			@RequestParam(defaultValue = "") LocalDate date,
			Model model) {

		Optional<Writer> optionalWriter = writerRepository.findByWriterNameAndDeleteJudgeFalse(writer);
		if (optionalWriter.isEmpty()) {
			if (title.equals("")) {
				model.addAttribute("errorTitle", "タイトルが入力されていません");
			}

			if (writer.equals("")) {
				model.addAttribute("errorWriter", "著者が入力されていません");
			}

			else if (optionalWriter.isEmpty()) {
				model.addAttribute("errorWriter", "入力された著者は登録されていません");
			}

			if (publisher.equals("")) {
				model.addAttribute("errorPublisher", "出版社が入力されていません");
			}

			if (summary.equals("")) {
				model.addAttribute("errorSummary", "本の要約文が入力されていません");
			}

			if (categoryName.equals("")) {
				model.addAttribute("errorCategoryName", "カテゴリーが入力されていません");
			}

			if (date == null) {
				model.addAttribute("errorDate", "日付が正しく入力されていません");
			}

			if (title.equals("") || writer.equals("") || summary.equals("") ||
					optionalWriter.isEmpty() || publisher.equals("") || categoryName.equals("") || date == null) {
				model.addAttribute("title", title);
				model.addAttribute("writer", writer);
				model.addAttribute("publisher", publisher);
				model.addAttribute("summary", summary);
				model.addAttribute("categoryName", categoryName);
				model.addAttribute("date", date);
				List<Category> categoryList = categoryRepository.findAll();
				model.addAttribute("categoryList", categoryList);
				return "AdminBooksAdd";
			}

			List<Category> categoryList = categoryRepository.findAll();
			model.addAttribute("categoryList", categoryList);

			return "AdminBooksAdd";
		}
		Optional<Category> optionalCategoryName = categoryRepository.findByCategoryName(categoryName);

		Book book = new Book();
		book.setTitle(title);
		book.setPublisher(publisher);
		book.setSummary(summary);
		book.setWriter(optionalWriter.get());
		book.setCategory(optionalCategoryName.get());
		book.setDate(date);
		book.setLoans(false);

		bookRepository.save(book);

		return "AdminHome";
	}

	@PostMapping("/{book_id}/delete")
	public String delete(
			@PathVariable Integer book_id,
			Model model) {

		try {
			Book book = bookRepository.findById(book_id).orElse(null);
			book.setDeleteJudge(true);
			bookRepository.save(book);

		} catch (Exception e) {
			System.out.println("本詳細取得エラー: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "本の詳細情報取得中にエラーが発生しました。");
		}

		return "redirect:/admin/search";
	}

	@GetMapping("books/{book_id}")
	public String show(
			@PathVariable Integer book_id,
			HttpSession session, Model model) {

		try {
			Book book = bookRepository.findById(book_id).orElse(null);

			if (book == null) {
				System.out.println("本が見つかりません: book_id=" + book_id);
				model.addAttribute("error", "本が見つかりませんでした。");
				return "AdminBookSearch";
			}

			//↓山本変更箇所、レビュー機能追加
			List<Review> reviews = reviewRepository.findByBookId(book_id);

			model.addAttribute("book", book);
			//↓山本変更箇所、レビュー機能追加
			model.addAttribute("reviews", reviews);

		} catch (Exception e) {
			System.out.println("本詳細取得エラー: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "本の詳細情報取得中にエラーが発生しました。");
		}

		return "AdminBookDetail";
	}
}