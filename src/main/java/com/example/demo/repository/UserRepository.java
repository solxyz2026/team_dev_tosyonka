package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByEmailAndPassword(String email, String password);

	List<User> findByEmailAndPasswordAndRole(String email, String password, String admin);

	//aaa
	//bbb
	///ccc

	Optional<User> findByEmail(String email);

	Optional<User> findByIdNotAndEmail(Integer id, String email);
}
