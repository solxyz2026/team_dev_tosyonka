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
}
