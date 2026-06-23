package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Likebook;

public interface LikebookRepository extends JpaRepository<Likebook, Integer> {

	List<Likebook> findByUserIdAndDeleteJudge(Integer userId, Boolean deleteJudge);

	List<Likebook> findByUserIdAndBookIdAndDeleteJudge(Integer userId, Integer bookId, Boolean deleteJudge);

}
