package com.hcp.objective.web;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {
	@RequestMapping("/login")
	public String login(String jsonpName,String userName,String password) {
		return  jsonpName + "(" + "{'name':'kevin','score':123}" + ")";

	}
}
