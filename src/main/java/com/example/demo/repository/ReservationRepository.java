package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	//予約確定
	List<Reservation> findByReservationDate(LocalDate today);

	//名前で絞り込み
	List<Reservation> findByUser_NameAndReservationdetails_DeleteJudgeFalseOrderByUser_IdAsc(String name);

	//reservationStatusがfalseのもののみ表示（予約が完了していないもののみ）
	List<Reservation> findDistinctByReservationdetails_DeleteJudgeFalseOrderByUser_IdAsc();

	List<Reservation> findByUserId(Integer userId);
}
