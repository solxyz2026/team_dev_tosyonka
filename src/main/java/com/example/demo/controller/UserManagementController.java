package com.example.demo.controller;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

//大森
@Controller
@RequestMapping("/admin")
public class UserManagementController {

	private final UserRepository userRepository;

	public UserManagementController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	//利用者一覧の表示
	@GetMapping("/users")
	public String index(Model model) {
		List<User> usersList = userRepository.findByDeleteJudgeFalseAndRole("User");
		model.addAttribute("usersList", usersList);

		return "adminUserManagement";
	}

	//ユーザの削除、パスワード、メールアドレスの暗号化処理
	@PostMapping("/users/{userId}/delete")
	public String delete(
			@PathVariable Integer userId,
			Model model) {

		String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{},.<>/?";
		Random random = new Random();
		int RANDOMMIN = 15;
		int RANDOMMAX = 25;
		int StringLength = random.nextInt((RANDOMMAX - RANDOMMIN) + 1) + RANDOMMIN;
		StringBuilder newPassword = new StringBuilder(StringLength);

		for (int i = 0; i < StringLength; i++) {
			newPassword.append((CHARSET.charAt(random.nextInt((CHARSET.length())))));
		}

		User user = userRepository.findById(userId).orElse(null);
		user.setDeleteJudge(true);
		user.setPassword(newPassword.toString());

		userRepository.save(user);
		return "redirect:/admin/users";

	}

}
