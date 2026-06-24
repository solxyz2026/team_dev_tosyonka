package com.example.demo.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Reservationdetail;
import com.example.demo.entity.User;
import com.example.demo.repository.ReservationdetailRepository;
import com.example.demo.repository.UserRepository;

//大森
@Controller
@RequestMapping("/admin")
public class UserManagementController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReservationdetailRepository reservationdetailRepository;

	//利用者一覧の表示
	//利用者一覧の表示（名前で絞り込み可能）
	@GetMapping("/users")
	public String index(
			@RequestParam(name = "keyword", required = false) String keyword,
			Model model) {

		List<User> usersList;

		// キーワードがあれば名前で絞り込み
		if (keyword != null && !keyword.isBlank()) {
			usersList = userRepository.findByDeleteJudgeFalseAndRoleAndNameContaining("User", keyword.trim());
		} else {
			usersList = userRepository.findByDeleteJudgeFalseAndRole("User");
		}

		model.addAttribute("usersList", usersList);
		model.addAttribute("keyword", keyword);

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

		List<Reservationdetail> reservationdetailList = reservationdetailRepository
				.findByReservation_User_IdAndDeleteJudgeFalseAndBook_DeleteJudgeFalse(userId);

		for (Reservationdetail reservationdetail : reservationdetailList) {
			reservationdetail.setDeleteJudge(true);
			reservationdetailRepository.save(reservationdetail);
		}

		User user = userRepository.findById(userId).orElse(null);
		user.setDeleteJudge(true);
		user.setPassword(newPassword.toString());

		userRepository.save(user);
		return "redirect:/admin/users";

	}

}
