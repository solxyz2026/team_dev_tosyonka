package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Rental;
import com.example.demo.repository.RentalRepository;

@RequestMapping("/admin")
@Controller
public class LoanController {

	@Autowired
	private RentalRepository rentalRepository;

	/**
	 * GET /admin/rental - 貸し出し状況一覧表示
	 */
	@GetMapping("/rental")
	public String index(Model model) {
		System.out.println("\n========== 貸し出し状況一覧 ==========");

		// 貸し出し中の本を取得
		List<Rental> rentalList = rentalRepository.findByReturnDateIsNull();
		System.out.println("✅ 貸出中の本: " + rentalList.size() + "件");

		model.addAttribute("rentalList", rentalList);

		System.out.println("========== ページ表示 ==========\n");

		System.out.println(rentalList.size());

		return "Rentalshow";
	}
}