package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class WriterController {

	@GetMapping("/writers")
	public String index() {
		return "writers";
	}

	@PostMapping("/writers")
	public String store(
			@RequestParam(defaultValue = "") String writer_name,
			@RequestParam(defaultValue = "") String writer_description,
			Model model) {

		List<String> errors = new ArrayList<>();

		if (writer_name.equals("")) {
			errors.add("著者名が未入力です");
		}
		if (writer_description.equals("")) {
			errors.add("著者紹介文が未入力です");
		}
		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			return "writers";
		}

		//なんだっけ
		//		Integer user_id = account.getId();
		//		Task task = new Task(user_id, title, closingDate, 0, memo, important);
		//		taskRepository.save(task);
		//				List<Task> taskList = taskRepository.findAll();
		//				model.addAttribute("tasks", taskList);

		return "redirect:/writers";
	}

}
