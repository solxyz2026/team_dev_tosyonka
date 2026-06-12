package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserMenuController {

	//メイン画面の表示
	@GetMapping("/")
	public String index(Model model) {
		return "userMenu";
	}
}
