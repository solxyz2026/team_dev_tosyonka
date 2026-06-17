package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Writer;

public interface WriterRepository extends JpaRepository<Writer, Integer> {

	Optional<Writer> findByWriterNameAndDeleteJudgeFalse(String writerName);

	List<Writer> findByDeleteJudgeFalse();
}
