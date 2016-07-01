package com.hcp.objective.service;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.component.ODataExecutor;
import com.hcp.objective.configuration.ExcludeForTest;
import com.hcp.objective.persistence.bean.Form;
import com.hcp.objective.persistence.repositories.FormRepository;

@Service
@Transactional
@ExcludeForTest
public class FormService {
	public static final Logger logger = LoggerFactory.getLogger(FormService.class);

	@Autowired
	private FormRepository formRepository;
	@Autowired
	public ODataExecutor odataExecutor;

	public Form findForm(@NotNull String id) {
		return null;
	}

	public Form storeForm(@NotNull String id) {
		Form form = findForm(id);
		if (form.getId() != null) {
			return formRepository.saveAndFlush(form);
		} else {
			return form;
		}
	}

}
