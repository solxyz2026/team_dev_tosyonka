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

	//著者更新ページへ
	@GetMapping("/admin/writers/{writer_id}")
	public String edit(
			@PathVariable Integer id, //←{writer_id}とidの使い分けが不安
			Model model) {
		Writer writer = writerRepository.findById(id).get();
		model.addAttribute("user", writer);
		return "WriterEdit";
	}

	//著者更新
	@PostMapping("/admin/writers/{writer_id}") //←{writer_id}とwriterIdの使い分けが不安
	public String update(
			@PathVariable Integer id,
			@RequestParam(defaultValue = "") String writerName,
			@RequestParam(defaultValue = "") String writerDescription,
			Model model) {

		ArrayList<String> list = new ArrayList<>();
		//空欄に対するエラー
		if (writerName.equals("")) {
			list.add("著者を入力してください");
		}
		if (writerDescription.equals("")) {
			list.add("著者紹介文を入力してください");
		}
		model.addAttribute("list", list);

		if (writerName.equals("") || writerDescription.equals("")) {
			Writer writer = writerRepository.findById(id).get();
			writer.setWriterName(writerName);
			writer.setWriterDescription(writerDescription);

			model.addAttribute("writer", writer);
			return "WriterEdit";
		}

		Writer writer = writerRepository.findById(id).get();
		writer.setWriterName(writerName);
		writer.setWriterDescription(writerDescription);
		writerRepository.save(writer);
		return "";//○○（本のタイトル）の画面へ(←未入力)

	}
}
