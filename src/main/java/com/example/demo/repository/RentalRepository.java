package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

}
