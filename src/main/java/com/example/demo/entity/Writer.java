package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "writers")
@Component
public class Writer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; //著者ID

	@Column(name = "writer_name")
	private String writerName; //著者名

	@Column(name = "writer_description")
	private String writerDescription; //著者説明

	//コンストラクタ
	public Writer() {
	}

	public Writer(String writerName, String writerDescription) {
		//追加用
		this.writerName = writerName;
		this.writerDescription = writerDescription;
	}

	public Writer(Integer id, String writerName, String writerDescription) {
		//更新用
		this.id = id;
		this.writerName = writerName;
		this.writerDescription = writerDescription;
	}

	//ゲッターセッター
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	public String getWriterDescription() {
		return writerDescription;
	}

	public void setWriterDescription(String writerDescription) {
		this.writerDescription = writerDescription;
	}

}
