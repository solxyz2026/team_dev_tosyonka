package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Rentaldetail;

public interface RentaldetailRepository extends JpaRepository<Rentaldetail, Integer> {

	List<Rentaldetail> findByBookIdAndRental_ReturnDateIsNull(Integer bookId);

}
