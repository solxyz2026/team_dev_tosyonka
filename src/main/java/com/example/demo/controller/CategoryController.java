package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

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

	public CategoryController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	// カテゴリーの追加画面表示
	@GetMapping("/categories")
	public String index(Model model) {
		
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		
		System.out.println("✅ カテゴリー一覧取得: " + categories.size() + "件");
		
		return "admin/categories";
	}

	// カテゴリーの登録
	@PostMapping("/categories")
	public String store(
			@RequestParam(defaultValue = "") String categoryName,
			Model model) {

		ArrayList<String> errors = new ArrayList<>();

		if (categoryName == null || categoryName.trim().isEmpty()) {
			errors.add("カテゴリー名を入力してください");
		}

		if (!errors.isEmpty()) {
			model.addAttribute("errors", errors);
			model.addAttribute("categoryName", categoryName);
			
			// 既存カテゴリー一覧も表示
			List<Category> categories = categoryRepository.findAll();
			model.addAttribute("categories", categories);
			
			System.out.println("❌ バリデーションエラー: " + errors);
			
			return "admin/categories";
		}

		Category category = new Category(null, categoryName);
		categoryRepository.save(category);

		System.out.println("✅ カテゴリー登録完了: " + categoryName);

		// 登録後、再度一覧画面に遷移（リダイレクト）
		return "redirect:/admin/categories";
	}
}