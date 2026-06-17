package com.example.demo.entity;

import java.io.Serializable;

/**
 * カートアイテム
 * セッションに保存されるため、Serializable を実装
 */
public class CartItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer bookId;      // 本のID
	private String title;        // 本のタイトル
	private String author;       // 著者名
	private String category;     // カテゴリー名
	private String tag;          // タグ
	
	// ============================================
	// コンストラクタ
	// ============================================
	
	public CartItem() {
	}
	
	public CartItem(Integer bookId, String title, String author, String category, String tag) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.category = category;
		this.tag = tag;
	}
	
	// ============================================
	// Getter / Setter
	// ============================================
	
	public Integer getBookId() {
		return bookId;
	}
	
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return "CartItem [bookId=" + bookId + ", title=" + title + ", author=" + author 
			+ ", category=" + category + ", tag=" + tag + "]";
	}
}