package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
@RequestMapping("/user")
public class UserAccountController {

	private final HttpSession session;
	private final Account account;
	private final UserRepository userRepository;

	public UserAccountController(HttpSession session, Account account, UserRepository userRepository) {
		this.session = session;
		this.account = account;
		this.userRepository = userRepository;
	}

	//ログイン画面表示
	@GetMapping("/login")
	public String index() {
		session.invalidate();
		return "UserLogin";
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
		User user = userList.get(0);
		//		session.setAttribute("userId", user.getId());
		//		session.setAttribute("userName", user.getName());

		account.setId(user.getId());
		account.setName(user.getName());
		System.out.println("✅ ログイン成功: userId=" + user.getId() + ", userName=" + user.getName());

		return "redirect:/user/";
	}

	//ログアウト
	@GetMapping("/logout")
	public String logout() {

		return "UserLogin";
	}

	//新規会員登録画面の表示
	@GetMapping("/register")
	public String registerForm() {

		return "UserAdd";
	}

	//会員情報を登録し、再度ログイン画面へ
	@PostMapping("/register")
	public String register(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(required = false) LocalDate birthday,
			@RequestParam(defaultValue = "") String telNumber,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			Model model) {
		ArrayList<String> list = new ArrayList<>();
		Optional<User> record = userRepository.findByEmail(email);
		//空欄に対するエラー
		if (name.equals("")) {
			list.add("名前を入力してください");

		}
		if (birthday == null) {
			list.add("生年月日を入力してください");

		}
		if (telNumber.equals("")) {
			list.add("電話番号を入力してください");

		}
		if (email.equals("")) {
			list.add("メールを入力してください");
		} else {

			if (record.isEmpty() == false) {
				list.add("このメールアドレスは既に登録されています");
			}

		}

		if (password.equals("")) {
			list.add("パスワードを入力してください");

		}
		if (name.equals("") || birthday == null || telNumber.equals("") || email.equals("") || password.equals("")
				|| (record.isEmpty() == false)) {
			model.addAttribute("con", list);
			model.addAttribute("name", name);
			model.addAttribute("birthday", birthday);
			model.addAttribute("telNumber", telNumber);
			model.addAttribute("email", email);
			model.addAttribute("password", password);

			return "UserAdd";
		}

		User user = new User(name, birthday, telNumber, email, password, "User");
		userRepository.save(user);
		return "UserLogin";
	}

	//会員情報変更画面を表示
	@GetMapping("/edit")
	public String edit(Model model) {

		Integer id = account.getId();

		User user = userRepository.findById(id).get();

		model.addAttribute("user", user);

		return "UserInfoChange";
	}

	//会員情報の変更
	@PostMapping("/edit")
	public String change(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") LocalDate birthday,
			@RequestParam(defaultValue = "") String telNumber,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "") String password,
			Model model) {

		Integer id = account.getId();
		User user = userRepository.findById(id).get();

		user.setName(name);
		user.setBirthday(birthday);
		user.setTelNumber(telNumber);
		user.setEmail(email);
		user.setPassword(password);

		userRepository.save(user);

		account.setName(name);

		return "redirect:/user/mypage";

	}

}
