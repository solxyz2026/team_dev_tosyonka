package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

@Controller
@RequestMapping("/admin")
public class CategoryController {

	private CategoryRepository categoryRepository;

	public CategoryController(
			CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	//カテゴリーの追加画面表示
	@GetMapping("/categories")
	public String index() {

		return "CategoryAdd";
	}

	//カテゴリーの登録
	@PostMapping("/categories")
	public String store(
			@RequestParam(defaultValue = "") String categoryName,
			Model model) {

		//空欄に対するエラー
		if (categoryName.equals("")) {

			model.addAttribute("message", "カテゴリーが未入力です");

			return "CategoryAdd";
		}

		//保存
		Category category = new Category(null, categoryName);
		categoryRepository.save(category);

		//		return "redirect:CategoryAdd";
		return "AdminHome";
	}
}