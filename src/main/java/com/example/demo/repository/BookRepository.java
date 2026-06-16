package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	//タイトルで本を検索

	List<Book> findByTitleContainingIgnoreCase(String title);

	// タイトルまたは著者名で本を検索

	@Query("SELECT b FROM Book b WHERE " +
			"LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
			"LOWER(b.writer.writerName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Book> searchByTitleOrWriter(@Param("keyword") String keyword);

	//キーワードとカテゴリの組み合わせで検索

	@Query("SELECT b FROM Book b WHERE " +
			"(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
			"LOWER(b.writer.writerName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
			"AND b.category.id = :categoryId")
	List<Book> searchByKeywordAndCategory(@Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId);

	//カテゴリのみで検索
	List<Book> findByCategoryId(Integer categoryId);
}
