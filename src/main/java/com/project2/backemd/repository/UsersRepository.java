package com.project2.backemd.repository;

import com.project2.backemd.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
