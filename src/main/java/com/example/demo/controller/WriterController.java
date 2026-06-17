package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.entity.Writer;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.WriterRepository;

@Controller
@RequestMapping("/admin")
public class WriterController {

	private WriterRepository writerRepository;
	@Autowired //←これ何？
	private BookRepository bookRepository;

	public WriterController(
			WriterRepository writerRepository) {
		this.writerRepository = writerRepository;
	}

	//著者追加画面へ
	@GetMapping("/writers")
	public String index() {

		return "WriterAdd";
	}

	//著者追加
	@PostMapping("/writers")
	public String store(
			@RequestParam(defaultValue = "") String writerName,
			@RequestParam(defaultValue = "") String writerDescription,
			Model model) {

		//		List<String> errors = new ArrayList<>();

		ArrayList<String> errors = new ArrayList<>();

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

		return "AdminHome";
	}

	//	//著者更新ページへ
	//	@GetMapping("/admin/writers/{writer_id}")
	//	public String edit(
	//			@PathVariable Integer writer_id, //←{writer_id}とidの使い分けが不安
	//			Model model) {
	//		Writer writer = writerRepository.findById(writer_id).get();
	//		model.addAttribute("user", writer);
	//		return "WriterEdit";
	//	}
	//
	//	//著者更新
	//	@PostMapping("/admin/writers/{writer_id}") //←{writer_id}とwriterIdの使い分けが不安
	//	public String update(
	//			@PathVariable Integer writer_id,
	//			@RequestParam(defaultValue = "") String writerName,
	//			@RequestParam(defaultValue = "") String writerDescription,
	//			Model model) {
	//
	//		ArrayList<String> list = new ArrayList<>();
	//		//空欄に対するエラー
	//		if (writerName.equals("")) {
	//			list.add("著者を入力してください");
	//		}
	//		if (writerDescription.equals("")) {
	//			list.add("著者紹介文を入力してください");
	//		}
	//		model.addAttribute("list", list);
	//
	//		if (writerName.equals("") || writerDescription.equals("")) {
	//			Writer writer = writerRepository.findById(writer_id).get();
	//			writer.setWriterName(writerName);
	//			writer.setWriterDescription(writerDescription);
	//
	//			model.addAttribute("writer", writer);
	//			return "WriterEdit";
	//		}
	//
	//		Writer writer = writerRepository.findById(writer_id).get();
	//		writer.setWriterName(writerName);
	//		writer.setWriterDescription(writerDescription);
	//		writerRepository.save(writer);
	//		return "";//○○（本のタイトル）の画面へ(←未入力)
	//	}

	//著者一覧画面へ
	@GetMapping("/writers/list")
	public String writerList(Model model) {

		List<Writer> writerList = writerRepository.findAll();

		model.addAttribute("writerList", writerList);

		return "WriterShow";
	}

	//著者削除
	@PostMapping("/writers/list/{id}/delete")
	public String deleteWriter(
			@PathVariable Integer id,
			Model model) {

		List<Book> books = bookRepository.findByWriterId(id);

		if (books.size() >= 1) {
			List<Writer> writerList = writerRepository.findAll();
			model.addAttribute("writerList", writerList);
			model.addAttribute("message", "この著者は削除できません");

			return "WriterShow";
			//			return "redirect:/admin/writers/list";
		}

		writerRepository.deleteById(id);

		return "redirect:/admin/writers/list";
	}
}
