package com.hcp.objective.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.jpa.bean.Form;

@Repository
@Transactional
public interface FormRepository extends JpaRepository<Form, Integer> {

}
