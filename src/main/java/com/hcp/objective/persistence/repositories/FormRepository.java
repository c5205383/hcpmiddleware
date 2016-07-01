package com.hcp.objective.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.configuration.ExcludeForTest;
import com.hcp.objective.persistence.bean.Form;


@Repository
@Transactional
@ExcludeForTest
public interface FormRepository extends JpaRepository<Form, Integer> {

}
