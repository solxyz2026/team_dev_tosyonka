package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Writer;

public interface WriterRepository extends JpaRepository<Writer, Integer> {

}
