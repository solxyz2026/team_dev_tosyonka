package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Reservationdetail;

public interface ReservationdetailRepository extends JpaRepository<Reservationdetail, Integer> {

	/**
	 * 指定した本が予約されているか確認
	 * → 予約禁止チェックで使用
	 */
	boolean existsByBookId(Integer bookId);

	/**
	 * 指定した本の予約詳細を取得（1冊につき予約は1つ想定）
	 */
	Optional<Reservationdetail> findByBookId(Integer bookId);

	/**
	 * ユーザーIDで予約一覧取得（画面表示用）
	 */
	@Query("SELECT rd FROM Reservationdetail rd WHERE rd.reservation.user.id = :userId")
	List<Reservationdetail> findByUserId(@Param("userId") Integer userId);

	/**
	 * 本IDで予約削除（貸出確定時などに使用）
	 */
	@Modifying
	@Query("DELETE FROM Reservationdetail rd WHERE rd.book.id = :bookId")
	void deleteByBookId(@Param("bookId") Integer bookId);

	/**
	 * 予約ID単位で削除（伝票単位キャンセルなど）
	 */
	@Modifying
	@Query("DELETE FROM Reservationdetail rd WHERE rd.reservation.id = :reservationId")
	void deleteByReservationId(@Param("reservationId") Integer reservationId);

	/**
	 * 指定予約IDの明細一覧取得
	 */
	List<Reservationdetail> findByReservationId(Integer reservationId);

	boolean existsByBookIdAndReservation_ReservationDate(Integer bookId, LocalDate reservationDate);

	Optional<Reservationdetail> findByBookIdAndDeleteJudgeFalse(Integer bookId);

	List<Reservationdetail> findByReservation_User_IdAndDeleteJudgeFalseAndBook_DeleteJudgeFalse(Integer userId);

}
