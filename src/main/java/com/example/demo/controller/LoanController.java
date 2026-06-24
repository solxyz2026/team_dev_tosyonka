package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
	 * 
	 * 改善内容：
	 * - @Transactional(readOnly=true) で Hibernateキャッシュを無効化
	 * - 未返却の本のみ（returnDate IS NULL）を取得するクエリを使用
	 * - 削除済みの本（deleteJudge = true）を除外
	 * - 返却ボタンをクリック後、その本が一覧から消える
	 */
	@Transactional(readOnly = true)
	@GetMapping("/rental")
	public String index(
			@RequestParam(name = "keyword", required = false) String keyword,
			Model model) {
		
		System.out.println("\n========== 貸し出し状況一覧 ==========");
		List<Rental> rentalList;

		try {
			// キーワードが入力されている場合は名前で絞り込み
			if (keyword != null && !keyword.isBlank()) {
				System.out.println("🔍 「" + keyword + "」で絞り込み検索");
				
				// 🔧 修正: 未返却の本のみを取得（returnDate IS NULL）
				rentalList = rentalRepository.findUnreturnedBooksByUserNameContaining(keyword.trim());
				System.out.println("✅ 検索結果: " + rentalList.size() + "件");

			} else {
				// 入力なしの場合は未返却の本を全件取得
				System.out.println("📋 未返却の全伝票を取得");
				
				// 🔧 修正: 未返却の本のみを取得（returnDate IS NULL）
				rentalList = rentalRepository.findByUnreturnedBooksOnly();
				System.out.println("✅ 未返却の伝票: " + rentalList.size() + "件");
			}

			// Lazy Loading で RentalDetail を明示的に初期化
			// 理由: View（Thymeleaf）で rental.rentaldetail にアクセス時に
			//      LazyInitializationException が発生するのを防止
			for (Rental rental : rentalList) {
				if (rental.getRentaldetail() != null) {
					// リストにアクセスして初期化
					rental.getRentaldetail().size();
					System.out.println("  📚 " + rental.getUser().getName() 
						+ ": " + rental.getRentaldetail().size() + "冊");
				}
			}

			// モデルに追加
			model.addAttribute("rentalList", rentalList);
			model.addAttribute("keyword", keyword);

			System.out.println("========== ページ表示完了 ==========\n");

		} catch (Exception e) {
			System.out.println("❌ エラー: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "貸し出し一覧の取得に失敗しました");
			model.addAttribute("rentalList", List.of());
		}

		return "Rentalshow";
	}
}