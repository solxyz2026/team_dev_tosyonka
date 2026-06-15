package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repository.CategoryRepository;

@Controller
@RequestMapping("/admin")
public class CategoryController {

	private CategoryRepository categoryRepository;

	public CategoryRepository(
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
				Model model
				) {

			
			Integer 
			
		}
}
