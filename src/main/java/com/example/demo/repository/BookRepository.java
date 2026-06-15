package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    //タイトルまたは著者名で本を検索（大文字小文字区別なし）
<<<<<<< HEAD
     
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.writer.writerName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
     
=======
    
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.writer.writerName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
           
>>>>>>> refs/heads/kitajima
    List<Book> searchByTitleOrWriter(@Param("keyword") String keyword);
}