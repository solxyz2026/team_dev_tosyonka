package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	List<Reservation> findByUserId(Integer userId);

	//予約確定
	List<Reservation> findByReservationDate(LocalDate today);
	//名前で絞り込み
	List<Reservation> findByUser_NameContaining(String name);
}
