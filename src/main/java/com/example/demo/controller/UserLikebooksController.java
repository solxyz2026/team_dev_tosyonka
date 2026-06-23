package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.entity.Likebook;
import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LikebookRepository;
import com.example.demo.repository.UserRepository;

//大森
@Controller
@RequestMapping("/user")
public class UserLikebooksController {

	private final Account account;
	private final LikebookRepository likebookRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;

	public UserLikebooksController(Account account, LikebookRepository likebookRepository,
			UserRepository userRepository, BookRepository bookRepository) {
		this.account = account;
		this.likebookRepository = likebookRepository;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
	}

	//お気に入りページ表示
	@GetMapping("/likebooks")
	public String index(Model model) {
		Integer userId = account.getId();
		List<Likebook> likebookList = likebookRepository.findByUserIdAndDeleteJudge(userId, false);
		model.addAttribute("likebookList", likebookList);
		return "userLikebooks";
	}

	//お気に入り登録
	@PostMapping("/likebook/{bookId}/add")
	public String add(
			@PathVariable Integer bookId, Model model) {
		Integer userId = account.getId();
		User user = userRepository.findById(userId).get();
		Book book = bookRepository.findById(bookId).get();
		Likebook addLikebook = new Likebook(user, book, false);
		likebookRepository.save(addLikebook);
		return "redirect:/user/books/" + bookId;
	}

	//お気に入り削除
	@PostMapping("/likebook/{id}/delete")
	public String delete(
			@PathVariable Integer id, Model model) {
		User user = likebookRepository.findById(id).get().getUser();
		Book book = likebookRepository.findById(id).get().getBook();
		Likebook deleteLikebook = new Likebook(id, user, book, true);
		likebookRepository.save(deleteLikebook);
		return "redirect:/user/likebooks";
	}

}
