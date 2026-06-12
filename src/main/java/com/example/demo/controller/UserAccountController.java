package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.UserRepository;

@Controller
public class UserAccountController {

	private final HttpSession session;
	private UserRepository userRepository;

	public UserAccountController(
			HttpSession session,
			Account account,
			UserRepository userRepository) {
		this.session = session;
		this.userRepository = userRepository;
	}

	//ログイン画面表示
	@GetMapping("/user/login")
	public String index() {

		return "UserLogin";
	}

	//ログイン処理
	@PostMapping("/user/login")
	public String login(
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			Model model) {

		ArrayList<String> list = new ArrayList<>();
		//空欄に対するエラー
		if (email.equals("")) {
			list.add("メールアドレスを入力してください");
			model.addAttribute("con", list);
		}
		if (password.equals("")) {
			list.add("パスワードを入力してください");
			model.addAttribute("con", list);
		}

		if (email.equals("") || password.equals("")) {
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			return "UserLogin";
		}

		List<User> userList = userRepository.findByEmailAndPassword(email, password);

		//メールアドレスとパスワードが一致しない
		if (userList.size() == 0) {
			model.addAttribute("message", "メールアドレスとパスワードが一致しません");
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			return "UserLogin";
		}

		return "UserHome";
	}

	//ログアウト
	@GetMapping("/user/logout")
	public String logout() {

		return "UserLogin";
	}

}
