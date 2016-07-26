package com.hcp.objective.service;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.BaseSpringTestCase;
import com.hcp.objective.persistence.bean.FormFolder;

public class FormFolderServiceTester extends BaseSpringTestCase {

	@Autowired
	FormFolderService oService;

	@Test
	public void testFindAll() {
		oService.findAll();

	}

	@Test
	public void testCreateOne() {
		FormFolder folder = new FormFolder();
		folder.setFolderId((long) -1);
		folder.setFolderName("test");
		folder.setUserId("JUnitTest");
		oService.createOne(folder);
		Assert.assertNotNull(oService.createOne(folder));
	}
}
