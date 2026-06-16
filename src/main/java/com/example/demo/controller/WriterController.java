package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Writer;
import com.example.demo.repository.WriterRepository;

@Controller
@RequestMapping("/admin")
public class WriterController {

	private WriterRepository writerRepository;

	public WriterController(
			WriterRepository writerRepository) {
		this.writerRepository = writerRepository;
	}

	@GetMapping("/writers")
	public String index() {
		return "writers";
	}

	@PostMapping("/writers")
	public String store(
			@RequestParam(defaultValue = "") String writerName,
			@RequestParam(defaultValue = "") String writerDescription,
			Model model) {

		List<String> errors = new ArrayList<>();

		if (writerName.equals("")) {
			errors.add("著者名が未入力です");
		}
		if (writerDescription.equals("")) {
			errors.add("著者紹介文が未入力です");
		}
		if (errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute("writerName", writerName);
			model.addAttribute("writerDescription", writerDescription);
			return "WriterAdd";
		}

		//なんだっけ
		//	
		Writer writer = new Writer(writerName, writerDescription);
		writerRepository.save(writer);

		return "redirect:/WriterAdd";
	}

}
