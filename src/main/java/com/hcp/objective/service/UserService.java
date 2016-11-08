package com.hcp.objective.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.User;
import com.hcp.objective.persistence.repositories.UserRepository;

@Service
@Transactional
public class UserService {

	public static int SUCCESS = 0; //
	public static int FAILED = -1;

	@Autowired
	private UserRepository userRepository;

	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public void deleteAll(){
		userRepository.deleteAll();
	}

	
	public User createOne(@NotNull User mergeRequest) {
		return userRepository.saveAndFlush(mergeRequest);
	}

	
	public int deleteOneByUserId(@NotNull String userId) {
		try {
			userRepository.delete(userId);
			return SUCCESS;
		} catch (IllegalArgumentException e) {
			return FAILED;
		}
	}
	
	public User findOne(@NotNull String userId){
		return userRepository.findOne(userId);
	}

	public User updateOne(@NotNull String id, @NotNull User mergeRequest) {
		
		User user = userRepository.findOne(id);
		
		if(user == null)
			return null;
		
		mergeScalarProperties(mergeRequest, user);
		return userRepository.saveAndFlush(user);
	}
	
	public void createMore(@NotNull List<User> users){
		userRepository.save(users);
		userRepository.flush();
	}
	
	private void mergeScalarProperties(User mergeRequest, User user) {
		//user.setUserId(mergeRequest.getUserId());
		user.setUsername(mergeRequest.getUsername());
		user.setBusinessPhone(mergeRequest.getBusinessPhone());
		user.setAddressLine1(mergeRequest.getAddressLine1());
		user.setAddressLine2(mergeRequest.getAddressLine2());
		user.setCity(mergeRequest.getCity());
		user.setCountry(mergeRequest.getCountry());
		user.setDepartment(mergeRequest.getDepartment());
		user.setEmail(mergeRequest.getEmail());
	}
}
