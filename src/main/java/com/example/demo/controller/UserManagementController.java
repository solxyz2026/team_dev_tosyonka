package com.example.demo.controller;

import java.util.List;

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

	@PostMapping("/users/{userId}/delete")
	public String delete(
			@PathVariable Integer userId,
			Model model) {

		System.out.println("a");
		User user = userRepository.findById(userId).orElse(null);
		System.out.println(user.isDeleteJudge());
		user.setDeleteJudge(true);
		userRepository.save(user);
		return "redirect:/admin/users";

	}

}
