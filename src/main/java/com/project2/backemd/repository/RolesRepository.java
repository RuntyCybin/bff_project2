package com.project2.backemd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project2.backemd.model.Role;

public interface RolesRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByDescription(String description);
    
}
