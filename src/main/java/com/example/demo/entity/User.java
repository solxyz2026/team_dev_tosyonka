package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "users")
@Component
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //顧客ID

	private String email; //メールアドレス
	private String name; //名前
	private String password; //パスワード

	@Column(name = "tel_number")
	private String telNumber; //電話番号

	private String birthday; //誕生日
	private String role; //ロール

	//コンストラクタ
	public User() {
	}

	public User(String email, String name, String password, String telNumber, String birthday, String role) {
		//新規会員登録用
		this.email = email;
		this.name = name;
		this.password = password;
		this.telNumber = telNumber;
		this.birthday = birthday;
		this.role = role;
	}

	public User(Integer id, String email, String name, String password, String telNumber, String birthday,
			String role) {
		//会員情報変更用
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.telNumber = telNumber;
		this.birthday = birthday;
		this.role = role;
	}

	//ゲッターセッター
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
