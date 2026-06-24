package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
	
	// 翌日に返却する本を取得
	List<Rental> findByUserIdAndDropDate(Integer userId, LocalDate tomorrow);

	// 🆕 修正: 未返却の本のみ（returnDate IS NULL）かつ、削除されていない本のみを取得
	@Query("SELECT DISTINCT r FROM Rental r " +
	       "WHERE EXISTS (SELECT 1 FROM Rentaldetail rd " +
	       "              WHERE rd.rental = r " +
	       "              AND rd.returnDate IS NULL " +
	       "              AND rd.book.deleteJudge = false)")
	List<Rental> findByUnreturnedBooksOnly();

	// 🆕 修正: 名前検索 + 未返却の本のみ
	@Query("SELECT DISTINCT r FROM Rental r " +
	       "WHERE r.user.name LIKE %:name% " +
	       "AND EXISTS (SELECT 1 FROM Rentaldetail rd " +
	       "            WHERE rd.rental = r " +
	       "            AND rd.returnDate IS NULL " +
	       "            AND rd.book.deleteJudge = false)")
	List<Rental> findUnreturnedBooksByUserNameContaining(@Param("name") String name);

	// 返却期限切れの本を取得する
	List<Rental> findByUserIdAndDropDateBeforeAndReturnDateIsNull(Integer userId, LocalDate today);

	// 貸し出し中の本(返却期限切れも含めた)を取得する
	List<Rental> findByUserIdAndReturnDateIsNull(Integer userId);

	// 貸し出し履歴
	List<Rental> findByUserId(Integer userId);

	// 直近の貸出伝票を取得
	Rental findFirstByOrderByIdDesc();
}