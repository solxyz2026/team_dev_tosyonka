package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Rentaldetail;

public interface RentaldetailRepository extends JpaRepository<Rentaldetail, Integer> {

	//貸し出しID貸し出し詳細を取得
    List<Rentaldetail> findByRentalId(Integer rentalId);
   
}
