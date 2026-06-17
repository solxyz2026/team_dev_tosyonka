package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.example.demo.entity.Book;

@Component
@SessionScope
public class ReservationsCart {

	// 本リスト
	private List<Book> books = new ArrayList<>();

	//ゲッターセッター
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	// カート追加
	public void add(Book newbook) {
		Book existsBook = null;
		// 現在のカートの商品から同一IDの商品を探す
		for (Book book : books) {
			if (book.getId() == newbook.getId()) {
				existsBook = book;
				break;
			}
		}

		// カート内に商品が存在しなかった場合はカート追加
		if (existsBook == null) {
			books.add(newbook);
		}
	}

	// カートから商品を削除
	public void delete(int bookId) {

		// 現在のカートの商品から同一IDの商品を探す
		for (Book book : books) {
			// 対象の商品IDが見つかった場合削除する
			if (book.getId() == bookId) {
				books.remove(book);
				break;
			}
		}
	}

	// カートの中身を全てクリア
	public void clear() {
		books = new ArrayList<>();
	}

}
