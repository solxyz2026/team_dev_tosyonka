/*編集、作成　原田大瑚　6月15日　13時
 * 
 * */

package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Rental;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.RentaldetailRepository;

@Controller
@RequestMapping("/admin")
public class LoanController {

	private final RentaldetailRepository rentaldetailRepository;

	private final Rental rental;
	private final RentalRepository rentalRepository;

	public LoanController(Rental rental, RentalRepository rentalRepository,
			RentaldetailRepository rentaldetailRepository) {
		this.rental = rental;
		this.rentalRepository = rentalRepository;
		this.rentaldetailRepository = rentaldetailRepository;
	}

	//貸し出し本一覧表示
	@GetMapping("/rental")
	public String index(Model model) {
		List<Rental> rentalList = null;
		rentalList = rentalRepository.findAll();
		model.addAttribute("rentalList", rentalList);
		return "Rental";
	}

}
