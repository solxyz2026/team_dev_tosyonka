package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Announcement;
import com.example.demo.repository.AnnouncementRepository;

@Controller
@RequestMapping("/admin")
public class AdminAnnouncementController {

	private final AnnouncementRepository announcementRepository;

	public AdminAnnouncementController(AnnouncementRepository announcementRepository) {
		this.announcementRepository = announcementRepository;
	}

	//お知らせ一覧ページ
	@GetMapping("/announcement")
	public String show(Model model) {
		List<Announcement> news = announcementRepository.findAll();
		model.addAttribute("news", news);
		return "adminAnnouncement";
	}

	//お知らせ追加ページ
	@GetMapping("/announcement/add")
	public String index() {
		return "adminAnnouncementAdd";
	}

	//お知らせ追加
	@PostMapping("/announcement/add")
	public String add(@RequestParam(defaultValue = "") String title,
			@RequestParam(defaultValue = "") String content,
			Model model) {

		List<String> msgList = new ArrayList<>();

		if (title.equals("") || title == null) {
			msgList.add("タイトルを記入してください。");
		}

		if (content.equals("") || content == null) {
			msgList.add("内容を記入してください。");
		}

		if (msgList.size() > 0) {
			model.addAttribute("msgList", msgList);
			return "adminAnnouncementAdd";
		}

		String titleAll = "【" + title + "】";
		String contentAll = titleAll + content;

		Announcement announcement = new Announcement(null, contentAll);
		announcementRepository.save(announcement);

		return "redirect:/admin/announcement";
	}

	//お知らせ削除
	@PostMapping("/announcement/{id}/delete")
	public String delete(@PathVariable int id) {
		announcementRepository.deleteById(id);
		return "redirect:/admin/announcement";
	}

}
