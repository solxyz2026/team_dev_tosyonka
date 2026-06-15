package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminAccountController {

	private final HttpSession session;
	private UserRepository userRepository;

	public AdminAccountController(
			HttpSession session,
			Account account,
			UserRepository userRepository) {
		this.session = session;
		this.userRepository = userRepository;
	}

	//ログイン画面表示
	@GetMapping("/login")
	public String index() {

		return "AdminLogin";
	}

	@GetMapping("/logout")
	public String logout() {

		return "AdminLogin";
	}

	//ログイン処理
	@PostMapping("/login")
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
			return "AdminLogin";
		}

		List<User> userList = userRepository.findByEmailAndPasswordAndRole(email, password, "Admin");

		//メールアドレスとパスワードが一致しない
		if (userList.size() == 0) {
			model.addAttribute("message", "メールアドレスとパスワードが一致しません");
			model.addAttribute("email", email);
			model.addAttribute("password", password);
			return "AdminLogin";
		}

		return "AdminHome";
	}

}
