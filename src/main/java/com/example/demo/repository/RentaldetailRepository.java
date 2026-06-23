package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Rentaldetail;

public interface RentaldetailRepository extends JpaRepository<Rentaldetail, Integer> {

	//貸し出しID貸し出し詳細を取得
	List<Rentaldetail> findByRentalId(Integer rentalId);

	Optional<Rentaldetail> findByBook_IdAndRental_User_IdAndRental_ReturnDateIsNull(
			Integer bookId,
			Integer userId);

}
