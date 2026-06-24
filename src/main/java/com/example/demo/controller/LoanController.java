package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Rental;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.RentaldetailRepository;

@RequestMapping("/admin")
@Controller
public class LoanController {

	private final RentaldetailRepository rentaldetailRepository;

	@Autowired
	private RentalRepository rentalRepository;

	LoanController(RentaldetailRepository rentaldetailRepository) {
		this.rentaldetailRepository = rentaldetailRepository;
	}

	/**
	 * GET /admin/rental - 貸し出し状況一覧表示（名前で絞り込み可能）
	 */
	@GetMapping("/rental")
	public String index(
			@RequestParam(name = "keyword", required = false) String keyword,
			Model model) {

		System.out.println("\n========== 貸し出し状況一覧 ==========");

		List<Rental> rentalList;

		// キーワードが入力されている場合は名前で絞り込み
		if (keyword != null && !keyword.isBlank()) {
			rentalList = rentalRepository.findByReturnDateIsNullAndUser_NameContaining(keyword.trim());
			System.out.println("🔍 「" + keyword + "」で絞り込み");
		} else {
			// 入力なしの場合は貸し出し中の本を全件取得
			rentalList = rentalRepository.findByReturnDateIsNullAndRentaldetail_Book_DeleteJudgeFalse();
		}

		System.out.println("✅ 貸出中の本: " + rentalList.size() + "件");

		model.addAttribute("rentalList", rentalList);
		// 検索ボックスに入力値を残す
		model.addAttribute("keyword", keyword);

		System.out.println("========== ページ表示 ==========\n");
		return "Rentalshow";
	}
}