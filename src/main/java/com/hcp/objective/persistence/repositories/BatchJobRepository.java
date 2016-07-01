package com.hcp.objective.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.BatchJob;

/**
 * The JPA Repository for BatchJob
 * 
 * @author Zero Yu
 */
@Repository
@Transactional
public interface BatchJobRepository extends JpaRepository<BatchJob, Long> {
	
	/**
	 * Find a list of batch jobs by owner
	 * 
	 * @param owner
	 *            {@link}BatchJob's owner
	 * @return List<BatchJob> a list of batch jobs
	 */
	List<BatchJob> findByOwner(String owner);
}
