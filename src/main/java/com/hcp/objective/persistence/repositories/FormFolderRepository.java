package com.hcp.objective.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.FormFolder;

@Repository
@Transactional
public interface FormFolderRepository extends JpaRepository<FormFolder, Long> {

}
