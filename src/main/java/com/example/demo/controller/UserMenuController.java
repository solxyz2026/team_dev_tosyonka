package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.repository.AnnouncementRepository;

@Controller
@RequestMapping("/user")
public class UserMenuController {

	private final AnnouncementRepository announcementRepository;

	public UserMenuController(AnnouncementRepository announcementRepository) {
		this.announcementRepository = announcementRepository;
	}

	//メイン画面の表示
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("newsList", announcementRepository);
		return "userMenu";
	}
}
