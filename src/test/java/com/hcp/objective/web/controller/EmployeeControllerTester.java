package com.hcp.objective.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hcp.objective.BaseSpringTestCase;

public class EmployeeControllerTester extends BaseSpringTestCase {
	@Autowired
	private WebApplicationContext wac;

	public MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testGetEmpDirectReports() throws Exception {
		// mockMvc.perform: 发起一个http请求。
		// get/post(url): 表示一个post请求，url对应的是Controller中被测方法的Rest url
		// andDo（print()）: 表示打印出request和response的详细信息，便于调试
		// andExpect（status().isOk()）: 表示期望返回的Response Status是200。
		this.mockMvc.perform(get("/empDirectReports")).andDo(print()).andExpect(status().isOk()).andReturn();
	}
}
