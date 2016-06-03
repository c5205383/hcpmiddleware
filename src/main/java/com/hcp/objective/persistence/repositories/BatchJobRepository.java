package com.hcp.objective.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.jpa.bean.BatchJob;

/**
 * The JPA Repository for BatchJob
 * 
 * @author Zero Yu
 */
@Repository
@Transactional
public interface BatchJobRepository extends JpaRepository<BatchJob, Long> {
	
	List<BatchJob> findByOwner(String owner);
}
