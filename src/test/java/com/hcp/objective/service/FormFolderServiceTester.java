package com.hcp.objective.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.BaseSpringTestCase;
import com.hcp.objective.persistence.bean.FormFolder;

@Transactional
public class FormFolderServiceTester extends BaseSpringTestCase {

	@Autowired
	FormFolderService oService;

	// @Test
	public void testFindAll() {
		List<FormFolder> results = oService.findAll();

		Assert.assertEquals(false, results.isEmpty());

		System.out.println(results.size());

		for (FormFolder folder : results) {
			System.out.println(folder.getFolderName());
		}

	}

	@Test
	@Rollback(false)
	public void testCreateOne() {
		FormFolder folder = new FormFolder();
		folder.setFolderId((long) -100);
		folder.setFolderName("test");
		folder.setUserId("JUnitTest");
		oService.createOne(folder);
		Assert.assertNotNull(oService.createOne(folder));
	}

	// @Test
	// @Transactional
	public void testCreatMore() {
		List<FormFolder> list = new ArrayList<FormFolder>();
		FormFolder folder1 = new FormFolder();
		folder1.setFolderId((long) -12);
		folder1.setFolderName("test12");
		folder1.setUserId("JUnitTest");
		list.add(folder1);

		FormFolder folder2 = new FormFolder();
		folder2.setFolderId((long) -13);
		folder2.setFolderName("test13");
		folder2.setUserId("JUnitTest");
		list.add(folder2);

		FormFolder folder3 = new FormFolder();
		folder3.setFolderId((long) -14);
		folder3.setFolderName("test14");
		folder3.setUserId("JUnitTest");
		list.add(folder3);

		oService.createMore(list);

		List<FormFolder> results = oService.findAll();

		Assert.assertEquals(false, results.isEmpty());

		System.out.println(results.size());

		for (FormFolder folder : results) {
			System.out.println(folder.getFolderName());
		}
	}
}
