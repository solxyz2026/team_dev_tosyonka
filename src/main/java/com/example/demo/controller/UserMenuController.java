package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Announcement;
import com.example.demo.repository.AnnouncementRepository;

@Controller
@RequestMapping("/user")
public class UserMenuController {

	private final Announcement announcement;

	private final AnnouncementRepository announcementRepository;

	public UserMenuController(AnnouncementRepository announcementRepository, Announcement announcement) {
		this.announcementRepository = announcementRepository;
		this.announcement = announcement;
	}

	//メイン画面の表示
	@GetMapping("/")
	public String index(Model model) {
		List<Announcement> newsList = announcementRepository.findAll();
		model.addAttribute("newsList", newsList);
		return "userMenu";
	}
}
