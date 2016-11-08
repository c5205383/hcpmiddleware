package com.hcp.objective.web.controller;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.configuration.ExcludeForTest;
import com.hcp.objective.persistence.bean.User;
import com.hcp.objective.service.UserService;
import com.hcp.objective.web.model.response.BaseResponse;

@RestController
@ExcludeForTest
public class UserController {
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	private Transformer<User, BaseResponse<User>> SuccessTransformer = new Transformer<User, BaseResponse<User>>() {

		@Override
		public BaseResponse<User> transform(User user) {
			return new BaseResponse<User>(user);
		}
	};
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalPlan() {
		Collection<BaseResponse<User>> list = null;
		list = CollectionUtils.collect(userService.findAll(), SuccessTransformer);

		return new JSONArray(list).toString();
	}
}
