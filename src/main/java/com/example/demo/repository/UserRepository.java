package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByEmailAndPassword(String email, String password);

	List<User> findByEmailAndPasswordAndRole(String email, String password, String admin);

	Optional<User> findByEmail(String email);

	Optional<User> findByIdNotAndEmail(Integer id, String email);

	List<User> findByRole(String role);

	List<User> findByDeleteJudgeFalseAndRole(String role);
	
	//名前で絞り込み（削除されていない一般利用者）
	List<User> findByDeleteJudgeFalseAndRoleAndNameContaining(String role, String name);
}
