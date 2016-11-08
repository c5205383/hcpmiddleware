package com.hcp.objective.persistence.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {
	
}
