package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
	//翌日に返却する本を取得
	//SELECT * FROM rentals WHERE user_id = ?1 AND ( drop_date = ?2)
	List<Rental> findByUserIdAndDropDate(Integer userId, LocalDate tomorrow);

	//返却日がないもの（貸し出し中のもの）だけを抽出する。貸し出しするときは再度nullにする必要がある。
	List<Rental> findByReturnDateIsNull();

	//返却期限切れの本を取得する
	List<Rental> findByUserIdAndDropDateBeforeAndReturnDateIsNull(Integer userId, LocalDate today);

	//貸し出し中の本(返却期限切れも含めた)を取得する
	List<Rental> findByUserIdAndReturnDateIsNull(Integer userId);

	//貸し出し履歴
	List<Rental> findByUserId(Integer userId);
	//名前で絞り込み（貸し出し中のもの）
	List<Rental> findByReturnDateIsNullAndUser_NameContaining(String name);
	
	Rental findFirstByOrderByIdDesc();
	
	@Override
	default long count() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

}
