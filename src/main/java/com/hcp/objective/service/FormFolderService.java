package com.hcp.objective.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.FormFolder;
import com.hcp.objective.persistence.repositories.FormFolderRepository;

/**
 * 
 * @author Bruce, Wang
 *
 */
@Service
@Transactional
public class FormFolderService {

	public static int SUCCESS = 0; //
	public static int FAILED = -1;

	@Autowired
	private FormFolderRepository formFolderRepository;

	public List<FormFolder> findAll() {
		return formFolderRepository.findAll();
	}

	public FormFolder findOne(@NotNull Long id) {
		return formFolderRepository.findOne(id);
	}

	/**
	 * 
	 * @param formFolder
	 * @return
	 */
	public FormFolder createOne(@NotNull FormFolder formFolder) {
		return formFolderRepository.saveAndFlush(formFolder);
	}
}
