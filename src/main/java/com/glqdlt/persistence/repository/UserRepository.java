package com.glqdlt.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glqdlt.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	

}
